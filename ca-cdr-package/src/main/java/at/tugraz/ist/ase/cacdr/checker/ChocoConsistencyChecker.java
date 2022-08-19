/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.checker;

import at.tugraz.ist.ase.cdrmodel.CDRModel;
import at.tugraz.ist.ase.cdrmodel.IChocoModel;
import at.tugraz.ist.ase.cdrmodel.IDebuggingModel;
import at.tugraz.ist.ase.cdrmodel.test.ITestCase;
import at.tugraz.ist.ase.cdrmodel.test.TestCase;
import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.kb.core.Constraint;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.Model;

import java.util.*;
import java.util.stream.Collectors;

import static at.tugraz.ist.ase.cacdr.eval.CAEvaluator.*;
import static at.tugraz.ist.ase.common.ConstraintUtils.*;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * A consistency checker implementation using the Choco solver
 */
@Slf4j
public class ChocoConsistencyChecker implements IConsistencyChecker {
    public static final String TIMER_SOLVER = "Timer for solver";

    /**
     * An internal models
     */
    protected Model model;
    protected CDRModel cdrModel;

    /**
     * Constructor
     *
     * CDRModel should have all constraints already posted.
     * - Testcases -> constraints should be posted before calling this function
     */
    public ChocoConsistencyChecker(@NonNull CDRModel diagModel) {
        this.cdrModel = diagModel;
        model = ((IChocoModel)diagModel).getModel();

        log.debug("{}Created ChocoConsistencyChecker for {}", LoggerUtils.tab(), diagModel);
    }

    /**
     * Checks the consistency of a set of constraints.
     * @param C       set of {@link Constraint}s
     * @return true if the given set of constraints are consistent, and false otherwise.
     */
    @Override
    public boolean isConsistent(@NonNull Collection<Constraint> C) {
        checkArgument(!C.isEmpty(), "Cannot check the consistency with an empty set of constraints");

        log.debug("{}Checking consistency for [C={}] >>>", LoggerUtils.tab(), C);
        LoggerUtils.indent();

        // post constraints of the parameter C
        if (cdrModel.isClone()) { // if the model is a clone model, we need to get the corresponding constraints from the clone model
            C = getConstraintFromCloneModel(C);
        }
        postConstraints(C, model);

        // Call solve()
        return check();
    }

