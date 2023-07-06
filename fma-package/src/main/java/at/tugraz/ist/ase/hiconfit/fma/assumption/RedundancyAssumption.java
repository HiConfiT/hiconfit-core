/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.assumption;

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

public class RedundancyAssumption implements IFMAnalysisAssumptionCreatable {
    @Override
    public <F extends AnomalyAwareFeature, R extends AbstractRelationship<F>, C extends CTConstraint> List<ITestCase>
    createAssumptions(@NonNull FeatureModel<F, R, C> fm) {
        return Collections.singletonList(AssumptionAwareTestCase.assumptionAwareTestCaseBuilder()
                .testcase("RedundancyAnalysis")
                .anomalyType(AnomalyType.REDUNDANT)
                .assignments(Collections.emptyList())
                .assumptions(Collections.emptyList())
                .build());
    }
}
