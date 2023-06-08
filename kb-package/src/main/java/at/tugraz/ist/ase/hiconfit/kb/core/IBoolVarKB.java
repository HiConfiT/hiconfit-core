/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.kb.core;

import lombok.NonNull;
import org.chocosolver.solver.variables.BoolVar;

public interface IBoolVarKB {
    BoolVar[] getBoolVars();
    BoolVar getBoolVar(@NonNull String variable);

    // Choco value
    boolean getBoolValue(@NonNull String var, @NonNull String value);
}
