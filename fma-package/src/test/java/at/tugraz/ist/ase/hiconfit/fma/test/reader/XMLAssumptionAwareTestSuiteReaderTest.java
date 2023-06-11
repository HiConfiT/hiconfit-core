/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.test.reader;

import at.tugraz.ist.ase.hiconfit.cacdr_core.TestSuite;
import at.tugraz.ist.ase.hiconfit.fm.builder.IFeatureBuildable;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import at.tugraz.ist.ase.hiconfit.fm.parser.FMParserFactory;
import at.tugraz.ist.ase.hiconfit.fm.parser.FeatureModelParser;
import at.tugraz.ist.ase.hiconfit.fm.parser.FeatureModelParserException;
import at.tugraz.ist.ase.hiconfit.fma.FMAnalyzer;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyAwareFeatureBuilder;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyType;
import at.tugraz.ist.ase.hiconfit.fma.builder.AutomatedAnalysisBuilder;
import at.tugraz.ist.ase.hiconfit.fma.explanation.AutomatedAnalysisExplanation;
import at.tugraz.ist.ase.hiconfit.fma.test.TestSuiteUtils;
import at.tugraz.ist.ase.hiconfit.fma.test.builder.XMLAssumptionAwareTestCaseBuilder;
import lombok.Cleanup;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;

import static at.tugraz.ist.ase.hiconfit.common.IOUtils.getInputStream;

class XMLAssumptionAwareTestSuiteReaderTest {
    @Test
    public void testMultiple_1() throws FeatureModelParserException, CloneNotSupportedException, IOException {
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

        // write the test suite
        XMLAssumptionAwareTestSuiteReader reader = new XMLAssumptionAwareTestSuiteReader(featureModel);
        XMLAssumptionAwareTestCaseBuilder testCaseFactory = new XMLAssumptionAwareTestCaseBuilder(featureModel);
        @Cleanup InputStream is = getInputStream(XMLAssumptionAwareTestSuiteReader.class.getClassLoader(), "testsuite_multiple1.xml");
        TestSuite testSuite = reader.read(is, testCaseFactory);

        // create an analyzer
        FMAnalyzer analyzer = new FMAnalyzer(featureModel);

        // get anomaly types from test suite
        EnumSet<AnomalyType> options = TestSuiteUtils.getAnomalyTypes(testSuite);

        // add test cases to analyzer
        AutomatedAnalysisBuilder builder = new AutomatedAnalysisBuilder();
        builder.build(featureModel,testSuite,analyzer);

        // generate analyses and run the analyzer
        analyzer.run(true);

        // print the result
        AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
        System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));
    }
}