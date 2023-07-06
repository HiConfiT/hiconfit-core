/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.analysis;

import at.tugraz.ist.ase.hiconfit.cacdr_core.ITestCase;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.hiconfit.fma.test.AssumptionAwareTestCase;
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
    @SuppressWarnings("unchecked")
    public <T extends ITestCase, F extends AnomalyAwareFeature>
    List<F> getAnomalyFeatures(List<AbstractFMAnalysis<T, F>> analyses) {
        return analyses.parallelStream()
                .flatMap(analysis -> ((AssumptionAwareTestCase<F>) analysis.getAssumption()).getAssumptions().stream())
                .distinct()
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Filter the list of analyses to get violated analyses
     * @param analyses the list of analyses
     * @return the list of violated analyses
     */
    public <T extends ITestCase, F extends AnomalyAwareFeature>
    List<AbstractFMAnalysis<T, F>> getViolatedAnalyses(List<AbstractFMAnalysis<T, F>> analyses) {
        return analyses.parallelStream().filter(analysis -> !analysis.isNon_violated()).collect(Collectors.toList());
    }

    /**
     * Get all specific and done analyses from the list of analyses
     * @param analyses list of analyses
     * @return a list of DeadFeatureAnalysis
     */
    public <T extends ITestCase, F extends AnomalyAwareFeature>
    List<AbstractFMAnalysis<T, F>> getAnalyses(@NonNull List<AbstractFMAnalysis<T, F>> analyses, @NonNull Class<?> clazz) {
        return analyses.parallelStream().filter(clazz::isInstance).toList();
    }

    public <T extends ITestCase, F extends AnomalyAwareFeature>
    List<AbstractFMAnalysis<T, F>> getDoneAnalyses(@NonNull List<AbstractFMAnalysis<T, F>> analyses, @NonNull Class<?> clazz) {
        return analyses.parallelStream().filter(analysis -> clazz.isInstance(analysis) && analysis.isDone())
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public <T extends ITestCase, F extends AnomalyAwareFeature>
    List<AbstractFMAnalysis<T, F>> getNotExecutedAnalyses(@NonNull List<AbstractFMAnalysis<T, F>> analyses) {
        return analyses.parallelStream().filter(analysis -> !analysis.isDone()).toList();
    }

    public <T extends ITestCase, F extends AnomalyAwareFeature>
    List<T> getTestcases(List<AbstractFMAnalysis<T, F>> analyses) {
        return analyses.parallelStream()
//                .filter(analysis -> analysis.getAssumption() instanceof ITestCase)
                .map(AbstractFMAnalysis::getAssumption)
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
