/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr_core.translator.kb;

import at.tugraz.ist.ase.hiconfit.cacdr_core.Assignment;
import at.tugraz.ist.ase.hiconfit.cacdr_core.Solution;
import at.tugraz.ist.ase.hiconfit.cacdr_core.translator.ISolutionTranslatable;
import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import at.tugraz.ist.ase.hiconfit.kb.core.Constraint;
import at.tugraz.ist.ase.hiconfit.kb.core.IIntVarKB;
import at.tugraz.ist.ase.hiconfit.kb.core.KB;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class KBSolutionTranslator implements ISolutionTranslatable {

    protected KBAssignmentsTranslator translator = new KBAssignmentsTranslator();

    /**
     * Translates a IIntVarKB solution to Constraint
     */
    @Override
    public Constraint translate(@NonNull Solution solution, @NonNull KB kb) {
        // check if the KB is a IIntVarKB
        checkArgument(kb instanceof IIntVarKB, "The KB must be a IIntVarKB");
//        CameraKB cameraKB = (CameraKB) kb;

        log.trace("{}Translating solution [solution={}] >>>", LoggerUtils.tab(), solution);

        // gets List<String> variables from solution
        List<String> variables = solution.getAssignments().stream()
                .map(Assignment::getVariable)
                .toList();
        Constraint constraint = new Constraint(solution.toString(), variables);

        translator.translate(solution.getAssignments(), kb,
                constraint.getChocoConstraints(), constraint.getNegChocoConstraints());

        // copy the generated constraints to Solution
        constraint.getChocoConstraints().forEach(solution::addChocoConstraint);
        constraint.getNegChocoConstraints().forEach(solution::addNegChocoConstraint);

        // remove the translated constraints from the Choco model
        // TODO - should move out to the configurator class
//        cameraKB.getModelKB().unpost(cameraKB.getModelKB().getCstrs());

        log.debug("{}Translated solution [solution={}] >>>", LoggerUtils.tab(), solution);
        return constraint;
    }

    /**
     * Translates a IIntVarKB solution to a list of Constraints
     */
    @Override
    public List<Constraint> translateToList(@NonNull Solution solution, @NonNull KB kb) {
        // check if the KB is a IIntVarKB
        checkArgument(kb instanceof IIntVarKB, "The KB must be a IIntVarKB");
//        CameraKB cameraKB = (CameraKB) kb;

        log.trace("{}Translating solution [solution={}] >>>", LoggerUtils.tab(), solution);
        List<Constraint> constraints = new LinkedList<>();

        for (Assignment assign: solution.getAssignments()) {
            // gets List<String> variables from solution
            List<String> variables = solution.getAssignments().stream()
                    .map(Assignment::getVariable)
                    .toList();
            Constraint constraint = new Constraint(assign.toString(), variables);

            translator.translate(assign, kb,
                    constraint.getChocoConstraints(), constraint.getNegChocoConstraints());

            // copy the generated constraints to Solution
            constraint.getChocoConstraints().forEach(solution::addChocoConstraint);
            constraint.getNegChocoConstraints().forEach(solution::addNegChocoConstraint);

            constraints.add(constraint);
        }

        // remove the translated constraints from the Choco model
        // TODO - should move out to the configurator class
//        cameraKB.getModelKB().unpost(cameraKB.getModelKB().getCstrs());

        log.debug("{}Translated solution [solution={}] >>>", LoggerUtils.tab(), solution);
        return constraints;
    }
}
