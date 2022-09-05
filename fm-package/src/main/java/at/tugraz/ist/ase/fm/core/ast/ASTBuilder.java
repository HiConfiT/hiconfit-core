/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core.ast;

import at.tugraz.ist.ase.fm.core.Feature;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ASTBuilder {
    public <F extends Feature> ASTNode buildOperand(@NonNull F operand) {
        return new Operand<>(operand);
    }

    public ASTNode buildRequires(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand) {
        return new RequiresOperator(leftOperand, rightOperand);
    }

    public ASTNode buildExcludes(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand) {
        return new ExcludesOperator(leftOperand, rightOperand);
    }

    public ASTNode buildNot(@NonNull ASTNode node) {
        return new NotOperator(node);
    }

    public ASTNode buildOr(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand) {
        return new OrOperator(leftOperand, rightOperand);
    }

    public ASTNode buildImplies(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand) {
        return new ImpliesOperator(leftOperand, rightOperand);
    }

    public ASTNode buildAnd(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand) {
        return new AndOperator(leftOperand, rightOperand);
    }

    public ASTNode buildEquivalence(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand) {
        return new EquivalenceOperator(leftOperand, rightOperand);
    }

    public ASTNode convertToRequiresOrExcludes(ASTNode formula) {
        ASTNode left = formula.getLeft();
        ASTNode right = formula.getRight();

        if (formula instanceof ImpliesOperator && left instanceof Operand && right instanceof Operand) {
            return buildRequires(left, right);
        }

        if (formula instanceof OrOperator) {
            if (left instanceof NotOperator && left.getRight() instanceof Operand && right instanceof Operand) {
                return buildRequires(left.getRight(), right);
            }

            if (right instanceof NotOperator && right.getRight() instanceof Operand && left instanceof Operand) {
                return buildRequires(right.getRight(), left);
            }

            if (left instanceof NotOperator && left.getRight() instanceof Operand && right instanceof NotOperator && right.getRight() instanceof Operand) {
                return buildExcludes(left.getRight(), right.getRight());
            }
        }

        return formula;
    }
}
