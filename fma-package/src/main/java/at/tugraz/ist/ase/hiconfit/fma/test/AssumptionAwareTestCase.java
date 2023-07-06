/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.test;

import at.tugraz.ist.ase.hiconfit.cacdr_core.Assignment;
import at.tugraz.ist.ase.hiconfit.cacdr_core.TestCase;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.IAnomalyType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.LinkedList;
import java.util.List;

@Getter
public class AssumptionAwareTestCase<F extends AnomalyAwareFeature> extends TestCase {

    private final IAnomalyType anomalyType;
    private List<F> assumptions = new LinkedList<>();

    @Builder(builderMethodName = "assumptionAwareTestCaseBuilder")
    public AssumptionAwareTestCase(@NonNull String testcase,
                                   @NonNull IAnomalyType anomalyType,
                                   @NonNull List<Assignment> assignments,
                                   @NonNull List<F> assumptions) {
        super(testcase, assignments);
        this.anomalyType = anomalyType;
        this.assumptions.addAll(assumptions);
    }

    @SuppressWarnings("unchecked")
    public Object clone() throws CloneNotSupportedException {
        AssumptionAwareTestCase<F> clone = (AssumptionAwareTestCase<F>) super.clone();
        // copy assumptions
        List<F> assumptions = new LinkedList<>();
        for (F ass : this.assumptions) {
            F cloneAss = (F) ass.clone();
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
