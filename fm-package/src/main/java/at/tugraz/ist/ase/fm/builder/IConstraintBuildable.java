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
import at.tugraz.ist.ase.fm.core.ast.ASTNode;
import lombok.NonNull;

public interface IConstraintBuildable {
    <C extends CTConstraint> C buildConstraint(@NonNull ASTNode formula);
    <F extends Feature, C extends CTConstraint> C buildRequires(@NonNull F leftOperand, @NonNull F rightOperand);
    <F extends Feature, C extends CTConstraint> C buildExcludes(@NonNull F leftOperand, @NonNull F rightOperand);
    <F extends Feature, C extends CTConstraint> C buildNot(@NonNull F feature);
    <C extends CTConstraint> C buildNot(@NonNull ASTNode node);
    <F extends Feature, C extends CTConstraint> C buildOr(@NonNull F feature1, @NonNull F feature2);
    <F extends Feature, C extends CTConstraint> C buildOr(@NonNull F feature1, @NonNull ASTNode rightOperand);
    <F extends Feature, C extends CTConstraint> C buildOr(@NonNull ASTNode leftOperand, @NonNull F feature2);
    <C extends CTConstraint> C buildOr(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand);
    <C extends CTConstraint> C buildImplies(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand);
    <F extends Feature, C extends CTConstraint> C buildImplies(@NonNull F feature1, @NonNull F feature2);
    <F extends Feature, C extends CTConstraint> C buildImplies(@NonNull ASTNode leftOperand, @NonNull F feature2);
    <F extends Feature, C extends CTConstraint> C buildImplies(@NonNull F feature1, @NonNull ASTNode rightOperand);
    <F extends Feature, C extends CTConstraint> C buildAnd(@NonNull F feature1, @NonNull F feature2);
    <F extends Feature, C extends CTConstraint> C buildAnd(@NonNull F feature1, @NonNull ASTNode rightOperand);
    <F extends Feature, C extends CTConstraint> C buildAnd(@NonNull ASTNode leftOperand, @NonNull F feature2);
    <C extends CTConstraint> C buildAnd(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand);
    <F extends Feature, C extends CTConstraint> C buildEquivalence(@NonNull F feature1, @NonNull F feature2);
    <F extends Feature, C extends CTConstraint> C buildEquivalence(@NonNull F feature1, @NonNull ASTNode rightOperand);
    <F extends Feature, C extends CTConstraint> C buildEquivalence(@NonNull ASTNode leftOperand, @NonNull F feature2);
    <C extends CTConstraint> C buildEquivalence(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand);
}
