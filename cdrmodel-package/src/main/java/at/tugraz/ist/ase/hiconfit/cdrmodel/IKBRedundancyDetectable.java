/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cdrmodel;

import at.tugraz.ist.ase.hiconfit.kb.core.Constraint;

import java.util.List;

public interface IKBRedundancyDetectable {
    /**
     * Gets the set of non-redundant constraints.
     * @return the set of non-redundant constraints.
     */
    List<Constraint> getNonRedundantConstraints();
}
