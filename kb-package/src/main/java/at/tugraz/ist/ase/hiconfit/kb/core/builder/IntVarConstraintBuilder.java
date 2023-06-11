/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.kb.core.builder;

import at.tugraz.ist.ase.hiconfit.common.ConstraintUtils;
import at.tugraz.ist.ase.hiconfit.kb.camera.CameraKB;
import at.tugraz.ist.ase.hiconfit.kb.core.Constraint;
import at.tugraz.ist.ase.hiconfit.kb.pc.PCKB;
import at.tugraz.ist.ase.hiconfit.kb.renault.RenaultKB;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.chocosolver.solver.Model;

/**
 * This builder is used by {@link CameraKB},
 * {@link PCKB}, and {@link RenaultKB}
 */
@UtilityClass
public class IntVarConstraintBuilder {
    /**
     * Post a Choco constraint to the model and add generated Choco constraints to the {@link Constraint} object.
     * If hasNegativeConstraints is true, the function also post the negation of the Choco constraint.
     * @param constraintName The name of the constraint.
     * @param modelKB The model to post the constraint to.
     * @param chocoConstraint The Choco constraint to post.
     * @param startIdx The index of the first Choco constraint.
     * @param hasNegativeConstraints Whether the constraint has negative constraints.
     * @return The {@link Constraint} object.
     */
    public Constraint build(@NonNull String constraintName, @NonNull Model modelKB,
                              @NonNull org.chocosolver.solver.constraints.Constraint chocoConstraint,
                              int startIdx, boolean hasNegativeConstraints) {
        modelKB.post(chocoConstraint);

        org.chocosolver.solver.constraints.Constraint negChocoConstraint = null;
        if (hasNegativeConstraints) {
            negChocoConstraint = chocoConstraint.getOpposite();
            modelKB.post(negChocoConstraint);
        }

        // in case of hasNegativeConstraints is true
        // after posting the negChocoConstraint
        // Ex: there are five constraints in the modelKB: c1, c2, c3, c4, c5
        // c1, c2, c3, c4 will be added to the list of chocoConstraints in the Constraint object
        // c1, c2, c3, c5 will be added to the list of negChocoConstraints in the Constraint object

        // create the Constraint object
        Constraint constraint = new Constraint(constraintName);
        ConstraintUtils.addChocoConstraintsToConstraint(constraint, modelKB, startIdx, modelKB.getNbCstrs() - 1, hasNegativeConstraints);
//        constraint.addChocoConstraints(modelKB, startIdx, modelKB.getNbCstrs() - 1, hasNegativeConstraints);
//        constraintList.add(constraint);

        // remove c5 from the modelKB
        // if we keep c5 in the modelKB, the model will be inconsistent
        if (hasNegativeConstraints && negChocoConstraint != null) {
            modelKB.unpost(negChocoConstraint);
        }

        return constraint;
    }
}
