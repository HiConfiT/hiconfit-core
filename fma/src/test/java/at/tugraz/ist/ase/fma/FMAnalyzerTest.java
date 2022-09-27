/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma;

import at.tugraz.ist.ase.cdrmodel.AbstractCDRModel;
import at.tugraz.ist.ase.cdrmodel.test.TestSuite;
import at.tugraz.ist.ase.cdrmodel.test.builder.fm.XMLTestCaseBuilder;
import at.tugraz.ist.ase.cdrmodel.test.reader.XMLTestSuiteReader;
import at.tugraz.ist.ase.fm.builder.*;
import at.tugraz.ist.ase.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.fm.core.CTConstraint;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import at.tugraz.ist.ase.fm.parser.FMParserFactory;
import at.tugraz.ist.ase.fm.parser.FeatureModelParser;
import at.tugraz.ist.ase.fm.parser.FeatureModelParserException;
import at.tugraz.ist.ase.fm.translator.ConfRuleTranslator;
import at.tugraz.ist.ase.fma.analysis.*;
import at.tugraz.ist.ase.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.fma.anomaly.AnomalyAwareFeatureBuilder;
import at.tugraz.ist.ase.fma.anomaly.AnomalyType;
import at.tugraz.ist.ase.fma.builder.*;
import at.tugraz.ist.ase.fma.explanation.AutomatedAnalysisExplanation;
import at.tugraz.ist.ase.fma.explanation.CompactExplanation;
import at.tugraz.ist.ase.fma.explanation.RedundancyAnalysisExplanation;
import at.tugraz.ist.ase.fma.explanation.VoidFMExplanation;
import at.tugraz.ist.ase.fma.monitor.ProgressMonitor;
import at.tugraz.ist.ase.fma.test.AssumptionAwareTestCase;
import at.tugraz.ist.ase.fma.test.builder.XMLAssumptionAwareTestCaseBuilder;
import at.tugraz.ist.ase.fma.test.reader.XMLAssumptionAwareTestSuiteReader;
import at.tugraz.ist.ase.kb.core.Constraint;
import com.google.common.collect.Iterators;
import lombok.Cleanup;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static at.tugraz.ist.ase.common.IOUtils.getInputStream;
import static org.junit.jupiter.api.Assertions.*;

class FMAnalyzerTest {
    @Test
    void testVoidFM_0() throws FeatureModelParserException, ExecutionException, InterruptedException {
        // load the feature model
        File fileFM = new File("src/test/resources/bamboobike_featureide_void.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        // create the parser
        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        // generates analyses and add them to the analyzer
        // USING the VoidFMAnalysisBuilder
        VoidFMAnalysisBuilder voidFMAnalysisBuilder = new VoidFMAnalysisBuilder();
        voidFMAnalysisBuilder.build(featureModel, analyzer);

        // run the analyzer
        analyzer.run(true);

        // print the result
        VoidFMExplanation voidFMExplanation = new VoidFMExplanation();
        String explanation = voidFMExplanation.getDescriptiveExplanation(analyzer.getAnalyses(), VoidFMAnalysis.class, AnomalyType.VOID);
        System.out.println(explanation);

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        VoidFMAnalysis voidAnalysis = (VoidFMAnalysis) analyses.get(0);

        assertFalse(voidAnalysis.get());
        assertTrue(((AssumptionAwareTestCase)voidAnalysis.getAssumption()).getAssumptions().get(0).isAnomalyType(AnomalyType.VOID));

        List<Set<Constraint>> allDiagnoses = voidAnalysis.getExplanator().getDiagnoses();

        AbstractCDRModel model = voidAnalysis.getModel();
        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 8));

