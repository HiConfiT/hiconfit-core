/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.checker;

import at.tugraz.ist.ase.kb.core.Constraint;
import at.tugraz.ist.ase.test.ITestCase;
import lombok.NonNull;

import java.util.Collection;
import java.util.Set;

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

    boolean isConsistent(@NonNull Collection<Constraint> C, @NonNull ITestCase testcase);

    boolean isConsistent(@NonNull ITestCase testcase, @NonNull ITestCase neg_testcase);

    boolean isConsistent(@NonNull Collection<Constraint> C, @NonNull Constraint cstr);

    Set<ITestCase> isConsistent(@NonNull Collection<Constraint> C, @NonNull Collection<ITestCase> TC, boolean onlyOne);

    /**
     * Supports a way to reset the internal checker
     */
    void reset();

    void dispose();
}
