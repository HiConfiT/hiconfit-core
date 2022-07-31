/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.kb.core.translator;

import at.tugraz.ist.ase.kb.core.Assignment;
import lombok.NonNull;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

import java.util.List;

public interface IAssignmentsTranslatable {
    /**
     * Translates {@link Assignment}s to Choco constraints.
     * @param assignments the {@link Assignment}s to translate
     * @param model the Choco model
     * @param chocoCstrs list of Choco constraints, to which the translated constraints are added
     * @param negChocoCstrs list of Choco constraints, to which the translated negative constraints are added
     */
    void translate(@NonNull List<Assignment> assignments, @NonNull Model model,
                   @NonNull List<Constraint> chocoCstrs, List<Constraint> negChocoCstrs);

    /**
     * Translates {@link Assignment}s to Choco constraints.
     * @param assignment the {@link Assignment} to translate
     * @param model the Choco model
     * @param chocoCstrs list of Choco constraints, to which the translated constraints are added
     * @param negChocoCstrs list of Choco constraints, to which the translated negative constraints are added
     */
    void translate(@NonNull Assignment assignment, @NonNull Model model,
                   @NonNull List<Constraint> chocoCstrs, List<Constraint> negChocoCstrs);
}
