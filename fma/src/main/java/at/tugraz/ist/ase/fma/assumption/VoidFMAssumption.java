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

import java.util.Collections;
import java.util.List;

/**
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 * @author: Tamim Burgstaller (tamim.burgstaller@student.tugraz.at)
 */
public class VoidFMAssumption implements IFMAnalysisAssumptionCreatable {

    public <F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint>
    List<ITestCase> createAssumptions(@NonNull FeatureModel<F, R, C> fm) {
        // void feature model - inconsistent(CF ∪ { c0 })
        String testcase = fm.getFeature(0).getName() + " = true";
        Assignment assignment = Assignment.builder()
                .variable(fm.getFeature(0).getName())
                .value("true")
                .build();
        AnomalyAwareFeature f0 = (AnomalyAwareFeature) fm.getFeature(0);

        return Collections.singletonList(AssumptionAwareTestCase.assumptionAwareTestCaseBuilder()
                .testcase(testcase)
                .anomalyType(AnomalyType.VOID)
                .assignments(Collections.singletonList(assignment))
                .assumptions(Collections.singletonList(f0))
                .build());
    }
}