        Set<Constraint> cs2 = new LinkedHashSet<>();
        cs2.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 1));

        Set<Constraint> cs3 = new LinkedHashSet<>();
        cs3.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 0));

        assertEquals(3, allDiagnoses.size());
        assertEquals(cs1, allDiagnoses.get(0));
        assertEquals(cs2, allDiagnoses.get(1));
        assertEquals(cs3, allDiagnoses.get(2));
    }

    /**
     * Test run() method
     * This unit test simulates the case when read test cases from a XML file, add them to the analyzer,
     * and run the analyzer.
     * DeadFeatureAnalysis won't be executed because VoidFMAnalysis is violated
     */
    @Test
    void testVoidFM_1() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
        // load the feature model
        File fileFM = new File("src/test/resources/bamboobike_featureide_void.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        // create the parser
        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.VOID,
                AnomalyType.DEAD); // DeadFeatureAnalysis won't be executed because VoidFMAnalysis is violated
        // generates analyses and add them to the analyzer
        // USING the AutomatedAnalysisBuilder
        AutomatedAnalysisBuilder analysisBuilder = new AutomatedAnalysisBuilder();
        analysisBuilder.build(featureModel, options, analyzer);

        // run the analyzer
        analyzer.run(true);

        // print the result using AutomatedAnalysisExplanation
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        VoidFMAnalysis voidAnalysis = (VoidFMAnalysis) analyses.get(0);

        assertFalse(voidAnalysis.get());
        assertTrue(((AssumptionAwareTestCase)voidAnalysis.getAssumption()).getAssumptions().get(0).isAnomalyType(AnomalyType.VOID));

        List<Set<Constraint>> allDiagnoses = voidAnalysis.getExplanator().getDiagnoses();

        AbstractCDRModel model = voidAnalysis.getModel();
        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 8));

        Set<Constraint> cs2 = new LinkedHashSet<>();
        cs2.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 1));

        Set<Constraint> cs3 = new LinkedHashSet<>();
        cs3.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 0));

        assertEquals(3, allDiagnoses.size());
        assertEquals(cs1, allDiagnoses.get(0));
        assertEquals(cs2, allDiagnoses.get(1));
        assertEquals(cs3, allDiagnoses.get(2));
    }

    /**
     * Test generateAndRun() method
     * DeadFeatureAnalysis won't be executed because VoidFMAnalysis is violated
     */
    @Test
    void testVoidFM_2() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
        // load the feature model
        File fileFM = new File("src/test/resources/bamboobike_featureide_void.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        // create the parser
        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.VOID,
                AnomalyType.DEAD); // DeadFeatureAnalysis won't be executed because VoidFMAnalysis is violated

        // run the analyzer
        analyzer.generateAndRun(options,true);

        // print the result using AutomatedAnalysisExplanation
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        VoidFMAnalysis voidAnalysis = (VoidFMAnalysis) analyses.get(0);

        assertFalse(voidAnalysis.get());
        assertTrue(((AssumptionAwareTestCase)voidAnalysis.getAssumption()).getAssumptions().get(0).isAnomalyType(AnomalyType.VOID));

        List<Set<Constraint>> allDiagnoses = voidAnalysis.getExplanator().getDiagnoses();

        AbstractCDRModel model = voidAnalysis.getModel();
        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 8));

        Set<Constraint> cs2 = new LinkedHashSet<>();
        cs2.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 1));

        Set<Constraint> cs3 = new LinkedHashSet<>();
        cs3.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 0));

        assertEquals(3, allDiagnoses.size());
        assertEquals(cs1, allDiagnoses.get(0));
        assertEquals(cs2, allDiagnoses.get(1));
        assertEquals(cs3, allDiagnoses.get(2));
    }

    @Test
    void testDeadFeature_0() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
        // load the feature model
        File fileFM = new File("src/test/resources/bamboobike_featureide_deadfeature1.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        // create the parser
        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        // generates analyses and add them to the analyzer
        // USING the DeadFeatureAnalysisBuilder
        DeadFeatureAnalysisBuilder deadFeatureAnalysisBuilder = new DeadFeatureAnalysisBuilder();
        deadFeatureAnalysisBuilder.build(featureModel, analyzer);

        // run the analyzer
        analyzer.run(true);

        // print the result
        CompactExplanation explanation = new CompactExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), DeadFeatureAnalysis.class, AnomalyType.DEAD));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        DeadFeatureAnalysis analysis = (DeadFeatureAnalysis) analyses.get(6);

        assertFalse(analysis.get());

        List<Set<Constraint>> allDiagnoses = analysis.getExplanator().getDiagnoses();

        AbstractCDRModel model = analysis.getModel();
        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 8));

        Set<Constraint> cs2 = new LinkedHashSet<>();
        cs2.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 1));

        assertEquals(2, allDiagnoses.size());
        assertEquals(cs1, allDiagnoses.get(0));
        assertEquals(cs2, allDiagnoses.get(1));
    }

    /**
     * Test run() method
     */
    @Test
    void testDeadFeature_1() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
        // load the feature model
        File fileFM = new File("src/test/resources/bamboobike_featureide_deadfeature1.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        // create the parser
        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.VOID,
                AnomalyType.DEAD);
        // generates analyses and add them to the analyzer
        AutomatedAnalysisBuilder analysisBuilder = new AutomatedAnalysisBuilder();
        analysisBuilder.build(featureModel, options, analyzer);

        // run the analyzer
        analyzer.run(true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        VoidFMAnalysis analysis1 = (VoidFMAnalysis) analyses.get(0);
        DeadFeatureAnalysis analysis2 = (DeadFeatureAnalysis) analyses.get(7);

        assertTrue(analysis1.get());
        assertFalse(analysis2.get());

        List<Set<Constraint>> allDiagnoses = analysis2.getExplanator().getDiagnoses();

        AbstractCDRModel model = analysis2.getModel();
        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 8));

        Set<Constraint> cs2 = new LinkedHashSet<>();
        cs2.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 1));

        assertEquals(2, allDiagnoses.size());
        assertEquals(cs1, allDiagnoses.get(0));
        assertEquals(cs2, allDiagnoses.get(1));
    }

    /**
     * Test generateAndRun() method
     */
    @Test
    void testDeadFeature_11() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
        // load the feature model
        File fileFM = new File("src/test/resources/bamboobike_featureide_deadfeature1.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        // create the parser
        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.VOID,
                AnomalyType.DEAD);

        // run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        VoidFMAnalysis analysis1 = (VoidFMAnalysis) analyses.get(0);
        DeadFeatureAnalysis analysis2 = (DeadFeatureAnalysis) analyses.get(7);

        assertTrue(analysis1.get());
        assertFalse(analysis2.get());

        List<Set<Constraint>> allDiagnoses = analysis2.getExplanator().getDiagnoses();

        AbstractCDRModel model = analysis2.getModel();
        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 8));

        Set<Constraint> cs2 = new LinkedHashSet<>();
        cs2.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 1));

        assertEquals(2, allDiagnoses.size());
        assertEquals(cs1, allDiagnoses.get(0));
        assertEquals(cs2, allDiagnoses.get(1));
    }

    /**
     * Test run() method
     */
    @Test
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.VOID,
                                                         AnomalyType.DEAD);
        // generates analyses and add them to the analyzer
        AutomatedAnalysisBuilder analysisBuilder = new AutomatedAnalysisBuilder();
        analysisBuilder.build(featureModel, options, analyzer);

        analyzer.run(true); // run the analyzer

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        VoidFMAnalysis analysis1 = (VoidFMAnalysis) analyses.get(0);
        DeadFeatureAnalysis analysis2 = (DeadFeatureAnalysis) analyses.get(5);
        DeadFeatureAnalysis analysis3 = (DeadFeatureAnalysis) analyses.get(7);

        assertTrue(analysis1.get());
        assertFalse(analysis2.get());
        assertFalse(analysis3.get());

        List<Set<Constraint>> allDiagnoses = analysis2.getExplanator().getDiagnoses();
        List<Set<Constraint>> allDiagnoses1 = analysis3.getExplanator().getDiagnoses();

        AbstractCDRModel model = analysis2.getModel();
        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 8));

        Set<Constraint> cs2 = new LinkedHashSet<>();
        cs2.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 4));

        Set<Constraint> cs3 = new LinkedHashSet<>();
        cs3.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 1));

        assertEquals(3, allDiagnoses.size());
        assertEquals(cs1, allDiagnoses.get(0));
        assertEquals(cs2, allDiagnoses.get(1));
        assertEquals(cs3, allDiagnoses.get(2));

        assertEquals(3, allDiagnoses1.size());
        assertEquals(cs1, allDiagnoses1.get(0));
        assertEquals(cs2, allDiagnoses1.get(1));
        assertEquals(cs3, allDiagnoses1.get(2));
    }

    /**
     * Test generateAndRun() method
     */
    @Test
    void testDeadFeature_21() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.VOID,
                AnomalyType.DEAD);

        analyzer.generateAndRun(options, true); // run the analyzer

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        VoidFMAnalysis analysis1 = (VoidFMAnalysis) analyses.get(0);
        DeadFeatureAnalysis analysis2 = (DeadFeatureAnalysis) analyses.get(5);
        DeadFeatureAnalysis analysis3 = (DeadFeatureAnalysis) analyses.get(7);

        assertTrue(analysis1.get());
        assertFalse(analysis2.get());
        assertFalse(analysis3.get());

        List<Set<Constraint>> allDiagnoses = analysis2.getExplanator().getDiagnoses();
        List<Set<Constraint>> allDiagnoses1 = analysis3.getExplanator().getDiagnoses();

        AbstractCDRModel model = analysis2.getModel();
        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 8));

        Set<Constraint> cs2 = new LinkedHashSet<>();
        cs2.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 4));

        Set<Constraint> cs3 = new LinkedHashSet<>();
        cs3.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 1));

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
    void testDeadFeature_22() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.allOf(AnomalyType.class);

        analyzer.generateAndRun(options, true); // run the analyzer

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        VoidFMAnalysis analysis1 = (VoidFMAnalysis) analyses.get(0);
        DeadFeatureAnalysis analysis2 = (DeadFeatureAnalysis) analyses.get(5);
        DeadFeatureAnalysis analysis3 = (DeadFeatureAnalysis) analyses.get(7);

        assertTrue(analysis1.get());
        assertFalse(analysis2.get());
        assertFalse(analysis3.get());

        List<Set<Constraint>> allDiagnoses = analysis2.getExplanator().getDiagnoses();
        List<Set<Constraint>> allDiagnoses1 = analysis3.getExplanator().getDiagnoses();

        AbstractCDRModel model = analysis2.getModel();
        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 8));

        Set<Constraint> cs2 = new LinkedHashSet<>();
        cs2.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 4));

        Set<Constraint> cs3 = new LinkedHashSet<>();
        cs3.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 1));

        assertEquals(3, allDiagnoses.size());
        assertEquals(cs1, allDiagnoses.get(0));
        assertEquals(cs2, allDiagnoses.get(1));
        assertEquals(cs3, allDiagnoses.get(2));

        assertEquals(3, allDiagnoses1.size());
        assertEquals(cs1, allDiagnoses1.get(0));
        assertEquals(cs2, allDiagnoses1.get(1));
        assertEquals(cs3, allDiagnoses1.get(2));
    }

    /**
     * Test run() method
     */
    @Test
    void testDeadFeature_3() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.VOID,
                AnomalyType.DEAD);
        // generates analyses and add them to the analyzer
        AutomatedAnalysisBuilder analysisBuilder = new AutomatedAnalysisBuilder();
        analysisBuilder.build(featureModel, options, analyzer);

        // run the analyzer
        analyzer.run(true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        VoidFMAnalysis analysis1 = (VoidFMAnalysis) analyses.get(0);
        DeadFeatureAnalysis analysis2 = (DeadFeatureAnalysis) analyses.get(4);

        assertTrue(analysis1.get());
        assertFalse(analysis2.get());

        List<Set<Constraint>> allDiagnoses = analysis2.getExplanator().getDiagnoses();

        AbstractCDRModel model = analysis2.getModel();
        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 8));

        Set<Constraint> cs2 = new LinkedHashSet<>();
        cs2.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 0));
        cs2.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 6));

        Set<Constraint> cs3 = new LinkedHashSet<>();
        cs3.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 0));
        cs3.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 4));

        assertEquals(3, allDiagnoses.size());
        assertEquals(cs1, allDiagnoses.get(0));
        assertEquals(cs2, allDiagnoses.get(1));
        assertEquals(cs3, allDiagnoses.get(2));
    }

    /**
     * Test generateAndRun() method
     */
    @Test
    void testDeadFeature_31() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.VOID,
                AnomalyType.DEAD);

        // run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        VoidFMAnalysis analysis1 = (VoidFMAnalysis) analyses.get(0);
        DeadFeatureAnalysis analysis2 = (DeadFeatureAnalysis) analyses.get(4);

        assertTrue(analysis1.get());
        assertFalse(analysis2.get());

        List<Set<Constraint>> allDiagnoses = analysis2.getExplanator().getDiagnoses();

        AbstractCDRModel model = analysis2.getModel();
        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 8));

        Set<Constraint> cs2 = new LinkedHashSet<>();
        cs2.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 0));
        cs2.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 6));

        Set<Constraint> cs3 = new LinkedHashSet<>();
        cs3.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 0));
        cs3.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 4));

        assertEquals(3, allDiagnoses.size());
        assertEquals(cs1, allDiagnoses.get(0));
        assertEquals(cs2, allDiagnoses.get(1));
        assertEquals(cs3, allDiagnoses.get(2));
    }

    @Test
    void testFullMandatory_0() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        // generates analyses and add them to the analyzer
        // USING the FullMandatoryAnalysisBuilder
        FullMandatoryAnalysisBuilder fullMandatoryAnalysisBuilder = new FullMandatoryAnalysisBuilder();
        fullMandatoryAnalysisBuilder.build(featureModel, analyzer);

        // run the analyzer
        analyzer.run(true);

        // print the result
        CompactExplanation explanation = new CompactExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), FullMandatoryAnalysis.class, AnomalyType.FULLMANDATORY));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        FullMandatoryAnalysis analysis = (FullMandatoryAnalysis) analyses.get(2);

        assertFalse(analysis.get());

        List<Set<Constraint>> allDiagnoses = analysis.getExplanator().getDiagnoses();

        AbstractCDRModel model = analysis.getModel();
        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 2));

        Set<Constraint> cs2 = new LinkedHashSet<>();
        cs2.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 0));

        assertEquals(2, allDiagnoses.size());
        assertEquals(cs1, allDiagnoses.get(0));
        assertEquals(cs2, allDiagnoses.get(1));
    }

    /**
     * Test run() method
     */
    @Test
    void testFullMandatory_1() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.FULLMANDATORY);
        // generates analyses and add them to the analyzer
        AutomatedAnalysisBuilder analysisBuilder = new AutomatedAnalysisBuilder();
        analysisBuilder.build(featureModel, options, analyzer);

        // run the analyzer
        analyzer.run(true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        FullMandatoryAnalysis analysis = (FullMandatoryAnalysis) analyses.get(2);

        assertFalse(analysis.get());

        List<Set<Constraint>> allDiagnoses = analysis.getExplanator().getDiagnoses();

        AbstractCDRModel model = analysis.getModel();
        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 2));

        Set<Constraint> cs2 = new LinkedHashSet<>();
        cs2.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 0));

        assertEquals(2, allDiagnoses.size());
        assertEquals(cs1, allDiagnoses.get(0));
        assertEquals(cs2, allDiagnoses.get(1));
    }

    /**
     * Test generateAndRun() method
     */
    @Test
    void testFullMandatory_11() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.FULLMANDATORY);

        // generate VoidFMAnalysis, DeadFeatureAnalysis, and FullMandatoryAnalysis
        // run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        FullMandatoryAnalysis analysis = (FullMandatoryAnalysis) analyses.get(6);

        assertFalse(analysis.get());

        List<Set<Constraint>> allDiagnoses = analysis.getExplanator().getDiagnoses();

        AbstractCDRModel model = analysis.getModel();
        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 2));

        Set<Constraint> cs2 = new LinkedHashSet<>();
        cs2.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 0));

        assertEquals(2, allDiagnoses.size());
        assertEquals(cs1, allDiagnoses.get(0));
        assertEquals(cs2, allDiagnoses.get(1));
    }

    @Test
    void testFalseOptional_0() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        // generates analyses and add them to the analyzer
        FalseOptionalAnalysisBuilder analysisBuilder = new FalseOptionalAnalysisBuilder();
        analysisBuilder.build(featureModel, analyzer);

        // run the analyzer
        analyzer.run(true);

        // print the result
        CompactExplanation explanation = new CompactExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), FalseOptionalAnalysis.class, AnomalyType.FALSEOPTIONAL));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        FalseOptionalAnalysis analysis = (FalseOptionalAnalysis) analyses.get(0);

        assertFalse(analysis.get());

        List<Set<Constraint>> allDiagnoses = analysis.getExplanator().getDiagnoses();

        AbstractCDRModel model = analysis.getModel();
        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 2));

        assertEquals(1, allDiagnoses.size());
        assertEquals(cs1, allDiagnoses.get(0));
    }

    /**
     * Test run() method
     */
    @Test
    void testFalseOptional_1() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.FALSEOPTIONAL);
        // generates analyses and add them to the analyzer
        AutomatedAnalysisBuilder analysisBuilder = new AutomatedAnalysisBuilder();
        analysisBuilder.build(featureModel, options, analyzer);

        // run the analyzer
        analyzer.run(true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        FalseOptionalAnalysis analysis = (FalseOptionalAnalysis) analyses.get(0);

        assertFalse(analysis.get());

        List<Set<Constraint>> allDiagnoses = analysis.getExplanator().getDiagnoses();

        AbstractCDRModel model = analysis.getModel();
        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 2));

        assertEquals(1, allDiagnoses.size());
        assertEquals(cs1, allDiagnoses.get(0));
    }

    /**
     * Test generateAndRun() method
     */
    @Test
    void testFalseOptional_11() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.FALSEOPTIONAL);

        // generate VoidFMAnalysis, DeadFeatureAnalysis, and FalseOptionalAnalysis
        // run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        FalseOptionalAnalysis analysis = (FalseOptionalAnalysis) analyses.get(3);

        assertFalse(analysis.get());

        List<Set<Constraint>> allDiagnoses = analysis.getExplanator().getDiagnoses();

        AbstractCDRModel model = analysis.getModel();
        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 2));

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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        // generates analyses and add them to the analyzer
        ConditionallyDeadAnalysisBuilder analysisBuilder = new ConditionallyDeadAnalysisBuilder();
        analysisBuilder.build(featureModel, analyzer);

        // run the analyzer
        analyzer.run(true);

        // print the result
        CompactExplanation explanation = new CompactExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), ConditionallyDeadAnalysis.class, AnomalyType.CONDITIONALLYDEAD));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        ConditionallyDeadAnalysis analysis = (ConditionallyDeadAnalysis) analyses.get(4);

        assertFalse(analysis.get());

        List<Set<Constraint>> allDiagnoses = analysis.getExplanator().getDiagnoses();

        assertEquals(1, allDiagnoses.size());
    }

    /**
     * Test run() method
     */
    @Test
    void testConditionallyDead_1() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.CONDITIONALLYDEAD);
        // generates analyses and add them to the analyzer
        AutomatedAnalysisBuilder analysisBuilder = new AutomatedAnalysisBuilder();
        analysisBuilder.build(featureModel, options, analyzer);

        // run the analyzer
        analyzer.run(true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        ConditionallyDeadAnalysis analysis = (ConditionallyDeadAnalysis) analyses.get(4);

        assertFalse(analysis.get());

        List<Set<Constraint>> allDiagnoses = analysis.getExplanator().getDiagnoses();

        assertEquals(1, allDiagnoses.size());
    }

    /**
     * Test generateAndRun() method
     */
    @Test
    void testConditionallyDead_11() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
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
//        featureModel.getFeature("Female").setAnomalyType(AnomalyType.DEAD);
//        featureModel.getFeature("Step-through").setAnomalyType(AnomalyType.DEAD);

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.CONDITIONALLYDEAD);

        // run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        ConditionallyDeadAnalysis analysis = (ConditionallyDeadAnalysis) analyses.get(15);

        assertFalse(analysis.get());

        List<Set<Constraint>> allDiagnoses = analysis.getExplanator().getDiagnoses();

        assertEquals(1, allDiagnoses.size());
    }

    /**
     * Test run() method
     */
    @Test
    void testConditionallyDead_2() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.CONDITIONALLYDEAD);
        // generates analyses and add them to the analyzer
        AutomatedAnalysisBuilder analysisBuilder = new AutomatedAnalysisBuilder();
        analysisBuilder.build(featureModel, options, analyzer);

        // run the analyzer
        analyzer.run(true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        ConditionallyDeadAnalysis analysis2 = (ConditionallyDeadAnalysis) analyses.get(1);
        ConditionallyDeadAnalysis analysis3 = (ConditionallyDeadAnalysis) analyses.get(3);

        assertFalse(analysis2.get());
        assertFalse(analysis3.get());

        List<Set<Constraint>> allDiagnoses2 = analysis2.getExplanator().getDiagnoses();
        List<Set<Constraint>> allDiagnoses3 = analysis3.getExplanator().getDiagnoses();

        assertEquals(2, allDiagnoses2.size());
        assertEquals(1, allDiagnoses3.size());
    }

    /**
     * Test generateAndRun() method
     */
    @Test
    void testConditionallyDead_21() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.CONDITIONALLYDEAD);

        // run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        ConditionallyDeadAnalysis analysis2 = (ConditionallyDeadAnalysis) analyses.get(6);
        ConditionallyDeadAnalysis analysis3 = (ConditionallyDeadAnalysis) analyses.get(8);

        assertFalse(analysis2.get());
        assertFalse(analysis3.get());

        List<Set<Constraint>> allDiagnoses2 = analysis2.getExplanator().getDiagnoses();
        List<Set<Constraint>> allDiagnoses3 = analysis3.getExplanator().getDiagnoses();

        assertEquals(2, allDiagnoses2.size());
        assertEquals(1, allDiagnoses3.size());
    }

    /**
     * Test run() method
     */
    @Test
    void testConditionallyDead_3() throws FeatureModelParserException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.allOf(AnomalyType.class);
        // generates analyses and add them to the analyzer
        AutomatedAnalysisBuilder analysisBuilder = new AutomatedAnalysisBuilder();
        analysisBuilder.build(featureModel, options, analyzer);

        // run the analyzer
        analyzer.run(true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));
    }

    /**
     * Test generateAndRun() method
     */
    @Test
    void testConditionallyDead_31() throws FeatureModelParserException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.allOf(AnomalyType.class);

        // run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));
    }

    /**
     * Test run() method
     */
    @Test
    public void testMultiple_0() throws FeatureModelParserException, CloneNotSupportedException, IOException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.allOf(AnomalyType.class);
        // generates analyses and add them to the analyzer
        // read pre-generated test cases from a file
        XMLAssumptionAwareTestSuiteReader reader = new XMLAssumptionAwareTestSuiteReader(featureModel);
        XMLAssumptionAwareTestCaseBuilder builder = new XMLAssumptionAwareTestCaseBuilder(featureModel);
        @Cleanup InputStream is = getInputStream(FMAnalyzerTest.class.getClassLoader(), "testsuite_multiple1.xml");
        TestSuite testSuite = reader.read(is, builder);

        AutomatedAnalysisBuilder analysisBuilder = new AutomatedAnalysisBuilder();
        analysisBuilder.build(featureModel, testSuite, analyzer);

        // generate analyses and run the analyzer
        analyzer.run(true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));
    }

    /**
     * Test generateAndRun() method
     */
    @Test
    public void testMultiple_1() throws FeatureModelParserException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.allOf(AnomalyType.class);
        // generate analyses and run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));
    }

    /**
     * Test generateAndRun() method
     */
    @Test
    public void testMultiple_2() throws FeatureModelParserException, CloneNotSupportedException {
        File fileFM = new File("src/test/resources/basic_featureide_multiple2.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.allOf(AnomalyType.class);
        // generate analyses and run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));
    }

    /**
     * Test generateAndRun() method
     */
    @Test
    public void testMultiple_21() throws FeatureModelParserException, CloneNotSupportedException {
        // Add redundant constraint
        File fileFM = new File("src/test/resources/basic_featureide_multiple21.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.allOf(AnomalyType.class);
        // generate analyses and run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));
    }

    /**
     * Test generateAndRun() method
     */
    @Test
    public void testMultiple_22() throws FeatureModelParserException, CloneNotSupportedException {
        // Add full mandatory feature
        File fileFM = new File("src/test/resources/basic_featureide_multiple22.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.allOf(AnomalyType.class);
        // generate analyses and run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));
    }

    /**
     * Test generateAndRun() method
     */
    @Test
    public void testMultiple_23() throws FeatureModelParserException, CloneNotSupportedException {
        // Add dead feature
        File fileFM = new File("src/test/resources/basic_featureide_multiple23.xml");

        // create the factory for anomaly feature models
        IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
        FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                factory = FMParserFactory.getInstance(featureBuilder);

        @Cleanup("dispose")
        FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                parser = factory.getParser(fileFM.getName());
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                featureModel = parser.parse(fileFM);

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.allOf(AnomalyType.class);
        // generate analyses and run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));
    }

    // these functions will take about 30-45 minutes to run
    @Disabled("Bad for Tamim's laptop battery...")
    @Test
    public void testLargeModel_1() throws FeatureModelParserException, CloneNotSupportedException {
        // 42 features in 6 layers - few, basic constraints
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.allOf(AnomalyType.class);

        // run the analyzer
        analyzer.setMonitor(new ProgressMonitor()); // MONITOR
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));
    }

    @Disabled("Bad for Tamim's laptop battery...")
    @Test
    public void testLargeModel_2() throws FeatureModelParserException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.allOf(AnomalyType.class);

        // run the analyzer
        analyzer.setMonitor(new ProgressMonitor()); // MONITOR
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));
    }

    @Disabled("Bad for Tamim's laptop battery...")
    @Test
    public void testLargeModel_3() throws FeatureModelParserException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.allOf(AnomalyType.class);

        // run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));
    }

    @Test
    void testRedundancy_0() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        // generates analyses and add them to the analyzer
        RedundancyAnalysisBuilder analysisBuilder = new RedundancyAnalysisBuilder();
        analysisBuilder.build(featureModel, analyzer);

        // run the analyzer
        analyzer.run(true);

        // print the result
        RedundancyAnalysisExplanation explanation = new RedundancyAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), RedundancyAnalysis.class, AnomalyType.REDUNDANT));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        RedundancyAnalysis analysis = (RedundancyAnalysis) analyses.get(0);

        assertFalse(analysis.get());

        AbstractCDRModel model = analysis.getModel();
        Set<Constraint> constraints = new LinkedHashSet<>();
        constraints.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 2));

        assertEquals(1, analysis.getRedundantConstraints().size());
        assertEquals(constraints, analysis.getRedundantConstraints());
    }

    @Test
    void testRedundancy_1() throws FeatureModelParserException, ExecutionException, InterruptedException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.REDUNDANT);
        // generates analyses and add them to the analyzer
        AutomatedAnalysisBuilder analysisBuilder = new AutomatedAnalysisBuilder();
        analysisBuilder.build(featureModel, options, analyzer);

        // run the analyzer
        analyzer.run(true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        RedundancyAnalysis analysis = (RedundancyAnalysis) analyses.get(0);

        assertFalse(analysis.get());

        AbstractCDRModel model = analysis.getModel();
        Set<Constraint> constraints = new LinkedHashSet<>();
        constraints.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 2));

        assertEquals(1, analysis.getRedundantConstraints().size());
        assertEquals(constraints, analysis.getRedundantConstraints());
    }

    @Test
    void testRedundancy_2() throws ExecutionException, InterruptedException, CloneNotSupportedException {
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
        fm.addOptionalRelationship(fm.getFeature("survey"), fm.getFeature("ABtesting"));
        fm.addMandatoryRelationship(fm.getFeature("survey"), fm.getFeature("statistics"));
        fm.addMandatoryRelationship(fm.getFeature("survey"), fm.getFeature("qa"));
        fm.addAlternativeRelationship(fm.getFeature("pay"), List.of(fm.getFeature("license"), fm.getFeature("nonlicense")));
        fm.addOrRelationship(fm.getFeature("qa"), List.of(fm.getFeature("multiplechoice"), fm.getFeature("singlechoice")));
        fm.addOptionalRelationship(fm.getFeature("statistics"), fm.getFeature("ABtesting")); // should be redundant
        fm.addRequires(fm.getFeature("ABtesting"), fm.getFeature("statistics")); // should be redundant
        fm.addExcludes(fm.getFeature("ABtesting"), fm.getFeature("nonlicense"));
        fm.addRequires(fm.getFeature("ABtesting"), fm.getFeature("survey")); // should be redundant

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(fm);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.REDUNDANT);
        // generates analyses and add them to the analyzer
        AutomatedAnalysisBuilder analysisBuilder = new AutomatedAnalysisBuilder();
        analysisBuilder.build(fm, options, analyzer);

        // run the analyzer
        analyzer.run(true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));

        // Assertions
        List<AbstractFMAnalysis<?>> analyses = analyzer.getAnalyses();
        RedundancyAnalysis analysis = (RedundancyAnalysis) analyses.get(0);

        assertFalse(analysis.get());

        AbstractCDRModel model = analysis.getModel();
        Set<Constraint> constraints = new LinkedHashSet<>();
        constraints.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 0));
        constraints.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 2));
        constraints.add(Iterators.get(model.getPossiblyFaultyConstraints().iterator(), 3));

        assertEquals(3, analysis.getRedundantConstraints().size());
        assertEquals(constraints, analysis.getRedundantConstraints());
    }

    @Test
    void testRedundancy_21() throws CloneNotSupportedException {
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
        fm.addOptionalRelationship(fm.getFeature("survey"), fm.getFeature("ABtesting"));
        fm.addMandatoryRelationship(fm.getFeature("survey"), fm.getFeature("statistics"));
        fm.addMandatoryRelationship(fm.getFeature("survey"), fm.getFeature("qa"));
        fm.addAlternativeRelationship(fm.getFeature("pay"), List.of(fm.getFeature("license"), fm.getFeature("nonlicense")));
        fm.addOrRelationship(fm.getFeature("qa"), List.of(fm.getFeature("multiplechoice"), fm.getFeature("singlechoice")));
        fm.addOptionalRelationship(fm.getFeature("statistics"), fm.getFeature("ABtesting")); // should be redundant
        fm.addRequires(fm.getFeature("ABtesting"), fm.getFeature("statistics")); // should be redundant
        fm.addExcludes(fm.getFeature("ABtesting"), fm.getFeature("nonlicense"));
        fm.addRequires(fm.getFeature("ABtesting"), fm.getFeature("survey")); // should be redundant

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(fm);

        EnumSet<AnomalyType> options = EnumSet.allOf(AnomalyType.class);

        // run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));
    }

    @Test
    public void testRedundancy_3() throws FeatureModelParserException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.allOf(AnomalyType.class);

        // run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));

        // TODO add assertions
    }

    @Test
    public void testRedundancy_4() throws FeatureModelParserException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.allOf(AnomalyType.class);

        // run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));

        // TODO add assertions
    }

    @Test
    public void testRedundancy_5() throws FeatureModelParserException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.allOf(AnomalyType.class);

        // run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));

        // TODO add assertions
    }

    @Test
    public void testRedundancy_6() throws FeatureModelParserException, CloneNotSupportedException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.allOf(AnomalyType.class);

        // run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));

        // TODO add assertions
    }

    @Test
    public void testSingleAnalysis_1() throws CloneNotSupportedException, FeatureModelParserException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.REDUNDANT);

        // run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));
    }

    @Test
    public void testSingleAnalysis_2() throws CloneNotSupportedException, FeatureModelParserException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.DEAD);

        // run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));
    }

    @Test
    public void testSingleAnalysis_3() throws CloneNotSupportedException, FeatureModelParserException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.FULLMANDATORY);

        // run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));
    }

    @Test
    public void testSingleAnalysis_4() throws CloneNotSupportedException, FeatureModelParserException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.CONDITIONALLYDEAD);

        // run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));
    }

    @Test
    public void testSingleAnalysis_5() throws CloneNotSupportedException, FeatureModelParserException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.FALSEOPTIONAL);

        // run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));
    }

    @Test
    public void testSingleAnalysis_6() throws CloneNotSupportedException, FeatureModelParserException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.DEAD,
                AnomalyType.REDUNDANT);

        // run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));
    }

    @Test
    public void testSingleAnalysis_7() throws CloneNotSupportedException, FeatureModelParserException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.DEAD,
                AnomalyType.FULLMANDATORY,
                AnomalyType.REDUNDANT);

        // run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));
    }

    @Test
    public void testSingleAnalysis_8() throws CloneNotSupportedException, FeatureModelParserException {
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

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.DEAD,
                AnomalyType.FULLMANDATORY);

        // run the analyzer
        analyzer.generateAndRun(options, true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));
    }
}