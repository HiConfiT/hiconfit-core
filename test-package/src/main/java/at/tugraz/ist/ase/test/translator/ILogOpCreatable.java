/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.test.translator;

import at.tugraz.ist.ase.test.Assignment;
import lombok.NonNull;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;

import java.util.List;

public interface ILogOpCreatable {
    LogOp create(@NonNull List<Assignment> assignments, @NonNull Model model);
    LogOp create(@NonNull Assignment assignment, @NonNull Model model);
    LogOp createNegation(@NonNull LogOp logOp, @NonNull Model model);
}
