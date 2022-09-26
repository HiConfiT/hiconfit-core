/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.explanation;

import at.tugraz.ist.ase.fma.analysis.*;
import at.tugraz.ist.ase.fma.anomaly.AnomalyType;
import lombok.NonNull;

import java.util.EnumSet;
import java.util.List;

public class AutomatedAnalysisExplanation {
    public String getDescriptiveExplanation(@NonNull List<AbstractFMAnalysis<?>> analyses,
                                            @NonNull EnumSet<AnomalyType> anomalyTypes) {
        StringBuilder sb = new StringBuilder();
        IAnalysisExplanable explanable;
        for (AnomalyType anomalyType : anomalyTypes) {
            switch (anomalyType) {
                case VOID -> {
                    explanable = new VoidFMExplanation();
                    sb.append(explanable.getDescriptiveExplanation(analyses, VoidFMAnalysis.class, anomalyType));

                    VoidFMAnalysis analysis = (VoidFMAnalysis) AnalysisUtils.getAnalyses(analyses, VoidFMAnalysis.class).get(0);
                    if (analysis != null && !analysis.isNon_violated()) {
                        return sb.toString();
                    }
                }
                case DEAD -> {
                    explanable = new CompactExplanation();
                    sb.append(explanable.getDescriptiveExplanation(analyses, DeadFeatureAnalysis.class, anomalyType));
                }
                case FULLMANDATORY -> {
                    explanable = new CompactExplanation();
                    sb.append(explanable.getDescriptiveExplanation(analyses, FullMandatoryAnalysis.class, anomalyType));
                }
                case FALSEOPTIONAL -> {
                    explanable = new CompactExplanation();
                    sb.append(explanable.getDescriptiveExplanation(analyses, FalseOptionalAnalysis.class, anomalyType));
                }
                case CONDITIONALLYDEAD -> {
                    explanable = new CompactExplanation();
                    sb.append(explanable.getDescriptiveExplanation(analyses, ConditionallyDeadAnalysis.class, anomalyType));
                }
                case REDUNDANT -> {
                    explanable = new RedundancyAnalysisExplanation();
                    sb.append(explanable.getDescriptiveExplanation(analyses, RedundancyAnalysis.class, anomalyType));
                }
            }
        }
        return sb.toString();
    }
}
