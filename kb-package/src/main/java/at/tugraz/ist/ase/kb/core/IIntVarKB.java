/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.kb.core;

import lombok.NonNull;
import org.chocosolver.solver.variables.IntVar;

public interface IIntVarKB {
    IntVar[] getIntVars();
    IntVar getIntVar(@NonNull String variable);

    // Choco value
    int getIntValue(@NonNull String var, @NonNull String value);
}
