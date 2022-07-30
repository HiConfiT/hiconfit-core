/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.kb.core.builder;

import at.tugraz.ist.ase.common.ChocoSolverUtils;
import at.tugraz.ist.ase.common.ConstraintUtils;
import at.tugraz.ist.ase.fm.core.Relationship;
import at.tugraz.ist.ase.fm.core.RelationshipType;
import at.tugraz.ist.ase.kb.core.Constraint;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;

@UtilityClass
public class BoolVarConstraintBuilder {
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
    public Constraint build(@NonNull Relationship relationship, @NonNull Model modelKB,
                              @NonNull LogOp logOp, LogOp negLogOp,
                              int startIdx, boolean hasNegativeConstraints) {
        modelKB.addClauses(logOp);

        Constraint constraint = new Constraint(relationship.getConfRule());

        ConstraintUtils.addChocoConstraintsToConstraint(false, constraint, modelKB, startIdx, modelKB.getNbCstrs() - 1);
//        addConstraint(constraint, relationship, startIdx, modelKB.getNbCstrs() - 1);

        if (hasNegativeConstraints) {
            if (relationship.isType(RelationshipType.ALTERNATIVE)) {
                ConstraintUtils.addChocoConstraintsToConstraint(true, constraint, modelKB, startIdx + 1, startIdx + 1);
//                addNegConstraint(constraint, startIdx + 1, startIdx + 1);
            }

            startIdx = modelKB.getNbCstrs();

            modelKB.addClauses(negLogOp);
            ConstraintUtils.addChocoConstraintsToConstraint(true, constraint, modelKB, startIdx, modelKB.getNbCstrs() - 1);
//            addNegConstraint(constraint, startIdx, modelKB.getNbCstrs() - 1);
        }

//        constraintList.add(constraint);

        // unpost the negative constraint
        if (hasNegativeConstraints) {
            ChocoSolverUtils.unpostConstraintsFrom(startIdx, modelKB);
        }

        return constraint;
    }
}
