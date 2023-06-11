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
import at.tugraz.ist.ase.hiconfit.fm.core.Feature;
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
public class DeadFeatureAssumptions implements IFMAnalysisAssumptionCreatable {
    @Override
    public <F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint>
    List<ITestCase> createAssumptions(@NonNull FeatureModel<F, R, C> fm) {
        // dead feature - inconsistent(CF ∪ { c0 } U {fi = true})
        List<AnomalyAwareFeature> candidateFeatures = IntStream.range(1, fm.getNumOfFeatures())
                .mapToObj(i -> (AnomalyAwareFeature) fm.getFeature(i))
                .collect(Collectors.toCollection(LinkedList::new));

        List<ITestCase> testCases = new LinkedList<>();
        for (AnomalyAwareFeature feature : candidateFeatures) {
            String testcase = fm.getFeature(0).getName() + " = true & " + feature.getName() + " = true";
            List<Assignment> assignments = new LinkedList<>();
            assignments.add(Assignment.builder()
                    .variable(fm.getFeature(0).getName())
                    .value("true")
                    .build());
            assignments.add(Assignment.builder()
                    .variable(feature.getName())
                    .value("true")
                    .build());

            testCases.add(AssumptionAwareTestCase.assumptionAwareTestCaseBuilder()
                    .testcase(testcase)
                    .anomalyType(AnomalyType.DEAD)
                    .assignments(assignments)
                    .assumptions(List.of(feature))
                    .build());
        }
        return testCases;
    }
}
