/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.explanation;

import at.tugraz.ist.ase.hiconfit.cacdr_core.ITestCase;
import at.tugraz.ist.ase.hiconfit.common.ConsoleColors;
import at.tugraz.ist.ase.hiconfit.fma.analysis.AbstractFMAnalysis;
import at.tugraz.ist.ase.hiconfit.fma.analysis.AnalysisUtils;
import at.tugraz.ist.ase.hiconfit.fma.analysis.VoidFMAnalysis;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyType;
import at.tugraz.ist.ase.hiconfit.fma.explanator.ExplanationColors;
import at.tugraz.ist.ase.hiconfit.fma.explanator.ExplanationUtils;
import lombok.NonNull;

import java.util.List;

public class VoidFMExplanation implements IAnalysisExplanable {
    /**
     * Get a descriptive explanation of the VoidFM analysis
     * @param analyses the list of analyses
     * @return a descriptive explanation, or "" if the VoidFMAnalysis is not found
     */
    public <T extends ITestCase, F extends AnomalyAwareFeature>
    String getDescriptiveExplanation(@NonNull List<AbstractFMAnalysis<T, F>> analyses,
                                            @NonNull Class<?> analysisClass,
                                            @NonNull AnomalyType anomalyType) {
        VoidFMAnalysis<T, F> analysis = (VoidFMAnalysis<T, F>) AnalysisUtils.getAnalyses(analyses, analysisClass).get(0);

        StringBuilder sb = new StringBuilder();
        if (analysis != null) {
            ExplanationColors.EXPLANATION = ConsoleColors.WHITE;
            if (analysis.isNon_violated()) {
                sb.append(ExplanationColors.OK).append(anomalyType.getNonViolatedDescription()).append("\n");
            } else {
                sb.append(ExplanationColors.ANOMALY).append(anomalyType.getViolatedDescription()).append("\n");

                if (analysis.getExplanator() != null && analysis.getExplanator().getDiagnoses() != null) {
                    sb.append(ExplanationUtils.convertToDescriptiveExplanation(analysis.getExplanator().getDiagnoses(),
                            anomalyType.getDescription()));
                }
            }
        }
        return sb.toString();
    }
}