    protected Set<Constraint> getConstraintFromCloneModel(Collection<Constraint> C) {
        /* for (Constraint c : cdrModel.getAllConstraints()) {
            if (C.contains(c)) {
                C_clone.add(c);
            }
        }*/
        return cdrModel.getAllConstraints().parallelStream().filter(C::contains).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Checks the consistency of a set of constraints with a test case.
     * @param C       set of {@link Constraint}s
     * @param testcase a {@link ITestCase}
     * @return true if the given test case isn't violated to the set of constraints, and false otherwise.
     */
    @Override
    public boolean isConsistent(@NonNull Collection<Constraint> C, @NonNull ITestCase testcase) {
        checkState(cdrModel instanceof IDebuggingModel, "Cannot check the consistency with a test case if the model is not debugging model");
        checkArgument(!C.isEmpty(), "Cannot check the consistency with an empty set of constraints");
        checkArgument(testcase instanceof TestCase, "Cannot check the consistency with a non-TestCase object");

        log.debug("{}Checking consistency for [C={}, testcase={}] >>>", LoggerUtils.tab(), C, testcase);
        LoggerUtils.indent();

        // post constraints of the parameter C
        if (cdrModel.isClone()) { // if the model is a clone model, we need to get the corresponding constraints from the clone model
            C = getConstraintFromCloneModel(C);
        }
        postConstraints(C, model);

        // post test case's constraints
        if (cdrModel.isClone()) { // if the model is a clone model, we need to get the corresponding testcase from the clone model
            testcase = getTestCaseFromCloneModel(testcase);
        }
        assert testcase != null;
        postTestCase((TestCase) testcase, false);

        // Call solve()
        return check();
    }

    protected ITestCase getTestCaseFromCloneModel(ITestCase testcase) {
        ITestCase testcase_clone = ((IDebuggingModel) cdrModel).getTestcases().parallelStream().filter(tc -> tc.equals(testcase)).findFirst().orElse(null);
        /* for (ITestCase tc : ((IDebuggingModel) cdrModel).getTestcases()) {
            if (tc.equals(testcase)) {
                testcase_clone = tc;
                break;
            }
        }*/

        if (testcase_clone == null) {
            log.error("{}Test case {} not found in the clone model", LoggerUtils.tab(), testcase);
        }
        return testcase_clone;
    }

    /**
     * consistent(tα ∧ ¬tγ)
     *
     * Checks the consistency between two test cases (tα ∧ ¬tγ) to identify a redundant test case.
     * If the output is false (inconsistent), then tγ is a redundant test case.
     *
     * Used by WipeOutR_T algorithm
     *
     * @param testcase a {@link ITestCase}
     * @param neg_testcase a {@link ITestCase}
     * @return true if the given test cases are not contradict, and false otherwise.
     */
    @Override
    public boolean isConsistent(@NonNull ITestCase testcase, @NonNull ITestCase neg_testcase) {
        checkState(cdrModel instanceof IDebuggingModel, "Cannot check the consistency with a test case if the model is not debugging model");
        checkArgument(testcase instanceof TestCase, "Cannot check the consistency with a non-TestCase object");
        checkArgument(neg_testcase instanceof TestCase, "Cannot check the consistency with a non-TestCase object");

        log.debug("{}Checking consistency for [testcase={}, neg_testcase={}] >>>", LoggerUtils.tab(), testcase, neg_testcase);
        LoggerUtils.indent();

        // post test case's constraints
        if (cdrModel.isClone()) { // if the model is a clone model, we need to get the corresponding testcase from the clone model
            testcase = getTestCaseFromCloneModel(testcase);
        }
        assert testcase != null;
        postTestCase((TestCase) testcase, false);

        // post neg test case's constraints
        if (cdrModel.isClone()) { // if the model is a clone model, we need to get the corresponding testcase from the clone model
            neg_testcase = getTestCaseFromCloneModel(neg_testcase);
        }
        assert neg_testcase != null;
        postTestCase((TestCase) neg_testcase, true);

        // Call solve()
        return check();
    }

    /**
     * consistent(C - {cstr} ∪ {¬cstr})
     *
     * Checks the consistency of (C - {cstr} ∪ {¬cstr}) to identify the redundant constraints.
     * If the output is false (inconsistent), then cstr is a redundant constraint.
     *
     * Used by WipeOutR_FM algorithm.
     *
     * @param C set of {@link Constraint}s
     * @param cstr a {@link Constraint}
     * @return true if the given test cases are not contradict, and false otherwise.
     */
    @Override
    public boolean isConsistent(@NonNull Collection<Constraint> C, @NonNull Constraint cstr) {
        checkArgument(!C.isEmpty(), "Cannot check the consistency with an empty set of constraints");

        log.debug("{}Checking consistency for [C={}, cstr={}] >>>", LoggerUtils.tab(), C, cstr);
        LoggerUtils.indent();

        if (cdrModel.isClone()) { // if the model is a clone model, we need to get the corresponding constraints from the clone model
            C = getConstraintFromCloneModel(C);
            cstr = getConstraintFromCloneModel(Collections.singleton(cstr)).iterator().next();
        }

        List<Constraint> CF = new LinkedList<>(C);
        // C - {cstr}
        CF.remove(cstr);

        // post constraints of the parameter C
        postConstraints(CF, model);

        // ∪ {¬cstr}
        postConstraint(cstr, model, true);

        // Call solve()
        return check();
    }

    /**
     * Checks the consistency of a set of constraints with a set of test cases.
     * This function returns true if every test case in {@param TC} is consistent with
     * the given set of constraints {@param C}, otherwise false. Test cases inducing
     * an inconsistency with {@param C} are stored in {@param TCp}.
     * Note that at the beginning, TC = TCp.
     *
     * Used by DirectDebug, TestHSDAG...
     * @param C a set of {@link Constraint}s
     * @param TC considering {@link ITestCase}s
     * @param TCp remaining inconsistent {@link ITestCase}s
     * @return true if every test case in {@param TC} is consistent with
     * the given set of constraints {@param C}, otherwise false.
     */
    @Deprecated
    public boolean isConsistent(@NonNull Collection<Constraint> C, @NonNull Collection<ITestCase> TC, @NonNull Collection<ITestCase> TCp) {
        checkState(cdrModel instanceof IDebuggingModel, "Cannot check the consistency with a test case if the model is not debugging model");
        checkArgument(!C.isEmpty(), "Cannot check the consistency with an empty set of constraints");
        checkArgument(!TC.isEmpty(), "Cannot check the consistency with an empty test case set");

        log.debug("{}Checking consistency [C={}, TC={}] >>>", LoggerUtils.tab(), C, TC);
        LoggerUtils.indent();

        boolean consistent = true;
        for (ITestCase tc: TC) {
            if (!isConsistent(C, tc)) {
                consistent = false;
            } else {
                TCp.remove(tc);
            }
        }

        LoggerUtils.outdent();
        log.debug("{}Checked [consistent={}, TCp={}]", LoggerUtils.tab(), consistent, TCp);

        return consistent;
    }

    /**
     * Checks the consistency of a set of constraints with a set of test cases, and
     * returns remaining inconsistent {@link ITestCase}s.
     *
     * Used by DirectDebug, TestHSDAG...
     * @param C a set of {@link Constraint}s
     * @param TC a considering {@link ITestCase}s
     * @param onlyOne true - to get only one inconsistent test case, false - to get all inconsistent test cases
     * @return remaining inconsistent {@link ITestCase}s.
     */
    @Override
    public Set<ITestCase> isConsistent(@NonNull Collection<Constraint> C, @NonNull Collection<ITestCase> TC, boolean onlyOne) {
        checkState(cdrModel instanceof IDebuggingModel, "Cannot check the consistency with a test case if the model is not debugging model");
        checkArgument(!C.isEmpty(), "Cannot check the consistency with an empty set of constraints");
        checkArgument(!TC.isEmpty(), "Cannot check the consistency with an empty test case set");

        log.debug("{}Checking consistency [C={}, TC={}] >>>", LoggerUtils.tab(), C, TC);
        LoggerUtils.indent();

        Set<ITestCase> TCp = new LinkedHashSet<>();
        for (ITestCase tc: TC) {
            if (!isConsistent(C, tc)) {
                TCp.add(tc);

                if (onlyOne) {
                    break;
                }
            }
        }

        LoggerUtils.outdent();
        log.debug("{}Checked [TCp={}]", LoggerUtils.tab(), TCp);

        return TCp;
    }

    /**
     * Resets the model to the original status
     * Restores constraints which are removed in the {@func isConsistent} function.
     */
    @Override
    public void reset() {
        model.getSolver().reset();
        incrementCounter(COUNTER_UNPOST_CONSTRAINT, model.getNbCstrs());
        model.unpost(model.getCstrs()); // unpost all constraints

        log.trace("{}Reset model", LoggerUtils.tab());
    }

    @Override
    public void dispose() {
        this.model = null;
        this.cdrModel = null;
    }

    /**
     * Runs the solver to check the consistency of the model.
     * @return true if the model is consistent, and false otherwise.
     */
    protected boolean check() {
        try {
            incrementCounter(COUNTER_CHOCO_SOLVER_CALLS);
            log.trace("{}Checking...", LoggerUtils.tab());
            incrementCounter(COUNTER_SIZE_CONSISTENCY_CHECKS, model.getNbCstrs());

            start(TIMER_SOLVER);
            boolean isFeasible = model.getSolver().solve();
            stop(TIMER_SOLVER);

            if (isFeasible) {
                incrementCounter(COUNTER_FEASIBLE);
            } else {
                incrementCounter(COUNTER_INFEASIBLE);
            }

//            model.getEnvironment().worldPush();
//            try {
//                start(TIMER_SOLVER);
//                model.getSolver().propagate(); // propagate
//                isFeasible = true; incrementCounter(COUNTER_FEASIBLE);
//
//            } catch (ContradictionException ex) { // in case of a contradiction
//
//                isFeasible = false; incrementCounter(COUNTER_INFEASIBLE);
//                model.getSolver().getEngine().flush();
//
//            } finally {
//                stop(TIMER_SOLVER);
//                // get back the original model
//                model.getEnvironment().worldPop();
//            }

            // resets the model to the beginning status
            reset();

            LoggerUtils.outdent();
            log.debug("{}<<< Checked [consistency={}]", LoggerUtils.tab(), isFeasible);

            return isFeasible;
        } catch (Exception e) {
            log.error("{}Error occurred while checking consistency: {}", LoggerUtils.tab(), e.getMessage());
            LoggerUtils.outdent();

            return false;
        }
    }

    /**
     * Posts the corresponding constraints of a textual test case to the model.
     * @param testcase a {@link TestCase}
     */
    protected void postTestCase(TestCase testcase, boolean negative) {
        if (!negative) {
            testcase.getChocoConstraints().forEach(model::post);
            incrementCounter(COUNTER_POST_CONSTRAINT, testcase.getChocoConstraints().size());
            log.trace("{}Added test case's constraints", LoggerUtils.tab());
        } else {
            testcase.getNegChocoConstraints().forEach(model::post);
            incrementCounter(COUNTER_POST_CONSTRAINT, testcase.getNegChocoConstraints().size());
            log.trace("{}Added neg test case's constraints", LoggerUtils.tab());
        }
    }
}
