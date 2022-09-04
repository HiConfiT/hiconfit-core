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

public class ASTBuilder {

    public static <F extends Feature> ASTNode buildRequires(@NonNull F leftOperand, @NonNull F rightOperand) {
        return new RequiresOperator(new Operand<>(leftOperand), new Operand<>(rightOperand));
    }

    public static <F extends Feature> ASTNode buildExcludes(@NonNull F leftOperand, @NonNull F rightOperand) {
        return new ExcludesOperator(new Operand<>(leftOperand), new Operand<>(rightOperand));
    }

    public static <F extends Feature> ASTNode buildNot(@NonNull F feature) {
        return buildNot(new Operand<>(feature));
    }

    public static ASTNode buildNot(@NonNull ASTNode node) {
        return new NotOperator(node);
    }

    public static <F extends Feature> ASTNode buildOr(@NonNull F feature1, @NonNull F feature2) {
        return buildOr(new Operand<>(feature1), new Operand<>(feature2));
    }

    public static <F extends Feature> ASTNode buildOr(@NonNull F feature1, @NonNull ASTNode rightOperand) {
        return buildOr(new Operand<>(feature1), rightOperand);
    }

    public static <F extends Feature> ASTNode buildOr(@NonNull ASTNode leftOperand, @NonNull F feature2) {
        return buildOr(leftOperand, new Operand<>(feature2));
    }

    public static ASTNode buildOr(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand) {
        return new OrOperator(leftOperand, rightOperand);
    }

    public static ASTNode buildImplies(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand) {
        return new ImpliesOperator(leftOperand, rightOperand);
    }

    public static <F extends Feature> ASTNode buildImplies(@NonNull F feature1, @NonNull F feature2) {
        return buildImplies(new Operand<>(feature1), new Operand<>(feature2));
    }

    public static <F extends Feature> ASTNode buildImplies(@NonNull ASTNode leftOperand, @NonNull F feature2) {
        return buildImplies(leftOperand, new Operand<>(feature2));
    }

    public static <F extends Feature> ASTNode buildImplies(@NonNull F feature1, @NonNull ASTNode rightOperand) {
        return buildImplies(new Operand<>(feature1), rightOperand);
    }

    public static <F extends Feature> ASTNode buildAnd(@NonNull F feature1, @NonNull F feature2) {
        return buildAnd(new Operand<>(feature1), new Operand<>(feature2));
    }

    public static <F extends Feature> ASTNode buildAnd(@NonNull F feature1, @NonNull ASTNode rightOperand) {
        return buildAnd(new Operand<>(feature1), rightOperand);
    }

    public static <F extends Feature> ASTNode buildAnd(@NonNull ASTNode leftOperand, @NonNull F feature2) {
        return buildAnd(leftOperand, new Operand<>(feature2));
    }

    public static ASTNode buildAnd(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand) {
        return new AndOperator(leftOperand, rightOperand);
    }

    public static <F extends Feature> ASTNode buildEquivalence(@NonNull F feature1, @NonNull F feature2) {
        return buildEquivalence(new Operand<>(feature1), new Operand<>(feature2));
    }

    public static <F extends Feature> ASTNode buildEquivalence(@NonNull F feature1, @NonNull ASTNode rightOperand) {
        return buildEquivalence(new Operand<>(feature1), rightOperand);
    }

    public static <F extends Feature> ASTNode buildEquivalence(@NonNull ASTNode leftOperand, @NonNull F feature2) {
        return buildEquivalence(leftOperand, new Operand<>(feature2));
    }

    public static ASTNode buildEquivalence(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand) {
        return new EquivalenceOperator(leftOperand, rightOperand);
    }
}
