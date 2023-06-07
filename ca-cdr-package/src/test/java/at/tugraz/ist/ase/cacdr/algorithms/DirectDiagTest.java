/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
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

import static at.tugraz.ist.ase.cacdr.eval.CAEvaluator.printPerformance;
import static at.tugraz.ist.ase.common.ConstraintUtils.convertToString;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectDiagTest {
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

        // run the directdiag to find diagnoses
        DirectDiag directDiag = new DirectDiag(checker);

        CAEvaluator.reset();
        Set<Constraint> firstDiag = directDiag.findDiagnosis(C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by DirectDiag:");
        System.out.println(firstDiag);
        printPerformance();

        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);
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

        // run the directDiag to find diagnoses
        DirectDiag directDiag = new DirectDiag(checker);

        CAEvaluator.reset();
        Set<Constraint> firstDiag = directDiag.findDiagnosis(C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by DirectDiag:");
        System.out.println(firstDiag);
        printPerformance();

        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);
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

        // run the directDiag to find diagnoses
        DirectDiag directDiag = new DirectDiag(checker);

        CAEvaluator.reset();
        Set<Constraint> firstDiag = directDiag.findDiagnosis(C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by DirectDiag:");
        System.out.println(firstDiag);
        printPerformance();

        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);
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

        // run the directDiag to find diagnoses
        DirectDiag directDiag = new DirectDiag(checker);

        CAEvaluator.reset();
        Set<Constraint> firstDiag = directDiag.findDiagnosis(C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by DirectDiag:");
        System.out.println(firstDiag);
        printPerformance();

        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);
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

        // run the directDiag to find diagnoses
        DirectDiag directDiag = new DirectDiag(checker);

        CAEvaluator.reset();
        Set<Constraint> firstDiag = directDiag.findDiagnosis(C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by DirectDiag:");
        System.out.println(firstDiag);
        printPerformance();

        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);
    }
}