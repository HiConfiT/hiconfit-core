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
    <F extends Feature> ASTNode buildOperand(@NonNull F operand);
    ASTNode buildRequires(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand);
    <F extends Feature> ASTNode buildRequires(@NonNull F leftOperand, @NonNull F rightOperand);
    ASTNode buildExcludes(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand);
    <F extends Feature> ASTNode buildExcludes(@NonNull F leftOperand, @NonNull F rightOperand);
    <F extends Feature> ASTNode buildNot(@NonNull F feature);
    ASTNode buildNot(@NonNull ASTNode node);
    <F extends Feature> ASTNode buildOr(@NonNull F feature1, @NonNull F feature2);
    <F extends Feature> ASTNode buildOr(@NonNull F feature1, @NonNull ASTNode rightOperand);
    <F extends Feature> ASTNode buildOr(@NonNull ASTNode leftOperand, @NonNull F feature2);
    ASTNode buildOr(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand);
    ASTNode buildImplies(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand);
    <F extends Feature> ASTNode buildImplies(@NonNull F feature1, @NonNull F feature2);
    <F extends Feature> ASTNode buildImplies(@NonNull ASTNode leftOperand, @NonNull F feature2);
    <F extends Feature> ASTNode buildImplies(@NonNull F feature1, @NonNull ASTNode rightOperand);
    <F extends Feature> ASTNode buildAnd(@NonNull F feature1, @NonNull F feature2);
    <F extends Feature> ASTNode buildAnd(@NonNull F feature1, @NonNull ASTNode rightOperand);
    <F extends Feature> ASTNode buildAnd(@NonNull ASTNode leftOperand, @NonNull F feature2);
    ASTNode buildAnd(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand);
    <F extends Feature> ASTNode buildEquivalence(@NonNull F feature1, @NonNull F feature2);
    <F extends Feature> ASTNode buildEquivalence(@NonNull F feature1, @NonNull ASTNode rightOperand);
    <F extends Feature> ASTNode buildEquivalence(@NonNull ASTNode leftOperand, @NonNull F feature2);
    ASTNode buildEquivalence(@NonNull ASTNode leftOperand, @NonNull ASTNode rightOperand);
}
