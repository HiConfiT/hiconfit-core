/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core.ast;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public abstract class ASTNode {
    protected ASTNode left = null;
    protected ASTNode right = null;

    /**
     * Constructor for unary operator
     * @param operand the operand
     */
    public ASTNode(ASTNode operand) {
        this.right = operand;
    }

    /**
     * Constructor for binary operator
     * @param left the left operand
     * @param right the right operand
     */
    public ASTNode(ASTNode left, ASTNode right) {
        this.left = left;
        this.right = right;
    }

    public abstract boolean isOperand();
    public abstract boolean isOperator();
    public abstract boolean isUnaryOperator();
    public abstract boolean isBinaryOperator();

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
