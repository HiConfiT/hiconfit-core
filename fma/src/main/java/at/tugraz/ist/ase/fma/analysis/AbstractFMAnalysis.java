/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.analysis;

import at.tugraz.ist.ase.cdrmodel.AbstractCDRModel;
import at.tugraz.ist.ase.fma.anomaly.IAnomalyType;
import at.tugraz.ist.ase.fma.explanator.AbstractAnomalyExplanator;
import at.tugraz.ist.ase.fma.test.AssumptionAwareTestCase;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RecursiveTask;

/**
 * Base class for a feature model analysis.
 *
 * @param <T> ITestCase/Constraint
 *
 * @author Sebastian Krieter
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 * @author: Tamim Burgstaller (tamim.burgstaller@student.tugraz.at)
 */
@Slf4j
@Getter
public abstract class AbstractFMAnalysis<T> extends RecursiveTask<Boolean> {

	protected AbstractCDRModel model;

	protected T assumption; // could be ITestCase or Constraint
	@Setter
	protected boolean withDiagnosis = true;

	protected boolean non_violated;

	protected AbstractAnomalyExplanator explanator = null;

//	@Getter
//	private boolean timeoutOccurred = false;
//	@Getter @Setter
//	private long timeout = 1000;

//	@Setter
//	protected IAnalysisMonitor monitor = null;

	public AbstractFMAnalysis(@NonNull AbstractCDRModel model, T assumption) {
		this.model = model;
		this.assumption = assumption;
	}

	@Override
	protected Boolean compute() {
//		solver.setTimeout(timeout); // TODO - support timeout
//		if (assumption != null) {
//			solver.assignmentPushAll(assumptions.getLiterals());
//		}
//		assumptions = new LiteralSet(solver.getAssignmentArray());
//		timeoutOccurred = false;
		// TODO - set assumption

//		monitor.checkCancel();
		return analyze();
	}

	protected abstract Boolean analyze();

	protected void setAnomalyType(IAnomalyType anomalyType) {
		if (assumption instanceof AssumptionAwareTestCase) {
			((AssumptionAwareTestCase)assumption).getAssumptions().forEach(feature -> feature.setAnomalyType(anomalyType));
		}
	}

	@Override
	public String toString() {
		return assumption.toString();
	}
}
