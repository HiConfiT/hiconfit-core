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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 * @author: Tamim Burgstaller (tamim.burgstaller@student.tugraz.at)
 */
public class FalseOptionalAssumptions implements IFMAnalysisAssumptionCreatable {
    @Override
    @SuppressWarnings("unchecked")
    public <F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint>
    List<ITestCase> createAssumptions(@NonNull FeatureModel<F, R, C> fm) {
        // get candidate features
        List<AnomalyAwareFeature> candidateFeatures = IntStream.range(1, fm.getNumOfFeatures())
                .mapToObj(i -> (AnomalyAwareFeature) fm.getFeature(i))
                .filter(this::isFalseOptionalCandidate)
                .collect(Collectors.toCollection(LinkedList::new));

        List<ITestCase> testCases = new LinkedList<>();
        for (AnomalyAwareFeature feature : candidateFeatures) {
            ArrayList<F> parents;
            parents = new ArrayList<>(fm.getMandatoryParents((F) feature));
            if (parents.isEmpty()) {
                continue;
            }

            for (Feature parent : parents) {
                String testcase = fm.getFeature(0).getName() + " = true & " + parent.getName() + " = true & "
                        + feature.getName() + " = false";
                List<Assignment> assignments = new LinkedList<>();
                assignments.add(Assignment.builder()
                        .variable(fm.getFeature(0).getName())
                        .value("true")
                        .build());
                assignments.add(Assignment.builder()
                        .variable(parent.getName())
                        .value("true")
                        .build());
                assignments.add(Assignment.builder()
                        .variable(feature.getName())
                        .value("false")
                        .build());

                testCases.add(AssumptionAwareTestCase.assumptionAwareTestCaseBuilder()
                        .testcase(testcase)
                        .anomalyType(AnomalyType.FALSEOPTIONAL)
                        .assignments(assignments)
                        .assumptions(List.of(feature))
                        .build());
            }
        }

        return testCases;
    }

    private boolean isFalseOptionalCandidate(AnomalyAwareFeature feature) {
        // a feature is not DEAD and has to be optional
        // Only optional features can be false optional (believe it or not) - dead features are dead anyway
        return feature.isOptional() && !feature.isAnomalyType(AnomalyType.DEAD);
    }
}
