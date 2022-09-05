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

    public ASTNode convertToRequiresOrExcludes(@NonNull ASTNode formula) {
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

    /**
     * Converts the given formula to CNF.
     * Adapted from <a href="https://github.com/flamapy/core/blob/master/flamapy/core/models/ast.py">...</a>.
     * @param formula the formula to be converted.
     * @return the formula in CNF.
     */
    public ASTNode convertToCNF(@NonNull ASTNode formula) {
        if (formula instanceof RequiresOperator || formula instanceof ExcludesOperator) {
            return formula;
        }

        ASTNode ast = simplify(formula);
        ast = propagateNegation(ast, false);
        return to_cnf(ast);
    }

    /**
     * Convert a propositional formula to an equivalent formula without
     * '->', '<->' operators.
     * @param formula the propositional formula
     * @return the equivalent formula without '->', '<->' operators.
     */
    private ASTNode simplify(ASTNode formula) {
        ASTNode left = formula.getLeft();
        ASTNode right = formula.getRight();

        if (formula instanceof ImpliesOperator) {
            // replace A -> B with !A \/ B
            left = simplify(left);
            right = simplify(right);
            return buildOr(buildNot(left), right);
        } else if (formula instanceof EquivalenceOperator) {
            // replace A <-> B with (A -> B) /\ (B -> A)
            ASTNode newleft = simplify(buildImplies(left, right));
            ASTNode newright = simplify(buildImplies(right, left));
            return buildAnd(newleft, newright);
        } else if (formula instanceof AndOperator) {
            left = simplify(left);
            right = simplify(right);
            return buildAnd(left, right);
        } else if (formula instanceof OrOperator) {
            left = simplify(left);
            right = simplify(right);
            return buildOr(left, right);
        } else if (formula instanceof NotOperator) {
            right = simplify(right);
            return buildNot(right);
        } else {
            return buildOperand(((Operand<?>) formula).getFeature());
        }
    }

    private ASTNode propagateNegation(ASTNode formula, boolean negated) {
        ASTNode left = formula.getLeft();
        ASTNode right = formula.getRight();

        if (formula instanceof NotOperator) {
            negated = !negated;
            return propagateNegation(right, negated);
        } else if (formula instanceof AndOperator) {
            if (negated) {
                // replace !(A /\ B) with !A \/ !B
                left = propagateNegation(left, negated);
                right = propagateNegation(right, negated);
                return buildOr(left, right);
            } else {
                // replace A /\ B with A /\ B
                left = propagateNegation(left, negated);
                right = propagateNegation(right, negated);
                return buildAnd(left, right);
            }
        } else if (formula instanceof OrOperator) {
            if (negated) {
                // replace !(A \/ B) with !A /\ !B
                left = propagateNegation(left, negated);
                right = propagateNegation(right, negated);
                return buildAnd(left, right);
            } else {
                // replace A \/ B with A \/ B
                left = propagateNegation(left, negated);
                right = propagateNegation(right, negated);
                return buildOr(left, right);
            }
        } else if (formula instanceof Operand) {
            if (negated) {
                return buildNot(formula);
            } else {
                return buildOperand(((Operand<?>) formula).getFeature());
            }
        } else {
            throw new IllegalArgumentException("Unknown operator: " + formula);
        }
    }

    private ASTNode to_cnf(ASTNode formula) {
        ASTNode left = formula.getLeft();
        ASTNode right = formula.getRight();

        if (formula instanceof AndOperator) {
            // replace A /\ B with A /\ B
            left = to_cnf(left);
            right = to_cnf(right);
            return buildAnd(left, right);
        } else if (formula instanceof OrOperator) {
            // replace A \/ B with A \/ B
            ASTNode newleft = to_cnf(left);
            ASTNode newright = to_cnf(right);

            if (newleft instanceof AndOperator) {
                // replace (A /\ B) \/ C with (A \/ C) /\ (B \/ C)
                ASTNode AorC = buildOr(newleft.getLeft(), newright);
                ASTNode BorC = buildOr(newleft.getRight(), newright);
                return to_cnf(buildAnd(AorC, BorC));
            } else if (newright instanceof AndOperator) {
                // replace A \/ (B /\ C) with (A \/ B) /\ (A \/ C)
                ASTNode AorB = buildOr(newleft, newright.getLeft());
                ASTNode AorC = buildOr(newleft, newright.getRight());
                return to_cnf(buildAnd(AorB, AorC));
            } else {
                return buildOr(newleft, newright);
            }
        }
        return formula;
    }
}
