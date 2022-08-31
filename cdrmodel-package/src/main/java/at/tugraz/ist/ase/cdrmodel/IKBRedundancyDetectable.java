/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cdrmodel;

import at.tugraz.ist.ase.kb.core.Constraint;

import java.util.List;

public interface IKBRedundancyDetectable {
    /**
     * Gets the set of non-redundant constraints.
     * @return the set of non-redundant constraints.
     */
    List<Constraint> getNonRedundantConstraints();
}
