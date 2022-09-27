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
import at.tugraz.ist.ase.fma.analysis.FalseOptionalAnalysis;
import at.tugraz.ist.ase.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.fma.anomaly.AnomalyType;
import at.tugraz.ist.ase.fma.explanator.ExplanationColors;
import at.tugraz.ist.ase.fma.explanator.ExplanationUtils;
import at.tugraz.ist.ase.fma.test.AssumptionAwareTestCase;
import com.google.common.base.Joiner;
import lombok.NonNull;

import java.util.List;

/**
 * Replace to DeadFeatureExplanation, FullMandatoryExplanation,
 * FalseOptionalExplanation, and ConditionallyDeadExplanation
 */
public class CompactExplanation implements IAnalysisExplanable {
    /**
     * Get a descriptive explanation of a specified analysis
     * @param allAnalyses the list of allAnalyses
     * @param analysisClass the class of the analysis
     * @param anomalyType the type of the anomaly
     * @return a descriptive explanation, or "" if the analysis is not found
     */
    public String getDescriptiveExplanation(@NonNull List<AbstractFMAnalysis<?>> allAnalyses,
                                            @NonNull Class<? extends AbstractFMAnalysis<?>> analysisClass,
                                            @NonNull AnomalyType anomalyType) {
        List<AbstractFMAnalysis<?>> filteredAnalyses = AnalysisUtils.getDoneAnalyses(allAnalyses, analysisClass);

        List<AbstractFMAnalysis<?>> violatedAnalyses = AnalysisUtils.getViolatedAnalyses(filteredAnalyses);

        StringBuilder sb = new StringBuilder();
        ExplanationColors.EXPLANATION = ConsoleColors.WHITE;
        if (violatedAnalyses.isEmpty()) {
            sb.append(ExplanationColors.OK).append(anomalyType.getNonViolatedDescription()).append("\n");
        } else {
            List<AnomalyAwareFeature> anomalyFeatures = AnalysisUtils.getAnomalyFeatures(violatedAnalyses);

            sb.append(ExplanationColors.ANOMALY)
                    .append(anomalyType.getViolatedDescription())
                    .append((anomalyFeatures.size() > 1) ? "s" : "")
                    .append(" (").append(anomalyFeatures.size()).append(")")
                    .append(": ")
                    .append(Joiner.on(", ").join(anomalyFeatures))
                    .append("\n");

            boolean hasExplanations = violatedAnalyses.parallelStream().anyMatch(AbstractFMAnalysis::isWithDiagnosis);

            if (hasExplanations) {
                boolean firstExplanation = true;
                for (AbstractFMAnalysis<?> analysis : violatedAnalyses) {
                    if (analysis.getExplanator() != null && analysis.getExplanator().getDiagnoses() != null) {
                        if (firstExplanation) {
                            firstExplanation = false;
                        } else {
                            sb.append("\n");
                        }

                        String featuresEx = Joiner.on(", ").join(((AssumptionAwareTestCase) analysis.getAssumption()).getAssumptions());

                        if (analysis instanceof FalseOptionalAnalysis) {
                            String parent = ((AssumptionAwareTestCase) analysis.getAssumption()).getAssignments().get(1).getVariable();
                            featuresEx = featuresEx + ExplanationColors.ASSUMPTION + " (parent=[" + parent + "])" + ConsoleColors.WHITE;
                        }

                        sb.append(ExplanationUtils.convertToDescriptiveExplanation(analysis.getExplanator().getDiagnoses(), featuresEx));
                    }
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
