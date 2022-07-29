/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.kb.core.builder;

import at.tugraz.ist.ase.fm.core.Relationship;
import at.tugraz.ist.ase.kb.core.Constraint;
import lombok.NonNull;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;

public interface IConstraintBuildable {
    /**
     * Post a Choco constraint to the model and create a {@link Constraint} object with generated Choco constraints.
     * The generated Choco constraints will be added to the list chocoConstraints in the {@link Constraint} object.
     * If hasNegativeConstraints is true, the function also post the negation of the Choco constraint.
     * The Choco constraints generated when posting the negation will be added to the list negChocoConstraints in the {@link Constraint} object.
     * @param constraintName The name of the constraint.
     * @param modelKB The model to post the constraint to.
     * @param chocoConstraint The Choco constraint to post.
     * @param startIdx The index of the first Choco constraint.
     * @param hasNegativeConstraints Whether the constraint has negative constraints.
     * @return The {@link Constraint} object.
     */
    Constraint buildConstraint(String constraintName, @NonNull Model modelKB,
              @NonNull org.chocosolver.solver.constraints.Constraint chocoConstraint,
              int startIdx, boolean hasNegativeConstraints);

    /**
     * Post Choco LogOps to the model and create a {@link Constraint} object with generated Choco constraints.
     * The generated Choco constraints will be added to the list chocoConstraints in the {@link Constraint} object.
     * If hasNegativeConstraints is true, the function also post the negation of the Choco constraint.
     * The Choco constraints generated when posting the negation will be added to the list negChocoConstraints in the {@link Constraint} object.
     * @param relationship the relationship to build the constraint for.
     * @param modelKB The model to post the constraint to.
     * @param logOp The Choco LogOp to post.
     * @param negLogOp The Choco LogOp to post for the negation.
     * @param startIdx The index of the first Choco constraint.
     * @param hasNegativeConstraints Whether the constraint has negative constraints.
     * @return The {@link Constraint} object.
     */
    Constraint buildConstraint(Relationship relationship, @NonNull Model modelKB,
                               @NonNull LogOp logOp, LogOp negLogOp,
                               int startIdx, boolean hasNegativeConstraints);
}
