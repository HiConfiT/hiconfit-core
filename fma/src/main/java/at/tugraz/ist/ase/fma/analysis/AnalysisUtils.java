/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.analysis;

import at.tugraz.ist.ase.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.fma.test.AssumptionAwareTestCase;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class AnalysisUtils {
    /**
     * Get the list of anomaly features from the list of analyses
     * @param analyses the list of analyses
     * @return the list of anomaly features
     */
    public List<AnomalyAwareFeature> getAnomalyFeatures(List<AbstractFMAnalysis<?>> analyses) {
        return analyses.parallelStream()
                .flatMap(analysis -> ((AssumptionAwareTestCase) analysis.getAssumption()).getAssumptions().stream())
                .distinct()
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Filter the list of analyses to get violated analyses
     * @param analyses the list of analyses
     * @return the list of violated analyses
     */
    public List<AbstractFMAnalysis<?>> getViolatedAnalyses(List<AbstractFMAnalysis<?>> analyses) {
        return analyses.parallelStream().filter(analysis -> !analysis.isNon_violated()).collect(Collectors.toList());
    }

    /**
     * Get all DeadFeatureAnalysis from the list of analyses
     * @param analyses list of analyses
     * @return a list of DeadFeatureAnalysis
     */
    public List<AbstractFMAnalysis<?>> getAnalyses(@NonNull List<AbstractFMAnalysis<?>> analyses, @NonNull Class<?> clazz) {
        return analyses.parallelStream().filter(clazz::isInstance)
                                        .toList();
    }
}
