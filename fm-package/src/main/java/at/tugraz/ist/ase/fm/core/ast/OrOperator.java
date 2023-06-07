/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core.ast;

import lombok.NonNull;

public class OrOperator extends ASTNode {

    public OrOperator(@NonNull ASTNode left, @NonNull ASTNode right) {
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
    public String toString() {
        return String.format("(%s \\/ %s)", left, right);
    }

    @Override
    public ASTNode clone() {
        return new OrOperator(left.clone(), right.clone());
    }
}
