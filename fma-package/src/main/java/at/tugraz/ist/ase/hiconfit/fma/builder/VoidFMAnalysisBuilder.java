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
import at.tugraz.ist.ase.hiconfit.fma.analysis.VoidFMAnalysis;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.hiconfit.fma.assumption.VoidFMAssumption;
import lombok.NonNull;

import java.util.List;

public class VoidFMAnalysisBuilder implements IAnalysisBuildable {
    /**
     * Build VoidFMAnalysis on the basis of a given feature model
     * and add the generated analyses to the FMAnalyzer
     * @param featureModel the given feature model
     * @param analyzer the FMAnalyzer
     */
    public <T extends ITestCase, F extends AnomalyAwareFeature>
    void build(@NonNull FeatureModel<F, AbstractRelationship<F>, CTConstraint> featureModel,
               @NonNull FMAnalyzer<T, F> analyzer) {
        /// VOID FEATURE MODEL
        // create a test case/assumption
        // check void feature model - inconsistent( CF ∪ { c0 })
        VoidFMAssumption voidFMAssumption = new VoidFMAssumption();
        List<ITestCase> testCases = voidFMAssumption.createAssumptions(featureModel);
        TestSuite testSuite = TestSuite.builder().testCases(testCases).build();

        build(featureModel, testSuite, analyzer);
    }

    @SuppressWarnings("unchecked")
    public <T extends ITestCase, F extends AnomalyAwareFeature>
    void build(@NonNull FeatureModel<F, AbstractRelationship<F>, CTConstraint> featureModel,
              @NonNull TestSuite testSuite,
              @NonNull FMAnalyzer<T, F> analyzer) {
        FMDebuggingModel<F, AbstractRelationship<F>, CTConstraint>
                debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, new FMTestCaseTranslator(), false, false, false);
        debuggingModel.initialize();

        // create the specified analysis and the corresponding explanator
        VoidFMAnalysis<T, F> analysis = new VoidFMAnalysis<>(debuggingModel, (T) testSuite.getTestCases().get(0));

        analyzer.addAnalysis(analysis);
    }
}
