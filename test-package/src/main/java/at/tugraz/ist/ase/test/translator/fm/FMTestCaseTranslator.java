/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.test.translator.fm;

//import at.tugraz.ist.ase.debugging.testcases.AggregatedTestCase;

import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.test.ITestCase;
import at.tugraz.ist.ase.test.TestCase;
import at.tugraz.ist.ase.test.translator.ITestCaseTranslatable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.Model;

@Slf4j
public class FMTestCaseTranslator implements ITestCaseTranslatable {

    protected FMAssignmentsTranslator translator;

    public FMTestCaseTranslator(FMAssignmentsTranslator translator) {
        this.translator = translator;
    }

    /**
     * Translates a test case to Choco constraints.
     */
    @Override
    public void translate(@NonNull ITestCase testCase, @NonNull Model model) {

        if (testCase instanceof TestCase tc) {
            log.trace("{}Translating test case [testcase={}] >>>", LoggerUtils.tab(), testCase);
            createTestCase(tc, model);
        }
//        else if (testCase instanceof AggregatedTestCase atc) {
//            log.trace("{}Translating aggregated test case [testcase={}] >>>", LoggerUtils.tab(), testCase);
//            for (ITestCase tc : atc.getTestcases()) {
//                createTestCase((TestCase) tc, model);
//            }
//        }
    }

    /**
     * Translates a test case to Choco constraints.
     */
    private void createTestCase(TestCase tc, Model model) {
//        int startIdx = model.getNbCstrs();
//        LogOp logOp = translator.create(tc.getAssignments(), model);
//        model.addClauses(logOp); // add the translated constraints to the Choco model
//        int endIdx = model.getNbCstrs();
//
//        // add the translated constraints to the TestCase object
//        setConstraintsToTestCase(tc, model, startIdx, endIdx - 1, false);
//
//        // Negative test cases
//        LogOp negLogOp = translator.createNegation(logOp, model);
//        startIdx = model.getNbCstrs();
//        model.addClauses(negLogOp);
//        endIdx = model.getNbCstrs();
//        setConstraintsToTestCase(tc, model, startIdx, endIdx - 1, true);
        translator.translate(tc.getAssignments(), model,
                tc.getChocoConstraints(), tc.getNegChocoConstraints());

        log.debug("{}Translated test case [testcase={}] >>>", LoggerUtils.tab(), tc);
    }

//    /**
//     * Sets translated Choco constraints to the {@link TestCase} object.
//     */
//    private void setConstraintsToTestCase(TestCase testCase, Model model, int startIdx, int endIdx, boolean hasNegativeConstraints) {
//        List<Constraint> constraints = ChocoSolverUtils.getConstraints(model, startIdx, endIdx);
//
//        if (hasNegativeConstraints) {
//            constraints.forEach(testCase::addNegChocoConstraint);
//        } else {
//            constraints.forEach(testCase::addChocoConstraint);
//        }
//
////        org.chocosolver.solver.constraints.Constraint[] constraints = model.getCstrs();
////        int index = startIdx;
////        while (index <= endIdx) {
////            if (hasNegativeConstraints) {
////                testCase.addNegChocoConstraint(constraints[index]);
////            } else {
////                testCase.addChocoConstraint(constraints[index]);
////            }
////            index++;
////        }
//    }
}
