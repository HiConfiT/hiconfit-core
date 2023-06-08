/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr.algorithms;

import at.tugraz.ist.ase.hiconfit.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.hiconfit.cacdr.eval.CAEvaluator;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test_model.model.*;
import at.tugraz.ist.ase.hiconfit.common.ConstraintUtils;
import at.tugraz.ist.ase.hiconfit.common.ThreadUtils;
import at.tugraz.ist.ase.hiconfit.eval.PerformanceEvaluator;
import at.tugraz.ist.ase.hiconfit.kb.core.Constraint;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThreadTimerTest {
    @Test
    void testFindDiagnosis1() throws Exception {
        TestModel1 testModel = new TestModel1();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(ConstraintUtils.convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        CAEvaluator.reset();
        PerformanceEvaluator.setCommonTimer(FastDiagV3.TIMER_FASTDIAGV3);
        Set<Constraint> firstDiag = fastDiag.findDiagnosis(C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
        CAEvaluator.printPerformance();

        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);

        double expectedTime = CAEvaluator.total(FastDiagV3.TIMER_FASTDIAGV3 + ThreadUtils.getThreadString()) / 1000000000.0;
        double actualTime = CAEvaluator.totalCommonTimer(FastDiagV3.TIMER_FASTDIAGV3) / 1000000000.0;

        assertEquals(expectedTime, actualTime, 0.000001);
    }

    @Test
    void testFindDiagnosis2() throws Exception {
        TestModel2 testModel = new TestModel2();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(ConstraintUtils.convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        CAEvaluator.reset();
        PerformanceEvaluator.setCommonTimer(FastDiagV3.TIMER_FASTDIAGV3);
        Set<Constraint> firstDiag = fastDiag.findDiagnosis(C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
        CAEvaluator.printPerformance();

        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);

        double expectedTime = CAEvaluator.total(FastDiagV3.TIMER_FASTDIAGV3 + ThreadUtils.getThreadString()) / 1000000000.0;
        double actualTime = CAEvaluator.totalCommonTimer(FastDiagV3.TIMER_FASTDIAGV3) / 1000000000.0;

        assertEquals(expectedTime, actualTime, 0.000001);
    }

    @Test
    void testFindDiagnosis3() throws Exception {
        TestModel3 testModel = new TestModel3();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(ConstraintUtils.convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        CAEvaluator.reset();
        PerformanceEvaluator.setCommonTimer(FastDiagV3.TIMER_FASTDIAGV3);
        Set<Constraint> firstDiag = fastDiag.findDiagnosis(C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
        CAEvaluator.printPerformance();

        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);

        double expectedTime = CAEvaluator.total(FastDiagV3.TIMER_FASTDIAGV3 + ThreadUtils.getThreadString()) / 1000000000.0;
        double actualTime = CAEvaluator.totalCommonTimer(FastDiagV3.TIMER_FASTDIAGV3) / 1000000000.0;

        assertEquals(expectedTime, actualTime, 0.000001);
    }

    @Test
    void testFindDiagnosis4() throws Exception {
        TestModel4 testModel = new TestModel4();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(ConstraintUtils.convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        CAEvaluator.reset();
        PerformanceEvaluator.setCommonTimer(FastDiagV3.TIMER_FASTDIAGV3);
        Set<Constraint> firstDiag = fastDiag.findDiagnosis(C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
        CAEvaluator.printPerformance();

        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);

        double expectedTime = CAEvaluator.total(FastDiagV3.TIMER_FASTDIAGV3 + ThreadUtils.getThreadString()) / 1000000000.0;
        double actualTime = CAEvaluator.totalCommonTimer(FastDiagV3.TIMER_FASTDIAGV3) / 1000000000.0;

        assertEquals(expectedTime, actualTime, 0.000001);
    }

    @Test
    void testFindDiagnosis5() throws Exception {
        TestModel5 testModel = new TestModel5();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(ConstraintUtils.convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        CAEvaluator.reset();
        PerformanceEvaluator.setCommonTimer(FastDiagV3.TIMER_FASTDIAGV3);
        Set<Constraint> firstDiag = fastDiag.findDiagnosis(C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
        CAEvaluator.printPerformance();

        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);

        double expectedTime = CAEvaluator.total(FastDiagV3.TIMER_FASTDIAGV3 + ThreadUtils.getThreadString()) / 1000000000.0;
        double actualTime = CAEvaluator.totalCommonTimer(FastDiagV3.TIMER_FASTDIAGV3) / 1000000000.0;

        assertEquals(expectedTime, actualTime, 0.000001);
    }
}
