/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.explanation;

import at.tugraz.ist.ase.hiconfit.cacdr_core.ITestCase;
import at.tugraz.ist.ase.hiconfit.common.ConstraintUtils;
import at.tugraz.ist.ase.hiconfit.fma.analysis.AbstractFMAnalysis;
import at.tugraz.ist.ase.hiconfit.fma.analysis.AnalysisUtils;
import at.tugraz.ist.ase.hiconfit.fma.analysis.FalseOptionalAnalysis;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyType;
import at.tugraz.ist.ase.hiconfit.fma.test.AssumptionAwareTestCase;
import com.google.common.base.Joiner;
import lombok.NonNull;
import org.javatuples.Pair;

import java.util.LinkedList;
import java.util.List;

public class RawExplanation {
    /**
     * Get a descriptive explanation of a specified analysis
     * @param allAnalyses the list of allAnalyses
     * @param analysisClass the class of the analysis
     * @param anomalyType the type of the anomaly
     * @return a list of parts of descriptive explanations, to be assembled together
     */
    public <T extends ITestCase, F extends AnomalyAwareFeature>
    Pair<String, List<Pair<String, String>>> getDescriptiveExplanation(@NonNull List<AbstractFMAnalysis<T, F>> allAnalyses,
                                                                       @NonNull Class<?> analysisClass,
                                                                       @NonNull AnomalyType anomalyType) {
        List<AbstractFMAnalysis<T, F>> filteredAnalyses = AnalysisUtils.getDoneAnalyses(allAnalyses, analysisClass);

        List<AbstractFMAnalysis<T, F>> violatedAnalyses = AnalysisUtils.getViolatedAnalyses(filteredAnalyses);

        StringBuilder title = new StringBuilder();
        List<Pair<String, String>> explanationList = null;

        if (violatedAnalyses.isEmpty()) {
            title.append(anomalyType.getRawNonViolatedDescription());
        } else {
            List<F> anomalyFeatures = AnalysisUtils.getAnomalyFeatures(violatedAnalyses);

            title.append(anomalyType.getRawViolatedDescription())
                    .append((anomalyFeatures.size() > 1) ? "s" : "")
                    .append(" (").append(anomalyFeatures.size()).append(")")
                    .append(": ")
                    .append(Joiner.on(", ").join(anomalyFeatures));

            boolean hasExplanations = violatedAnalyses.parallelStream().anyMatch(AbstractFMAnalysis::isWithDiagnosis);

            if (hasExplanations) {
                explanationList = new LinkedList<>();

                for (AbstractFMAnalysis<T, F> analysis : violatedAnalyses) {
                    if (analysis.getExplanator() != null && analysis.getExplanator().getDiagnoses() != null) {
                        String featuresEx = Joiner.on(", ").join(((AssumptionAwareTestCase<?>) analysis.getAssumption()).getAssumptions());

                        if (analysis instanceof FalseOptionalAnalysis) {
                            String parent = analysis.getAssumption().getAssignments().get(1).getVariable();
                            featuresEx = featuresEx + " (parent=[" + parent + "])";
                        }

                        explanationList.add(new Pair<>(featuresEx, ConstraintUtils.convertToStringWithMessage(analysis.getExplanator().getDiagnoses(), "Diagnosis", "", ",", true)));
                    }
                }
            }
        }
        return new Pair<>(title.toString(), explanationList);
    }
}
