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
import at.tugraz.ist.ase.hiconfit.cacdr_core.translator.fm.FMTestCaseTranslator;
import at.tugraz.ist.ase.hiconfit.cdrmodel.fm.FMDebuggingModel;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import at.tugraz.ist.ase.hiconfit.fma.FMAnalyzer;
import at.tugraz.ist.ase.hiconfit.fma.analysis.FullMandatoryAnalysis;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.hiconfit.fma.assumption.FullMandatoryAssumptions;
import lombok.NonNull;

import java.util.List;

public class FullMandatoryAnalysisBuilder implements IAnalysisBuildable {
    /**
     * Build FullMandatoryAnalysis on the basis of a given feature model
     * and add the generated analyses to the FMAnalyzer
     * @param featureModel the given feature model
     * @param analyzer the FMAnalyzer
     */
    @Override
    public <T extends ITestCase, F extends AnomalyAwareFeature>
    void build(@NonNull FeatureModel<F, AbstractRelationship<F>, CTConstraint> featureModel,
               @NonNull FMAnalyzer<T, F> analyzer) throws CloneNotSupportedException {
        /// FULL MANDATORY
        // create a test case/assumption
        // check full mandatory features - inconsistent( CF ∪ { c0 } U { fi = false })
        FullMandatoryAssumptions fullMandatoryAssumptions = new FullMandatoryAssumptions();
        List<ITestCase> testCases = fullMandatoryAssumptions.createAssumptions(featureModel);
        TestSuite testSuite = TestSuite.builder().testCases(testCases).build();

        build(featureModel, testSuite, analyzer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ITestCase, F extends AnomalyAwareFeature>
    void build(@NonNull FeatureModel<F, AbstractRelationship<F>, CTConstraint> featureModel,
               @NonNull TestSuite testSuite,
               @NonNull FMAnalyzer<T, F> analyzer) throws CloneNotSupportedException {
        FMDebuggingModel<F, AbstractRelationship<F>, CTConstraint>
                debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, new FMTestCaseTranslator(), false, false, false);
        debuggingModel.initialize();

        // create the specified analyses and the corresponding explanators
        for (ITestCase testCase : testSuite.getTestCases()) {
            FMDebuggingModel<F, AbstractRelationship<F>, CTConstraint>
                    clonedDebuggingModel = (FMDebuggingModel<F, AbstractRelationship<F>, CTConstraint>) debuggingModel.clone();
            clonedDebuggingModel.initialize();

            FullMandatoryAnalysis<T, F> analysis = new FullMandatoryAnalysis<>(clonedDebuggingModel, (T) testCase);

            analyzer.addAnalysis(analysis);
        }
    }
}
