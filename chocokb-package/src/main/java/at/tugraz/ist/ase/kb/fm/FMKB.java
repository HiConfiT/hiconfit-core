/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.kb.fm;

import at.tugraz.ist.ase.common.ChocoSolverUtils;
import at.tugraz.ist.ase.common.ConstraintUtils;
import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.fm.core.*;
import at.tugraz.ist.ase.fm.core.ast.*;
import at.tugraz.ist.ase.kb.core.*;
import at.tugraz.ist.ase.kb.core.builder.BoolVarConstraintBuilder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;
import org.chocosolver.solver.variables.BoolVar;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public class FMKB<F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint> extends KB implements IBoolVarKB {

    private FeatureModel<F, R, C> featureModel;

    @Getter
    private Constraint rootConstraint = null;

    public FMKB(@NonNull FeatureModel<F, R, C> featureModel, boolean hasNegativeConstraints) {
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

        // create the root constraint, remove created Choco constraints after this step
        defineRootConstraint();

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
        for (R relationship: featureModel.getRelationships()) {
            BoolVar leftVar = getVarWithName(relationship.getParent().getName());
            BoolVar rightVar;

            startIdx = modelKB.getNbCstrs();

            if (relationship instanceof MandatoryRelationship) {
                rightVar = getVarWithName(relationship.getChild().getName());
                // leftVar <=> rightVar
                logOp = LogOp.ifOnlyIf(leftVar, rightVar);
                // negative logOp
                if (hasNegativeConstraints) {
                    negLogOp = LogOp.nand(LogOp.implies(leftVar, rightVar), LogOp.implies(rightVar, leftVar));
                }
            } else if (relationship instanceof OptionalRelationship) {
                rightVar = getVarWithName(relationship.getChild().getName());
                // rightVar => leftVar
                logOp = LogOp.implies(rightVar, leftVar);
                // negative logOp
                if (hasNegativeConstraints) {
                    negLogOp = LogOp.and(rightVar, leftVar.not());
                }
            } else if (relationship instanceof OrRelationship) {// LogOp of rule {A \/ B \/ ... \/ C}
                LogOp rightLogOp = getRightSideOfOrRelationship(relationship.getChildren());
                // leftVar <=> rightLogOp
                logOp = LogOp.ifOnlyIf(leftVar, rightLogOp);
                // negative logOp
                if (hasNegativeConstraints) {
                    negLogOp = LogOp.nand(LogOp.implies(leftVar, rightLogOp), LogOp.implies(rightLogOp, leftVar));
                }
            } else if (relationship instanceof AlternativeRelationship) {// LogOp of an ALTERNATIVE relationship
                logOp = getLogOpOfAlternativeRelationship(relationship, false);
                // negative logOp
                if (hasNegativeConstraints) {
                    negLogOp = getLogOpOfAlternativeRelationship(relationship, true);
                }
            } else {
                throw new IllegalStateException("Unexpected class: " + relationship.getClass());
            }

            Constraint constraint = BoolVarConstraintBuilder.build(relationship, modelKB, logOp, negLogOp, startIdx, hasNegativeConstraints);
            constraintList.add(constraint);
        }

        // second convert constraints of {@link FeatureModel} into ChocoSolver constraints
        for (C cstr: featureModel.getConstraints()) {
            ASTNode formula = cstr.getFormula();
            if (formula instanceof RequiresOperator || formula instanceof ExcludesOperator) {
                BoolVar leftVar = getVarWithName(((Operand<?>)formula.getLeft()).getFeature().getName());
                BoolVar rightVar = getVarWithName(((Operand<?>)formula.getRight()).getFeature().getName());

                startIdx = modelKB.getNbCstrs();

                if (formula instanceof RequiresOperator) {
                    // leftVar => rightVar
                    logOp = LogOp.implies(leftVar, rightVar);
                    // negative logOp
                    if (hasNegativeConstraints) {
                        negLogOp = LogOp.and(leftVar, rightVar.not());
                    }
                } else {
                    // leftVar => !rightVar
                    logOp = LogOp.or(leftVar.not(), rightVar.not());
                    // negative logOp
                    if (hasNegativeConstraints) {
                        negLogOp = LogOp.nor(leftVar.not(), rightVar.not());
                    }
                }
            } else {
                startIdx = modelKB.getNbCstrs();

                logOp = convertToLopOp(cstr.getCnf(), false);
                if (hasNegativeConstraints) {
                    negLogOp = convertToLopOp(cstr.getCnf(), true);
                }
            }

            Constraint constraint = BoolVarConstraintBuilder.build(cstr, modelKB, logOp, negLogOp, startIdx,hasNegativeConstraints);
            constraintList.add(constraint);
        }

        LoggerUtils.outdent();
        log.trace("{}<<< Created constraints", LoggerUtils.tab());
    }

    private LogOp convertToLopOp(ASTNode cnf, boolean isNot) {
        LogOp logOp = null;
        ASTNode left = cnf.getLeft();
        ASTNode right = cnf.getRight();
        if (cnf instanceof AndOperator) {
            if (isNot) {
                logOp = LogOp.or();
            } else {
                logOp = LogOp.and();
            }
            logOp.addChild(convertToLopOp(left, isNot));
            logOp.addChild(convertToLopOp(right, isNot));
        } else if (cnf instanceof OrOperator) {
            if (isNot) {
                logOp = LogOp.and();
            } else {
                logOp = LogOp.or();
            }
            logOp.addChild(convertToLopOp(left, isNot));
            logOp.addChild(convertToLopOp(right, isNot));
        } else if (cnf instanceof NotOperator) {
            logOp = LogOp.and();
            logOp.addChild(convertToLopOp(right, !isNot));
        } else if (cnf instanceof Operand) {
            if (isNot) {
                logOp = LogOp.and();
                logOp.addChild(getVarWithName(((Operand<?>)cnf).getFeature().getName()).not());
            } else {
                logOp = LogOp.and();
                logOp.addChild(getVarWithName(((Operand<?>)cnf).getFeature().getName()));
            }
        } else {
            throw new IllegalStateException("Unexpected class: " + cnf.getClass());
        }
        return logOp;
    }

    /**
     * Create the root constraint: f0 = true.
     */
    private void defineRootConstraint() {
        // {f0 = true}
        int startIdx = modelKB.getNbCstrs();
        String f0 = this.getVariable(0).getName();
        BoolVar f0Var = (BoolVar) ChocoSolverUtils.getVariable(modelKB, f0);
        modelKB.addClauseTrue(f0Var);

        this.rootConstraint = new Constraint(f0 + " = true");
        ConstraintUtils.addChocoConstraintsToConstraint(false, this.rootConstraint, modelKB, startIdx, modelKB.getNbCstrs() - 1);
//        this.rootConstraint.addChocoConstraints(modelKB, startIdx, modelKB.getNbCstrs() - 1, false);

        // unpost the created Choco constraints
        ChocoSolverUtils.unpostConstraintsFrom(startIdx, modelKB);
    }

    /**
     * Create a {@link LogOp} that represent to an ALTERNATIVE relationship.
     * The form of rule is {C1 <=> (not C2 /\ ... /\ not Cn /\ P) /\
     *                      C2 <=> (not C1 /\ ... /\ not Cn /\ P) /\
     *                      ... /\
     *                      Cn <=> (not C1 /\ ... /\ not Cn-1 /\ P)
     *
     * @param relationship - a {@link AbstractRelationship} of {@link FeatureModel}
     * @return A {@link LogOp} that represent to an ALTERNATIVE relationship
     * @throws IllegalArgumentException when couldn't find the corresponding variable in the model
     */
    private LogOp getLogOpOfAlternativeRelationship(R relationship, boolean negative) throws IllegalArgumentException {
        LogOp logOp = LogOp.or(); // an LogOp of OR operators
        BoolVar[] vars = relationship.getChildren().parallelStream().map(feature -> getVarWithName(feature.getName())).toArray(BoolVar[]::new);
        /*for (int i = 0; i < basicRelationship.getRightSide().size(); i++) {
            vars[i] = getVarWithName(basicRelationship.getRightSide().get(i).getName());
        }*/

        BoolVar leftVar = getVarWithName(relationship.getParent().getName());
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
    private LogOp getRightSideOfAlternativeRelationship(String leftSide, List<F> rightSide, int removedIndex) throws IllegalArgumentException {
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
    private LogOp getRightSideOfOrRelationship(List<F> rightSide) throws IllegalArgumentException {
        if (rightSide.size() == 0) return null;
        LogOp op = LogOp.or(); // create a LogOp of OR operators
        rightSide.parallelStream().map(feature -> getVarWithName(feature.getName()))
                .forEachOrdered(op::addChild);
        /*for (F feature : rightSide) {
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
    public boolean getBoolValue(@NonNull String var, @NonNull String value) {
        Domain domain = getDomain(var);

        return domain.getChocoValue(value) != 0;
    }

    @Override
    public void dispose() {
        super.dispose();
        featureModel = null;
    }
}
