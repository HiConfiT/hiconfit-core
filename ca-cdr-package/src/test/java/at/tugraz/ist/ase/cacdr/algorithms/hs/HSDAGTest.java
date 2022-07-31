/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hs;

import at.tugraz.ist.ase.cacdr.algorithms.hs.labeler.*;
import at.tugraz.ist.ase.cacdr.algorithms.hs.parameters.*;
import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.cacdr.eval.CAEvaluator;
import at.tugraz.ist.ase.cdrmodel.test_model.model.*;
import at.tugraz.ist.ase.kb.core.Constraint;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static at.tugraz.ist.ase.cacdr.eval.CAEvaluator.printPerformance;
import static at.tugraz.ist.ase.common.ConstraintUtils.convertToString;
import static at.tugraz.ist.ase.common.ConstraintUtils.convertToStringWithMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HSDAGTest {
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

        HSDAG hsdag = new HSDAG(quickXplain);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSDAG + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSDAG + QuickXplain:");
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

        HSDAG hsdag = new HSDAG(quickXplain);
        hsdag.setMaxNumberOfDiagnoses(1);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSDAG + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSDAG + QuickXplain:");
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

        HSDAG hsdag = new HSDAG(quickXplain);
        hsdag.setMaxNumberOfDiagnoses(2);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSDAG + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSDAG + QuickXplain:");
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

        HSDAG hsdag = new HSDAG(quickXplain);
        hsdag.setMaxNumberOfConflicts(1);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSDAG + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSDAG + QuickXplain:");
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

        HSDAG hsdag = new HSDAG(quickXplain);
        hsdag.setMaxNumberOfConflicts(2);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSDAG + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSDAG + QuickXplain:");
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

        HSDAG hsdag = new HSDAG(quickXplain);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSDAG + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSDAG + QuickXplain:");
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

        HSDAG hsdag = new HSDAG(quickXplain);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSDAG + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSDAG + QuickXplain:");
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

        HSDAG hsdag = new HSDAG(quickXplain);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSDAG + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSDAG + QuickXplain:");
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

        HSDAG hsdag = new HSDAG(quickXplain);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSDAG + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSDAG + QuickXplain:");
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

        HSDAG hsdag = new HSDAG(fastDiag);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-dag:");
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

        HSDAG hsdag = new HSDAG(fastDiag);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-dag:");
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

        HSDAG hsdag = new HSDAG(fastDiag);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-dag:");
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

        HSDAG hsdag = new HSDAG(fastDiag);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-dag:");
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

        HSDAG hsdag = new HSDAG(fastDiag);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-dag:");
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

        HSDAG hsdag = new HSDAG(fastDiag);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-dag:");
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

        HSDAG hsdag = new HSDAG(fastDiag);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-dag:");
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

        HSDAG hsdag = new HSDAG(fastDiag);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-dag:");
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

        HSDAG hsdag = new HSDAG(fastDiag);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-dag:");
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

        HSDAG hsdag = new HSDAG(fastDiag);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-dag:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void test1_FlexD() throws Exception {
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
        FlexDiagParameters params = FlexDiagParameters.builder()
                .S(C)
                .AC(AC)
                .m(1)
                .build();
        FlexDiagLabeler flexDiag = new FlexDiagLabeler(checker, params);

        HSDAG hsdag = new HSDAG(flexDiag);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-dag:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void test2_FlexD() throws Exception {
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
        FlexDiagParameters params = FlexDiagParameters.builder()
                .S(C)
                .AC(AC)
                .m(1)
                .build();
        FlexDiagLabeler flexDiag = new FlexDiagLabeler(checker, params);

        HSDAG hsdag = new HSDAG(flexDiag);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-dag:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void test3_FlexD() throws Exception {
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
        FlexDiagParameters params = FlexDiagParameters.builder()
                .S(C)
                .AC(AC)
                .m(1)
                .build();
        FlexDiagLabeler flexDiag = new FlexDiagLabeler(checker, params);

        HSDAG hsdag = new HSDAG(flexDiag);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-dag:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void test4_FlexD() throws Exception {
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
        FlexDiagParameters params = FlexDiagParameters.builder()
                .S(C)
                .AC(AC)
                .m(1)
                .build();
        FlexDiagLabeler flexDiag = new FlexDiagLabeler(checker, params);

        HSDAG hsdag = new HSDAG(flexDiag);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-dag:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void test5_FlexD() throws Exception {
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
        FlexDiagParameters params = FlexDiagParameters.builder()
                .S(C)
                .AC(AC)
                .m(1)
                .build();
        FlexDiagLabeler flexDiag = new FlexDiagLabeler(checker, params);

        HSDAG hsdag = new HSDAG(flexDiag);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-dag:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void test1_DD() throws Exception {
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
        DirectDiagParameters params = DirectDiagParameters.builder()
                .C(C)
                .B(B).build();
        DirectDiagLabeler directDiag = new DirectDiagLabeler(checker, params);

        HSDAG hsdag = new HSDAG(directDiag);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by DirectDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-dag:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void test2_DD() throws Exception {
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
        DirectDiagParameters params = DirectDiagParameters.builder()
                .C(C)
                .B(B).build();
        DirectDiagLabeler directDiag = new DirectDiagLabeler(checker, params);

        HSDAG hsdag = new HSDAG(directDiag);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by DirectDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-dag:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void test3_DD() throws Exception {
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
        DirectDiagParameters params = DirectDiagParameters.builder()
                .C(C)
                .B(B).build();
        DirectDiagLabeler directDiag = new DirectDiagLabeler(checker, params);

        HSDAG hsdag = new HSDAG(directDiag);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by DirectDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-dag:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void test4_DD() throws Exception {
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
        DirectDiagParameters params = DirectDiagParameters.builder()
                .C(C)
                .B(B).build();
        DirectDiagLabeler directDiag = new DirectDiagLabeler(checker, params);

        HSDAG hsdag = new HSDAG(directDiag);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by DirectDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-dag:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void test5_DD() throws Exception {
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
        DirectDiagParameters params = DirectDiagParameters.builder()
                .C(C)
                .B(B).build();
        DirectDiagLabeler directDiag = new DirectDiagLabeler(checker, params);

        HSDAG hsdag = new HSDAG(directDiag);

        CAEvaluator.reset();
        hsdag.construct();

        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsdag.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by DirectDiag:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HS-dag:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }
}