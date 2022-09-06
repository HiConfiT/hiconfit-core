/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma;

import at.tugraz.ist.ase.cdrmodel.fm.FMDebuggingModel;
import at.tugraz.ist.ase.cdrmodel.test.ITestCase;
import at.tugraz.ist.ase.cdrmodel.test.TestSuite;
import at.tugraz.ist.ase.cdrmodel.test.translator.fm.FMTestCaseTranslator;
import at.tugraz.ist.ase.common.ConsoleColors;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FMAnalyzerTest {
    static FMParserFactory<Feature, AbstractRelationship<Feature>, CTConstraint> factory = FMParserFactory.getInstance();

    // TODO: fix all JUnit Assertions.* that have the wrong order! (actual vs. expected)
    @Test
    void testVoidFM() throws FeatureModelParserException, ExecutionException, InterruptedException {
        // load the feature model
        File fileFM = new File("src/test/resources/bamboobike_featureide_void.xml");
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = factory.getParser(fileFM.getName());
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(fileFM);

//        FeatureModelParser<F extends Feature, R AbstractRelationship<F>, C extends CTConstraint> parser = factory.getParser(FMFormat.FEATUREIDE);


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

        Assertions.assertEquals(3, allDiagnoses.size());
        Assertions.assertEquals(cs1, allDiagnoses.get(0));
        Assertions.assertEquals(cs2, allDiagnoses.get(1));
        Assertions.assertEquals(cs3, allDiagnoses.get(2));
    }

    @Test
    void testDeadFeature_1() throws FeatureModelParserException, ExecutionException, InterruptedException {
        // load the feature model
        File fileFM = new File("src/test/resources/bamboobike_featureide_deadfeature1.xml");
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = factory.getParser(fileFM.getName());
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(fileFM);


//        FMParserFactory factory = FMParserFactory.getInstance();
//        FeatureModelParser parser = factory.getParser(FMFormat.FEATUREIDE);
//        FeatureModel featureModel = parser.parse(fileFM);

        // create a test case/assumption
        // check void feature model - inconsistent( CF ∪ { c0 })
        VoidFMAssumption voidFMAssumption = new VoidFMAssumption();
        List<ITestCase> testCases = voidFMAssumption.createAssumptions(featureModel);
        TestSuite testSuite = TestSuite.builder().testCases(testCases).build();

        FMDebuggingModel<Feature, AbstractRelationship<Feature>, CTConstraint> debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, new FMTestCaseTranslator(), false, false, false);
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

        Assertions.assertEquals(2, allDiagnoses.size());
        Assertions.assertEquals(cs1, allDiagnoses.get(0));
        Assertions.assertEquals(cs2, allDiagnoses.get(1));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testDeadFeature_2() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
        // load the feature model
        File fileFM = new File("src/test/resources/bamboobike_featureide_deadfeature2.xml");
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = factory.getParser(fileFM.getName());
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(fileFM);
//        FMParserFactory factory = FMParserFactory.getInstance();
//        FeatureModelParser parser = factory.getParser(FMFormat.FEATUREIDE);
//        FeatureModel featureModel = parser.parse(fileFM);

        // create a test case/assumption
        // check void feature model - inconsistent( CF ∪ { c0 })
        VoidFMAssumption voidFMAssumption = new VoidFMAssumption();
        List<ITestCase> testCases = voidFMAssumption.createAssumptions(featureModel);
        TestSuite testSuite = TestSuite.builder().testCases(testCases).build();

        FMDebuggingModel<Feature, AbstractRelationship<Feature>, CTConstraint> debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, new FMTestCaseTranslator(), false, false, false);
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

        // check the feature Step-through
        FMDebuggingModel<Feature, AbstractRelationship<Feature>, CTConstraint> debuggingModel1 = (FMDebuggingModel<Feature, AbstractRelationship<Feature>, CTConstraint>) debuggingModel.clone();
        debuggingModel1.initialize();

        DeadFeatureAnalysis analysis3 = new DeadFeatureAnalysis(debuggingModel1, testCases.get(6));
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

        Assertions.assertEquals(3, allDiagnoses.size());
        Assertions.assertEquals(cs1, allDiagnoses.get(0));
        Assertions.assertEquals(cs2, allDiagnoses.get(1));
        Assertions.assertEquals(cs3, allDiagnoses.get(2));

        Assertions.assertEquals(3, allDiagnoses1.size());
        Assertions.assertEquals(cs1, allDiagnoses1.get(0));
        Assertions.assertEquals(cs2, allDiagnoses1.get(1));
        Assertions.assertEquals(cs3, allDiagnoses1.get(2));
    }

    @Test
    void testDeadFeature_3() throws FeatureModelParserException, ExecutionException, InterruptedException {
        // load the feature model
        File fileFM = new File("src/test/resources/bamboobike_featureide_deadfeature3.xml");
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = factory.getParser(fileFM.getName());
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(fileFM);
//        FMParserFactory factory = FMParserFactory.getInstance();
//        FeatureModelParser parser = factory.getParser(FMFormat.FEATUREIDE);
//        FeatureModel featureModel = parser.parse(fileFM);

        // create a test case/assumption
        // check void feature model - inconsistent( CF ∪ { c0 })
        VoidFMAssumption voidFMAssumption = new VoidFMAssumption();
        List<ITestCase> testCases = voidFMAssumption.createAssumptions(featureModel);
        TestSuite testSuite = TestSuite.builder().testCases(testCases).build();

        FMDebuggingModel<Feature, AbstractRelationship<Feature>, CTConstraint> debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, new FMTestCaseTranslator(), false, false, false);
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

        Assertions.assertEquals(3, allDiagnoses.size());
        Assertions.assertEquals(cs1, allDiagnoses.get(0));
        Assertions.assertEquals(cs2, allDiagnoses.get(1));
        Assertions.assertEquals(cs3, allDiagnoses.get(2));
    }

    @Test
    void testFullMandatory() throws FeatureModelParserException, ExecutionException, InterruptedException {
        File fileFM = new File("src/test/resources/basic_featureide_fullmandatory1.xml");
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = factory.getParser(fileFM.getName());
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(fileFM);
//        FMParserFactory factory = FMParserFactory.getInstance();
//        FeatureModelParser parser = factory.getParser(FMFormat.FEATUREIDE);
//        FeatureModel featureModel = parser.parse(fileFM);

        // create a test case/assumption
        // check for full mandatory features
        FullMandatoryAssumptions fullMandatoryAssumptions = new FullMandatoryAssumptions();
        List<ITestCase> testCases = fullMandatoryAssumptions.createAssumptions(featureModel);
        TestSuite testSuite = TestSuite.builder().testCases(testCases).build();

        FMDebuggingModel<Feature, AbstractRelationship<Feature>, CTConstraint> debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, new FMTestCaseTranslator(), false, false, false);
        debuggingModel.initialize();

        // create the specified analysis and the corresponding explanator
        FullMandatoryAnalysis analysis = new FullMandatoryAnalysis(debuggingModel, testCases.get(1));
        FullMandatoryExplanator explanator = new FullMandatoryExplanator(debuggingModel, testCases.get(1));

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
        cs1.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 1));

        Set<Constraint> cs2 = new LinkedHashSet<>();
        cs2.add(Iterators.get(debuggingModel.getPossiblyFaultyConstraints().iterator(), 0));

        Assertions.assertEquals(2, allDiagnoses.size());
        Assertions.assertEquals(cs1, allDiagnoses.get(0));
        Assertions.assertEquals(cs2, allDiagnoses.get(1));
    }

    @Test
    void testFalseOptional() throws FeatureModelParserException, ExecutionException, InterruptedException {
        File fileFM = new File("src/test/resources/basic_featureide_falseoptional1.xml");
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = factory.getParser(fileFM.getName());
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(fileFM);
//        FMParserFactory factory = FMParserFactory.getInstance();
//        FeatureModelParser parser = factory.getParser(FMFormat.FEATUREIDE);
//        FeatureModel featureModel = parser.parse(fileFM);

        // create a test case/assumption
        // check for false optional features
        FalseOptionalAssumptions falseOptionalAssumptions = new FalseOptionalAssumptions();
        List<ITestCase> testCases = falseOptionalAssumptions.createAssumptions(featureModel);
        TestSuite testSuite = TestSuite.builder().testCases(testCases).build();

        FMDebuggingModel<Feature, AbstractRelationship<Feature>, CTConstraint> debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, new FMTestCaseTranslator(), false, false, false);
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

        Assertions.assertEquals(1, allDiagnoses.size());
        Assertions.assertEquals(cs1, allDiagnoses.get(0));
    }

    @Test
    void testConditionallyDead_0() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
        File fileFM = new File("src/test/resources/bamboobike_featureide_deadfeature2.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        ConfRuleTranslator ruleTranslator = new ConfRuleTranslator();
        IRelationshipBuildable relationshipBuilder = new RelationshipBuilder(ruleTranslator);
        IConstraintBuildable constraintBuilder = new ConstraintBuilder(ruleTranslator);

        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> factory = FMParserFactory.getInstance(featureBuilder, relationshipBuilder, constraintBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> featureModel = parser.parse(fileFM);

        // set Female and Step-through as dead
        featureModel.getFeature("Female").setAnomalyType(AnomalyType.DEAD);
        featureModel.getFeature("Step-through").setAnomalyType(AnomalyType.DEAD);

        // create a test case/assumption
        // check for conditionally dead features
        ConditionallyDeadAssumptions conditionallyDeadAssumptions = new ConditionallyDeadAssumptions();
        List<ITestCase> testCases = conditionallyDeadAssumptions.createAssumptions(featureModel);
        TestSuite testSuite = TestSuite.builder().testCases(testCases).build();

        FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, new FMTestCaseTranslator(), false, false, false);
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

        Assertions.assertEquals(1, allDiagnoses.size());
    }

