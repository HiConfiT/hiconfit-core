/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.kb.core.builder;

import at.tugraz.ist.ase.hiconfit.kb.core.Requirement;
import lombok.NonNull;

public interface IRequirementBuildable {
    Requirement build(@NonNull String stringUR);
}
