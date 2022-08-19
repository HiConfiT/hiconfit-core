/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
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

public class QuickXplainTest {

    @Test
    void testQuickXPlain1() throws Exception {
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
        QuickXPlain quickXplain = new QuickXPlain(checker);
//        quickXplain.setMaxNumberOfDiagnoses(10);

        CAEvaluator.reset();
        Set<Constraint> firstConflictSet = quickXplain.findConflictSet(C, B);

//        List<Set<Constraint>> allConflictSets = quickXplain.findAllConflictSets(firstConflictSet, C, B);

        System.out.println("=========================================");
        System.out.println("Conflict sets found by QuickXplain:");
        System.out.println(firstConflictSet);
//        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(firstConflictSet, testModel.getExpectedFirstConflict());
//        assertEquals(allConflictSets, testModel.getExpectedAllConflicts());
    }

    @Test
    void testQuickXPlain2() throws Exception {
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
        QuickXPlain quickXplain = new QuickXPlain(checker);
//        quickXplain.setMaxNumberOfDiagnoses(10);

        CAEvaluator.reset();
        Set<Constraint> firstConflictSet = quickXplain.findConflictSet(C, B);

//        List<Set<Constraint>> allConflictSets = quickXplain.findAllConflictSets(firstConflictSet, C, B);

        System.out.println("=========================================");
        System.out.println("Conflict sets found by QuickXplain:");
        System.out.println(firstConflictSet);
//        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(firstConflictSet, testModel.getExpectedFirstConflict());
//        assertEquals(allConflictSets, testModel.getExpectedAllConflicts());
    }

    @Test
    void testQuickXPlain3() throws Exception {
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
        QuickXPlain quickXplain = new QuickXPlain(checker);
//        quickXplain.setMaxNumberOfDiagnoses(10);

        CAEvaluator.reset();
        Set<Constraint> firstConflictSet = quickXplain.findConflictSet(C, B);

//        List<Set<Constraint>> allConflictSets = quickXplain.findAllConflictSets(firstConflictSet, C, B);

        System.out.println("=========================================");
        System.out.println("Conflict sets found by QuickXplain:");
        System.out.println(firstConflictSet);
//        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(firstConflictSet, testModel.getExpectedFirstConflict());
//        assertEquals(allConflictSets, testModel.getExpectedAllConflicts());
    }

    @Test
    void testQuickXPlain4() throws Exception {
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
        QuickXPlain quickXplain = new QuickXPlain(checker);

        CAEvaluator.reset();
        Set<Constraint> firstConflictSet = quickXplain.findConflictSet(C, B);

//        List<Set<Constraint>> allConflictSets = quickXplain.findAllConflictSets(firstConflictSet, C, B);

        System.out.println("=========================================");
        System.out.println("Conflict sets found by QuickXplain:");
        System.out.println(firstConflictSet);
//        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(firstConflictSet, testModel.getExpectedFirstConflict());
//        assertEquals(allConflictSets, testModel.getExpectedAllConflicts());
    }

    @Test
    void testQuickXPlain5() throws Exception {
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
        QuickXPlain quickXplain = new QuickXPlain(checker);

        CAEvaluator.reset();
        Set<Constraint> firstConflictSet = quickXplain.findConflictSet(C, B);

//        List<Set<Constraint>> allConflictSets = quickXplain.findAllConflictSets(firstConflictSet, C, B);

        System.out.println("=========================================");
        System.out.println("Conflict sets found by QuickXplain:");
        System.out.println(firstConflictSet);
//        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(firstConflictSet, testModel.getExpectedFirstConflict());
//        assertEquals(allConflictSets, testModel.getExpectedAllConflicts());
    }

//    @Test
//    public void testQuickXPlain6() {
//        TestDiagnosisModel6 diagModel = new TestDiagnosisModel6("Test");
//        diagModel.initialize();
//
//        System.out.println("=========================================");
//        System.out.println("Choco's commands translated from the text file:");
//        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
//        System.out.println("=========================================");
//
//        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(diagModel);
//
//        Set<String> C = diagModel.getPossiblyFaultyConstraints();
//        Set<String> B = diagModel.getCorrectConstraints();
//
//        // run the fastDiag to find diagnoses
//        QuickXPlain quickXplain = new QuickXPlain(checker);
//
//        PerformanceEvaluation.reset();
//        Set<String> firstConflictSet = quickXplain.findConflictSet(C, B);
//
//        List<Set<String>> allConflictSets = quickXplain.findAllConflictSets(firstConflictSet, C, B);
//
//        System.out.println("=========================================");
//        System.out.println("Conflict sets found by QuickXplain:");
//        System.out.println(firstConflictSet);
//        UtilsForTest.printListSetConstraints(allConflictSets, "Conflict set");
//        printPerformance();
//
//        // Expected results
//        Set<String> cs1 = new LinkedHashSet<>();
//        cs1.add(IteratorUtils.get(C.iterator(), 4));
//        cs1.add(IteratorUtils.get(C.iterator(), 6));
//
//        List<Set<String>> allDiagTest = new ArrayList<>();
//        allDiagTest.add(cs1);
//
//        assertEquals(firstConflictSet, cs1);
//        assertEquals(allConflictSets, allDiagTest);
//    }
}