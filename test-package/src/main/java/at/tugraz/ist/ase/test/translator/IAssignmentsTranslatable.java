/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.test.translator;

import at.tugraz.ist.ase.test.Assignment;
import lombok.NonNull;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

import java.util.List;

public interface IAssignmentsTranslatable {
    /**
     * Translates {@link at.tugraz.ist.ase.test.Assignment}s to Choco constraints.
     * @param assignments the {@link at.tugraz.ist.ase.test.Assignment}s to translate
     * @param model the Choco model
     * @param chocoCstrs list of Choco constraints, to which the translated constraints are added
     * @param negChocoCstrs list of Choco constraints, to which the translated negative constraints are added
     */
    void translate(@NonNull List<Assignment> assignments, @NonNull Model model,
                   @NonNull List<Constraint> chocoCstrs, @NonNull List<Constraint> negChocoCstrs);

    /**
     * Translates {@link at.tugraz.ist.ase.test.Assignment}s to Choco constraints.
     * @param assignment the {@link at.tugraz.ist.ase.test.Assignment} to translate
     * @param model the Choco model
     * @param chocoCstrs list of Choco constraints, to which the translated constraints are added
     * @param negChocoCstrs list of Choco constraints, to which the translated negative constraints are added
     */
    void translate(@NonNull Assignment assignment, @NonNull Model model,
                   @NonNull List<Constraint> chocoCstrs, @NonNull List<Constraint> negChocoCstrs);
}
