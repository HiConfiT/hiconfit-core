/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.builder;

import at.tugraz.ist.ase.fm.core.CTConstraint;
import at.tugraz.ist.ase.fm.core.Feature;
import at.tugraz.ist.ase.fm.core.ast.ASTBuilder;
import at.tugraz.ist.ase.fm.core.ast.ASTNode;
import at.tugraz.ist.ase.fm.core.ast.Operand;
import at.tugraz.ist.ase.fm.translator.IConfRuleTranslatable;
import lombok.NonNull;

/**
 * Provides methods for building constraints.
 */
public class ConstraintBuilder implements IConstraintBuildable {

    private final IConfRuleTranslatable translator;

    public ConstraintBuilder(@NonNull IConfRuleTranslatable translator) {
        this.translator = translator;
    }

    @SuppressWarnings("unchecked")
    public <C extends CTConstraint> C buildConstraint(@NonNull ASTNode formula) {
        ASTNode cnf = ASTBuilder.convertToCNF(formula);
        return (C) new CTConstraint(formula, cnf, translator);
    }

    public <F extends Feature> ASTNode buildOperand(@NonNull F operand) {
        return ASTBuilder.buildOperand(operand);
    }

    public ASTNode buildRequires(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand) {
        return ASTBuilder.buildRequires(leftOperand, rightOperand);
    }

    public <F extends Feature> ASTNode buildRequires(@NonNull F leftOperand, @NonNull F rightOperand) {
        return ASTBuilder.buildRequires(new Operand<>(leftOperand), new Operand<>(rightOperand));
    }

    public ASTNode buildExcludes(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand) {
        return ASTBuilder.buildExcludes(leftOperand, rightOperand);
    }

    public <F extends Feature> ASTNode buildExcludes(@NonNull F leftOperand, @NonNull F rightOperand) {
        return ASTBuilder.buildExcludes(new Operand<>(leftOperand), new Operand<>(rightOperand));
    }

    public ASTNode convertToRequiresOrExcludes(ASTNode formula) {
        return ASTBuilder.convertToRequiresOrExcludes(formula);
    }

    public <F extends Feature> ASTNode buildNot(@NonNull F feature) {
        return ASTBuilder.buildNot(new Operand<>(feature));
    }

    public ASTNode buildNot(@NonNull ASTNode node) {
        return ASTBuilder.buildNot(node);
    }

    public <F extends Feature> ASTNode buildOr(@NonNull F feature1, @NonNull F feature2) {
        return ASTBuilder.buildOr(new Operand<>(feature1), new Operand<>(feature2));
    }

    public <F extends Feature> ASTNode buildOr(@NonNull F feature1, @NonNull ASTNode rightOperand) {
        return ASTBuilder.buildOr(new Operand<>(feature1), rightOperand);
    }

    public <F extends Feature> ASTNode buildOr(@NonNull ASTNode leftOperand, @NonNull F feature2) {
        return ASTBuilder.buildOr(leftOperand, new Operand<>(feature2));
    }

    public ASTNode buildOr(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand) {
        return ASTBuilder.buildOr(leftOperand, rightOperand);
    }

    public ASTNode buildImplies(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand) {
        return ASTBuilder.buildImplies(leftOperand, rightOperand);
    }

    public <F extends Feature> ASTNode buildImplies(@NonNull F feature1, @NonNull F feature2) {
        return ASTBuilder.buildImplies(new Operand<>(feature1), new Operand<>(feature2));
    }

    public <F extends Feature> ASTNode buildImplies(@NonNull ASTNode leftOperand, @NonNull F feature2) {
        return ASTBuilder.buildImplies(leftOperand, new Operand<>(feature2));
    }

    public <F extends Feature> ASTNode buildImplies(@NonNull F feature1, @NonNull ASTNode rightOperand) {
        return ASTBuilder.buildImplies(new Operand<>(feature1), rightOperand);
    }

    public <F extends Feature> ASTNode buildAnd(@NonNull F feature1, @NonNull F feature2) {
        return ASTBuilder.buildAnd(new Operand<>(feature1), new Operand<>(feature2));
    }

    public <F extends Feature> ASTNode buildAnd(@NonNull F feature1, @NonNull ASTNode rightOperand) {
        return ASTBuilder.buildAnd(new Operand<>(feature1), rightOperand);
    }

    public <F extends Feature> ASTNode buildAnd(@NonNull ASTNode leftOperand, @NonNull F feature2) {
        return ASTBuilder.buildAnd(leftOperand, new Operand<>(feature2));
    }

    public ASTNode buildAnd(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand) {
        return ASTBuilder.buildAnd(leftOperand, rightOperand);
    }

    public <F extends Feature> ASTNode buildEquivalence(@NonNull F feature1, @NonNull F feature2) {
        return ASTBuilder.buildEquivalence(new Operand<>(feature1), new Operand<>(feature2));
    }

    public <F extends Feature> ASTNode buildEquivalence(@NonNull F feature1, @NonNull ASTNode rightOperand) {
        return ASTBuilder.buildEquivalence(new Operand<>(feature1), rightOperand);
    }

    public <F extends Feature> ASTNode buildEquivalence(@NonNull ASTNode leftOperand, @NonNull F feature2) {
        return ASTBuilder.buildEquivalence(leftOperand, new Operand<>(feature2));
    }

    public ASTNode buildEquivalence(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand) {
        return ASTBuilder.buildEquivalence(leftOperand, rightOperand);
    }
}
