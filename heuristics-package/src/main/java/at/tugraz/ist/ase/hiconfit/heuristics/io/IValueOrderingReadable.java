/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.heuristics.io;

import at.tugraz.ist.ase.hiconfit.heuristics.ValueOrdering;
import at.tugraz.ist.ase.hiconfit.kb.core.Variable;
import lombok.NonNull;

public interface IValueOrderingReadable {
    ValueOrdering read(@NonNull String[] items, @NonNull Variable variable);
}
