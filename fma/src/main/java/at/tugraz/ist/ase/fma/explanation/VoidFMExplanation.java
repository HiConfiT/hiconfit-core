/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.explanation;

import at.tugraz.ist.ase.common.ConsoleColors;
import at.tugraz.ist.ase.fma.analysis.AbstractFMAnalysis;
import at.tugraz.ist.ase.fma.analysis.AnalysisUtils;
import at.tugraz.ist.ase.fma.analysis.VoidFMAnalysis;
import at.tugraz.ist.ase.fma.anomaly.AnomalyType;
import at.tugraz.ist.ase.fma.explanator.ExplanationColors;
import at.tugraz.ist.ase.fma.explanator.ExplanationUtils;
import lombok.NonNull;

import java.util.List;

public class VoidFMExplanation implements IAnalysisExplanable {
    /**
     * Get a descriptive explanation of the VoidFM analysis
     * @param analyses the list of analyses
     * @return a descriptive explanation, or "" if the VoidFMAnalysis is not found
     */
    public String getDescriptiveExplanation(@NonNull List<AbstractFMAnalysis<?>> analyses,
                                            @NonNull Class<? extends AbstractFMAnalysis<?>> analysisClass,
                                            @NonNull AnomalyType anomalyType) {
        VoidFMAnalysis analysis = (VoidFMAnalysis) AnalysisUtils.getAnalyses(analyses, analysisClass).get(0);

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
