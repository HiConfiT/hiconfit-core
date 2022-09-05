/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core.ast;

import lombok.NonNull;

public class RequiresOperator extends ASTNode {
    public RequiresOperator(@NonNull ASTNode left, @NonNull ASTNode right) {
        super(left, right);
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
        return false;
    }

    @Override
    public boolean isBinaryOperator() {
        return true;
    }

    @Override
    public boolean isRequires() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("requires(%s, %s)", left, right);
    }

    @Override
    public ASTNode clone() {
        return new RequiresOperator(left.clone(), right.clone());
    }
}
