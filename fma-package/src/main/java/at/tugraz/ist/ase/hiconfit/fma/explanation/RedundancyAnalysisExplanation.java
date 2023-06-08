/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.explanation;

import at.tugraz.ist.ase.hiconfit.common.ConsoleColors;
import at.tugraz.ist.ase.hiconfit.common.ConstraintUtils;
import at.tugraz.ist.ase.hiconfit.fma.analysis.AbstractFMAnalysis;
import at.tugraz.ist.ase.hiconfit.fma.analysis.AnalysisUtils;
import at.tugraz.ist.ase.hiconfit.fma.analysis.RedundancyAnalysis;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyType;
import at.tugraz.ist.ase.hiconfit.fma.explanator.ExplanationColors;
import lombok.NonNull;

import java.util.List;

public class RedundancyAnalysisExplanation implements IAnalysisExplanable {
    @Override
    public String getDescriptiveExplanation(@NonNull List<AbstractFMAnalysis<?>> analyses,
                                            @NonNull Class<? extends AbstractFMAnalysis<?>> analysisClass,
                                            @NonNull AnomalyType anomalyType) {
        RedundancyAnalysis analysis = (RedundancyAnalysis) AnalysisUtils.getAnalyses(analyses, analysisClass).get(0);

        StringBuilder sb = new StringBuilder();
        if (analysis != null) {
            ExplanationColors.EXPLANATION = ConsoleColors.WHITE;
            if (analysis.isNon_violated()) {
                sb.append(ExplanationColors.OK).append(anomalyType.getNonViolatedDescription()).append("\n");
            } else {
                sb.append(ExplanationColors.ANOMALY)
                  .append(anomalyType.getViolatedDescription())
                  .append(analysis.getRedundantConstraints().size() > 1 ? "s" : "")
                  .append(":\n");
                sb.append(ExplanationColors.EXPLANATION)
                  .append(ConstraintUtils.convertToString(analysis.getRedundantConstraints(), "\n", "\t", false));
            }
        }
        return sb.toString();
    }
}
