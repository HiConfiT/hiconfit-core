/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core.ast;

import at.tugraz.ist.ase.fm.core.Feature;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.LinkedList;
import java.util.List;

@Getter
@NoArgsConstructor
public abstract class ASTNode {
    protected ASTNode left = null;
    protected ASTNode right = null;

    /**
     * Constructor for unary operator
     * @param right the operand
     */
    public ASTNode(@NonNull ASTNode right) {
        this.right = right;
    }

    /**
     * Constructor for binary operator
     * @param left the left operand
     * @param right the right operand
     */
    public ASTNode(@NonNull ASTNode left, @NonNull ASTNode right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Gets the list of features that are involved in the constraint.
     * @return the list of features that are involved in the constraint.
     */
    public <F extends Feature> List<F> getFeatures() {
        List<F> features = new LinkedList<>();
        if (left != null) {
            features.addAll(left.getFeatures());
        }
        if (right != null) {
            features.addAll(right.getFeatures());
        }
        return features;
    }

    public abstract boolean isOperand();
    public abstract boolean isOperator();
    public abstract boolean isUnaryOperator();
    public abstract boolean isBinaryOperator();

    public boolean isRequires() {
        return false;
    }

    public boolean isExcludes() {
        return false;
    }

    public void dispose() {
        if (left != null)
            left.dispose();
        left = null;
        if (right != null)
            right.dispose();
        right = null;
    }

    public abstract ASTNode clone();
}
