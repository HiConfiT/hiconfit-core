/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.kb.core.translator;

import at.tugraz.ist.ase.kb.core.Assignment;
import at.tugraz.ist.ase.kb.core.KB;
import lombok.NonNull;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;

import java.util.List;

public interface ILogOpCreatable {
    LogOp create(@NonNull List<Assignment> assignments, @NonNull KB kb);
    LogOp create(@NonNull Assignment assignment, @NonNull KB kb);
    LogOp createNegation(@NonNull LogOp logOp);
}
