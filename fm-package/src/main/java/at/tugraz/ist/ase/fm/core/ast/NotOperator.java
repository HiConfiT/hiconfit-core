/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core.ast;

import lombok.NonNull;

public class NotOperator extends ASTNode {

    public NotOperator(@NonNull ASTNode right) {
        super(right);
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
        if (right.isOperand()) {
            return String.format("~%s", right);
        } else {
            return String.format("~(%s)", right);
        }
    }

    @Override
    public ASTNode clone() {
        return new NotOperator(right.clone());
    }
}
