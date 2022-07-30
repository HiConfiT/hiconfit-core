/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.test.translator.fm;

import at.tugraz.ist.ase.common.ChocoSolverUtils;
import at.tugraz.ist.ase.test.Assignment;
import at.tugraz.ist.ase.test.translator.IAssignmentsTranslatable;
import at.tugraz.ist.ase.test.translator.ILogOpCreatable;
import lombok.NonNull;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;

import java.util.List;

public class FMAssignmentsTranslator implements IAssignmentsTranslatable, ILogOpCreatable {

    /**
     * Translates {@link at.tugraz.ist.ase.test.Assignment}s to Choco constraints.
     * @param assignments the {@link at.tugraz.ist.ase.test.Assignment}s to translate
     * @param model the Choco model
     * @param chocoCstrs list of Choco constraints, to which the translated constraints are added
     * @param negChocoCstrs list of Choco constraints, to which the translated negative constraints are added
     */
    @Override
    public void translate(@NonNull List<Assignment> assignments, @NonNull Model model,
                          @NonNull List<Constraint> chocoCstrs, List<Constraint> negChocoCstrs) {
        int startIdx = model.getNbCstrs();
        LogOp logOp = create(assignments, model);
        model.addClauses(logOp); // add the translated constraints to the Choco model

        chocoCstrs.addAll(ChocoSolverUtils.getConstraints(model, startIdx, model.getNbCstrs() - 1));

        // Negation of the translated constraints
        if (negChocoCstrs != null) {
            translateToNegation(logOp, model, negChocoCstrs);
        }
    }

    /**
     * Translates {@link at.tugraz.ist.ase.test.Assignment}s to Choco constraints.
     * @param assignment the {@link at.tugraz.ist.ase.test.Assignment} to translate
     * @param model the Choco model
     * @param chocoCstrs list of Choco constraints, to which the translated constraints are added
     * @param negChocoCstrs list of Choco constraints, to which the translated negative constraints are added
     */
    @Override
    public void translate(@NonNull Assignment assignment, @NonNull Model model,
                          @NonNull List<Constraint> chocoCstrs, List<Constraint> negChocoCstrs) {
        int startIdx = model.getNbCstrs();
        LogOp logOp = create(assignment, model);
        model.addClauses(logOp); // add the translated constraints to the Choco model

        chocoCstrs.addAll(ChocoSolverUtils.getConstraints(model, startIdx, model.getNbCstrs() - 1));

        // Negation of the translated constraints
        if (negChocoCstrs != null) {
            translateToNegation(logOp, model, negChocoCstrs);
        }
    }

    private void translateToNegation(LogOp logOp, Model model, List<Constraint> negChocoCstrs) {
        LogOp negLogOp = createNegation(logOp, model);
        int startIdx = model.getNbCstrs();
        model.addClauses(negLogOp);

        negChocoCstrs.addAll(ChocoSolverUtils.getConstraints(model, startIdx, model.getNbCstrs() - 1));
    }

    @Override
    public LogOp create(@NonNull List<Assignment> assignments, @NonNull Model model) {
        LogOp logOp = LogOp.and(); // creates a AND LogOp
        for (Assignment assignment : assignments) { // get each clause
            ChocoSolverUtils.addAssignmentToLogOp(logOp, model, assignment.getVariable(), assignment.getValue());
        }
        return logOp;
    }

    @Override
    public LogOp create(@NonNull Assignment assignment, @NonNull Model model) {
        LogOp logOp = LogOp.and(); // creates a AND LogOp
        ChocoSolverUtils.addAssignmentToLogOp(logOp, model, assignment.getVariable(), assignment.getValue());
        return logOp;
    }

    @Override
    public LogOp createNegation(@NonNull LogOp logOp, @NonNull Model model) {
        return LogOp.nand(logOp);
    }
}
