/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.analysis;

import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.cdrmodel.fm.FMDebuggingModel;
import at.tugraz.ist.ase.cdrmodel.test.ITestCase;
import lombok.NonNull;

/**
 * Analysis checks if a feature is dead.
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 * @author: Tamim Burgstaller (tamim.burgstaller@student.tugraz.at)
 */
public class DeadFeatureAnalysis extends AbstractFMAnalysis<ITestCase> {

    public DeadFeatureAnalysis(@NonNull FMDebuggingModel debuggingModel, ITestCase assumption) {
        super(debuggingModel, assumption);
    }

    @Override
    protected Boolean analyze() {
        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(model);

        // inconsistent( CF ∪ { c0 } U {fi = true})
        return checker.isConsistent(model.getAllConstraints(), assumption);
    }
}
