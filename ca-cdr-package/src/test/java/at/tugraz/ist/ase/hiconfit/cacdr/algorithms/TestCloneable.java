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
import at.tugraz.ist.ase.hiconfit.cdrmodel.fm.FMDebuggingModel;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test.ITestCase;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test.TestSuite;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test.builder.fm.FMTestCaseBuilder;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test.reader.TestSuiteReader;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test.translator.fm.FMTestCaseTranslator;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test_model.model.*;
import at.tugraz.ist.ase.hiconfit.common.ConstraintUtils;
import at.tugraz.ist.ase.hiconfit.common.IOUtils;
import at.tugraz.ist.ase.hiconfit.eval.PerformanceEvaluator;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.Feature;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import at.tugraz.ist.ase.hiconfit.fm.parser.FMParserFactory;
import at.tugraz.ist.ase.hiconfit.fm.parser.FeatureModelParser;
import at.tugraz.ist.ase.hiconfit.fm.parser.FeatureModelParserException;
import at.tugraz.ist.ase.hiconfit.kb.core.Constraint;
import com.google.common.collect.Iterators;
import lombok.Cleanup;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCloneable {
    @Test
    void quickXplain() throws Exception {
        TestModel1 testModel = new TestModel1();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(ConstraintUtils.convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // cloneable
        TestModel1 testModel1 = (TestModel1) testModel.clone();
        testModel1.initialize();
        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel1);

        // run the fastDiag to find diagnoses
        QuickXPlain quickXplain = new QuickXPlain(checker);

        CAEvaluator.reset();
        Set<Constraint> firstConflictSet = quickXplain.findConflictSet(C, B);

        System.out.println("=========================================");
        System.out.println("Conflict sets found by QuickXplain:");
        System.out.println(firstConflictSet);
//        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        CAEvaluator.printPerformance();

        assertEquals(firstConflictSet, testModel.getExpectedFirstConflict());
//        assertEquals(allConflictSets, testModel.getExpectedAllConflicts());
    }

    @Test
    void fastDiagV3_1() throws Exception {
        TestModel1 testModel = new TestModel1();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(ConstraintUtils.convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // cloneable
        TestModel1 testModel1 = (TestModel1) testModel.clone();
        testModel1.initialize();
        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel1);

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        CAEvaluator.reset();
        Set<Constraint> firstDiag = fastDiag.findDiagnosis(C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
//        System.out.println(convertToStringWithMessage(allDiag, "Diagnosis"));
        CAEvaluator.printPerformance();

//        assertAll(() -> assertEquals(testModel.getExpectedFirstDiagnosis().size(), firstDiag.size()),
//                () -> {
//                    for (Constraint constraint : firstDiag) {
//                        assertTrue(testModel.getExpectedFirstDiagnosis().contains(constraint));
//                    }},
//                () -> assertEquals(testModel.getExpectedAllDiagnoses().size(), allDiag.size()),
//                () -> {
//                    for (int i = 0; i < allDiag.size(); i++) {
//                        Set<Constraint> expectedDiagnoses = testModel.getExpectedAllDiagnoses().get(i);
//                        Set<Constraint> actualDiagnoses = allDiag.get(i);
//                        for (Constraint constraint : actualDiagnoses) {
//                            assertTrue(expectedDiagnoses.contains(constraint));
//                        }
//                    }
//                }
//                );
        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);
//        assertAll(() -> assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag),
//                () -> assertEquals(testModel.getExpectedAllDiagnoses(), allDiag));
    }

    @Test
    void fastDiagV3_2() throws Exception {
        TestModel2 testModel = new TestModel2();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(ConstraintUtils.convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // cloneable
        TestModel2 testModel1 = (TestModel2) testModel.clone();
        testModel1.initialize();
        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel1);

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        CAEvaluator.reset();
        Set<Constraint> firstDiag = fastDiag.findDiagnosis(C, B);

//        List<Set<Constraint>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
//        System.out.println(convertToStringWithMessage(allDiag, "Diagnosis"));
        CAEvaluator.printPerformance();

//        assertAll(() -> assertEquals(testModel.getExpectedFirstDiagnosis().size(), firstDiag.size()),
//                () -> {
//                    for (Constraint constraint : firstDiag) {
//                        assertTrue(testModel.getExpectedFirstDiagnosis().contains(constraint));
//                    }},
//                () -> assertEquals(testModel.getExpectedAllDiagnoses().size(), allDiag.size()),
//                () -> {
//                    for (int i = 0; i < allDiag.size(); i++) {
//                        Set<Constraint> expectedDiagnoses = testModel.getExpectedAllDiagnoses().get(i);
//                        Set<Constraint> actualDiagnoses = allDiag.get(i);
//                        for (Constraint constraint : actualDiagnoses) {
//                            assertTrue(expectedDiagnoses.contains(constraint));
//                        }
//                    }
//                }
//        );
        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);
//        assertAll(() -> assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag),
//                () -> assertEquals(testModel.getExpectedAllDiagnoses(), allDiag));
    }

    @Test
    void fastDiagV3_3() throws Exception {
        TestModel3 testModel = new TestModel3();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(ConstraintUtils.convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // cloneable
        TestModel3 testModel1 = (TestModel3) testModel.clone();
        testModel1.initialize();
        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel1);

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        CAEvaluator.reset();
        Set<Constraint> firstDiag = fastDiag.findDiagnosis(C, B);

//        List<Set<Constraint>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
//        System.out.println(convertToStringWithMessage(allDiag, "Diagnosis"));
        CAEvaluator.printPerformance();

//        assertAll(() -> assertEquals(testModel.getExpectedFirstDiagnosis().size(), firstDiag.size()),
//                () -> {
//                    for (Constraint constraint : firstDiag) {
//                        assertTrue(testModel.getExpectedFirstDiagnosis().contains(constraint));
//                    }},
//                () -> assertEquals(testModel.getExpectedAllDiagnoses().size(), allDiag.size()),
//                () -> {
//                    for (int i = 0; i < allDiag.size(); i++) {
//                        Set<Constraint> expectedDiagnoses = testModel.getExpectedAllDiagnoses().get(i);
//                        Set<Constraint> actualDiagnoses = allDiag.get(i);
//                        for (Constraint constraint : actualDiagnoses) {
//                            assertTrue(expectedDiagnoses.contains(constraint));
//                        }
//                    }
//                }
//        );
        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);
