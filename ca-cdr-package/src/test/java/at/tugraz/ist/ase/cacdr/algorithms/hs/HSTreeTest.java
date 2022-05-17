/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hs;

import at.tugraz.ist.ase.cacdr.algorithms.hs.labeler.FastDiagV2Labeler;
import at.tugraz.ist.ase.cacdr.algorithms.hs.labeler.FastDiagV3Labeler;
import at.tugraz.ist.ase.cacdr.algorithms.hs.labeler.QuickXPlainLabeler;
import at.tugraz.ist.ase.cacdr.algorithms.hs.parameters.FastDiagV2Parameters;
import at.tugraz.ist.ase.cacdr.algorithms.hs.parameters.FastDiagV3Parameters;
import at.tugraz.ist.ase.cacdr.algorithms.hs.parameters.QuickXPlainParameters;
import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.cacdr.eval.CAEvaluator;
import at.tugraz.ist.ase.cdrmodel.test.model.*;
import at.tugraz.ist.ase.kb.core.Constraint;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static at.tugraz.ist.ase.cacdr.eval.CAEvaluator.printPerformance;
import static at.tugraz.ist.ase.common.ConstraintUtils.convertToString;
import static at.tugraz.ist.ase.common.ConstraintUtils.convertToStringWithMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HSTreeTest {
    @Test
    void testQX1() throws Exception {
        TestModel1 testModel = new TestModel1();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the hstree to find diagnoses
        QuickXPlainParameters parameter = QuickXPlainParameters.builder()
                                                .C(C)
                                                .B(B).build();
        QuickXPlainLabeler quickXplain = new QuickXPlainLabeler(checker, parameter);

        HSTree hsTree = new HSTree(quickXplain, checker);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllConflicts(), allConflictSets);
        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void shouldStopAfterFirstDiagnosis_testQX1() throws Exception {
        TestModel1 testModel = new TestModel1();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the hstree to find diagnoses
        QuickXPlainParameters parameter = QuickXPlainParameters.builder()
                .C(C)
                .B(B).build();
        QuickXPlainLabeler quickXplain = new QuickXPlainLabeler(checker, parameter);

        HSTree hsTree = new HSTree(quickXplain, checker);
        hsTree.setMaxNumberOfDiagnoses(1);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(1, allDiagnoses.size());
    }

    @Test
    void shouldStopAfterSecondDiagnosis_testQX1() throws Exception {
        TestModel1 testModel = new TestModel1();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the hstree to find diagnoses
        QuickXPlainParameters parameter = QuickXPlainParameters.builder()
                .C(C)
                .B(B).build();
        QuickXPlainLabeler quickXplain = new QuickXPlainLabeler(checker, parameter);

        HSTree hsTree = new HSTree(quickXplain, checker);
        hsTree.setMaxNumberOfDiagnoses(2);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(2, allDiagnoses.size());
    }

    @Test
    void shouldStopAfterFirstConflict_testQX1() throws Exception {
        TestModel1 testModel = new TestModel1();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the hstree to find diagnoses
        QuickXPlainParameters parameter = QuickXPlainParameters.builder()
                .C(C)
                .B(B).build();
        QuickXPlainLabeler quickXplain = new QuickXPlainLabeler(checker, parameter);

        HSTree hsTree = new HSTree(quickXplain, checker);
        hsTree.setMaxNumberOfConflicts(1);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(1, allConflictSets.size());
        assertEquals(0, allDiagnoses.size());
    }

    @Test
    void shouldStopAfterSecondConflict_testQX1() throws Exception {
        TestModel1 testModel = new TestModel1();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the hstree to find diagnoses
        QuickXPlainParameters parameter = QuickXPlainParameters.builder()
                .C(C)
                .B(B).build();
        QuickXPlainLabeler quickXplain = new QuickXPlainLabeler(checker, parameter);

        HSTree hsTree = new HSTree(quickXplain, checker);
        hsTree.setMaxNumberOfConflicts(2);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(2, allConflictSets.size());
//        assertEquals(0, allDiagnoses.size());
    }

    @Test
    void testQX2() throws Exception {
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
        QuickXPlainParameters parameter = QuickXPlainParameters.builder()
                .C(C)
                .B(B).build();
        QuickXPlainLabeler quickXplain = new QuickXPlainLabeler(checker, parameter);

        HSTree hsTree = new HSTree(quickXplain, checker);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllConflicts(), allConflictSets);
        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void testQX3() throws Exception {
        TestModel3 testModel = new TestModel3();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the hstree to find diagnoses
        QuickXPlainParameters parameter = QuickXPlainParameters.builder()
                .C(C)
                .B(B).build();
        QuickXPlainLabeler quickXplain = new QuickXPlainLabeler(checker, parameter);

        HSTree hsTree = new HSTree(quickXplain, checker);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllConflicts(), allConflictSets);
        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void testQX4() throws Exception {
        TestModel4 testModel = new TestModel4();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the hstree to find diagnoses
        QuickXPlainParameters parameter = QuickXPlainParameters.builder()
                .C(C)
                .B(B).build();
        QuickXPlainLabeler quickXplain = new QuickXPlainLabeler(checker, parameter);

        HSTree hsTree = new HSTree(quickXplain, checker);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllConflicts(), allConflictSets);
        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void testQX5() throws Exception {
        TestModel5 testModel = new TestModel5();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the hstree to find diagnoses
        QuickXPlainParameters parameter = QuickXPlainParameters.builder()
                .C(C)
                .B(B).build();
        QuickXPlainLabeler quickXplain = new QuickXPlainLabeler(checker, parameter);

        HSTree hsTree = new HSTree(quickXplain, checker);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllConflicts(), allConflictSets);
        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void test1_FDv2() throws Exception {
        TestModel1 testModel = new TestModel1();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> AC = testModel.getAllConstraints();

        // run the hstree to find diagnoses
        FastDiagV2Parameters params = FastDiagV2Parameters.builder()
                .C(C)
                .AC(AC).build();
        FastDiagV2Labeler fastDiag = new FastDiagV2Labeler(checker, params);

        HSTree hsTree = new HSTree(fastDiag, checker);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-tree:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void test2_FDv2() throws Exception {
        TestModel2 testModel = new TestModel2();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> AC = testModel.getAllConstraints();

        // run the hstree to find diagnoses
        FastDiagV2Parameters params = FastDiagV2Parameters.builder()
                .C(C)
                .AC(AC).build();
        FastDiagV2Labeler fastDiag = new FastDiagV2Labeler(checker, params);

        HSTree hsTree = new HSTree(fastDiag, checker);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-tree:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void test3_FDv2() throws Exception {
        TestModel3 testModel = new TestModel3();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> AC = testModel.getAllConstraints();

        // run the hstree to find diagnoses
        FastDiagV2Parameters params = FastDiagV2Parameters.builder()
                .C(C)
                .AC(AC).build();
        FastDiagV2Labeler fastDiag = new FastDiagV2Labeler(checker, params);

        HSTree hsTree = new HSTree(fastDiag, checker);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-tree:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void test4_FDv2() throws Exception {
        TestModel4 testModel = new TestModel4();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> AC = testModel.getAllConstraints();

        // run the hstree to find diagnoses
        FastDiagV2Parameters params = FastDiagV2Parameters.builder()
                .C(C)
                .AC(AC).build();
        FastDiagV2Labeler fastDiag = new FastDiagV2Labeler(checker, params);

        HSTree hsTree = new HSTree(fastDiag, checker);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-tree:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void test5_FDv2() throws Exception {
        TestModel5 testModel = new TestModel5();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> AC = testModel.getAllConstraints();

        // run the hstree to find diagnoses
        FastDiagV2Parameters params = FastDiagV2Parameters.builder()
                .C(C)
                .AC(AC).build();
        FastDiagV2Labeler fastDiag = new FastDiagV2Labeler(checker, params);

        HSTree hsTree = new HSTree(fastDiag, checker);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-tree:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void test1_FDv3() throws Exception {
        TestModel1 testModel = new TestModel1();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the hstree to find diagnoses
        FastDiagV3Parameters params = FastDiagV3Parameters.builder()
                .C(C)
                .B(B).build();
        FastDiagV3Labeler fastDiag = new FastDiagV3Labeler(checker, params);

        HSTree hsTree = new HSTree(fastDiag, checker);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-tree:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void test2_FDv3() throws Exception {
        TestModel2 testModel = new TestModel2();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the hstree to find diagnoses
        FastDiagV3Parameters params = FastDiagV3Parameters.builder()
                .C(C)
                .B(B).build();
        FastDiagV3Labeler fastDiag = new FastDiagV3Labeler(checker, params);

        HSTree hsTree = new HSTree(fastDiag, checker);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-tree:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void test3_FDv3() throws Exception {
        TestModel3 testModel = new TestModel3();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the hstree to find diagnoses
        FastDiagV3Parameters params = FastDiagV3Parameters.builder()
                .C(C)
                .B(B).build();
        FastDiagV3Labeler fastDiag = new FastDiagV3Labeler(checker, params);

        HSTree hsTree = new HSTree(fastDiag, checker);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-tree:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void test4_FDv3() throws Exception {
        TestModel4 testModel = new TestModel4();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the hstree to find diagnoses
        FastDiagV3Parameters params = FastDiagV3Parameters.builder()
                .C(C)
                .B(B).build();
        FastDiagV3Labeler fastDiag = new FastDiagV3Labeler(checker, params);

        HSTree hsTree = new HSTree(fastDiag, checker);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-tree:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void test5_FDv3() throws Exception {
        TestModel5 testModel = new TestModel5();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the hstree to find diagnoses
        FastDiagV3Parameters params = FastDiagV3Parameters.builder()
                .C(C)
                .B(B).build();
        FastDiagV3Labeler fastDiag = new FastDiagV3Labeler(checker, params);

        HSTree hsTree = new HSTree(fastDiag, checker);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-tree:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }
}