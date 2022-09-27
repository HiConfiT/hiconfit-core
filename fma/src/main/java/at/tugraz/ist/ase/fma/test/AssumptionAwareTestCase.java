/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.test;

import at.tugraz.ist.ase.cdrmodel.test.TestCase;
import at.tugraz.ist.ase.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.fma.anomaly.IAnomalyType;
import at.tugraz.ist.ase.kb.core.Assignment;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.LinkedList;
import java.util.List;

@Getter
public class AssumptionAwareTestCase extends TestCase {

    private final IAnomalyType anomalyType;
    private List<AnomalyAwareFeature> assumptions = new LinkedList<>();

    @Builder(builderMethodName = "assumptionAwareTestCaseBuilder")
    public AssumptionAwareTestCase(@NonNull String testcase,
                                   @NonNull IAnomalyType anomalyType,
                                   @NonNull List<Assignment> assignments,
                                   @NonNull List<AnomalyAwareFeature> assumptions) {
        super(testcase, assignments);
        this.anomalyType = anomalyType;
        this.assumptions.addAll(assumptions);
    }

    public Object clone() throws CloneNotSupportedException {
        AssumptionAwareTestCase clone = (AssumptionAwareTestCase) super.clone();
        // copy assumptions
        List<AnomalyAwareFeature> assumptions = new LinkedList<>();
        for (AnomalyAwareFeature ass : this.assumptions) {
            AnomalyAwareFeature cloneAss = (AnomalyAwareFeature) ass.clone();
            assumptions.add(cloneAss);
        }
        clone.assumptions = assumptions;

        return clone;
    }

    public void dispose() {
        super.dispose();
        assumptions.clear();
        assumptions = null;
    }
}
