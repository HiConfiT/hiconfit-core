/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.kb.core.translator.fm;

import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import at.tugraz.ist.ase.hiconfit.kb.core.Assignment;
import at.tugraz.ist.ase.hiconfit.kb.core.Constraint;
import at.tugraz.ist.ase.hiconfit.kb.core.KB;
import at.tugraz.ist.ase.hiconfit.kb.core.Solution;
import at.tugraz.ist.ase.hiconfit.kb.core.translator.ISolutionTranslatable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class FMSolutionTranslator implements ISolutionTranslatable {

    protected FMAssignmentsTranslator translator = new FMAssignmentsTranslator();

    /**
     * Translates an FM solution to Constraint
     */
    @Override
    public Constraint translate(@NonNull Solution solution, @NonNull KB kb) {
        log.trace("{}Translating solution [solution={}] >>>", LoggerUtils.tab(), solution);
        Constraint constraint = new Constraint(solution.toString());

        translator.translate(solution.getAssignments(), kb,
                constraint.getChocoConstraints(), constraint.getNegChocoConstraints());

        // copy the generated constraints to Solution
        constraint.getChocoConstraints().forEach(solution::addChocoConstraint);
        constraint.getNegChocoConstraints().forEach(solution::addNegChocoConstraint);

        // remove the translated constraints from the Choco model
        // TODO - should move out to the configurator class
//        kb.getModelKB().unpost(kb.getModelKB().getCstrs());

        log.debug("{}Translated solution [solution={}] >>>", LoggerUtils.tab(), solution);
        return constraint;
    }

    /**
     * Translates an FM solution to a list of Constraints
     */
    @Override
    public List<Constraint> translateToList(@NonNull Solution solution, @NonNull KB kb) {
        log.trace("{}Translating solution [solution={}] >>>", LoggerUtils.tab(), solution);
        List<Constraint> constraints = new LinkedList<>();

        for (Assignment assign: solution.getAssignments()) {
            Constraint constraint = new Constraint(assign.toString());

            translator.translate(assign, kb,
                    constraint.getChocoConstraints(), constraint.getNegChocoConstraints());

            // copy the generated constraints to Solution
            constraint.getChocoConstraints().forEach(solution::addChocoConstraint);
            constraint.getNegChocoConstraints().forEach(solution::addNegChocoConstraint);

            constraints.add(constraint);
        }

        // remove the translated constraints from the Choco model
        // TODO - should move out to the configurator class
//        kb.getModelKB().unpost(kb.getModelKB().getCstrs());

        log.debug("{}Translated solution [solution={}] >>>", LoggerUtils.tab(), solution);
        return constraints;
    }

//    private boolean isCorrectAssignment(String varName, IntVar var, String value, int chocoValue) {
//        return var != null && (!value.equals("NULL"))
//                && (chocoValue != -1);
//    }
}