//    @Test
//    void testConditionallyDead_1() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
//        File fileFM = new File("src/test/resources/basic_featureide_conditionallydead1.xml");
//
//        // create the factory for anomaly feature models
//        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
//        ConfRuleTranslator ruleTranslator = new ConfRuleTranslator();
//        IRelationshipBuildable relationshipBuilder = new RelationshipBuilder(ruleTranslator);
//        IConstraintBuildable constraintBuilder = new ConstraintBuilder(ruleTranslator);
//
//        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> factory = FMParserFactory.getInstance(featureBuilder, relationshipBuilder, constraintBuilder);
//
//        @Cleanup("dispose")
//        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> parser = factory.getParser(fileFM.getName());
//        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> featureModel = parser.parse(fileFM);
//
//        // create a test case/assumption
//        // check for conditionally dead features
//        ConditionallyDeadAssumptions conditionallyDeadAssumptions = new ConditionallyDeadAssumptions();
//        List<ITestCase> testCases = conditionallyDeadAssumptions.createAssumptions(featureModel);
//        TestSuite testSuite = TestSuite.builder().testCases(testCases).build();
//
//        FMDebuggingModel debuggingModel = new FMDebuggingModel(featureModel, testSuite, new FMTestCaseTranslator(), false, false, false);
//        debuggingModel.initialize();
//
//        // create the specified analysis and the corresponding explanator
//        ConditionallyDeadAnalysis analysis = new ConditionallyDeadAnalysis(debuggingModel, testCases.get(3)); // Check feature C
//        ConditionallyDeadExplanator explanator = new ConditionallyDeadExplanator(debuggingModel, testCases.get(3));
//
//        FMDebuggingModel debuggingModel2 = (FMDebuggingModel) debuggingModel.clone();
//        debuggingModel2.initialize();
//
//        ConditionallyDeadAnalysis analysis2 = new ConditionallyDeadAnalysis(debuggingModel2, testCases.get(5)); // Check feature D
//        ConditionallyDeadExplanator explanator2 = new ConditionallyDeadExplanator(debuggingModel2, testCases.get(5));
//
//        FMDebuggingModel debuggingModel3 = (FMDebuggingModel) debuggingModel.clone();
//        debuggingModel3.initialize();
//
//        ConditionallyDeadAnalysis analysis3 = new ConditionallyDeadAnalysis(debuggingModel3, testCases.get(1)); // Check feature B
//        ConditionallyDeadExplanator explanator3 = new ConditionallyDeadExplanator(debuggingModel3, testCases.get(1));
//
//        FMAnalyzer analyzer = new FMAnalyzer();
//        analyzer.addAnalysis(analysis, explanator); // add the analysis to the analyzer
//        analyzer.addAnalysis(analysis2, explanator2); // add the analysis to the analyzer
//        analyzer.addAnalysis(analysis3, explanator3); // add the analysis to the analyzer
//        analyzer.run(); // run the analyzer
//
//        // print the result
//        ExplanationColors.EXPLANATION = ConsoleColors.WHITE;
//        if (analysis.get()) {
//            System.out.println(ExplanationColors.OK + "\u2713 DConsistency: ok");
//        } else {
//            System.out.println(ExplanationColors.ANOMALY + "X Conditionally dead feature");
//            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(explanator.get(), "conditionally dead feature"));
//        }
//        if (analysis2.get()) {
//            System.out.println(ExplanationColors.OK + "\u2713 Consistency: ok");
//        } else {
//            System.out.println(ExplanationColors.ANOMALY + "X Conditionally dead feature");
//            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(explanator2.get(), "conditionally dead feature"));
//        }
//        if (analysis3.get()) {
//            System.out.println(ExplanationColors.OK + "\u2713 Consistency: ok");
//        } else {
//            System.out.println(ExplanationColors.ANOMALY + "X Conditionally dead feature");
//            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(explanator3.get(), "conditionally dead feature"));
//        }
//
//        assertFalse(analysis.get());
//
//        List<Set<Constraint>> allDiagnoses = explanator.get();
//
//        Assertions.assertEquals(1, allDiagnoses.size());
//    }

