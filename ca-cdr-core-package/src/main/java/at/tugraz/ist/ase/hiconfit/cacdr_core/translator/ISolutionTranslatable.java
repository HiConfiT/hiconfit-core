/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr_core.translator;

import at.tugraz.ist.ase.hiconfit.cacdr_core.Solution;
import at.tugraz.ist.ase.hiconfit.kb.core.Constraint;
import at.tugraz.ist.ase.hiconfit.kb.core.KB;
import lombok.NonNull;

import java.util.List;

public interface ISolutionTranslatable {
    Constraint translate(@NonNull Solution solution, @NonNull KB kb);

    List<Constraint> translateToList(@NonNull Solution solution, @NonNull KB kb);
}
