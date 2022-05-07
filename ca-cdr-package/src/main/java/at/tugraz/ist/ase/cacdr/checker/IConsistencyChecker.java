/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.checker;

import at.tugraz.ist.ase.kb.core.Constraint;

import java.util.Collection;

/**
 * A common interface for the different consistency checkers.
 * Note that checkers must not modify any of the input parameters!
 */
public interface IConsistencyChecker {

    /**
     * Checks consistency of a set of constraints
     *
     * @param constraints       set of constraints
     * @return <code>true</code> if constraints are consistent and <code>false</code> otherwise
     */
    boolean isConsistent(Collection<Constraint> constraints);

//    /**
//     * Checks consistency of a set of constraints
//     *
//     * @param constraints       set of constraints
//     * @param resetAfterCheck - if true, the function will reset the model after checking
//     * @return <code>true</code> if constraints are consistent and <code>false</code> otherwise
//     */
//    boolean isConsistent(Collection<String> constraints, boolean resetAfterCheck);

    /**
     * Supports a way to reset the internal checker
     */
    void reset();

    void dispose();
}
