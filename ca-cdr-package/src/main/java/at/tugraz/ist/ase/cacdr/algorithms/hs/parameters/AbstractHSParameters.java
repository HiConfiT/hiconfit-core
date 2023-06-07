/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hs.parameters;

import at.tugraz.ist.ase.kb.core.Constraint;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

/**
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Getter
@AllArgsConstructor
public abstract class AbstractHSParameters {
    private Set<Constraint> C;

    public void dispose() {
        C = null;
    }
}
