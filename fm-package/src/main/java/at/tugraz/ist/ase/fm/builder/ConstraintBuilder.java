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
import lombok.NonNull;

public class ConstraintBuilder implements IConstraintBuildable {

    @SuppressWarnings("unchecked")
    public <C extends CTConstraint> C buildConstraint(@NonNull ASTNode formula) {
        return (C) new CTConstraint(formula);
    }

    public <F extends Feature, C extends CTConstraint> C buildRequires(@NonNull F leftOperand, @NonNull F rightOperand) {
        return buildConstraint(ASTBuilder.buildRequires(leftOperand, rightOperand));
    }

    public <F extends Feature, C extends CTConstraint> C buildExcludes(@NonNull F leftOperand, @NonNull F rightOperand) {
        return buildConstraint(ASTBuilder.buildExcludes(leftOperand, rightOperand));
    }

    public <F extends Feature, C extends CTConstraint> C buildNot(@NonNull F feature) {
        return buildConstraint(ASTBuilder.buildNot(feature));
    }

    public <C extends CTConstraint> C buildNot(@NonNull ASTNode node) {
        return buildConstraint(ASTBuilder.buildNot(node));
    }

    public <F extends Feature, C extends CTConstraint> C buildOr(@NonNull F feature1, @NonNull F feature2) {
        return buildConstraint(ASTBuilder.buildOr(feature1, feature2));
    }

    public <F extends Feature, C extends CTConstraint> C buildOr(@NonNull F feature1, @NonNull ASTNode rightOperand) {
        return buildConstraint(ASTBuilder.buildOr(feature1, rightOperand));
    }

    public <F extends Feature, C extends CTConstraint> C buildOr(@NonNull ASTNode leftOperand, @NonNull F feature2) {
        return buildConstraint(ASTBuilder.buildOr(leftOperand, feature2));
    }

    public <C extends CTConstraint> C buildOr(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand) {
        return buildConstraint(ASTBuilder.buildOr(leftOperand, rightOperand));
    }

    public <C extends CTConstraint> C buildImplies(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand) {
        return buildConstraint(ASTBuilder.buildImplies(leftOperand, rightOperand));
    }

    public <F extends Feature, C extends CTConstraint> C buildImplies(@NonNull F feature1, @NonNull F feature2) {
        return buildConstraint(ASTBuilder.buildImplies(feature1, feature2));
    }

    public <F extends Feature, C extends CTConstraint> C buildImplies(@NonNull ASTNode leftOperand, @NonNull F feature2) {
        return buildConstraint(ASTBuilder.buildImplies(leftOperand, feature2));
    }

    public <F extends Feature, C extends CTConstraint> C buildImplies(@NonNull F feature1, @NonNull ASTNode rightOperand) {
        return buildConstraint(ASTBuilder.buildImplies(feature1, rightOperand));
    }

    public <F extends Feature, C extends CTConstraint> C buildAnd(@NonNull F feature1, @NonNull F feature2) {
        return buildConstraint(ASTBuilder.buildAnd(feature1, feature2));
    }

    public <F extends Feature, C extends CTConstraint> C buildAnd(@NonNull F feature1, @NonNull ASTNode rightOperand) {
        return buildConstraint(ASTBuilder.buildAnd(feature1, rightOperand));
    }

    public <F extends Feature, C extends CTConstraint> C buildAnd(@NonNull ASTNode leftOperand, @NonNull F feature2) {
        return buildConstraint(ASTBuilder.buildAnd(leftOperand, feature2));
    }

    public <C extends CTConstraint> C buildAnd(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand) {
        return buildConstraint(ASTBuilder.buildAnd(leftOperand, rightOperand));
    }

    public <F extends Feature, C extends CTConstraint> C buildEquivalence(@NonNull F feature1, @NonNull F feature2) {
        return buildConstraint(ASTBuilder.buildEquivalence(feature1, feature2));
    }

    public <F extends Feature, C extends CTConstraint> C buildEquivalence(@NonNull F feature1, @NonNull ASTNode rightOperand) {
        return buildConstraint(ASTBuilder.buildEquivalence(feature1, rightOperand));
    }

    public <F extends Feature, C extends CTConstraint> C buildEquivalence(@NonNull ASTNode leftOperand, @NonNull F feature2) {
        return buildConstraint(ASTBuilder.buildEquivalence(leftOperand, feature2));
    }

    public <C extends CTConstraint> C buildEquivalence(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand) {
        return buildConstraint(ASTBuilder.buildEquivalence(leftOperand, rightOperand));
    }
}
