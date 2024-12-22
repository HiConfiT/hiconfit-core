/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.test.factory;

import at.tugraz.ist.ase.hiconfit.cacdr_core.Assignment;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.IAnomalyType;
import at.tugraz.ist.ase.hiconfit.fma.test.AssumptionAwareTestCase;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class AssumptionAwareTestCases {

    public AssumptionAwareTestCase<AnomalyAwareFeature> from(@NonNull String testcase,
                                                             @NonNull IAnomalyType anomalyType,
                                                             @NonNull List<Assignment> assignments,
                                                             @NonNull List<AnomalyAwareFeature> assumptions) {
        return AssumptionAwareTestCase.assumptionAwareTestCaseBuilder()
                                    .testcase(testcase)
                                    .anomalyType(anomalyType)
                                    .assignments(assignments)
                                    .assumptions(assumptions)
                                    .build();
    }

}
