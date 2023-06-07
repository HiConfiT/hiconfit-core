/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.kb.core.translator;

import at.tugraz.ist.ase.hiconfit.kb.core.Assignment;
import at.tugraz.ist.ase.hiconfit.kb.core.KB;
import lombok.NonNull;
import org.chocosolver.solver.constraints.Constraint;

import java.util.List;

public interface IAssignmentsTranslatable {
    /**
     * Translates {@link Assignment}s to Choco constraints.
     * @param assignments the {@link Assignment}s to translate
     * @param kb the {@link KB}
     * @param chocoCstrs list of Choco constraints, to which the translated constraints are added
     * @param negChocoCstrs list of Choco constraints, to which the translated negative constraints are added
     */
    void translate(@NonNull List<Assignment> assignments, @NonNull KB kb,
                   @NonNull List<Constraint> chocoCstrs, List<Constraint> negChocoCstrs);

    /**
     * Translates {@link Assignment}s to Choco constraints.
     * @param assignment the {@link Assignment} to translate
     * @param kb the {@link KB}
     * @param chocoCstrs list of Choco constraints, to which the translated constraints are added
     * @param negChocoCstrs list of Choco constraints, to which the translated negative constraints are added
     */
    void translate(@NonNull Assignment assignment, @NonNull KB kb,
                   @NonNull List<Constraint> chocoCstrs, List<Constraint> negChocoCstrs);
}
