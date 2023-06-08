/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr.algorithms;

import at.tugraz.ist.ase.hiconfit.cacdr.algorithms.hs.HSDAG;
import at.tugraz.ist.ase.hiconfit.cacdr.algorithms.hs.HSDAGPruningEngine;
import at.tugraz.ist.ase.hiconfit.cacdr.algorithms.hs.labeler.DirectDebugLabeler;
import at.tugraz.ist.ase.hiconfit.cacdr.algorithms.hs.parameters.DirectDebugParameters;
import at.tugraz.ist.ase.hiconfit.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.hiconfit.cacdr.eval.CAEvaluator;
import at.tugraz.ist.ase.hiconfit.cdrmodel.fm.FMDebuggingModel;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test.ITestCase;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test.TestSuite;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test.builder.fm.FMTestCaseBuilder;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test.reader.TestSuiteReader;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test.translator.fm.FMTestCaseTranslator;
import at.tugraz.ist.ase.hiconfit.common.IOUtils;
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
import java.util.*;

import static at.tugraz.ist.ase.hiconfit.cacdr.algorithms.DirectDebug.TIMER_DIRECTDEBUG;
import static at.tugraz.ist.ase.hiconfit.cacdr.eval.CAEvaluator.COUNTER_CHOCO_SOLVER_CALLS;
import static at.tugraz.ist.ase.hiconfit.cacdr.eval.CAEvaluator.COUNTER_CONSISTENCY_CHECKS;
import static at.tugraz.ist.ase.hiconfit.eval.PerformanceEvaluator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectDebugTest {

    @Test
    void testDirectDebug1() throws FeatureModelParserException, IOException {
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

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(debuggingModel);

        DirectDebug directDebug = new DirectDebug(checker);

        CAEvaluator.reset();
        setCommonTimer(TIMER_DIRECTDEBUG);
        Map.Entry<Set<ITestCase>, Set<Constraint>> result = directDebug.findDiagnosis(debuggingModel.getPossiblyFaultyConstraints(),
                                                            debuggingModel.getCorrectConstraints(),
                                                            debuggingModel.getTestcases());
        Set<Constraint> diag = result.getValue();

        if (!diag.isEmpty()) {
            System.out.println("\t\tDiag: " + diag);
            System.out.println("\t\tRuntime: " + ((double) totalCommonTimer(TIMER_DIRECTDEBUG) / 1_000_000_000.0));
        } else {
            System.out.println("\t\tNO DIAGNOSIS----------------");
        }
        System.out.println("\t\tThe number of consistency check calls:" + (getCounter(COUNTER_CONSISTENCY_CHECKS).getValue()));

        Set<Constraint> cs = new LinkedHashSet<>();
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 6));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 3));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 1));

        assertEquals(diag, cs);
    }

    @Test
    void testAllDiagnoses1() throws FeatureModelParserException, IOException {
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

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(debuggingModel);

        // run the hstree to find diagnoses
        DirectDebugParameters params = DirectDebugParameters.builder()
                .C(debuggingModel.getPossiblyFaultyConstraints())
                .B(debuggingModel.getCorrectConstraints())
                .TV(Collections.emptySet())
                .TC(debuggingModel.getTestcases()).build();
        DirectDebugLabeler directDebug = new DirectDebugLabeler(checker, params);

        HSDAG hsdag = new HSDAG(directDebug);
        hsdag.setPruningEngine(new HSDAGPruningEngine(hsdag));

        CAEvaluator.reset();
        hsdag.construct();

//        List<Set<Constraint>> allConflictSets = hsdag.getDiagnoses();
        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();

        if (!allDiagnoses.isEmpty()) {
            System.out.println("\t\t\t" + allDiagnoses.size() + " diagnoses found");

//            int counter = 0;
            for (Set<Constraint> diag : allDiagnoses) {
                System.out.println("\t\t\tDiag: " + diag);
                System.out.println("\t\t\tDiag size: " + diag.size());

//                double timeMSSDirect = (double) getTimer(TIMER_DIRECTDEBUG).getTimings().get(counter) / 1_000_000_000.0;
//                System.out.println("\t\t\tTime for mssDirect: " + timeMSSDirect);
//                counter = counter + 1;
            }
        } else {
            System.out.println("\t\t\tNO DIAGNOSIS----------------");
        }
        System.out.println("\t\tThe number of consistency check calls:" + (getCounter(COUNTER_CONSISTENCY_CHECKS).getValue()));

        Set<Constraint> cs = new LinkedHashSet<>();
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 6));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 3));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 1));

        assertEquals(allDiagnoses.size(), 1);
        assertEquals(allDiagnoses.get(0), cs);
    }

    @Test
    void testDirectDebug2() throws FeatureModelParserException, IOException {
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

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(debuggingModel);

        // KBDIAG
        DirectDebug directDebug = new DirectDebug(checker);

        CAEvaluator.reset();
        Map.Entry<Set<ITestCase>, Set<Constraint>> result = directDebug.findDiagnosis(debuggingModel.getPossiblyFaultyConstraints(),
                debuggingModel.getCorrectConstraints(),
                debuggingModel.getTestcases());
        Set<Constraint> diag = result.getValue();

        if (!diag.isEmpty()) {
            System.out.println("\t\tDiag: " + diag);
            System.out.println("\t\tRuntime: " + ((double) getTimer(TIMER_DIRECTDEBUG).total() / 1_000_000_000.0));
        } else {
            System.out.println("\t\tNO DIAGNOSIS----------------");
        }
        System.out.println("\t\tThe number of consistency check calls:" + (getCounter(COUNTER_CONSISTENCY_CHECKS).getValue()));
        System.out.println("\t\tThe number of Solver calls:" + (getCounter(COUNTER_CHOCO_SOLVER_CALLS).getValue()));

        Set<Constraint> cs = new LinkedHashSet<>();
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 0));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 3));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 6));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 7));

        assertEquals(diag, cs);
    }

    @Test
    void testAllDiagnoses2() throws FeatureModelParserException, IOException {
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

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(debuggingModel);

        // run the hstree to find diagnoses
        DirectDebugParameters params = DirectDebugParameters.builder()
                .C(debuggingModel.getPossiblyFaultyConstraints())
                .B(debuggingModel.getCorrectConstraints())
                .TV(Collections.emptySet())
                .TC(debuggingModel.getTestcases()).build();
        DirectDebugLabeler directDebug = new DirectDebugLabeler(checker, params);

        HSDAG hsdag = new HSDAG(directDebug);
        hsdag.setPruningEngine(new HSDAGPruningEngine(hsdag));

        CAEvaluator.reset();
        hsdag.construct();

//        List<Set<Constraint>> allConflictSets = hsdag.getDiagnoses();
        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();

        if (!allDiagnoses.isEmpty()) {
            System.out.println("\t\t\t" + allDiagnoses.size() + " diagnoses found");

//            int counter = 0;
            for (Set<Constraint> diag : allDiagnoses) {
                System.out.println("\t\t\tDiag: " + diag);
                System.out.println("\t\t\tDiag size: " + diag.size());

//                double timeMSSDirect = (double) getTimer(TIMER_MSSDIRECT).getTimings().get(counter) / 1_000_000_000.0;
//                System.out.println("\t\t\tTime for mssDirect: " + timeMSSDirect);
//                counter = counter + 1;
            }
        } else {
            System.out.println("\t\t\tNO DIAGNOSIS----------------");
        }

        Set<Constraint> cs = new LinkedHashSet<>();
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 0));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 3));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 6));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 7));

        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 0));
        cs1.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 1));
        cs1.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 3));

        assertEquals(allDiagnoses.size(), 2);
        assertEquals(allDiagnoses.get(0), cs);
        assertEquals(allDiagnoses.get(1), cs1);
    }

    @Test
    void testDirectDebug3() throws FeatureModelParserException, IOException {
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

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(debuggingModel);

        // KBDIAG
        DirectDebug directDebug = new DirectDebug(checker);

        CAEvaluator.reset();
        Map.Entry<Set<ITestCase>, Set<Constraint>> result = directDebug.findDiagnosis(debuggingModel.getPossiblyFaultyConstraints(),
                debuggingModel.getCorrectConstraints(),
                debuggingModel.getTestcases());
        Set<Constraint> diag = result.getValue();

        if (!diag.isEmpty()) {
            System.out.println("\t\tDiag: " + diag);
            System.out.println("\t\tRuntime: " + ((double) getTimer(TIMER_DIRECTDEBUG).total() / 1_000_000_000.0));
        } else {
            System.out.println("\t\tNO DIAGNOSIS----------------");
        }
        System.out.println("\t\tThe number of consistency check calls:" + (getCounter(COUNTER_CONSISTENCY_CHECKS).getValue()));
        System.out.println("\t\tThe number of Solver calls:" + (getCounter(COUNTER_CHOCO_SOLVER_CALLS).getValue()));

        Set<Constraint> cs = new LinkedHashSet<>();
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 0));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 3));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 4));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 5));

        assertEquals(diag, cs);
    }

    @Test
    void testAllDiagnoses3() throws FeatureModelParserException, IOException {
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

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(debuggingModel);

        // run the hstree to find diagnoses
        DirectDebugParameters params = DirectDebugParameters.builder()
                .C(debuggingModel.getPossiblyFaultyConstraints())
                .B(debuggingModel.getCorrectConstraints())
                .TV(Collections.emptySet())
                .TC(debuggingModel.getTestcases()).build();
        DirectDebugLabeler directDebug = new DirectDebugLabeler(checker, params);

        HSDAG hsdag = new HSDAG(directDebug);
        hsdag.setPruningEngine(new HSDAGPruningEngine(hsdag));

        CAEvaluator.reset();
        hsdag.construct();

//        List<Set<Constraint>> allConflictSets = hsdag.getDiagnoses();
        List<Set<Constraint>> allDiagnoses = hsdag.getDiagnoses();

        if (!allDiagnoses.isEmpty()) {
            System.out.println("\t\t\t" + allDiagnoses.size() + " diagnoses found");

//            int counter = 0;
            for (Set<Constraint> diag : allDiagnoses) {
                System.out.println("\t\t\tDiag: " + diag);
                System.out.println("\t\t\tDiag size: " + diag.size());

//                double timeMSSDirect = (double) getTimer(TIMER_MSSDIRECT).getTimings().get(counter) / 1_000_000_000.0;
//                System.out.println("\t\t\tTime for mssDirect: " + timeMSSDirect);
//                counter = counter + 1;
            }
        } else {
            System.out.println("\t\t\tNO DIAGNOSIS----------------");
        }

        Set<Constraint> cs = new LinkedHashSet<>();
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 0));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 3));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 4));
        cs.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 5));

        assertEquals(allDiagnoses.get(0), cs);
    }
}