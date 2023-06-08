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
import org.chocosolver.solver.constraints.nary.cnf.LogOp;

import java.util.List;

public interface ILogOpCreatable {
    LogOp create(@NonNull List<Assignment> assignments, @NonNull KB kb);
    LogOp create(@NonNull Assignment assignment, @NonNull KB kb);
    LogOp createNegation(@NonNull LogOp logOp);
}
