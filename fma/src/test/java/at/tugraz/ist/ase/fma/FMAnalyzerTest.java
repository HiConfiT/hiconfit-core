/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma;

import at.tugraz.ist.ase.cdrmodel.fm.FMCdrModel;
import at.tugraz.ist.ase.cdrmodel.fm.FMDebuggingModel;
import at.tugraz.ist.ase.cdrmodel.test.ITestCase;
import at.tugraz.ist.ase.cdrmodel.test.TestSuite;
import at.tugraz.ist.ase.cdrmodel.test.translator.fm.FMTestCaseTranslator;
import at.tugraz.ist.ase.common.ConsoleColors;
import at.tugraz.ist.ase.common.ConstraintUtils;
import at.tugraz.ist.ase.fm.builder.*;
import at.tugraz.ist.ase.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.fm.core.CTConstraint;
import at.tugraz.ist.ase.fm.core.Feature;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import at.tugraz.ist.ase.fm.parser.FMParserFactory;
import at.tugraz.ist.ase.fm.parser.FeatureModelParser;
import at.tugraz.ist.ase.fm.parser.FeatureModelParserException;
import at.tugraz.ist.ase.fm.translator.ConfRuleTranslator;
import at.tugraz.ist.ase.fma.analysis.*;
import at.tugraz.ist.ase.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.fma.anomaly.AnomalyAwareFeatureBuilder;
import at.tugraz.ist.ase.fma.anomaly.AnomalyType;
import at.tugraz.ist.ase.fma.assumption.*;
import at.tugraz.ist.ase.fma.explanator.*;
import at.tugraz.ist.ase.fma.test.AssumptionAwareTestCase;
import at.tugraz.ist.ase.kb.core.Constraint;
import com.google.common.collect.Iterators;
import lombok.Cleanup;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class FMAnalyzerTest {
    static FMParserFactory<Feature, AbstractRelationship<Feature>, CTConstraint> factory = FMParserFactory.getInstance();

    @Test
    void testVoidFM() throws FeatureModelParserException, ExecutionException, InterruptedException {
        // load the feature model
        File fileFM = new File("src/test/resources/bamboobike_featureide_void.xml");
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = factory.getParser(fileFM.getName());
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(fileFM);

        // create a test case/assumption
        // check void feature model - inconsistent( CF ∪ { c0 })
        VoidFMAssumption voidFMAssumption = new VoidFMAssumption();
        List<ITestCase> testCases = voidFMAssumption.createAssumptions(featureModel);
        TestSuite testSuite = TestSuite.builder().testCases(testCases).build();

        FMDebuggingModel<Feature, AbstractRelationship<Feature>, CTConstraint> debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, new FMTestCaseTranslator(), false, false, false);
        debuggingModel.initialize();

        // create the specified analysis and the corresponding explanator
        VoidFMAnalysis analysis = new VoidFMAnalysis(debuggingModel, testCases.get(0));
        VoidFMExplanator explanator = new VoidFMExplanator(debuggingModel, testCases.get(0));

        FMAnalyzer analyzer = new FMAnalyzer();
        analyzer.addAnalysis(analysis, explanator); // add the analysis to the analyzer
        analyzer.run(); // run the analyzer

        // print the result
        ExplanationColors.EXPLANATION = ConsoleColors.WHITE;
        if (analysis.get()) {
            System.out.println(ExplanationColors.OK + "\u2713 Consistency: ok");
        } else {
            System.out.println(ExplanationColors.ANOMALY + "X Void feature model");
            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(explanator.get(), "void feature model"));
        }

        assertFalse(analysis.get());

        List<Set<Constraint>> allDiagnoses = explanator.get();

        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 8));

        Set<Constraint> cs2 = new LinkedHashSet<>();
        cs2.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 1));

        Set<Constraint> cs3 = new LinkedHashSet<>();
        cs3.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 0));

        assertEquals(3, allDiagnoses.size());
        assertEquals(cs1, allDiagnoses.get(0));
        assertEquals(cs2, allDiagnoses.get(1));
        assertEquals(cs3, allDiagnoses.get(2));
    }

    @Test
    void testDeadFeature_1() throws FeatureModelParserException, ExecutionException, InterruptedException {
        // load the feature model
        File fileFM = new File("src/test/resources/bamboobike_featureide_deadfeature1.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        // create a test case/assumption
        // check void feature model - inconsistent( CF ∪ { c0 })
        VoidFMAssumption voidFMAssumption = new VoidFMAssumption();
        List<ITestCase> testCases = voidFMAssumption.createAssumptions(featureModel);
        TestSuite testSuite = TestSuite.builder().testCases(testCases).build();

        FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, new FMTestCaseTranslator(), false, false, false);
        debuggingModel.initialize();

        // create the specified analysis and the corresponding explanator
        VoidFMAnalysis analysis1 = new VoidFMAnalysis(debuggingModel, testCases.get(0));
        VoidFMExplanator explanator1 = new VoidFMExplanator(debuggingModel, testCases.get(0));

        // create a test case/assumption
        // check dead features - inconsistent( CF ∪ { c0 } U { fi = true })
        DeadFeatureAssumptions deadFeatureAssumptions = new DeadFeatureAssumptions();
        testCases = deadFeatureAssumptions.createAssumptions(featureModel);
        testSuite = TestSuite.builder().testCases(testCases).build();

        debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, new FMTestCaseTranslator(), false, false, false);
        debuggingModel.initialize();

        // create the specified analysis and the corresponding explanator
        DeadFeatureAnalysis analysis2 = new DeadFeatureAnalysis(debuggingModel, testCases.get(6)); // check the feature Step-through
        DeadFeatureExplanator explanator2 = new DeadFeatureExplanator(debuggingModel, testCases.get(6));

        FMAnalyzer analyzer = new FMAnalyzer();
        analyzer.addAnalysis(analysis1, explanator1); // add the analysis to the analyzer
        analyzer.addAnalysis(analysis2, explanator2); // add the analysis to the analyzer
        analyzer.run(); // run the analyzer

        // print the result
        ExplanationColors.EXPLANATION = ConsoleColors.WHITE;
        if (analysis1.get()) {
            System.out.println(ExplanationColors.OK + "\u2713 Consistency: ok");
        } else {
            System.out.println(ExplanationColors.ANOMALY + "X Void feature model");
            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(explanator1.get(), "void feature model"));
        }
        if (!analysis2.get()) {
            System.out.println(ExplanationColors.ANOMALY + "X Dead feature");
            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(explanator2.get(), "dead feature"));
        }

        assertTrue(analysis1.get());
        assertFalse(analysis2.get());

        List<Set<Constraint>> allDiagnoses = explanator2.get();

        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 8));

        Set<Constraint> cs2 = new LinkedHashSet<>();
        cs2.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 1));

        assertEquals(2, allDiagnoses.size());
        assertEquals(cs1, allDiagnoses.get(0));
        assertEquals(cs2, allDiagnoses.get(1));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testDeadFeature_2() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
        // load the feature model
        File fileFM = new File("src/test/resources/bamboobike_featureide_deadfeature2.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        // create a test case/assumption
        // check void feature model - inconsistent( CF ∪ { c0 })
        VoidFMAssumption voidFMAssumption = new VoidFMAssumption();
        List<ITestCase> testCases = voidFMAssumption.createAssumptions(featureModel);
        TestSuite testSuite = TestSuite.builder().testCases(testCases).build();

        FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, new FMTestCaseTranslator(), false, false, false);
        debuggingModel.initialize();

        // create the specified analysis and the corresponding explanator
        VoidFMAnalysis analysis1 = new VoidFMAnalysis(debuggingModel, testCases.get(0));
        VoidFMExplanator explanator1 = new VoidFMExplanator(debuggingModel, testCases.get(0));

        // create a test case/assumption
        // check dead features - inconsistent( CF ∪ { c0 } U { fi = true })
        DeadFeatureAssumptions deadFeatureAssumptions = new DeadFeatureAssumptions();
        testCases = deadFeatureAssumptions.createAssumptions(featureModel);
        testSuite = TestSuite.builder().testCases(testCases).build();

        debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, new FMTestCaseTranslator(), false, false, false);
        debuggingModel.initialize();

        // create the specified analysis and the corresponding explanator
        DeadFeatureAnalysis analysis2 = new DeadFeatureAnalysis(debuggingModel, testCases.get(4)); // check the feature Female
        DeadFeatureExplanator explanator2 = new DeadFeatureExplanator(debuggingModel, testCases.get(4));

        // check the feature Step-through
        FMDebuggingModel<Feature, AbstractRelationship<Feature>, CTConstraint> debuggingModel1 = (FMDebuggingModel<Feature, AbstractRelationship<Feature>, CTConstraint>) debuggingModel.clone();
        debuggingModel1.initialize();

        DeadFeatureAnalysis analysis3 = new DeadFeatureAnalysis(debuggingModel1, testCases.get(6)); // check the feature Step-through
        DeadFeatureExplanator explanator3 = new DeadFeatureExplanator(debuggingModel1, testCases.get(6));

        FMAnalyzer analyzer = new FMAnalyzer();
        analyzer.addAnalysis(analysis1, explanator1); // add the analysis to the analyzer
        analyzer.addAnalysis(analysis2, explanator2); // add the analysis to the analyzer
        analyzer.addAnalysis(analysis3, explanator3); // add the analysis to the analyzer
        analyzer.run(); // run the analyzer

        // print the result
        ExplanationColors.EXPLANATION = ConsoleColors.WHITE;
        if (analysis1.get()) {
            System.out.println(ExplanationColors.OK + "\u2713 Consistency: ok");
        } else {
            System.out.println(ExplanationColors.ANOMALY + "X Void feature model");
            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(explanator1.get(), "void feature model"));
        }
        if (!analysis2.get()) {
            System.out.println(ExplanationColors.ANOMALY + "X Dead feature - Female");
            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(explanator2.get(), "dead feature"));
        }
        if (!analysis3.get()) {
            System.out.println(ExplanationColors.ANOMALY + "X Dead feature - Step-through");
            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(explanator2.get(), "dead feature"));
        }

        assertTrue(analysis1.get());
        assertFalse(analysis2.get());
        assertFalse(analysis3.get());

        List<Set<Constraint>> allDiagnoses = explanator2.get();
        List<Set<Constraint>> allDiagnoses1 = explanator3.get();

        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 8));

        Set<Constraint> cs2 = new LinkedHashSet<>();
        cs2.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 4));

        Set<Constraint> cs3 = new LinkedHashSet<>();
        cs3.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 1));

        assertEquals(3, allDiagnoses.size());
        assertEquals(cs1, allDiagnoses.get(0));
        assertEquals(cs2, allDiagnoses.get(1));
        assertEquals(cs3, allDiagnoses.get(2));

        assertEquals(3, allDiagnoses1.size());
        assertEquals(cs1, allDiagnoses1.get(0));
        assertEquals(cs2, allDiagnoses1.get(1));
        assertEquals(cs3, allDiagnoses1.get(2));
    }

    @Test
    void testDeadFeature_3() throws FeatureModelParserException, ExecutionException, InterruptedException {
        // load the feature model
        File fileFM = new File("src/test/resources/bamboobike_featureide_deadfeature3.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        // create a test case/assumption
        // check void feature model - inconsistent( CF ∪ { c0 })
        VoidFMAssumption voidFMAssumption = new VoidFMAssumption();
        List<ITestCase> testCases = voidFMAssumption.createAssumptions(featureModel);
        TestSuite testSuite = TestSuite.builder().testCases(testCases).build();

        FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, new FMTestCaseTranslator(), false, false, false);
        debuggingModel.initialize();

        // create the specified analysis and the corresponding explanator
        VoidFMAnalysis analysis1 = new VoidFMAnalysis(debuggingModel, testCases.get(0));
        VoidFMExplanator explanator1 = new VoidFMExplanator(debuggingModel, testCases.get(0));

        // create a test case/assumption
        // check dead features - inconsistent( CF ∪ { c0 } U { fi = true })
        DeadFeatureAssumptions deadFeatureAssumptions = new DeadFeatureAssumptions();
        testCases = deadFeatureAssumptions.createAssumptions(featureModel);
        testSuite = TestSuite.builder().testCases(testCases).build();

        debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, new FMTestCaseTranslator(), false, false, false);
        debuggingModel.initialize();

        // create the specified analysis and the corresponding explanator
        DeadFeatureAnalysis analysis2 = new DeadFeatureAnalysis(debuggingModel, testCases.get(3)); // check the feature Drop Handlebar
        DeadFeatureExplanator explanator2 = new DeadFeatureExplanator(debuggingModel, testCases.get(3));

        FMAnalyzer analyzer = new FMAnalyzer();
        analyzer.addAnalysis(analysis1, explanator1); // add the analysis to the analyzer
        analyzer.addAnalysis(analysis2, explanator2); // add the analysis to the analyzer
        analyzer.run(); // run the analyzer

        // print the result
        ExplanationColors.EXPLANATION = ConsoleColors.WHITE;
        if (analysis1.get()) {
            System.out.println(ExplanationColors.OK + "\u2713 Consistency: ok");
        } else {
            System.out.println(ExplanationColors.ANOMALY + "X Void feature model");
            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(explanator1.get(), "void feature model"));
        }
        if (!analysis2.get()) {
            System.out.println(ExplanationColors.ANOMALY + "X Dead feature");
            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(explanator2.get(), "dead feature"));
        }

        assertTrue(analysis1.get());
        assertFalse(analysis2.get());

        List<Set<Constraint>> allDiagnoses = explanator2.get();

        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 8));

        Set<Constraint> cs2 = new LinkedHashSet<>();
        cs2.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 0));
        cs2.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 6));

        Set<Constraint> cs3 = new LinkedHashSet<>();
        cs3.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 0));
        cs3.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 4));

        assertEquals(3, allDiagnoses.size());
        assertEquals(cs1, allDiagnoses.get(0));
        assertEquals(cs2, allDiagnoses.get(1));
        assertEquals(cs3, allDiagnoses.get(2));
    }

    @Test
    void testFullMandatory() throws FeatureModelParserException, ExecutionException, InterruptedException {
        File fileFM = new File("src/test/resources/basic_featureide_fullmandatory1.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        // create a test case/assumption
        // check for full mandatory features
        FullMandatoryAssumptions fullMandatoryAssumptions = new FullMandatoryAssumptions();
        List<ITestCase> testCases = fullMandatoryAssumptions.createAssumptions(featureModel);
        TestSuite testSuite = TestSuite.builder().testCases(testCases).build();

        FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, new FMTestCaseTranslator(), false, false, false);
        debuggingModel.initialize();

        // create the specified analysis and the corresponding explanator
        FullMandatoryAnalysis analysis = new FullMandatoryAnalysis(debuggingModel, testCases.get(2));
        FullMandatoryExplanator explanator = new FullMandatoryExplanator(debuggingModel, testCases.get(2));

        FMAnalyzer analyzer = new FMAnalyzer();
        analyzer.addAnalysis(analysis, explanator); // add the analysis to the analyzer
        analyzer.run(); // run the analyzer

        // print the result
        ExplanationColors.EXPLANATION = ConsoleColors.WHITE;
        if (analysis.get()) {
            System.out.println(ExplanationColors.OK + "\u2713 Consistency: ok");
        } else {
            System.out.println(ExplanationColors.ANOMALY + "X Full mandatory feature");
            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(explanator.get(), "full mandatory feature"));
        }

        assertFalse(analysis.get());

        List<Set<Constraint>> allDiagnoses = explanator.get();

        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 2));

        Set<Constraint> cs2 = new LinkedHashSet<>();
        cs2.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 0));

        assertEquals(2, allDiagnoses.size());
        assertEquals(cs1, allDiagnoses.get(0));
        assertEquals(cs2, allDiagnoses.get(1));
    }

    @Test
    void testFalseOptional() throws FeatureModelParserException, ExecutionException, InterruptedException {
        File fileFM = new File("src/test/resources/basic_featureide_falseoptional1.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        // create a test case/assumption
        // check for false optional features
        FalseOptionalAssumptions falseOptionalAssumptions = new FalseOptionalAssumptions();
        List<ITestCase> testCases = falseOptionalAssumptions.createAssumptions(featureModel);
        TestSuite testSuite = TestSuite.builder().testCases(testCases).build();

        FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, new FMTestCaseTranslator(), false, false, false);
        debuggingModel.initialize();

        // create the specified analysis and the corresponding explanator
        FalseOptionalAnalysis analysis = new FalseOptionalAnalysis(debuggingModel, testCases.get(0));
        FalseOptionalExplanator explanator = new FalseOptionalExplanator(debuggingModel, testCases.get(0));

        testCases.forEach(System.out::println);
        featureModel.getConstraints().forEach(System.out::println);

        FMAnalyzer analyzer = new FMAnalyzer();
        analyzer.addAnalysis(analysis, explanator); // add the analysis to the analyzer
        analyzer.run(); // run the analyzer

        // print the result
        ExplanationColors.EXPLANATION = ConsoleColors.WHITE;
        if (analysis.get()) {
            System.out.println(ExplanationColors.OK + "\u2713 Consistency: ok");
        } else {
            System.out.println(ExplanationColors.ANOMALY + "X False optional feature");
            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(explanator.get(), "false optional feature"));
        }

        assertFalse(analysis.get());

        List<Set<Constraint>> allDiagnoses = explanator.get();

        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 2));

        assertEquals(1, allDiagnoses.size());
        assertEquals(cs1, allDiagnoses.get(0));
    }

    @Test
    void testConditionallyDead_0() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
        File fileFM = new File("src/test/resources/bamboobike_featureide_deadfeature2.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        // set Female and Step-through as dead
        featureModel.getFeature("Female").setAnomalyType(AnomalyType.DEAD);
        featureModel.getFeature("Step-through").setAnomalyType(AnomalyType.DEAD);

        // create a test case/assumption
        // check for conditionally dead features
        ConditionallyDeadAssumptions conditionallyDeadAssumptions = new ConditionallyDeadAssumptions();
        List<ITestCase> testCases = conditionallyDeadAssumptions.createAssumptions(featureModel);
        TestSuite testSuite = TestSuite.builder().testCases(testCases).build();

        FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, new FMTestCaseTranslator(), false, false, false);
        debuggingModel.initialize();

        // create the specified analysis and the corresponding explanator
        ITestCase testCase = testCases.get(4);
        ConditionallyDeadAnalysis analysis = new ConditionallyDeadAnalysis(debuggingModel, testCase); // Check feature Engine and Back-pedal
        ConditionallyDeadExplanator explanator = new ConditionallyDeadExplanator(debuggingModel, testCase);

        FMAnalyzer analyzer = new FMAnalyzer();
        analyzer.addAnalysis(analysis, explanator); // add the analysis to the analyzer
        analyzer.run(); // run the analyzer

        // print the result
        ExplanationColors.EXPLANATION = ConsoleColors.WHITE;
        if (analysis.get()) {
            System.out.println(ExplanationColors.OK + "\u2713 Consistency: ok");
        } else {
            System.out.println(ExplanationColors.ANOMALY + "X Conditionally dead feature: " + ((AssumptionAwareTestCase) testCase).getAssumptions());
            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(explanator.get(), "conditionally dead feature"));
        }

        assertFalse(analysis.get());

        List<Set<Constraint>> allDiagnoses = explanator.get();

        assertEquals(1, allDiagnoses.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testConditionallyDead_1() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
        File fileFM = new File("src/test/resources/basic_featureide_conditionallydead1.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        // create a test case/assumption
        // check for conditionally dead features
        ConditionallyDeadAssumptions conditionallyDeadAssumptions = new ConditionallyDeadAssumptions();
        List<ITestCase> testCases = conditionallyDeadAssumptions.createAssumptions(featureModel);
        TestSuite testSuite = TestSuite.builder().testCases(testCases).build();

        FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, new FMTestCaseTranslator(), false, false, false);
        debuggingModel.initialize();

        // create the specified analysis and the corresponding explanator
        ConditionallyDeadAnalysis analysis = new ConditionallyDeadAnalysis(debuggingModel, testCases.get(0)); // C & B
        ConditionallyDeadExplanator explanator = new ConditionallyDeadExplanator(debuggingModel, testCases.get(0));

        FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                debuggingModel2 = (FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>) debuggingModel.clone();
        debuggingModel2.initialize();

        ConditionallyDeadAnalysis analysis2 = new ConditionallyDeadAnalysis(debuggingModel2, testCases.get(1)); // D & B
        ConditionallyDeadExplanator explanator2 = new ConditionallyDeadExplanator(debuggingModel2, testCases.get(1));

        FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                debuggingModel3 = (FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>) debuggingModel.clone();
        debuggingModel3.initialize();

        ConditionallyDeadAnalysis analysis3 = new ConditionallyDeadAnalysis(debuggingModel3, testCases.get(2)); // D & C
        ConditionallyDeadExplanator explanator3 = new ConditionallyDeadExplanator(debuggingModel3, testCases.get(2));

        FMAnalyzer analyzer = new FMAnalyzer();
        analyzer.addAnalysis(analysis, explanator); // add the analysis to the analyzer
        analyzer.addAnalysis(analysis2, explanator2); // add the analysis to the analyzer
        analyzer.addAnalysis(analysis3, explanator3); // add the analysis to the analyzer
        analyzer.run(); // run the analyzer

        // print the result
        ExplanationColors.EXPLANATION = ConsoleColors.WHITE;
        if (analysis.get()) {
            System.out.println(ExplanationColors.OK + "\u2713 DConsistency: ok");
        } else {
            System.out.println(ExplanationColors.ANOMALY + "X Conditionally dead feature");
            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(explanator.get(), "conditionally dead feature"));
        }
        if (analysis2.get()) {
            System.out.println(ExplanationColors.OK + "\u2713 Consistency: ok");
        } else {
            System.out.println(ExplanationColors.ANOMALY + "X Conditionally dead feature");
            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(explanator2.get(), "conditionally dead feature"));
        }
        if (analysis3.get()) {
            System.out.println(ExplanationColors.OK + "\u2713 Consistency: ok");
        } else {
            System.out.println(ExplanationColors.ANOMALY + "X Conditionally dead feature");
            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(explanator3.get(), "conditionally dead feature"));
        }

        assertFalse(analysis2.get());
        assertFalse(analysis3.get());

        List<Set<Constraint>> allDiagnoses2 = explanator2.get();
        List<Set<Constraint>> allDiagnoses3 = explanator3.get();

        assertEquals(2, allDiagnoses2.size());
        assertEquals(1, allDiagnoses3.size());
    }

    @Test
    void testConditionallyDead_2() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
        File fileFM = new File("src/test/resources/basic_featureide_conditionallydead2.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        AutomatedAnalysisBuilder automatedAnalyzer = new AutomatedAnalysisBuilder();
        automatedAnalyzer.performFullAnalysis(featureModel);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMultiple_1() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
        File fileFM = new File("src/test/resources/basic_featureide_multiple1.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        /// VOID FEATURE MODEL
        // create a test case/assumption
        // check void feature model - inconsistent( CF ∪ { c0 })
        VoidFMAssumption voidFMAssumption = new VoidFMAssumption();
        List<ITestCase> testCases = voidFMAssumption.createAssumptions(featureModel);
        TestSuite testSuite = TestSuite.builder().testCases(testCases).build();

        FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, new FMTestCaseTranslator(), false, false, false);
        debuggingModel.initialize();

        // create the specified analysis and the corresponding explanator
        VoidFMAnalysis analysis = new VoidFMAnalysis(debuggingModel, testCases.get(0));
        VoidFMExplanator explanator = new VoidFMExplanator(debuggingModel, testCases.get(0));

        FMAnalyzer analyzer = new FMAnalyzer();
        analyzer.addAnalysis(analysis, explanator); // add the analysis to the analyzer
        analyzer.run(); // run the analyzer

        // print the result
        ExplanationColors.EXPLANATION = ConsoleColors.WHITE;
        if (analysis.get()) {
            System.out.println(ExplanationColors.OK + "\u2713 Consistency: ok");
        } else {
            System.out.println(ExplanationColors.ANOMALY + "X Void feature model");
            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(explanator.get(), "void feature model"));
        }

        assertTrue(analysis.get());

        List<List<AbstractFMAnalysis<?>>> allAnalyses = new ArrayList<>(Collections.emptyList());
        List<List<AbstractAnomalyExplanator<List<Set<Constraint>>>>> allExplanators = new ArrayList<>(Collections.emptyList());

        // prepare for collecting analyses and explanators
        for (AnomalyType a : AnomalyType.values()) {
            allAnalyses.add(new ArrayList<>(Collections.emptyList()));
            allExplanators.add(new ArrayList<>(Collections.emptyList()));
        }

        analyzer = new FMAnalyzer(); // TODO necessary?
        FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                debuggingModelClone;

        boolean anomalyFound = false;

        /// DEAD FEATURES
        // create a test case/assumption
        // check dead features - inconsistent( CF ∪ { c0 } U { fi = true })
        DeadFeatureAssumptions deadFeatureAssumptions = new DeadFeatureAssumptions();
        List<ITestCase> deadFeatureTestCases = deadFeatureAssumptions.createAssumptions(featureModel);
        TestSuite deadFeatureTestSuite = TestSuite.builder().testCases(deadFeatureTestCases).build();

        FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> deadFeatureDebuggingModel = new FMDebuggingModel<>(featureModel, deadFeatureTestSuite, new FMTestCaseTranslator(), false, false, false);
        deadFeatureDebuggingModel.initialize();

        for (ITestCase deadFeatureTestCase : deadFeatureTestCases) {
            // create the specified analyses and the corresponding explanators
            debuggingModelClone = (FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>) deadFeatureDebuggingModel.clone();
            debuggingModelClone.initialize();
            DeadFeatureAnalysis deadFeatureAnalysis = new DeadFeatureAnalysis(debuggingModelClone, deadFeatureTestCase);
            DeadFeatureExplanator deadFeatureExplanator = new DeadFeatureExplanator(debuggingModelClone, deadFeatureTestCase);
            analyzer.addAnalysis(deadFeatureAnalysis, deadFeatureExplanator); // add the analysis to the analyzer

            allAnalyses.get(AnomalyType.DEAD.ordinal()).add(deadFeatureAnalysis);
            allExplanators.get(AnomalyType.DEAD.ordinal()).add(deadFeatureExplanator);
        }

        analyzer.run(); // run the analyzer

        // Check the results and set dead features
        for (int runningAnalysis = 0; runningAnalysis < allAnalyses.get(AnomalyType.DEAD.ordinal()).size(); runningAnalysis++) {
            if (!allAnalyses.get(AnomalyType.DEAD.ordinal()).get(runningAnalysis).get()) {
                System.out.println(ExplanationColors.ANOMALY + "X Dead feature: " + ((AssumptionAwareTestCase) allAnalyses.get(AnomalyType.DEAD.ordinal()).get(runningAnalysis).getAssumption()).getAssumptions());
                System.out.println(ExplanationUtils.convertToDescriptiveExplanation(allExplanators.get(AnomalyType.DEAD.ordinal()).get(runningAnalysis).get(), "dead feature"));

                for (AnomalyAwareFeature deadFeature : ((AssumptionAwareTestCase) allAnalyses.get(AnomalyType.DEAD.ordinal()).get(runningAnalysis).getAssumption()).getAssumptions()) {
                    featureModel.getFeature(deadFeature.getId()).setAnomalyType(AnomalyType.DEAD);
                }

                anomalyFound = true;
            }
        }

        analyzer = new FMAnalyzer(); // TODO necessary?

        /// FULL MANDATORY
        // create a test case/assumption
        // check full mandatory features - inconsistent( CF ∪ { c0 } U { fi = false })
        FullMandatoryAssumptions fullMandatoryAssumptions = new FullMandatoryAssumptions();
        List<ITestCase> fullMandatoryTestCases = fullMandatoryAssumptions.createAssumptions(featureModel);
        TestSuite fullMandatoryTestSuite = TestSuite.builder().testCases(fullMandatoryTestCases).build();

        FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> fullMandatoryDebuggingModel = new FMDebuggingModel<>(featureModel, fullMandatoryTestSuite, new FMTestCaseTranslator(), false, false, false);
        fullMandatoryDebuggingModel.initialize();

        for (ITestCase fullMandatoryTestCase : fullMandatoryTestCases) {
            // create the specified analyses and the corresponding explanators
            debuggingModelClone = (FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>) fullMandatoryDebuggingModel.clone();
            debuggingModelClone.initialize();
            FullMandatoryAnalysis fullMandatoryAnalysis = new FullMandatoryAnalysis(debuggingModelClone, fullMandatoryTestCase);
            FullMandatoryExplanator fullMandatoryExplanator = new FullMandatoryExplanator(debuggingModelClone, fullMandatoryTestCase);
            analyzer.addAnalysis(fullMandatoryAnalysis, fullMandatoryExplanator); // add the analysis to the analyzer

            allAnalyses.get(AnomalyType.FULLMANDATORY.ordinal()).add(fullMandatoryAnalysis);
            allExplanators.get(AnomalyType.FULLMANDATORY.ordinal()).add(fullMandatoryExplanator);
        }

        // CONDITIONALLY DEAD
        // create a test case/assumption
        // check conditionally dead features - inconsistent( CF ∪ { c0 } U { fj = true } U { fi = true } ) for any fj
        ConditionallyDeadAssumptions conditionallyDeadAssumptions = new ConditionallyDeadAssumptions();
        List<ITestCase> conditionallyDeadTestCases = conditionallyDeadAssumptions.createAssumptions(featureModel);
        TestSuite conditionallyDeadTestSuite = TestSuite.builder().testCases(conditionallyDeadTestCases).build();

        FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> conditionallyDeadDebuggingModel = new FMDebuggingModel<>(featureModel, conditionallyDeadTestSuite, new FMTestCaseTranslator(), false, false, false);
        conditionallyDeadDebuggingModel.initialize();

        for (ITestCase conditionallyDeadTestCase : conditionallyDeadTestCases) {
            // create the specified analyses and the corresponding explanators
            debuggingModelClone = (FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>) conditionallyDeadDebuggingModel.clone();
            debuggingModelClone.initialize();
            ConditionallyDeadAnalysis conditionallyDeadAnalysis = new ConditionallyDeadAnalysis(debuggingModelClone, conditionallyDeadTestCase);
            ConditionallyDeadExplanator conditionallyDeadExplanator = new ConditionallyDeadExplanator(debuggingModelClone, conditionallyDeadTestCase);
            analyzer.addAnalysis(conditionallyDeadAnalysis, conditionallyDeadExplanator); // add the analysis to the analyzer

            allAnalyses.get(AnomalyType.CONDITIONALLYDEAD.ordinal()).add(conditionallyDeadAnalysis);
            allExplanators.get(AnomalyType.CONDITIONALLYDEAD.ordinal()).add(conditionallyDeadExplanator);
        }

        /// FALSE OPTIONAL
        // create a test case/assumption
        // check false optional features  - inconsistent( CF ∪ { c0 } U { fpar = true ^ fopt = false } )
        FalseOptionalAssumptions falseOptionalAssumptions = new FalseOptionalAssumptions();
        List<ITestCase> falseOptionalTestCases = falseOptionalAssumptions.createAssumptions(featureModel);
        TestSuite falseOptionalTestSuite = TestSuite.builder().testCases(falseOptionalTestCases).build();

        FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> falseOptionalDebuggingModel = new FMDebuggingModel<>(featureModel, falseOptionalTestSuite, new FMTestCaseTranslator(), false, false, false);
        falseOptionalDebuggingModel.initialize();

        for (ITestCase falseOptionalTestCase : falseOptionalTestCases) {
            // create the specified analyses and the corresponding explanators
            debuggingModelClone = (FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>) falseOptionalDebuggingModel.clone();
            debuggingModelClone.initialize();
            FalseOptionalAnalysis falseOptionalAnalysis = new FalseOptionalAnalysis(debuggingModelClone, falseOptionalTestCase);
            FalseOptionalExplanator falseOptionalExplanator = new FalseOptionalExplanator(debuggingModelClone, falseOptionalTestCase);
            analyzer.addAnalysis(falseOptionalAnalysis, falseOptionalExplanator); // add the analysis to the analyzer

            allAnalyses.get(AnomalyType.FALSEOPTIONAL.ordinal()).add(falseOptionalAnalysis);
            allExplanators.get(AnomalyType.FALSEOPTIONAL.ordinal()).add(falseOptionalExplanator);
        }

        analyzer.run(); // run the analyzer

        for (AnomalyType anomaly : AnomalyType.values()) {
            if (anomaly.ordinal() < 2) {
                continue;
            }
            for (int runningAnalysis = 0; runningAnalysis < allAnalyses.get(anomaly.ordinal()).size(); runningAnalysis++) {
                if (!allAnalyses.get(anomaly.ordinal()).get(runningAnalysis).get()) {
                    switch (anomaly) {
                        case FULLMANDATORY -> {
                            System.out.println(ExplanationColors.ANOMALY + "X Full mandatory feature: " + ((AssumptionAwareTestCase) allAnalyses.get(anomaly.ordinal()).get(runningAnalysis).getAssumption()).getAssumptions());
                            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(allExplanators.get(anomaly.ordinal()).get(runningAnalysis).get(), "full mandatory feature"));
                        }
                        case CONDITIONALLYDEAD -> {
                            System.out.println(ExplanationColors.ANOMALY + "X Conditionally dead feature: " + ((AssumptionAwareTestCase) allAnalyses.get(anomaly.ordinal()).get(runningAnalysis).getAssumption()).getAssumptions());
                            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(allExplanators.get(anomaly.ordinal()).get(runningAnalysis).get(), "conditionally dead feature"));
                        }
                        case FALSEOPTIONAL -> {
                            System.out.println(ExplanationColors.ANOMALY + "X False optional feature: " + ((AssumptionAwareTestCase) allAnalyses.get(anomaly.ordinal()).get(runningAnalysis).getAssumption()).getAssumptions());
                            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(allExplanators.get(anomaly.ordinal()).get(runningAnalysis).get(), "false optional feature"));
                        }
                    }

                    for (AnomalyAwareFeature featureWithAnomaly : ((AssumptionAwareTestCase) allAnalyses.get(anomaly.ordinal()).get(runningAnalysis).getAssumption()).getAssumptions()) {
                        featureModel.getFeature(featureWithAnomaly.getId()).setAnomalyType(anomaly);
                    }

                    anomalyFound = true;
                }
            }
        }

        if (!anomalyFound) {
            System.out.println(ConsoleColors.GREEN + "\u2713 No anomaly found" + ConsoleColors.RESET);
        }
    }


    @Test
    public void testFullAnalysis() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
        File fileFM = new File("src/test/resources/basic_featureide_multiple1.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        AutomatedAnalysisBuilder automatedAnalyzer = new AutomatedAnalysisBuilder();
        automatedAnalyzer.performFullAnalysis(featureModel);
    }

    @Disabled("Bad for Tamim's laptop battery...")
    @Test
    public void testLargeModel_1() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
        // 42 features in 6 layers - no constraints
        File fileFM = new File("src/test/resources/basic_featureide_large1.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        AutomatedAnalysisBuilder automatedAnalyzer = new AutomatedAnalysisBuilder();
        automatedAnalyzer.performFullAnalysis(featureModel);
    }

    @Disabled("Bad for Tamim's laptop battery...")
    @Test
    public void testLargeModel_2() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
        // 42 features in 6 layers - few, basic constraints
        File fileFM = new File("src/test/resources/basic_featureide_large2.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        AutomatedAnalysisBuilder automatedAnalyzer = new AutomatedAnalysisBuilder();
        automatedAnalyzer.performFullAnalysis(featureModel);
    }

    @Disabled("Bad for Tamim's laptop battery...")
    @Test
    public void testLargeModel_3() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
        // 42 features in 6 layers - more constraints
        File fileFM = new File("src/test/resources/basic_featureide_large3.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        AutomatedAnalysisBuilder automatedAnalyzer = new AutomatedAnalysisBuilder();
        automatedAnalyzer.performFullAnalysis(featureModel);
    }

    @Test
    void testRedundancy_1() throws FeatureModelParserException, ExecutionException, InterruptedException {
        // load the feature model
        File fileFM = new File("src/test/resources/bamboobike_featureide_deadfeature2.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        FMCdrModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                model = new FMCdrModel<>(featureModel, true, false, true);
        model.initialize();

        // create the redundancy analysis
        RedundancyAnalysis analysis = new RedundancyAnalysis(model);

        FMAnalyzer analyzer = new FMAnalyzer();
        analyzer.addAnalysis(analysis, null); // add the analysis to the analyzer
        analyzer.run(); // run the analyzer

        // print the result
        ExplanationColors.EXPLANATION = ConsoleColors.WHITE;
        if (!analysis.get()) {
            System.out.println(ExplanationColors.ANOMALY + "X Redundant constraint:");
            System.out.println(ExplanationColors.EXPLANATION + ConstraintUtils.convertToString(analysis.getRedundantConstraints(), "\n", "\t", false));
        }

        assertFalse(analysis.get());

        Set<Constraint> constraints = new LinkedHashSet<>();
        constraints.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 2));

        assertEquals(1, analysis.getRedundantConstraints().size());
        assertEquals(constraints, analysis.getRedundantConstraints());
    }

    @Test
    void testRedundancy_2() throws ExecutionException, InterruptedException {
        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        ConfRuleTranslator ruleTranslator = new ConfRuleTranslator();
        IRelationshipBuildable relationshipBuilder = new RelationshipBuilder(ruleTranslator);
        IConstraintBuildable constraintBuilder = new ConstraintBuilder(ruleTranslator);

        // create the feature model
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> fm = new FeatureModel<>("survey-tool", featureBuilder, relationshipBuilder, constraintBuilder);
        fm.addRoot("survey", "survey");
        fm.addFeature("pay", "pay");
        fm.addFeature("ABtesting", "ABtesting");
        fm.addFeature("statistics", "statistics");
        fm.addFeature("qa", "qa");
        fm.addFeature("license", "license");
        fm.addFeature("nonlicense", "nonlicense");
        fm.addFeature("multiplechoice", "multiplechoice");
        fm.addFeature("singlechoice", "singlechoice");
        fm.addMandatoryRelationship(fm.getFeature("survey"), fm.getFeature("pay"));
        fm.addOptionalRelationship(fm.getFeature("survey"), fm.getFeature("ABtesting")); // TODO @Man, as info, I had to swap the features here (maybe a reason for failing assertions?)
        fm.addMandatoryRelationship(fm.getFeature("survey"), fm.getFeature("statistics"));
        fm.addMandatoryRelationship(fm.getFeature("survey"), fm.getFeature("qa"));
        fm.addAlternativeRelationship(fm.getFeature("pay"), List.of(fm.getFeature("license"), fm.getFeature("nonlicense")));
        fm.addOrRelationship(fm.getFeature("qa"), List.of(fm.getFeature("multiplechoice"), fm.getFeature("singlechoice")));
        fm.addOptionalRelationship(fm.getFeature("statistics"), fm.getFeature("ABtesting")); // should be redundant // TODO @Tamim, I changed the order of features to trigger the correct assertion
        fm.addRequires(fm.getFeature("ABtesting"), fm.getFeature("statistics")); // should be redundant
        fm.addExcludes(fm.getFeature("ABtesting"), fm.getFeature("nonlicense"));
        fm.addRequires(fm.getFeature("ABtesting"), fm.getFeature("survey")); // should be redundant

        FMCdrModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                model = new FMCdrModel<>(fm, true, false, true);
        model.initialize();

        // create the redundancy analysis
        RedundancyAnalysis analysis = new RedundancyAnalysis(model);

        FMAnalyzer analyzer = new FMAnalyzer();
        analyzer.addAnalysis(analysis, null); // add the analysis to the analyzer
        analyzer.run(); // run the analyzer

        // print the result
        ExplanationColors.EXPLANATION = ConsoleColors.WHITE;
        if (!analysis.get()) {
            System.out.println(ExplanationColors.ANOMALY + "X Redundant constraints:");
            System.out.println(ExplanationColors.EXPLANATION + ConstraintUtils.convertToString(analysis.getRedundantConstraints(), "\n", "\t", false));
        }

        assertFalse(analysis.get());

        Set<Constraint> constraints = new LinkedHashSet<>();
        constraints.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 0));
        constraints.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 2));
        constraints.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 3));

        assertEquals(3, analysis.getRedundantConstraints().size());
        assertEquals(constraints, analysis.getRedundantConstraints());
    }

    @Test
    public void testRedundancy_3() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
        File fileFM = new File("src/test/resources/bamboobike_featureide_redundancies1.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        AutomatedAnalysisBuilder automatedAnalyzer = new AutomatedAnalysisBuilder();
        automatedAnalyzer.performFullAnalysis(featureModel);

        // TODO add assertions
    }

    @Test
    public void testRedundancy_4() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
        File fileFM = new File("src/test/resources/bamboobike_featureide_redundancies2.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        AutomatedAnalysisBuilder automatedAnalyzer = new AutomatedAnalysisBuilder();
        automatedAnalyzer.performFullAnalysis(featureModel);

        // TODO add assertions
    }

    @Test
    public void testRedundancy_5() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
        File fileFM = new File("src/test/resources/bamboobike_featureide_redundancies3.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        AutomatedAnalysisBuilder automatedAnalyzer = new AutomatedAnalysisBuilder();
        automatedAnalyzer.performFullAnalysis(featureModel);

        // TODO add assertions
    }

    @Test
    public void testRedundancy_6() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
        File fileFM = new File("src/test/resources/bamboobike_featureide_redundancies4.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        AutomatedAnalysisBuilder automatedAnalyzer = new AutomatedAnalysisBuilder();
        automatedAnalyzer.performFullAnalysis(featureModel);

        // TODO add assertions
    }

    @Test
    public void testSingleAnalysis_1() throws ExecutionException, InterruptedException, CloneNotSupportedException, FeatureModelParserException {
        File fileFM = new File("src/test/resources/basic_featureide_redundant1.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        AutomatedAnalysisBuilder automatedAnalyzer = new AutomatedAnalysisBuilder();
        automatedAnalyzer.performAnalysis(featureModel, AnomalyType.REDUNDANT.bitValue());
    }

    @Test
    public void testSingleAnalysis_2() throws ExecutionException, InterruptedException, CloneNotSupportedException, FeatureModelParserException {
        File fileFM = new File("src/test/resources/basic_featureide_multiple1.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        AutomatedAnalysisBuilder automatedAnalyzer = new AutomatedAnalysisBuilder();
        automatedAnalyzer.performAnalysis(featureModel, AnomalyType.DEAD.bitValue());
    }

    @Test
    public void testSingleAnalysis_3() throws ExecutionException, InterruptedException, CloneNotSupportedException, FeatureModelParserException {
        File fileFM = new File("src/test/resources/basic_featureide_multiple1.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        AutomatedAnalysisBuilder automatedAnalyzer = new AutomatedAnalysisBuilder();
        automatedAnalyzer.performAnalysis(featureModel, AnomalyType.FULLMANDATORY.bitValue());
    }

    @Test
    public void testSingleAnalysis_4() throws ExecutionException, InterruptedException, CloneNotSupportedException, FeatureModelParserException {
        File fileFM = new File("src/test/resources/basic_featureide_multiple1.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        AutomatedAnalysisBuilder automatedAnalyzer = new AutomatedAnalysisBuilder();
        automatedAnalyzer.performAnalysis(featureModel, AnomalyType.CONDITIONALLYDEAD.bitValue());
    }

    @Test
    public void testSingleAnalysis_5() throws ExecutionException, InterruptedException, CloneNotSupportedException, FeatureModelParserException {
        File fileFM = new File("src/test/resources/basic_featureide_multiple1.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        AutomatedAnalysisBuilder automatedAnalyzer = new AutomatedAnalysisBuilder();
        automatedAnalyzer.performAnalysis(featureModel, AnomalyType.FALSEOPTIONAL.bitValue());
    }

    @Test
    public void testSingleAnalysis_6() throws ExecutionException, InterruptedException, CloneNotSupportedException, FeatureModelParserException {
        File fileFM = new File("src/test/resources/basic_featureide_redundant1.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        AutomatedAnalysisBuilder automatedAnalyzer = new AutomatedAnalysisBuilder();
        automatedAnalyzer.performAnalysis(featureModel, AnomalyType.REDUNDANT.bitValue() | AnomalyType.DEAD.bitValue());
    }

    @Test
    public void testSingleAnalysis_7() throws ExecutionException, InterruptedException, CloneNotSupportedException, FeatureModelParserException {
        File fileFM = new File("src/test/resources/basic_featureide_redundant1.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        AutomatedAnalysisBuilder automatedAnalyzer = new AutomatedAnalysisBuilder();
        automatedAnalyzer.performAnalysis(featureModel, AnomalyType.REDUNDANT.bitValue() | AnomalyType.DEAD.bitValue() | AnomalyType.FULLMANDATORY.bitValue());
    }

    @Test
    public void testSingleAnalysis_8() throws ExecutionException, InterruptedException, CloneNotSupportedException, FeatureModelParserException {
        File fileFM = new File("src/test/resources/basic_featureide_redundant1.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        AutomatedAnalysisBuilder automatedAnalyzer = new AutomatedAnalysisBuilder();
        automatedAnalyzer.performAnalysis(featureModel, AnomalyType.DEAD.bitValue() | AnomalyType.FULLMANDATORY.bitValue());
    }
}