//        assertAll(() -> assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag),
//                () -> assertEquals(testModel.getExpectedAllDiagnoses(), allDiag));
    }

    @Test
    void fastDiagV3_4() throws Exception {
        TestModel4 testModel = new TestModel4();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(ConstraintUtils.convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // cloneable
        TestModel4 testModel1 = (TestModel4) testModel.clone();
        testModel1.initialize();
        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel1);

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        CAEvaluator.reset();
        Set<Constraint> firstDiag = fastDiag.findDiagnosis(C, B);

//        List<Set<Constraint>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
//        System.out.println(convertToStringWithMessage(allDiag, "Diagnosis"));
        CAEvaluator.printPerformance();

//        assertAll(() -> assertEquals(testModel.getExpectedFirstDiagnosis().size(), firstDiag.size()),
//                () -> {
//                    for (Constraint constraint : firstDiag) {
//                        assertTrue(testModel.getExpectedFirstDiagnosis().contains(constraint));
//                    }},
//                () -> assertEquals(testModel.getExpectedAllDiagnoses().size(), allDiag.size()),
//                () -> {
//                    for (int i = 0; i < allDiag.size(); i++) {
//                        Set<Constraint> expectedDiagnoses = testModel.getExpectedAllDiagnoses().get(i);
//                        Set<Constraint> actualDiagnoses = allDiag.get(i);
//                        for (Constraint constraint : actualDiagnoses) {
//                            assertTrue(expectedDiagnoses.contains(constraint));
//                        }
//                    }
//                }
//        );
        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);
