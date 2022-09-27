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
import at.tugraz.ist.ase.fma.anomaly.AnomalyType;
import at.tugraz.ist.ase.fma.test.AssumptionAwareTestCase;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;

public class RedundancyAssumption implements IFMAnalysisAssumptionCreatable {
    @Override
    public <F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint> List<ITestCase>
    createAssumptions(@NonNull FeatureModel<F, R, C> fm) {
        return Collections.singletonList(AssumptionAwareTestCase.assumptionAwareTestCaseBuilder()
                .testcase("RedundancyAnalysis")
                .anomalyType(AnomalyType.REDUNDANT)
                .assignments(Collections.emptyList())
                .assumptions(Collections.emptyList())
                .build());
    }
}
