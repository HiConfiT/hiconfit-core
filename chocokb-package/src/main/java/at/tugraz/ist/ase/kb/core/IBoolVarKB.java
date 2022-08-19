/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.kb.core;

import lombok.NonNull;
import org.chocosolver.solver.variables.BoolVar;

public interface IBoolVarKB {
    BoolVar[] getBoolVars();
    BoolVar getBoolVar(@NonNull String variable);

    // Choco value
    boolean getBoolValue(@NonNull String var, @NonNull String value);
}