//    @Test
//    void testConditionallyDead_2() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException, FeatureModelException {
//        File fileFM = new File("src/test/resources/basic_featureide_conditionallydead2.xml");
//        @Cleanup("dispose")
//        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = factory.getParser(fileFM.getName());
//        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(fileFM);
////        FMParserFactory factory = FMParserFactory.getInstance();
////        FeatureModelParser parser = factory.getParser(FMFormat.FEATUREIDE);
////        FeatureModel featureModel = parser.parse(fileFM);
//
//        FMAnalyzer analyzer = new FMAnalyzer();
//        analyzer.performFullAnalysis(featureModel);
//    }
//
//    @Test
//    public void testMultiple_1() throws FeatureModelParserException, ExecutionException, InterruptedException, FeatureModelException, CloneNotSupportedException {
//        File fileFM = new File("src/test/resources/basic_featureide_multiple1.xml");
//        FMParserFactory factory = FMParserFactory.getInstance();
//        FeatureModelParser parser = factory.getParser(FMFormat.FEATUREIDE);
//        FeatureModel fm = parser.parse(fileFM);
//        AnomalyAwareFeatureModel featureModel = new AnomalyAwareFeatureModel(fm);
//
//        /// VOID FEATURE MODEL
//        // create a test case/assumption
//        // check void feature model - inconsistent( CF ∪ { c0 })
//        VoidFMAssumption voidFMAssumption = new VoidFMAssumption();
//        List<ITestCase> testCases = voidFMAssumption.createAssumptions(featureModel);
//        TestSuite testSuite = TestSuite.builder().testCases(testCases).build();
//
//        FMDebuggingModel debuggingModel = new FMDebuggingModel(featureModel, testSuite, new FMTestCaseTranslator(), false, false, false);
//        debuggingModel.initialize();
//
//        // create the specified analysis and the corresponding explanator
//        VoidFMAnalysis analysis = new VoidFMAnalysis(debuggingModel, testCases.get(0));
//        VoidFMExplanator explanator = new VoidFMExplanator(debuggingModel, testCases.get(0));
//
//        FMAnalyzer analyzer = new FMAnalyzer();
//        analyzer.addAnalysis(analysis, explanator); // add the analysis to the analyzer
//        analyzer.run(); // run the analyzer
//
//        // print the result
//        ExplanationColors.EXPLANATION = ConsoleColors.WHITE;
//        if (analysis.get()) {
//            System.out.println(ExplanationColors.OK + "\u2713 Consistency: ok");
//        } else {
//            System.out.println(ExplanationColors.ANOMALY + "X Void feature model");
//            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(explanator.get(), "void feature model"));
//        }
//
//        assertTrue(analysis.get());
//
//        // Store all analyses in here to access them later
//        List<List<AbstractFMAnalysis<?>>> allAnalyses = new ArrayList<>(Collections.emptyList());
//        List<List<AbstractAnomalyExplanator<List<Set<Constraint>>>>> allExplanators = new ArrayList<>(Collections.emptyList());
//        List<List<AnomalyType>> anomalyTypes = new ArrayList<>(Collections.emptyList());
//
//        // Create an analyzer
//        analyzer = new FMAnalyzer();
//        FMDebuggingModel debuggingModelClone = null;
//
//        /// DEAD FEATURES
//        // create a test case/assumption
//        // check dead features - inconsistent( CF ∪ { c0 } U { fi = true })
//        DeadFeatureAssumptions deadFeatureAssumptions = new DeadFeatureAssumptions();
//        List<ITestCase> deadFeatureTestCases = deadFeatureAssumptions.createAssumptions(featureModel);
//        TestSuite deadFeatureTestSuite = TestSuite.builder().testCases(deadFeatureTestCases).build();
//
//        FMDebuggingModel deadFeatureDebuggingModel = new FMDebuggingModel(featureModel, deadFeatureTestSuite, new FMTestCaseTranslator(), false, false, false);
//        deadFeatureDebuggingModel.initialize();
//
//        for (int f = 1; f < featureModel.getNumOfFeatures(); f++) {
//            allAnalyses.add(new ArrayList<>(Collections.emptyList()));
//            allExplanators.add(new ArrayList<>(Collections.emptyList()));
//            anomalyTypes.add(new ArrayList<>(Collections.emptyList()));
//
//            // create the specified analyses and the corresponding explanators
//            debuggingModelClone = (FMDebuggingModel) deadFeatureDebuggingModel.clone();
//            debuggingModelClone.initialize();
//            DeadFeatureAnalysis deadFeatureAnalysis = new DeadFeatureAnalysis(debuggingModelClone, deadFeatureTestCases.get(f - 1));
//            DeadFeatureExplanator deadFeatureExplanator = new DeadFeatureExplanator(debuggingModelClone, deadFeatureTestCases.get(f - 1));
//            analyzer.addAnalysis(deadFeatureAnalysis, deadFeatureExplanator); // add the analysis to the analyzer
//
//            allAnalyses.get(f - 1).add(deadFeatureAnalysis);
//            allExplanators.get(f - 1).add(deadFeatureExplanator);
//            anomalyTypes.get(f - 1).add(AnomalyType.DEAD);
//        }
//
//        analyzer.run(); // run the analyzer
//
//        // Check the results and set dead features - printing will happen later
//        for (int f = 1; f < featureModel.getNumOfFeatures(); f++) {
//            for (int runningAnalysis = 0; runningAnalysis < allAnalyses.get(f - 1).size(); runningAnalysis++) {
//                if (!allAnalyses.get(f - 1).get(runningAnalysis).get()) {
//                    featureModel.getAnomalyAwareFeature(f).setAnomalyType(AnomalyType.DEAD);
//                }
//            }
//        }
//
//        /// FULL MANDATORY
//        // create a test case/assumption
//        // check full mandatory features - inconsistent( CF ∪ { c0 } U { fi = false })
//        FullMandatoryAssumptions fullMandatoryAssumptions = new FullMandatoryAssumptions();
//        List<ITestCase> fullMandatoryTestCases = fullMandatoryAssumptions.createAssumptions(featureModel);
//        TestSuite fullMandatoryTestSuite = TestSuite.builder().testCases(fullMandatoryTestCases).build();
//
//        FMDebuggingModel fullMandatoryDebuggingModel = new FMDebuggingModel(featureModel, fullMandatoryTestSuite, new FMTestCaseTranslator(), false, false, false);
//        fullMandatoryDebuggingModel.initialize();
//
//        /// FALSE OPTIONAL
//        // create a test case/assumption
//        // check false optional features  - inconsistent( CF ∪ { c0 } U { fpar = true ^ fopt = false } )
//        FalseOptionalAssumptions falseOptionalAssumptions = new FalseOptionalAssumptions();
//        List<ITestCase> falseOptionalTestCases = falseOptionalAssumptions.createAssumptions(featureModel);
//        TestSuite falseOptionalTestSuite = TestSuite.builder().testCases(falseOptionalTestCases).build();
//
//        FMDebuggingModel falseOptionalDebuggingModel = new FMDebuggingModel(featureModel, falseOptionalTestSuite, new FMTestCaseTranslator(), false, false, false);
//        falseOptionalDebuggingModel.initialize();
//
//        // CONDITIONALLY DEAD
//        // create a test case/assumption
//        // check conditionally dead features - inconsistent( CF ∪ { c0 } U { fj = true } U { fi = true } ) for any fj
//        ConditionallyDeadAssumptions conditionallyDeadAssumptions = new ConditionallyDeadAssumptions();
//        List<ITestCase> conditionallyDeadTestCases = conditionallyDeadAssumptions.createAssumptions(featureModel);
//        TestSuite conditionallyDeadTestSuite = TestSuite.builder().testCases(conditionallyDeadTestCases).build();
//
//        FMDebuggingModel conditionallyDeadDebuggingModel = new FMDebuggingModel(featureModel, conditionallyDeadTestSuite, new FMTestCaseTranslator(), false, false, false);
//        conditionallyDeadDebuggingModel.initialize();
//
//        // counting variables for indexes
//        int condDead = 0;
//        int optWithParent = 0;
//        for (int f = 1; f < featureModel.getNumOfFeatures(); f++) {
//            if (featureModel.getAnomalyAwareFeature(f).isAnomalyType(AnomalyType.DEAD)) {
//                continue;
//            }
//
//            Feature feature = featureModel.getFeature(f);
//
//            // create the specified analyses and the corresponding explanators
//            debuggingModelClone = (FMDebuggingModel) fullMandatoryDebuggingModel.clone();
//            debuggingModelClone.initialize();
//            FullMandatoryAnalysis fullMandatoryAnalysis = new FullMandatoryAnalysis(debuggingModelClone, fullMandatoryTestCases.get(f - 1));
//            FullMandatoryExplanator fullMandatoryExplanator = new FullMandatoryExplanator(debuggingModelClone, fullMandatoryTestCases.get(f - 1));
//            analyzer.addAnalysis(fullMandatoryAnalysis, fullMandatoryExplanator); // add the analysis to the analyzer
//
//            allAnalyses.get(f - 1).add(fullMandatoryAnalysis);
//            allExplanators.get(f - 1).add(fullMandatoryExplanator);
//            anomalyTypes.get(f - 1).add(AnomalyType.FULLMANDATORY);
//
//            if (featureModel.isOptionalFeature(feature)) {
//                for (int j = 1; j < featureModel.getNumOfFeatures(); j++) {
//                    if (f == j || !featureModel.isOptionalFeature(featureModel.getFeature(j)) || featureModel.getAnomalyAwareFeature(j).isAnomalyType(AnomalyType.DEAD)) {
//                        continue;
//                    }
//
//                    // create the specified analyses and the corresponding explanators
//                    debuggingModelClone = (FMDebuggingModel) conditionallyDeadDebuggingModel.clone();
//                    debuggingModelClone.initialize();
//                    ConditionallyDeadAnalysis conditionallyDeadAnalysis = new ConditionallyDeadAnalysis(debuggingModelClone, conditionallyDeadTestCases.get(condDead));
//                    ConditionallyDeadExplanator conditionallyDeadExplanator = new ConditionallyDeadExplanator(debuggingModelClone, conditionallyDeadTestCases.get(condDead));
//                    analyzer.addAnalysis(conditionallyDeadAnalysis, conditionallyDeadExplanator); // add the analysis to the analyzer
//
//                    allAnalyses.get(f - 1).add(conditionallyDeadAnalysis);
//                    allExplanators.get(f - 1).add(conditionallyDeadExplanator);
//                    anomalyTypes.get(f - 1).add(AnomalyType.CONDITIONALLYDEAD);
//                    condDead++;
//                }
//
//                for (Feature parent : featureModel.getMandatoryParents(feature)) {
//                    // create the specified analyses and the corresponding explanators
//                    debuggingModelClone = (FMDebuggingModel) falseOptionalDebuggingModel.clone();
//                    debuggingModelClone.initialize();
//                    FalseOptionalAnalysis falseOptionalAnalysis = new FalseOptionalAnalysis(debuggingModelClone, falseOptionalTestCases.get(optWithParent));
//                    FalseOptionalExplanator falseOptionalExplanator = new FalseOptionalExplanator(debuggingModelClone, falseOptionalTestCases.get(optWithParent));
//                    analyzer.addAnalysis(falseOptionalAnalysis, falseOptionalExplanator); // add the analysis to the analyzer
//
//                    allAnalyses.get(f - 1).add(falseOptionalAnalysis);
//                    allExplanators.get(f - 1).add(falseOptionalExplanator);
//                    anomalyTypes.get(f - 1).add(AnomalyType.FALSEOPTIONAL);
//                    optWithParent++;
//                }
//            }
//        }
//
//        analyzer.run(); // run the analyzer
//
//        // Fetch the results
//        for (int f = 1; f < featureModel.getNumOfFeatures(); f++) {
//            System.out.println(ConsoleColors.RESET + "[*] Feature: " + featureModel.getFeature(f));
//
//            // print the result
//            ExplanationColors.EXPLANATION = ConsoleColors.WHITE;
//
//            boolean anomaly = false;
//            for (int runningAnalysis = 0; runningAnalysis < allAnalyses.get(f - 1).size(); runningAnalysis++) {
//                if (!allAnalyses.get(f - 1).get(runningAnalysis).get()) {
//                    anomaly = true;
//                    switch (anomalyTypes.get(f - 1).get(runningAnalysis)) {
//                        case DEAD -> {
//                            System.out.println(ExplanationColors.ANOMALY + "X Dead feature");
//                            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(allExplanators.get(f - 1).get(runningAnalysis).get(), "dead feature"));
//                        }
//                        case FULLMANDATORY -> {
//                            System.out.println(ExplanationColors.ANOMALY + "X Full mandatory feature");
//                            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(allExplanators.get(f - 1).get(runningAnalysis).get(), "full mandatory feature"));
//                        }
//                        case CONDITIONALLYDEAD -> {
//                            System.out.println(ExplanationColors.ANOMALY + "X Conditionally dead feature");
//                            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(allExplanators.get(f - 1).get(runningAnalysis).get(), "conditionally dead feature"));
//                        }
//                        case FALSEOPTIONAL -> {
//                            System.out.println(ExplanationColors.ANOMALY + "X False optional feature");
//                            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(allExplanators.get(f - 1).get(runningAnalysis).get(), "false optional feature"));
//                        }
//                    }
//                }
//            }
//            if (!anomaly) {
//                System.out.println(ConsoleColors.GREEN + "\u2713 No anomaly found" + ConsoleColors.RESET);
//            }
//        }
//    }
//
//    @Test
//    public void testFullAnalysis() throws FeatureModelParserException, ExecutionException, FeatureModelException, InterruptedException, CloneNotSupportedException {
//        File fileFM = new File("src/test/resources/basic_featureide_multiple1.xml");
//        FMParserFactory factory = FMParserFactory.getInstance();
//        FeatureModelParser parser = factory.getParser(FMFormat.FEATUREIDE);
//        FeatureModel featureModel = parser.parse(fileFM);
//
//        FMAnalyzer analyzer = new FMAnalyzer();
//        analyzer.performFullAnalysis(featureModel);
//    }
//
//    @Test
//    public void testLargeModel_1() throws FeatureModelParserException, ExecutionException, FeatureModelException, InterruptedException, CloneNotSupportedException {
//        // 42 features in 6 layers - no constraints
//        File fileFM = new File("src/test/resources/basic_featureide_large1.xml");
//        FMParserFactory factory = FMParserFactory.getInstance();
//        FeatureModelParser parser = factory.getParser(FMFormat.FEATUREIDE);
//        FeatureModel featureModel = parser.parse(fileFM);
//
//        FMAnalyzer analyzer = new FMAnalyzer();
//        analyzer.performFullAnalysis(featureModel);
//    }
//
//    @Test
//    public void testLargeModel_2() throws FeatureModelParserException, ExecutionException, FeatureModelException, InterruptedException, CloneNotSupportedException {
//        // 42 features in 6 layers - few, basic constraints
//        File fileFM = new File("src/test/resources/basic_featureide_large2.xml");
//        FMParserFactory factory = FMParserFactory.getInstance();
//        FeatureModelParser parser = factory.getParser(FMFormat.FEATUREIDE);
//        FeatureModel featureModel = parser.parse(fileFM);
//
//        FMAnalyzer analyzer = new FMAnalyzer();
//        analyzer.performFullAnalysis(featureModel);
//    }
//
//    @Test
//    public void testLargeModel_3() throws FeatureModelParserException, ExecutionException, FeatureModelException, InterruptedException, CloneNotSupportedException {
//        // 42 features in 6 layers - more constraints
//        File fileFM = new File("src/test/resources/basic_featureide_large3.xml");
//        FMParserFactory factory = FMParserFactory.getInstance();
//        FeatureModelParser parser = factory.getParser(FMFormat.FEATUREIDE);
//        FeatureModel featureModel = parser.parse(fileFM);
//
//        FMAnalyzer analyzer = new FMAnalyzer();
//        analyzer.performFullAnalysis(featureModel);
//    }
//
//    @Test
//    void testRedundancy_1() throws FeatureModelParserException, ExecutionException, InterruptedException {
//        // load the feature model
//        File fileFM = new File("src/test/resources/bamboobike_featureide_deadfeature2.xml");
//        FMParserFactory factory = FMParserFactory.getInstance();
//        FeatureModelParser parser = factory.getParser(FMFormat.FEATUREIDE);
//        FeatureModel featureModel = parser.parse(fileFM);
//
//        FMCdrModel model = new FMCdrModel(featureModel, true, false, true);
//        model.initialize();
//
//        // create the redundancy analysis
//        RedundancyAnalysis analysis = new RedundancyAnalysis(model);
//
//        FMAnalyzer analyzer = new FMAnalyzer();
//        analyzer.addAnalysis(analysis, null); // add the analysis to the analyzer
//        analyzer.run(); // run the analyzer
//
//        // print the result
//        ExplanationColors.EXPLANATION = ConsoleColors.WHITE;
//        if (!analysis.get()) {
//            System.out.println(ExplanationColors.ANOMALY + "X Redundant constraint:");
//            System.out.println(ExplanationColors.EXPLANATION + ConstraintUtils.convertToString(analysis.getRedundantConstraints(), "\n", "\t", false));
//        }
//
//        assertFalse(analysis.get());
//
//        Set<Constraint> constraints = new LinkedHashSet<>();
//        constraints.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 2));
//
//        assertEquals(1, analysis.getRedundantConstraints().size());
//        assertEquals(constraints, analysis.getRedundantConstraints());
//    }
//
//    @Test
//    void testRedundancy_2() throws ExecutionException, InterruptedException, FeatureModelException {
//        // create the feature model
//        FeatureModel fm = new FeatureModel();
//        fm.addFeature("survey", "survey");
//        fm.addFeature("pay", "pay");
//        fm.addFeature("ABtesting", "ABtesting");
//        fm.addFeature("statistics", "statistics");
//        fm.addFeature("qa", "qa");
//        fm.addFeature("license", "license");
//        fm.addFeature("nonlicense", "nonlicense");
//        fm.addFeature("multiplechoice", "multiplechoice");
//        fm.addFeature("singlechoice", "singlechoice");
//        fm.addRelationship(RelationshipType.MANDATORY, fm.getFeature("survey"), Collections.singletonList(fm.getFeature("pay")));
//        fm.addRelationship(RelationshipType.OPTIONAL, fm.getFeature("ABtesting"), Collections.singletonList(fm.getFeature("survey")));
//        fm.addRelationship(RelationshipType.MANDATORY, fm.getFeature("survey"), Collections.singletonList(fm.getFeature("statistics")));
//        fm.addRelationship(RelationshipType.MANDATORY, fm.getFeature("survey"), Collections.singletonList(fm.getFeature("qa")));
//        fm.addRelationship(RelationshipType.ALTERNATIVE, fm.getFeature("pay"), List.of(fm.getFeature("license"), fm.getFeature("nonlicense")));
//        fm.addRelationship(RelationshipType.OR, fm.getFeature("qa"), List.of(fm.getFeature("multiplechoice"), fm.getFeature("singlechoice")));
//        fm.addRelationship(RelationshipType.OPTIONAL, fm.getFeature("ABtesting"), Collections.singletonList(fm.getFeature("statistics")));
//        fm.addConstraint(RelationshipType.REQUIRES, fm.getFeature("ABtesting"), Collections.singletonList(fm.getFeature("statistics")));
//        fm.addConstraint(RelationshipType.EXCLUDES, fm.getFeature("ABtesting"), Collections.singletonList(fm.getFeature("nonlicense")));
//        fm.addConstraint(RelationshipType.REQUIRES, fm.getFeature("ABtesting"), Collections.singletonList(fm.getFeature("survey")));
//
//        FMCdrModel model = new FMCdrModel(fm, true, false, true);
//        model.initialize();
//
//        // create the redundancy analysis
//        RedundancyAnalysis analysis = new RedundancyAnalysis(model);
//
//        FMAnalyzer analyzer = new FMAnalyzer();
//        analyzer.addAnalysis(analysis, null); // add the analysis to the analyzer
//        analyzer.run(); // run the analyzer
//
//        // print the result
//        ExplanationColors.EXPLANATION = ConsoleColors.WHITE;
//        if (!analysis.get()) {
//            System.out.println(ExplanationColors.ANOMALY + "X Redundant constraints:");
//            System.out.println(ExplanationColors.EXPLANATION + ConstraintUtils.convertToString(analysis.getRedundantConstraints(), "\n", "\t", false));
//        }
//
//        assertFalse(analysis.get());
//
//        Set<Constraint> constraints = new LinkedHashSet<>();
//        constraints.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 0));
//        constraints.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 2));
//        constraints.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 3));
//
//        assertEquals(3, analysis.getRedundantConstraints().size());
//        assertEquals(constraints, analysis.getRedundantConstraints());
//    }
//
//    @Test
//    public void testRedundancy_3() throws FeatureModelParserException, ExecutionException, FeatureModelException, InterruptedException, CloneNotSupportedException {
//        File fileFM = new File("src/test/resources/bamboobike_featureide_redundancies1.xml");
//        FMParserFactory factory = FMParserFactory.getInstance();
//        FeatureModelParser parser = factory.getParser(FMFormat.FEATUREIDE);
//        FeatureModel featureModel = parser.parse(fileFM);
//
//        FMAnalyzer analyzer = new FMAnalyzer();
//        analyzer.performFullAnalysis(featureModel);
//
//        // TODO add assertions
//    }
//
//    @Test
//    public void testRedundancy_4() throws FeatureModelParserException, ExecutionException, FeatureModelException, InterruptedException, CloneNotSupportedException {
//        File fileFM = new File("src/test/resources/bamboobike_featureide_redundancies2.xml");
//        FMParserFactory factory = FMParserFactory.getInstance();
//        FeatureModelParser parser = factory.getParser(FMFormat.FEATUREIDE);
//        FeatureModel featureModel = parser.parse(fileFM);
//
//        FMAnalyzer analyzer = new FMAnalyzer();
//        analyzer.performFullAnalysis(featureModel);
//
//        // TODO add assertions
//    }
//
//    @Test
//    public void testRedundancy_5() throws FeatureModelParserException, ExecutionException, FeatureModelException, InterruptedException, CloneNotSupportedException {
//        File fileFM = new File("src/test/resources/bamboobike_featureide_redundancies3.xml");
//        FMParserFactory factory = FMParserFactory.getInstance();
//        FeatureModelParser parser = factory.getParser(FMFormat.FEATUREIDE);
//        FeatureModel featureModel = parser.parse(fileFM);
//
//        FMAnalyzer analyzer = new FMAnalyzer();
//        analyzer.performFullAnalysis(featureModel);
//
//        // TODO add assertions
//    }
//
//    @Test
//    public void testRedundancy_6() throws FeatureModelParserException, ExecutionException, FeatureModelException, InterruptedException, CloneNotSupportedException {
//        File fileFM = new File("src/test/resources/bamboobike_featureide_redundancies4.xml");
//        FMParserFactory factory = FMParserFactory.getInstance();
//        FeatureModelParser parser = factory.getParser(FMFormat.FEATUREIDE);
//        FeatureModel featureModel = parser.parse(fileFM);
//
//        FMAnalyzer analyzer = new FMAnalyzer();
//        analyzer.performFullAnalysis(featureModel);
//
//        // TODO add assertions
//    }
}