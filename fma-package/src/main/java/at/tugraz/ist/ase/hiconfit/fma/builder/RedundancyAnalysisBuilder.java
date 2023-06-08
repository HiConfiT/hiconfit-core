/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.builder;

import at.tugraz.ist.ase.hiconfit.cdrmodel.fm.FMCdrModel;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test.ITestCase;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test.TestSuite;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import at.tugraz.ist.ase.hiconfit.fma.FMAnalyzer;
import at.tugraz.ist.ase.hiconfit.fma.analysis.RedundancyAnalysis;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.hiconfit.fma.assumption.RedundancyAssumption;
import lombok.NonNull;

import java.util.List;

public class RedundancyAnalysisBuilder implements IAnalysisBuildable {
    /**
     * Build RedundancyAnalysis on the basis of a given feature model
     * and add the generated analyses to the FMAnalyzer
     * @param featureModel the given feature model
     * @param analyzer the FMAnalyzer
     */
    @Override
    public void build(@NonNull FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> featureModel,
                      @NonNull FMAnalyzer analyzer) throws CloneNotSupportedException {
        // REDUNDANCIES
        RedundancyAssumption redundancyAssumption = new RedundancyAssumption();
        List<ITestCase> testCases = redundancyAssumption.createAssumptions(featureModel);
        TestSuite testSuite = TestSuite.builder().testCases(testCases).build();

        build(featureModel, testSuite, analyzer);
    }

    @Override
    public void build(@NonNull FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> featureModel,
                      @NonNull TestSuite testSuite,
                      @NonNull FMAnalyzer analyzer) {
        FMCdrModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                model = new FMCdrModel<>(featureModel, true, false, true, true);
        model.initialize();

        // create the redundancy analysis
        ITestCase testCase = testSuite.getTestCases().get(0);

        RedundancyAnalysis redundancyAnalysis = new RedundancyAnalysis(model, testCase);

        analyzer.addAnalysis(redundancyAnalysis);
    }
}
