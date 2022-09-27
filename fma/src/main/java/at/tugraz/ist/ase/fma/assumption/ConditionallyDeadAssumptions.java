/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.assumption;

import at.tugraz.ist.ase.cdrmodel.test.ITestCase;
import at.tugraz.ist.ase.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.fm.core.CTConstraint;
import at.tugraz.ist.ase.fm.core.Feature;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import at.tugraz.ist.ase.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.fma.anomaly.AnomalyType;
import at.tugraz.ist.ase.fma.test.AssumptionAwareTestCase;
import at.tugraz.ist.ase.kb.core.Assignment;
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
    public <F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint>
    List<ITestCase> createAssumptions(@NonNull FeatureModel<F, R, C> fm) {
        // get candidate features
        List<AnomalyAwareFeature> candidateFeatures = IntStream.range(1, fm.getNumOfFeatures())
                .mapToObj(i -> (AnomalyAwareFeature) fm.getFeature(i))
                .filter(this::isConditionallyDeadCandidate)
                .collect(Collectors.toCollection(LinkedList::new));

        // create test cases
        List<ITestCase> testCases = new LinkedList<>();
        for (int i = 0; i < candidateFeatures.size() - 1; i++) {
            AnomalyAwareFeature f1 = candidateFeatures.get(i);

            if (!f1.isOptional()) { continue; }

            for (int j = i + 1; j < candidateFeatures.size(); j++) {
                AnomalyAwareFeature f2 = candidateFeatures.get(j);

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

    private boolean isConditionallyDeadCandidate(AnomalyAwareFeature feature) {
        // a feature is not DEAD and has to be optional
        // Only optional features can be conditionally dead - dead features are dead anyway
        return !feature.isAnomalyType(AnomalyType.DEAD);
//        feature.isOptional() &&
    }
}
