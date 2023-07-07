/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.assumption;

import at.tugraz.ist.ase.hiconfit.cacdr_core.Assignment;
import at.tugraz.ist.ase.hiconfit.cacdr_core.ITestCase;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyType;
import at.tugraz.ist.ase.hiconfit.fma.test.AssumptionAwareTestCase;
import lombok.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 * @author: Tamim Burgstaller (tamim.burgstaller@student.tugraz.at)
 */
public class ConditionallyDeadAssumptions implements IFMAnalysisAssumptionCreatable {
    @Override
    public <F extends AnomalyAwareFeature, R extends AbstractRelationship<F>, C extends CTConstraint>
    List<ITestCase> createAssumptions(@NonNull FeatureModel<F, R, C> fm) {
        // get candidate features
        List<F> candidateFeatures = IntStream.range(1, fm.getNumOfFeatures())
                .mapToObj(fm::getFeature)
                .filter(this::isConditionallyDeadCandidate)
                .collect(Collectors.toCollection(LinkedList::new));

        // create test cases
        List<ITestCase> testCases = new LinkedList<>();
        for (int i = 0; i < candidateFeatures.size() - 1; i++) {
            F f1 = candidateFeatures.get(i);

            if (!f1.isOptional()) { continue; }

            for (int j = i + 1; j < candidateFeatures.size(); j++) {
                F f2 = candidateFeatures.get(j);

                String testcase = fm.getFeature(0).getName() + " = true & " + f2.getName() + " = true & " + f1.getName() + " = true";
                List<Assignment> assignments = new LinkedList<>();
                assignments.add(Assignment.builder()
                        .variable(fm.getFeature(0).getName())
                        .value("true")
                        .build());
                assignments.add(Assignment.builder()
                        .variable(f2.getName())
                        .value("true")
                        .build());
                assignments.add(Assignment.builder()
                        .variable(f1.getName())
                        .value("true")
                        .build());

                testCases.add(AssumptionAwareTestCase.assumptionAwareTestCaseBuilder()
                        .testcase(testcase)
                        .anomalyType(AnomalyType.CONDITIONALLYDEAD)
                        .assignments(assignments)
                        .assumptions(List.of(f1, f2))
                        .build());
            }
        }

        return testCases;
    }

    private <F extends AnomalyAwareFeature> boolean isConditionallyDeadCandidate(F feature) {
        // a feature is not DEAD and has to be optional
        // Only optional features can be conditionally dead - dead features are dead anyway
        return !feature.isAnomalyType(AnomalyType.DEAD);
//        feature.isOptional() &&
    }
}
