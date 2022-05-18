/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.kb.fm;

import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.fm.core.*;
import at.tugraz.ist.ase.kb.core.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public class FMKB extends KB {

    private final FeatureModel featureModel;

    public FMKB(FeatureModel featureModel, boolean hasNegativeConstraints) {
        super(featureModel.getName(), "SPLOT", hasNegativeConstraints);

        this.featureModel = featureModel;

        reset(hasNegativeConstraints);
    }

    @Override
    public void reset(boolean hasNegativeConstraints) {
        log.trace("{}Creating FMKB for feature model [fm={}] >>>", LoggerUtils.tab(), name);
        LoggerUtils.indent();

        modelKB = new Model(name);
        variableList = new LinkedList<>();
        domainList = new LinkedList<>();
        constraintList = new LinkedList<>();

        defineVariables();
        defineConstraints(hasNegativeConstraints);

        LoggerUtils.outdent();
        log.debug("{}<<< Created FMKB for [fm={}]", LoggerUtils.tab(), name);
    }

    public void defineVariables (){
        log.trace("{}Creating variables >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        IntStream.range(0, featureModel.getNumOfFeatures()).mapToObj(i -> featureModel.getFeature(i).getName()).forEachOrdered(varName -> {
            /*for (int i = 0; i < featureModel.getNumOfFeatures(); i++) {
                String varName = featureModel.getFeature(i).getName();*/
            Domain domain = Domain.builder()
                    .name(varName)
                    .build();
            domainList.add(domain);
            BoolVar boolVar = modelKB.boolVar(varName);
            Variable var = BoolVariable.builder()
                    .name(varName)
                    .domain(domain)
                    .chocoVar(boolVar).build();
            variableList.add(var);
        });

        LoggerUtils.outdent();
        log.trace("{}<<< Created variables", LoggerUtils.tab());
    }

    public void defineConstraints(boolean hasNegativeConstraints) {
        log.trace("{}Creating constraints >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        int startIdx;
        LogOp logOp = null;
        LogOp negLogOp = null;

        // first convert relationships into constraints
        for (Relationship relationship: featureModel.getRelationships()) {
            BasicRelationship basicRelationship = (BasicRelationship) relationship;

            BoolVar leftVar = getVarWithName(basicRelationship.getLeftSide().getName());
            BoolVar rightVar;

            startIdx = modelKB.getNbCstrs();

            switch (relationship.getType()) {
                case MANDATORY -> {
                    rightVar = getVarWithName(basicRelationship.getRightSide().get(0).getName());
                    // leftVar <=> rightVar
                    logOp = LogOp.ifOnlyIf(leftVar, rightVar);
                    // negative logOp
                    if (hasNegativeConstraints) {
                        negLogOp = LogOp.nand(LogOp.implies(leftVar, rightVar), LogOp.implies(rightVar, leftVar));
                    }
                }
                case OPTIONAL -> {
                    rightVar = getVarWithName(basicRelationship.getRightSide().get(0).getName());
                    // leftVar => rightVar
                    logOp = LogOp.implies(leftVar, rightVar);
                    // negative logOp
                    if (hasNegativeConstraints) {
                        negLogOp = LogOp.and(leftVar, rightVar.not());
                    }
                }
                case OR -> {
                    // LogOp of rule {A \/ B \/ ... \/ C}
                    LogOp rightLogOp = getRightSideOfOrRelationship(basicRelationship.getRightSide());
                    // leftVar <=> rightLogOp
                    logOp = LogOp.ifOnlyIf(leftVar, rightLogOp);
                    // negative logOp
                    if (hasNegativeConstraints) {
                        negLogOp = LogOp.nand(LogOp.implies(leftVar, rightLogOp), LogOp.implies(rightLogOp, leftVar));
                    }
                }
                case ALTERNATIVE -> {
                    // LogOp of an ALTERNATIVE relationship
                    logOp = getLogOpOfAlternativeRelationship(basicRelationship, false);
                    // negative logOp
                    if (hasNegativeConstraints) {
                        negLogOp = getLogOpOfAlternativeRelationship(basicRelationship, true);
                    }
                }
                default -> throw new IllegalStateException("Unexpected value: " + relationship.getType());
            }

            addConstraintsToModel(hasNegativeConstraints, startIdx, logOp, negLogOp, basicRelationship);
        }

        // second convert constraints of {@link FeatureModel} into ChocoSolver constraints
        for (Relationship relationship: featureModel.getConstraints()) {
            if (relationship.isType(RelationshipType.ThreeCNF)) {
                ThreeCNFConstraint threeCNFConstraint = (ThreeCNFConstraint) relationship;

                startIdx = modelKB.getNbCstrs();

                logOp = LogOp.or();
                if (hasNegativeConstraints) {
                    negLogOp = LogOp.nor();
                }
                for (Clause clause: threeCNFConstraint.getClauses()) {
                    boolean value = clause.isPositive();
                    BoolVar var = getVarWithName(clause.getLiteral());

                    if (value) {
                        logOp.addChild(var);
                        if (hasNegativeConstraints) {
                            negLogOp.addChild(var);
                        }
                    } else {
                        logOp.addChild(var.not());
                        if (hasNegativeConstraints) {
                            negLogOp.addChild(var.not());
                        }
                    }
                }
            } else {
                BasicRelationship basicRelationship = (BasicRelationship) relationship;

                BoolVar leftVar = getVarWithName(basicRelationship.getLeftSide().getName());
                BoolVar rightVar = getVarWithName(basicRelationship.getRightSide().get(0).getName());

                startIdx = modelKB.getNbCstrs();
                switch (relationship.getType()) {
                    case REQUIRES -> {
                        logOp = LogOp.implies(leftVar, rightVar);
                        if (hasNegativeConstraints) {
                            negLogOp = LogOp.and(leftVar, rightVar.not());
                        }
                    }
                    case EXCLUDES -> {
                        logOp = LogOp.or(leftVar.not(), rightVar.not());
                        if (hasNegativeConstraints) {
                            negLogOp = LogOp.nor(leftVar.not(), rightVar.not());
                        }
                    }
                }
            }

            addConstraintsToModel(hasNegativeConstraints, startIdx, logOp, negLogOp, relationship);
        }

        LoggerUtils.outdent();
        log.trace("{}<<< Created constraints", LoggerUtils.tab());
    }

    private void addConstraintsToModel(boolean hasNegativeConstraints, int startIdx, LogOp logOp, LogOp negLogOp, Relationship relationship) {
        modelKB.addClauses(logOp);

        Constraint constraint = new Constraint(relationship.getConfRule());
        addConstraint(constraint, relationship, startIdx, modelKB.getNbCstrs() - 1);

        if (hasNegativeConstraints) {
            if (relationship.isType(RelationshipType.ALTERNATIVE)) {
                addNegConstraint(constraint, startIdx + 1, startIdx + 1);
            }

            startIdx = modelKB.getNbCstrs();

            modelKB.addClauses(negLogOp);
            addNegConstraint(constraint, startIdx, modelKB.getNbCstrs() - 1);
        }

        constraintList.add(constraint);

        // unpost the negative constraint
        if (hasNegativeConstraints) {
            int index = modelKB.getNbCstrs() - 1;
            while (index >= startIdx) {
                modelKB.unpost(modelKB.getCstrs()[index]);
                index--;
            }
        }
    }

    private void addConstraint(Constraint constraint, Relationship relationship, int startIdx, int endIdx) {
        org.chocosolver.solver.constraints.Constraint[] constraints = modelKB.getCstrs();

        int index = startIdx;
        while (index <= endIdx) {
            // add to constraint
            constraint.addChocoConstraint(constraints[index]);
            // add to Relationship
            relationship.setConstraint(constraints[index].toString());

            index++;
        }
    }

    private void addNegConstraint(Constraint constraint, int startIdx, int endIdx) {
        org.chocosolver.solver.constraints.Constraint[] constraints = modelKB.getCstrs();

        int index = startIdx;
        while (index <= endIdx) {
            constraint.addNegChocoConstraint(constraints[index]);

            index++;
        }
    }

//    /**
//     * Create a {@link LogOp} that represent to an ALTERNATIVE relationship.
//     * The form of rule is {C1 <=> (not C2 /\ ... /\ not Cn /\ P) /\
//     *                      C2 <=> (not C1 /\ ... /\ not Cn /\ P) /\
//     *                      ... /\
//     *                      Cn <=> (not C1 /\ ... /\ not Cn-1 /\ P)
//     *
//     * @param relationship - a {@link Relationship} of {@link FeatureModel}
//     * @return A {@link LogOp} that represent to an ALTERNATIVE relationship
//     * @throws IllegalArgumentException when couldn't find the corresponding variable in the model
//     */
//    private LogOp getLogOpOfAlternativeRelationship(Relationship relationship, boolean negative) throws IllegalArgumentException {
//        LogOp logOp;
//        if (negative) { // negative constraint
//            logOp = LogOp.nand(); // an LogOp of NAND operators
//        } else {
//            logOp = LogOp.and(); // an LogOp of AND operators
//        }
//        BasicRelationship basicRelationship = (BasicRelationship) relationship;
//        for (int i = 0; i < basicRelationship.getRightSide().size(); i++) {
//            BoolVar rightVar = getVarWithName(basicRelationship.getRightSide().get(i).getName());
//            // (not C2 /\ ... /\ not Cn /\ P)
//            LogOp rightSide = getRightSideOfAlternativeRelationship(basicRelationship.getLeftSide().getName(), basicRelationship.getRightSide(), i);
//            // {C1 <=> (not C2 /\ ... /\ not Cn /\ P)}
//            LogOp part = LogOp.ifOnlyIf(rightVar, rightSide);
//
//            logOp.addChild(part);
//        }
//        return logOp;
//    }

    /**
     * Create a {@link LogOp} that represent to an ALTERNATIVE relationship.
     * The form of rule is {C1 <=> (not C2 /\ ... /\ not Cn /\ P) /\
     *                      C2 <=> (not C1 /\ ... /\ not Cn /\ P) /\
     *                      ... /\
     *                      Cn <=> (not C1 /\ ... /\ not Cn-1 /\ P)
     *
     * @param relationship - a {@link Relationship} of {@link FeatureModel}
     * @return A {@link LogOp} that represent to an ALTERNATIVE relationship
     * @throws IllegalArgumentException when couldn't find the corresponding variable in the model
     */
    private LogOp getLogOpOfAlternativeRelationship(Relationship relationship, boolean negative) throws IllegalArgumentException {
        LogOp logOp = LogOp.or(); // an LogOp of OR operators
        BasicRelationship basicRelationship = (BasicRelationship) relationship;
        BoolVar[] vars = basicRelationship.getRightSide().parallelStream().map(feature -> getVarWithName(feature.getName())).toArray(BoolVar[]::new);
        /*for (int i = 0; i < basicRelationship.getRightSide().size(); i++) {
            vars[i] = getVarWithName(basicRelationship.getRightSide().get(i).getName());
        }*/

        BoolVar leftVar = getVarWithName(basicRelationship.getLeftSide().getName());
        if (negative) { // negative constraint
            BoolVar notEqualVar = modelKB.boolVar();
            modelKB.sum(vars, "!=", 1).reifyWith(notEqualVar); // B + C + D != 1

            // LogOp.or(LogOp.and(A.not(), LogOp.or(B, C, D),
            //          LogOp.and(A, notEqualVar)));
            logOp.addChild(LogOp.and(leftVar.not(), LogOp.or(vars)));
            logOp.addChild(LogOp.and(leftVar, notEqualVar));
        } else {
            BoolVar equalVar = modelKB.boolVar();
            modelKB.sum(vars, "=", 1).reifyWith(equalVar); // B + C + D = 1

            // LogOp.or(LogOp.and(A.not(), B.not(), C.not(), D.not()),
            //          LogOp.and(A, equalVar));
            LogOp rule1 = LogOp.and(leftVar.not());
            Arrays.stream(vars).map(BoolVar::not).forEachOrdered(rule1::addChild);
            /*for (BoolVar var : vars) {
                rule1.addChild(var.not());
            }*/

            logOp.addChild(rule1);
            logOp.addChild(LogOp.and(leftVar, equalVar));
        }
        return logOp;
    }

    /**
     * Create a {@link LogOp} that represent the rule {(not C2 /\ ... /\ not Cn /\ P)}.
     * This is the right side of the rule {C1 <=> (not C2 /\ ... /\ not Cn /\ P)}
     *
     * @param leftSide - the name of the parent feature
     * @param rightSide - names of the child features
     * @param removedIndex - the index of the child feature that is the left side of the rule
     * @return a {@link LogOp} that represent the rule {(not C2 /\ ... /\ not Cn /\ P)}.
     * @throws IllegalArgumentException when couldn't find the variable in the model
     */
    private LogOp getRightSideOfAlternativeRelationship(String leftSide, List<Feature> rightSide, int removedIndex) throws IllegalArgumentException {
        BoolVar leftVar = getVarWithName(leftSide);
        LogOp op = LogOp.and(leftVar);
        IntStream.range(0, rightSide.size())
                .filter(i -> i != removedIndex)
                .mapToObj(i -> LogOp.nor(getVarWithName(rightSide.get(i).getName())))
                .forEachOrdered(op::addChild);
        /*for (int i = 0; i < rightSide.size(); i++) {
            if (i != removedIndex) {
                op.addChild(LogOp.nor(getVarWithName(rightSide.get(i).getName())));
            }
        }*/
        return op;
    }

    /**
     * Create a {@link LogOp} for the right side of an OR relationship.
     * The form of rule is {A \/ B \/ ... \/ C}.
     *
     * @param rightSide - an array of feature names which belong to the right side of an OR relationship
     * @return a {@link LogOp} or null if the rightSide is empty
     * @throws IllegalArgumentException when couldn't find a variable which corresponds to the given feature name
     */
    private LogOp getRightSideOfOrRelationship(List<Feature> rightSide) throws IllegalArgumentException {
        if (rightSide.size() == 0) return null;
        LogOp op = LogOp.or(); // create a LogOp of OR operators
        rightSide.parallelStream().map(feature -> getVarWithName(feature.getName()))
                .forEachOrdered(op::addChild);
        /*for (Feature feature : rightSide) {
            BoolVar var = getVarWithName(feature.getName());
            op.addChild(var);
        }*/
        return op;
    }

    /**
     * On the basic of a feature name, this function return
     * the corresponding ChocoSolver variable in the model.
     *
     * @param name - a feature name
     * @return the corresponding ChocoSolver variable in the model or null
     * @throws IllegalArgumentException when couldn't find the variable in the model
     */
    private BoolVar getVarWithName(String name) throws IllegalArgumentException {
        for (Variable v : this.getVariableList()) {
            if (v.getName().equals(name)) {
                return ((BoolVariable) v).getChocoVar();
            }
        }
        throw new IllegalArgumentException("Feature " + name + " not found in the model");
    }

    @Override
    public IntVar[] getIntVars() {
        throw new UnsupportedOperationException("Not supported by this knowledge base.");
    }

    @Override
    public IntVar getIntVar(@NonNull String variable) {
        throw new UnsupportedOperationException("Not supported by this knowledge base.");
    }

    @Override
    public BoolVar[] getBoolVars() {
        org.chocosolver.solver.variables.Variable[] vars = getModelKB().getVars();

        return Arrays.stream(vars).map(v -> (BoolVar) v).toArray(BoolVar[]::new);
    }

    @Override
    public BoolVar getBoolVar(@NonNull String variable) {
        Variable var = getVariable(variable);

        return ((BoolVariable) var).getChocoVar();
    }

    // Choco value
    @Override
    public int getIntValue(@NonNull String var, @NonNull String value) {
        throw new UnsupportedOperationException("Not supported by this knowledge base.");
    }

    @Override
    public boolean getBoolValue(@NonNull String var, @NonNull String value) {
        Domain domain = getDomain(var);

        return domain.getChocoValue(value) != 0;
    }
}
