/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms;

import at.tugraz.ist.ase.cacdr.checker.IConsistencyChecker;
import lombok.NonNull;

/**
 * An abstract class for all consistency algorithms, such as QuickXPlain, FastDiag, etc.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public abstract class IConsistencyAlgorithm {
    protected IConsistencyChecker checker;

    public IConsistencyAlgorithm(@NonNull IConsistencyChecker checker) {
        this.checker = checker;
    }

    public void dispose() {
        this.checker = null;
    }
}
