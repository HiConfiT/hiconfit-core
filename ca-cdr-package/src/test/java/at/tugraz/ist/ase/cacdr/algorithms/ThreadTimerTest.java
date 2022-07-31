/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms;

import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.cacdr.eval.CAEvaluator;
import at.tugraz.ist.ase.cdrmodel.test_model.model.*;
import at.tugraz.ist.ase.kb.core.Constraint;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static at.tugraz.ist.ase.cacdr.algorithms.FastDiagV3.TIMER_FASTDIAGV3;
import static at.tugraz.ist.ase.cacdr.eval.CAEvaluator.printPerformance;
import static at.tugraz.ist.ase.common.ConstraintUtils.convertToString;
import static at.tugraz.ist.ase.common.IOUtils.getThreadString;
import static at.tugraz.ist.ase.eval.PerformanceEvaluator.setCommonTimer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThreadTimerTest {
    @Test
    void testFindDiagnosis1() throws Exception {
        TestModel1 testModel = new TestModel1();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        CAEvaluator.reset();
        setCommonTimer(TIMER_FASTDIAGV3);
        Set<Constraint> firstDiag = fastDiag.findDiagnosis(C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
        printPerformance();

        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);

        double expectedTime = CAEvaluator.total(TIMER_FASTDIAGV3 + getThreadString()) / 1000000000.0;
        double actualTime = CAEvaluator.totalCommonTimer(TIMER_FASTDIAGV3) / 1000000000.0;

        assertEquals(expectedTime, actualTime, 0.000001);
    }

    @Test
    void testFindDiagnosis2() throws Exception {
        TestModel2 testModel = new TestModel2();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        CAEvaluator.reset();
        setCommonTimer(TIMER_FASTDIAGV3);
        Set<Constraint> firstDiag = fastDiag.findDiagnosis(C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
        printPerformance();

        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);

        double expectedTime = CAEvaluator.total(TIMER_FASTDIAGV3 + getThreadString()) / 1000000000.0;
        double actualTime = CAEvaluator.totalCommonTimer(TIMER_FASTDIAGV3) / 1000000000.0;

        assertEquals(expectedTime, actualTime, 0.000001);
    }

    @Test
    void testFindDiagnosis3() throws Exception {
        TestModel3 testModel = new TestModel3();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        CAEvaluator.reset();
        setCommonTimer(TIMER_FASTDIAGV3);
        Set<Constraint> firstDiag = fastDiag.findDiagnosis(C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
        printPerformance();

        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);

        double expectedTime = CAEvaluator.total(TIMER_FASTDIAGV3 + getThreadString()) / 1000000000.0;
        double actualTime = CAEvaluator.totalCommonTimer(TIMER_FASTDIAGV3) / 1000000000.0;

        assertEquals(expectedTime, actualTime, 0.000001);
    }

    @Test
    void testFindDiagnosis4() throws Exception {
        TestModel4 testModel = new TestModel4();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        CAEvaluator.reset();
        setCommonTimer(TIMER_FASTDIAGV3);
        Set<Constraint> firstDiag = fastDiag.findDiagnosis(C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
        printPerformance();

        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);

        double expectedTime = CAEvaluator.total(TIMER_FASTDIAGV3 + getThreadString()) / 1000000000.0;
        double actualTime = CAEvaluator.totalCommonTimer(TIMER_FASTDIAGV3) / 1000000000.0;

        assertEquals(expectedTime, actualTime, 0.000001);
    }

    @Test
    void testFindDiagnosis5() throws Exception {
        TestModel5 testModel = new TestModel5();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        CAEvaluator.reset();
        setCommonTimer(TIMER_FASTDIAGV3);
        Set<Constraint> firstDiag = fastDiag.findDiagnosis(C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
        printPerformance();

        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);

        double expectedTime = CAEvaluator.total(TIMER_FASTDIAGV3 + getThreadString()) / 1000000000.0;
        double actualTime = CAEvaluator.totalCommonTimer(TIMER_FASTDIAGV3) / 1000000000.0;

        assertEquals(expectedTime, actualTime, 0.000001);
    }
}
