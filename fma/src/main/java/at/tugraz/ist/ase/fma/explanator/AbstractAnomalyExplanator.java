/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.explanator;

import at.tugraz.ist.ase.cdrmodel.fm.FMDebuggingModel;
import at.tugraz.ist.ase.cdrmodel.test.ITestCase;
import lombok.NonNull;

import java.util.concurrent.RecursiveTask;

/**
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 * @author: Tamim Burgstaller (tamim.burgstaller@student.tugraz.at)
 */
public abstract class AbstractAnomalyExplanator<T> extends RecursiveTask<T> {
    protected FMDebuggingModel debuggingModel;

    protected ITestCase assumption;

    public AbstractAnomalyExplanator(@NonNull FMDebuggingModel debuggingModel, ITestCase assumption) {
        this.debuggingModel = debuggingModel;
        this.assumption = assumption;
    }

    public T compute() {
        return identify();
    }

    public abstract T identify();
}
