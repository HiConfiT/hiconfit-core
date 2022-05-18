/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms;

import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import lombok.NonNull;

/**
 * An abstract class for all consistency algorithms, such as QuickXPlain, FastDiag, etc.
 */
public abstract class IConsistencyAlgorithm {
    protected ChocoConsistencyChecker checker;

    public IConsistencyAlgorithm(@NonNull ChocoConsistencyChecker checker) {
        this.checker = checker;
    }
}