//        assertAll(() -> assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag),
//                () -> assertEquals(testModel.getExpectedAllDiagnoses(), allDiag));
    }

    @Test
    void fastDiagV3_5() throws Exception {
        TestModel5 testModel = new TestModel5();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(ConstraintUtils.convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // cloneable
        TestModel5 testModel1 = (TestModel5) testModel.clone();
        testModel1.initialize();
        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel1);

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        CAEvaluator.reset();
        Set<Constraint> firstDiag = fastDiag.findDiagnosis(C, B);

//        List<Set<Constraint>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
//        System.out.println(convertToStringWithMessage(allDiag, "Diagnosis"));
        CAEvaluator.printPerformance();

//        assertAll(() -> assertEquals(testModel.getExpectedFirstDiagnosis().size(), firstDiag.size()),
//                () -> {
//                    for (Constraint constraint : firstDiag) {
//                        assertTrue(testModel.getExpectedFirstDiagnosis().contains(constraint));
//                    }},
//                () -> assertEquals(testModel.getExpectedAllDiagnoses().size(), allDiag.size()),
//                () -> {
//                    for (int i = 0; i < allDiag.size(); i++) {
//                        Set<Constraint> expectedDiagnoses = testModel.getExpectedAllDiagnoses().get(i);
//                        Set<Constraint> actualDiagnoses = allDiag.get(i);
//                        for (Constraint constraint : actualDiagnoses) {
//                            assertTrue(expectedDiagnoses.contains(constraint));
//                        }
//                    }
//                }
//        );
        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);
