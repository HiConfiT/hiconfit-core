/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr_core.translator.camera;

import at.tugraz.ist.ase.hiconfit.cacdr_core.Assignment;
import at.tugraz.ist.ase.hiconfit.cacdr_core.translator.IAssignmentsTranslatable;
import at.tugraz.ist.ase.hiconfit.common.ChocoSolverUtils;
import at.tugraz.ist.ase.hiconfit.kb.camera.CameraKB;
import at.tugraz.ist.ase.hiconfit.kb.core.KB;
import com.google.common.base.Preconditions;
import lombok.NonNull;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

import java.util.List;

/**
 * No remove the translated constraints from the Choco model
 */
public class CameraAssignmentsTranslator implements IAssignmentsTranslatable {

    /**
     * Translates {@link Assignment}s to Choco constraints.
     * @param assignments the {@link Assignment}s to translate
     * @param kb the {@link KB}
     * @param chocoCstrs list of Choco constraints, to which the translated constraints are added
     * @param negChocoCstrs list of Choco constraints, to which the translated negative constraints are added
     */
    @Override
    public void translate(@NonNull List<Assignment> assignments, @NonNull KB kb, @NonNull List<Constraint> chocoCstrs, List<Constraint> negChocoCstrs) {
        CameraKB cameraKB = checkAndGetCameraKB(kb);

        int startIdx = cameraKB.getNumChocoConstraints();
        createAndPost(assignments, cameraKB);

        afterPostingAndUnpost(chocoCstrs, cameraKB, startIdx);

        // TODO - negation
        // Negation of the translated constraints
//        if (negChocoCstrs != null) {
//            translateToNegation(logOp, model, negChocoCstrs);
//        }
    }

    private static void afterPostingAndUnpost(List<Constraint> chocoCstrs, CameraKB cameraKB, int startIdx) {
        Model model = cameraKB.getModelKB();
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
    public void translate(@NonNull Assignment assignment, @NonNull KB kb, @NonNull List<Constraint> chocoCstrs, List<Constraint> negChocoCstrs) {
        // check if the KB is a CameraKB
        CameraKB cameraKB = checkAndGetCameraKB(kb);

        int startIdx = cameraKB.getNumChocoConstraints();
        createAndPost(assignment, cameraKB); // add the translated constraints to the Choco model

        afterPostingAndUnpost(chocoCstrs, cameraKB, startIdx);

        // TODO - negation
        // Negation of the translated constraints
//        if (negChocoCstrs != null) {
//            translateToNegation(logOp, model, negChocoCstrs);
//        }
    }

    private static CameraKB checkAndGetCameraKB(KB kb) {
        // check if the KB is a CameraKB
        Preconditions.checkArgument(kb instanceof CameraKB, "The KB must be a CameraKB");
        return (CameraKB) kb;
    }

    private boolean isCorrectAssignment(String varName, IntVar var, String value, int chocoValue) {
        return var != null && (!value.equals("NULL"))
                && (chocoValue != -1);
    }

    private void createAndPost(@NonNull List<Assignment> assignments, @NonNull CameraKB kb) {
        for (Assignment assign: assignments) {
            createAndPost(assign, kb);
        }
    }

    private void createAndPost(@NonNull Assignment assignment, @NonNull CameraKB kb) {
        String varName = assignment.getVariable();
        IntVar var = kb.getIntVar(varName);
        String value = assignment.getValue();
        int chocoValue = kb.getIntValue(varName, value);

        if (isCorrectAssignment(varName, var, value, chocoValue)) {
            kb.getModelKB().arithm(var, "=", chocoValue).post();
        }
    }

//    private void translateToNegation(LogOp logOp, Model model, List<Constraint> negChocoCstrs) {
//        LogOp negLogOp = createNegation(logOp);
//        int startIdx = model.getNbCstrs();
//        model.addClauses(negLogOp);
//
//        negChocoCstrs.addAll(ChocoSolverUtils.getConstraints(model, startIdx, model.getNbCstrs() - 1));
//    }
//
//    public LogOp createNegation(@NonNull LogOp logOp) {
//        return LogOp.nand(logOp);
//    }
}
