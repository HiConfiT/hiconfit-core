/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core.ast;

import at.tugraz.ist.ase.fm.core.Feature;

import java.util.Collections;
import java.util.List;

public class Operand<F extends Feature> extends ASTNode {
    private final F feature;

    public Operand(F feature) {
        super();
        this.feature = feature;
    }

    /**
     * Gets the list of features that are involved in the constraint.
     * @return the list of features that are involved in the constraint.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <F extends Feature> List<F> getFeatures() {
        return (List<F>) Collections.singletonList(feature);
    }

    @Override
    public boolean isOperand() {
        return true;
    }

    @Override
    public boolean isOperator() {
        return false;
    }

    @Override
    public boolean isUnaryOperator() {
        return false;
    }

    @Override
    public boolean isBinaryOperator() {
        return false;
    }

    @Override
    public String toString() {
        return feature.toString();
    }

    @Override
    public ASTNode clone() {
        return new Operand<>(feature);
    }
}
