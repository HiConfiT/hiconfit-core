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

import java.util.Collections;
import java.util.List;

/**
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 * @author: Tamim Burgstaller (tamim.burgstaller@student.tugraz.at)
 */
public class VoidFMAssumption implements IFMAnalysisAssumptionCreatable {

    public <F extends AnomalyAwareFeature, R extends AbstractRelationship<F>, C extends CTConstraint>
    List<ITestCase> createAssumptions(@NonNull FeatureModel<F, R, C> fm) {
        // void feature model - inconsistent(CF ∪ { c0 })
        String testcase = fm.getFeature(0).getName() + " = true";
        Assignment assignment = Assignment.builder()
                .variable(fm.getFeature(0).getName())
                .value("true")
                .build();
        F f0 = fm.getFeature(0);

        return Collections.singletonList(AssumptionAwareTestCase.assumptionAwareTestCaseBuilder()
                .testcase(testcase)
                .anomalyType(AnomalyType.VOID)
                .assignments(Collections.singletonList(assignment))
                .assumptions(Collections.singletonList(f0))
                .build());
    }
}
