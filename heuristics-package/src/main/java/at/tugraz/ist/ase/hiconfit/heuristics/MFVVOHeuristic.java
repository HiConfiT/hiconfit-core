/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.heuristics;

import at.tugraz.ist.ase.hiconfit.common.VariableUtils;
import at.tugraz.ist.ase.hiconfit.heuristics.mf.MatrixFactorization;
import at.tugraz.ist.ase.hiconfit.kb.core.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.similarity.AveragingPreferenceInferrer;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.math.Vector;
import org.chocosolver.solver.variables.IntVar;

import java.util.*;

/**
 * An implementation of Matrix Factorization Based Variable and Value Ordering Heuristics for Constraint Solving
 * @version 1.0
 * @author Seda Polat Erdeniz (AIG, TUGraz)
 * @author <a href="http://ase.ist.tugraz.at">...</a>
 *
 * @version 2.0
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class MFVVOHeuristic {
    // the data model structure
    private final MFVVOModel mfvvoModel;

    private MatrixFactorization mf;

    private UserSimilarity userSimilarity = null;
    private UserNeighborhood userNeighborhood = null;
    @Getter @Setter
    private int numNeighbors = 10;
    @Getter @Setter
    private int numFeatures = 6; // mxk, kxn -> k - numNeighbors
    @Getter @Setter
    private int numIterations = 50;

    @Getter
    private boolean fitted = false;

    public MFVVOHeuristic(@NonNull MFVVOModel mfvvoModel) {
        this.mfvvoModel = mfvvoModel;
    }

    // SECTION Matrix Factorization
    public void fit(List<Solution> solutions) throws TasteException {
        // convert solutions to data model
        DataModel dataModel = mfvvoModel.getDataModel(solutions);

        // factorize the matrix, and calculate the dense matrix (mf.X_Prime)
        mf = new MatrixFactorization(dataModel);
        mf.svd(numFeatures, numIterations);

        fitted = true;
    }

    public void saveMF(String filename) {
        // TODO: save IFMatrix, UFMatrix, and X_Prime
    }

    public void loadMF(String filename) {
        // TODO: load
        // check the size of X_Prime, IFMatrix, and UFMatrix before loading
    }

    // SECTION Heuristics
    public void setSimilarity(UserSimilarity userSimilarity) {
        this.userSimilarity = userSimilarity;
    }

    public void setNeighborhood(UserNeighborhood userNeighborhood) {
        this.userNeighborhood = userNeighborhood;
    }

    // get heuristics for a requirement
    public ValueVariableOrdering getHeuristic(Requirement requirement) throws TasteException {
        // convert requirement to PreferenceArray
        List<GenericPreference> preferenceListOfOneUser = mfvvoModel.getGenericPreferences(0, requirement);
        PreferenceArray preferencesOfOneUser = new GenericUserPreferenceArray(preferenceListOfOneUser);

        // Add requirement into dataModel
        DataModel dataModel = getDataModelOfXPrimeWith(preferencesOfOneUser);
        // A. selects k most similar estimated transactions
        long[] neighbors = identifyNeighbors(dataModel, numNeighbors);

        // Testing
//        System.out.println("Neighbors");
//        assert neighbors != null;
//        for (long neighborID : neighbors) {
//            System.out.println(neighborID);
//        }
//
//        System.out.println("UserSimilarity");
//        for (long neighborID : neighbors) {
//            System.out.println(userSimilarity.userSimilarity(0, neighborID));
//        }
//
//        System.out.println("Data");
//        System.out.println(dataModel.getPreferencesFromUser(0));
//        for (long neighborID : neighbors) {
//            System.out.println(dataModel.getPreferencesFromUser(neighborID));
//        }

        // B. aggregated into one predicted user factors for requirement (UF)
        if (neighbors != null) {
            neighbors = Arrays.stream(neighbors).map(a -> a - 1).toArray();
        } else {
            return null;
        }

//        System.out.println("Neighbors-----------------------");
//        assert neighbors != null;
//        for (long neighborID : neighbors) {
//            System.out.println(neighborID);
//        }
//        System.out.println("End Neighbors-----------------------");

        Vector predictedUF = getPredictedUF(neighbors);

        if (predictedUF == null)
            return null;

//        System.out.println("Predicted UF");
//        System.out.println(predictedUF);

        // C. multiply with IF to obtain a predicted solution
        Vector predictedSolution = mf.getIFMatrix().times(predictedUF);

//        System.out.println("Predicted solution");
//        System.out.println(predictedSolution);

        // generates and return the heuristic
        return generateHeuristics(predictedSolution);
    }

    private ValueVariableOrdering generateHeuristics(Vector predictedSolution) {
        ValueVariableOrdering vvo = new ValueVariableOrdering();

        List<Map<Double, Integer>> splitSolution = mfvvoModel.splitPredictedSolution(predictedSolution);
        // Map <order, varName>
        Map<Double, String> varOrdering = new TreeMap<>(Collections.reverseOrder());

//        System.out.println("SPLIT SOLUTION");
        for (int i = 0; i < splitSolution.size(); i++) {
            Variable var = mfvvoModel.getVariables().get(i);

            String varName = var.getName();
            IntVar intVar = null;
            if (var instanceof IntVariable) {
                intVar = ((IntVariable) var).getChocoVar();
            } else if (var instanceof BoolVariable) {
                intVar = ((BoolVariable) var).getChocoVar();
            }

            ValueOrdering vo = new ValueOrdering(varName);
            vo.setIntVar(intVar); // TODO

            Map<Double, Integer> orderedValues = splitSolution.get(i);
//            System.out.println(orderedValues);
            for (Double value : orderedValues.keySet()) {
//                System.out.println("key: " + value + " - value: " + orderedValues.get(value));
                vo.setOrderedValue(orderedValues.get(value));
            }

            vvo.setValueOrdering(vo);

            varOrdering.put(orderedValues.keySet().iterator().next(), varName);
        }

        // print varOrdering
//        System.out.println("VAR ORDERING");
//        System.out.println(varOrdering);

        vvo.setVarOrdering(varOrdering.values().stream().toList());

        // TODO - need to check
        List<IntVar> intVars = VariableUtils.getIntVarOrdering(varOrdering.values().stream().toList(), mfvvoModel.getVariables());
        vvo.setIntVarOrdering(intVars);

        return vvo;
    }

    protected DataModel getDataModelOfXPrimeWith(PreferenceArray preferencesOfOneUser) {
        FastByIDMap<PreferenceArray> preferences = new FastByIDMap<>();

        // add preferences from requirement
        preferences.put(0, preferencesOfOneUser);

        for (int userID = 0; userID < mf.getX_Prime().numRows(); userID++) {
            List<GenericPreference> preferenceListOfOneUser = new LinkedList<>();
            for (int itemID = 0; itemID < mf.getX_Prime().numCols(); itemID++) {
                float value = (float) mf.getX_Prime().get(userID, itemID);

                GenericPreference preference = new GenericPreference(userID + 1, itemID, value);
                preferenceListOfOneUser.add(preference);
            }

            preferencesOfOneUser = new GenericUserPreferenceArray(preferenceListOfOneUser);
            preferences.put(userID + 1, preferencesOfOneUser);
        }

        return new GenericDataModel(preferences);
    }

    /**
     * Identify the numNeighbors most similar users to the user 0.
     */
    private long[] identifyNeighbors(DataModel dataModel, int numNeighbors) throws TasteException {
        int userID = 0;

        if (userSimilarity == null) { // uses default
            userSimilarity = new PearsonCorrelationSimilarity(dataModel);
            userSimilarity.setPreferenceInferrer(new AveragingPreferenceInferrer(dataModel));
        }

        if (userNeighborhood == null) {
            userNeighborhood = new NearestNUserNeighborhood(numNeighbors, userSimilarity, dataModel);
        }

        return userNeighborhood.getUserNeighborhood(userID);
    }

    private Vector getPredictedUF(long[] neighbors) {
//        System.out.println("Vectors");
        List<Vector> vectors = new LinkedList<>();
        for (long neighborID: neighbors) {
            Vector v = mf.getUFMatrix().viewRow((int) neighborID);
//            System.out.println(v);
            vectors.add(v);
        }

        if (vectors.isEmpty()) {
            return null;
        }

        Vector v = vectors.get(0);
        for (int i = 1; i < vectors.size(); i++) {
            v = v.plus(vectors.get(i));
        }
        v = v.divide(vectors.size());
//        System.out.println("Aggregated vectors");
//        System.out.println(v);

        return v;
    }
}
