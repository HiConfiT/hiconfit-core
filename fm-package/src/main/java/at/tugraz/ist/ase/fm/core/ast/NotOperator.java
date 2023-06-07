/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core.ast;

import at.tugraz.ist.ase.fm.core.Feature;
import lombok.NonNull;

import java.util.LinkedList;
import java.util.List;

public class NotOperator extends ASTNode {

    public NotOperator(@NonNull ASTNode right) {
        super(right);
    }

    /**
     * Gets the list of features that are involved in the constraint.
     * @return the list of features that are involved in the constraint.
     */
    @Override
    public <F extends Feature> List<F> getFeatures() {
        List<F> features = new LinkedList<>();
        if (right != null) {
            features.addAll(right.getFeatures());
        }
        return features;
    }

    @Override
    public boolean isOperand() {
        return false;
    }

    @Override
    public boolean isOperator() {
        return true;
    }

    @Override
    public boolean isUnaryOperator() {
        return true;
    }

    @Override
    public boolean isBinaryOperator() {
        return false;
    }

    @Override
    public String toString() {
        if (right instanceof NotOperator) {
            return String.format("~(%s)", right);
        } else {
            return String.format("~%s", right);
        }
    }

    @Override
    public ASTNode clone() {
        return new NotOperator(right.clone());
    }
}
