/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr_core;

import at.tugraz.ist.ase.hiconfit.common.ChocoSolverUtils;
import at.tugraz.ist.ase.hiconfit.kb.core.KB;
import lombok.NonNull;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;

import java.util.List;

public class LogOpCreator implements ILogOpCreatable {
    @Override
    public LogOp create(@NonNull List<Assignment> assignments, @NonNull KB kb, CONNECTION_TYPE connectionType) {
        LogOp logOp = connectionType == CONNECTION_TYPE.OR ? LogOp.or() : LogOp.and();

        for (Assignment assignment : assignments) { // get each clause
            ChocoSolverUtils.addAssignmentToLogOp(logOp, kb.getModelKB(), assignment.getVariable(), assignment.getValue());
        }
        return logOp;
    }

    @Override
    public LogOp create(@NonNull Assignment assignment, @NonNull KB kb, CONNECTION_TYPE connectionType) {
//        LogOp logOp = LogOp.and(); // creates a AND LogOp
        LogOp logOp = connectionType == CONNECTION_TYPE.OR ? LogOp.or() : LogOp.and();
        ChocoSolverUtils.addAssignmentToLogOp(logOp, kb.getModelKB(), assignment.getVariable(), assignment.getValue());
        return logOp;
    }

    @Override
    public LogOp createNegation(@NonNull LogOp logOp, CONNECTION_TYPE connectionType) {
//        return LogOp.nand(logOp);
        return connectionType == CONNECTION_TYPE.OR ? LogOp.nor(logOp) : LogOp.nand(logOp);
    }
}
