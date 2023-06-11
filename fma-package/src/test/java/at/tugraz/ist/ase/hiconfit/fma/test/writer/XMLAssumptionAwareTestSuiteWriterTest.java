/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.test.writer;

import at.tugraz.ist.ase.hiconfit.fm.builder.IFeatureBuildable;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import at.tugraz.ist.ase.hiconfit.fm.parser.FMParserFactory;
import at.tugraz.ist.ase.hiconfit.fm.parser.FeatureModelParser;
import at.tugraz.ist.ase.hiconfit.fm.parser.FeatureModelParserException;
import at.tugraz.ist.ase.hiconfit.fma.FMAnalyzer;
import at.tugraz.ist.ase.hiconfit.fma.analysis.AbstractFMAnalysis;
import at.tugraz.ist.ase.hiconfit.fma.analysis.AnalysisUtils;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyAwareFeatureBuilder;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyType;
import at.tugraz.ist.ase.hiconfit.fma.explanation.AutomatedAnalysisExplanation;
import lombok.Cleanup;
import org.junit.jupiter.api.Test;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.util.EnumSet;

class XMLAssumptionAwareTestSuiteWriterTest {
    @Test
    public void testMultiple_1() throws FeatureModelParserException, CloneNotSupportedException, ParserConfigurationException, TransformerException {
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

        // print out all test cases
        for (AbstractFMAnalysis<?> analysis : analyzer.getAnalyses()) {
            System.out.println(analysis);
        }

        // write the test suite
        XMLAssumptionAwareTestSuiteWriter writer = new XMLAssumptionAwareTestSuiteWriter();
        writer.write(AnalysisUtils.getTestcases(analyzer.getAnalyses()), "src/test/resources/testsuite_multiple1.xml");
    }
}