/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.heuristics;

import at.tugraz.ist.ase.hiconfit.kb.core.Assignment;
import at.tugraz.ist.ase.hiconfit.kb.core.Domain;
import at.tugraz.ist.ase.hiconfit.kb.core.Solution;
import at.tugraz.ist.ase.hiconfit.kb.core.Variable;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.math.Vector;

import java.util.*;

/**
 * Manages the structure of a data model for the matrix factorization.
 * <p>
 * This class supports to customize the order of the variables in the matrix factorization model.
 * <p>
 * Provides:
 * 1. getModelData() - returns the data model, in which data items are matched to the variable order in the list of variables.
 * 2. getGenericPreferences() - returns the list of preferences, in which preferences are matched to the variable order in the list of variables.
 * 3. splitPredictedSolution() - splits the predicted vector into group of values
 */
@Slf4j
public class MFVVOModel {
    // the data model - specifies the variable order in the matrix factorization model
    @Getter
    private final List<Variable> variables;

    @Builder
    public MFVVOModel(@NonNull List<Variable> variables) {
        this.variables = variables;
    }

    public DataModel getDataModel(@NonNull List<Solution> solutions) {
        log.info("Getting the dataModel >>>");
        FastByIDMap<PreferenceArray> preferences = new FastByIDMap<>();

        // iterate over the solutions to create List<GenericPreference>
        int userId = 0;
        for (Solution solution : solutions) {

            List<GenericPreference> preferenceListOfOneUser = getGenericPreferences(userId, solution);

            PreferenceArray preferencesOfOneUser = new GenericUserPreferenceArray(preferenceListOfOneUser);
            preferences.put(userId, preferencesOfOneUser);

            log.info("User " + userId + ": " + preferencesOfOneUser);

            userId++;
        }

        log.info("<<< dataModel returned");
        return new GenericDataModel(preferences);
    }

    public List<GenericPreference> getGenericPreferences(int userId, @NonNull Solution solution) {
        int itemId = 0;
        List<GenericPreference> preferenceListOfOneUser = new LinkedList<>();
        for (Variable variable : variables) { // foreach variable
            // get the corresponding assignment of the variable in the solution
            Assignment assignment;
            try {
                assignment = solution.getAssignment(variable.getName());
            } catch (IllegalArgumentException e) {
                assignment = null;
            }

            if (assignment == null) {
                itemId = itemId + variable.getDomain().getValues().size();
                log.info(userId + " - " + variable.getName() + " - NULL");
            } else {
                String value = assignment.getValue();
                Domain domain = variable.getDomain();

                for (String domainValue : domain.getValues()) {

                    float valueMF;
                    if (domainValue.equals(value)) {
                        valueMF = 1.0f;
                    } else {
                        valueMF = 0.0f;
                    }

                    log.info(userId + " - " + variable.getName() + " - " + itemId + " - " + valueMF);

                    GenericPreference preference = new GenericPreference(userId, itemId, valueMF);
                    preferenceListOfOneUser.add(preference);

                    itemId++;
                }
            }
        }
        return preferenceListOfOneUser;
    }

    /**
     * Split the predicted vector into groups of values, which each group corresponds to values of a variable.
     * Besides, each value will be attached with its choco value (get from the variable domain).
     * @param predictedSolution a vector of the predicted solution
     * @return A list of dictionary of values in the predicted solution and chocoValue at the correlative position
     */
    public List<Map<Double, Integer>> splitPredictedSolution(@NonNull Vector predictedSolution) {
        List<Map<Double, Integer>> splitSolutions = new LinkedList<>();

        int indexSolution = 0;
        for (Variable variable : variables) {
            int size = variable.getDomain().size();
            Map<Double, Integer> splitVar = new TreeMap<>(Collections.reverseOrder());

            for (int indexDomainValue = 0; indexDomainValue < size; indexDomainValue++) {
                double key = predictedSolution.get(indexSolution);

                splitVar.put(key, indexDomainValue);

                indexSolution++;
            }

            splitSolutions.add(splitVar);
        }

        return splitSolutions;
    }
}
