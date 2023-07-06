/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.builder;

import at.tugraz.ist.ase.hiconfit.cacdr_core.ITestCase;
import at.tugraz.ist.ase.hiconfit.cacdr_core.TestSuite;
import at.tugraz.ist.ase.hiconfit.cdrmodel.fm.FMCdrModel;
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
    public <T extends ITestCase, F extends AnomalyAwareFeature>
    void build(@NonNull FeatureModel<F, AbstractRelationship<F>, CTConstraint> featureModel,
               @NonNull FMAnalyzer<T, F> analyzer) {
        // REDUNDANCIES
        RedundancyAssumption redundancyAssumption = new RedundancyAssumption();
        List<ITestCase> testCases = redundancyAssumption.createAssumptions(featureModel);
        TestSuite testSuite = TestSuite.builder().testCases(testCases).build();

        build(featureModel, testSuite, analyzer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ITestCase, F extends AnomalyAwareFeature>
    void build(@NonNull FeatureModel<F, AbstractRelationship<F>, CTConstraint> featureModel,
               @NonNull TestSuite testSuite,
               @NonNull FMAnalyzer<T, F> analyzer) {
        FMCdrModel<F, AbstractRelationship<F>, CTConstraint>
                model = new FMCdrModel<>(featureModel, true, false, true, true);
        model.initialize();

        // create the redundancy analysis
        ITestCase testCase = testSuite.getTestCases().get(0);

        RedundancyAnalysis<T, F> redundancyAnalysis = new RedundancyAnalysis<>(model, (T) testCase);

        analyzer.addAnalysis(redundancyAnalysis);
    }
}
