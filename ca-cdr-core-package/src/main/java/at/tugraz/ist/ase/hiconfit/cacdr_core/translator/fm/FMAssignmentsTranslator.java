/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr_core.translator.fm;

import at.tugraz.ist.ase.hiconfit.cacdr_core.Assignment;
import at.tugraz.ist.ase.hiconfit.cacdr_core.translator.IAssignmentsTranslatable;
import at.tugraz.ist.ase.hiconfit.cacdr_core.translator.ILogOpCreatable;
import at.tugraz.ist.ase.hiconfit.common.ChocoSolverUtils;
import at.tugraz.ist.ase.hiconfit.kb.core.KB;
import lombok.NonNull;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;

import java.util.List;

/**
 * No remove the translated constraints from the Choco model
 */
public class FMAssignmentsTranslator implements IAssignmentsTranslatable, ILogOpCreatable {

    /**
     * Translates {@link Assignment}s to Choco constraints.
     * @param assignments the {@link Assignment}s to translate
     * @param kb the {@link KB}
     * @param chocoCstrs list of Choco constraints, to which the translated constraints are added
     * @param negChocoCstrs list of Choco constraints, to which the translated negative constraints are added
     */
    @Override
    public void translate(@NonNull List<Assignment> assignments, @NonNull KB kb,
                          @NonNull List<Constraint> chocoCstrs, List<Constraint> negChocoCstrs) {
        int startIdx = kb.getNumChocoConstraints();
        Model model = kb.getModelKB();

        LogOp logOp = create(assignments, kb);
        post(logOp, model, chocoCstrs, startIdx);

        // Negation of the translated constraints
        if (negChocoCstrs != null) {
            translateToNegation(logOp, model, negChocoCstrs);
        }
    }

    private static void post(LogOp logOp, Model model, List<Constraint> chocoCstrs, int startIdx) {
        model.addClauses(logOp); // add the translated constraints to the Choco kb

        List<Constraint> postedCstrs = ChocoSolverUtils.getConstraints(model, startIdx, model.getNbCstrs() - 1);

        chocoCstrs.addAll(postedCstrs);

        // remove the posted constraints from the Choco model
        postedCstrs.forEach(model::unpost);
    }

    /**
     * Translates {@link Assignment}s to Choco constraints.
     * @param assignment the {@link Assignment} to translate
     * @param kb the {@link KB}
     * @param chocoCstrs list of Choco constraints, to which the translated constraints are added
     * @param negChocoCstrs list of Choco constraints, to which the translated negative constraints are added
     */
    @Override
    public void translate(@NonNull Assignment assignment, @NonNull KB kb,
                          @NonNull List<Constraint> chocoCstrs, List<Constraint> negChocoCstrs) {
        int startIdx = kb.getNumChocoConstraints();
        Model model = kb.getModelKB();

        LogOp logOp = create(assignment, kb);
        post(logOp, model, chocoCstrs, startIdx);

        // Negation of the translated constraints
        if (negChocoCstrs != null) {
            translateToNegation(logOp, model, negChocoCstrs);
        }
    }

    private void translateToNegation(LogOp logOp, Model model, List<Constraint> negChocoCstrs) {
        int startIdx = model.getNbCstrs();
        LogOp negLogOp = createNegation(logOp);
        post(negLogOp, model, negChocoCstrs, startIdx);
    }

    @Override
    public LogOp create(@NonNull List<Assignment> assignments, @NonNull KB kb) {
        LogOp logOp = LogOp.and(); // creates a AND LogOp
        for (Assignment assignment : assignments) { // get each clause
            ChocoSolverUtils.addAssignmentToLogOp(logOp, kb.getModelKB(), assignment.getVariable(), assignment.getValue());
        }
        return logOp;
    }

    @Override
    public LogOp create(@NonNull Assignment assignment, @NonNull KB kb) {
        LogOp logOp = LogOp.and(); // creates a AND LogOp
        ChocoSolverUtils.addAssignmentToLogOp(logOp, kb.getModelKB(), assignment.getVariable(), assignment.getValue());
        return logOp;
    }

    @Override
    public LogOp createNegation(@NonNull LogOp logOp) {
        return LogOp.nand(logOp);
    }
}