//        assertAll(() -> assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag),
//                () -> assertEquals(testModel.getExpectedAllDiagnoses(), allDiag));
    }

    @Test
    @SuppressWarnings("unchecked")
    void directDebug1() throws FeatureModelParserException, IOException, CloneNotSupportedException {
        File fileFM = new File("src/test/resources/FM_10_0.splx");
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(fileFM);

        TestSuiteReader factory = new TestSuiteReader();
        FMTestCaseBuilder testCaseFactory = new FMTestCaseBuilder();
        @Cleanup InputStream is = IOUtils.getInputStream(DirectDebugTest.class.getClassLoader(), "FM_10_0.testcases");

        TestSuite testSuite = factory.read(is, testCaseFactory);

        FMTestCaseTranslator translator = new FMTestCaseTranslator();
        FMDebuggingModel<Feature, AbstractRelationship<Feature>, CTConstraint> debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, translator,
                false, true, false);
        debuggingModel.initialize();

        // cloneable
        FMDebuggingModel<Feature, AbstractRelationship<Feature>, CTConstraint> debuggingModel1 = (FMDebuggingModel<Feature, AbstractRelationship<Feature>, CTConstraint>) debuggingModel.clone();
        debuggingModel1.initialize();
        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(debuggingModel1);

        DirectDebug directDebug = new DirectDebug(checker);

        CAEvaluator.reset();
        Map.Entry<Set<ITestCase>, Set<Constraint>> result = directDebug.findDiagnosis(debuggingModel.getPossiblyFaultyConstraints(),
                debuggingModel.getCorrectConstraints(),
                debuggingModel.getTestcases());
        Set<Constraint> diag = result.getValue();

        if (!diag.isEmpty()) {
            System.out.println("\t\tDiag: " + diag);
            System.out.println("\t\tRuntime: " + ((double) PerformanceEvaluator.getTimer(DirectDebug.TIMER_DIRECTDEBUG).total() / 1_000_000_000.0));
        } else {
            System.out.println("\t\tNO DIAGNOSIS----------------");
        }
        System.out.println("\t\tThe number of consistency check calls:" + (PerformanceEvaluator.getCounter(CAEvaluator.COUNTER_CONSISTENCY_CHECKS).getValue()));

        Set<Constraint> cs = new LinkedHashSet<>();
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 6));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 3));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 1));

        assertEquals(diag, cs);
    }

    @Test
    @SuppressWarnings("unchecked")
    void directDebug2() throws FeatureModelParserException, IOException, CloneNotSupportedException {
        File fileFM = new File("src/test/resources/FM_10_1.splx");
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(fileFM);

        TestSuiteReader factory = new TestSuiteReader();
        FMTestCaseBuilder testCaseFactory = new FMTestCaseBuilder();
        @Cleanup InputStream is = IOUtils.getInputStream(DirectDebugTest.class.getClassLoader(), "FM_10_1.testcases");

        TestSuite testSuite = factory.read(is, testCaseFactory);

        FMTestCaseTranslator translator = new FMTestCaseTranslator();
        FMDebuggingModel<Feature, AbstractRelationship<Feature>, CTConstraint> debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, translator,
                false, true, false);
        debuggingModel.initialize();

        // cloneable
        FMDebuggingModel<Feature, AbstractRelationship<Feature>, CTConstraint> debuggingModel1 = (FMDebuggingModel<Feature, AbstractRelationship<Feature>, CTConstraint>) debuggingModel.clone();
        debuggingModel1.initialize();
        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(debuggingModel1);

        // KBDIAG
        DirectDebug directDebug = new DirectDebug(checker);

        CAEvaluator.reset();
        Map.Entry<Set<ITestCase>, Set<Constraint>> result = directDebug.findDiagnosis(debuggingModel.getPossiblyFaultyConstraints(),
                debuggingModel.getCorrectConstraints(),
                debuggingModel.getTestcases());
        Set<Constraint> diag = result.getValue();

        if (!diag.isEmpty()) {
            System.out.println("\t\tDiag: " + diag);
            System.out.println("\t\tRuntime: " + ((double) PerformanceEvaluator.getTimer(DirectDebug.TIMER_DIRECTDEBUG).total() / 1_000_000_000.0));
        } else {
            System.out.println("\t\tNO DIAGNOSIS----------------");
        }
        System.out.println("\t\tThe number of consistency check calls:" + (PerformanceEvaluator.getCounter(CAEvaluator.COUNTER_CONSISTENCY_CHECKS).getValue()));
        System.out.println("\t\tThe number of Solver calls:" + (PerformanceEvaluator.getCounter(CAEvaluator.COUNTER_CHOCO_SOLVER_CALLS).getValue()));

        Set<Constraint> cs = new LinkedHashSet<>();
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 0));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 3));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 6));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 7));

        assertEquals(diag, cs);
    }

    @Test
    @SuppressWarnings("unchecked")
    void directDebug3() throws FeatureModelParserException, IOException, CloneNotSupportedException {
        File fileFM = new File("src/test/resources/FM_10_2.splx");
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(fileFM);

        TestSuiteReader factory = new TestSuiteReader();
        FMTestCaseBuilder testCaseFactory = new FMTestCaseBuilder();
        @Cleanup InputStream is = IOUtils.getInputStream(DirectDebugTest.class.getClassLoader(), "FM_10_2.testcases");

        TestSuite testSuite = factory.read(is, testCaseFactory);

        FMTestCaseTranslator translator = new FMTestCaseTranslator();
        FMDebuggingModel<Feature, AbstractRelationship<Feature>, CTConstraint> debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, translator,
                false, true, false);
        debuggingModel.initialize();

        // cloneable
        FMDebuggingModel<Feature, AbstractRelationship<Feature>, CTConstraint> debuggingModel1 = (FMDebuggingModel<Feature, AbstractRelationship<Feature>, CTConstraint>) debuggingModel.clone();
        debuggingModel1.initialize();
        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(debuggingModel1);

        // KBDIAG
        DirectDebug directDebug = new DirectDebug(checker);

        CAEvaluator.reset();
        Map.Entry<Set<ITestCase>, Set<Constraint>> result = directDebug.findDiagnosis(debuggingModel.getPossiblyFaultyConstraints(),
                debuggingModel.getCorrectConstraints(),
                debuggingModel.getTestcases());
        Set<Constraint> diag = result.getValue();

        if (!diag.isEmpty()) {
            System.out.println("\t\tDiag: " + diag);
            System.out.println("\t\tRuntime: " + ((double) PerformanceEvaluator.getTimer(DirectDebug.TIMER_DIRECTDEBUG).total() / 1_000_000_000.0));
        } else {
            System.out.println("\t\tNO DIAGNOSIS----------------");
        }
        System.out.println("\t\tThe number of consistency check calls:" + (PerformanceEvaluator.getCounter(CAEvaluator.COUNTER_CONSISTENCY_CHECKS).getValue()));
        System.out.println("\t\tThe number of Solver calls:" + (PerformanceEvaluator.getCounter(CAEvaluator.COUNTER_CHOCO_SOLVER_CALLS).getValue()));

        Set<Constraint> cs = new LinkedHashSet<>();
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 0));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 3));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 4));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 5));

        assertEquals(diag, cs);
    }
}