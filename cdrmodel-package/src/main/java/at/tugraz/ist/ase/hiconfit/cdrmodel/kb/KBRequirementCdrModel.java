/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cdrmodel.kb;

import at.tugraz.ist.ase.hiconfit.cacdr_core.Requirement;
import at.tugraz.ist.ase.hiconfit.cacdr_core.translator.ISolutionTranslatable;
import at.tugraz.ist.ase.hiconfit.cacdr_core.translator.kb.KBSolutionTranslator;
import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import at.tugraz.ist.ase.hiconfit.kb.core.Constraint;
import at.tugraz.ist.ase.hiconfit.kb.core.KB;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

/**
 * An extension class of {@link KBCdrModel} for a diagnosis task of kbs, in which:
 * If cfInConflicts, then:
 *     + C = CF + Requirement
 *     + B = {}
 * else:
 *     + C = Requirement
 *     + B = CF
 */
@Slf4j
public class KBRequirementCdrModel extends KBCdrModel {

    protected Requirement requirement;
    @Setter
    protected ISolutionTranslatable translator = new KBSolutionTranslator();

    public KBRequirementCdrModel(@NonNull KB kb,
                                 @NonNull Requirement requirement,
                                 boolean cfInConflicts,
                                 boolean reversedConstraintsOrder) {
        super(kb, cfInConflicts, reversedConstraintsOrder);

        this.requirement = requirement;
    }

    /**
     * This function adds constraints to the possibly faulty constraints set, the correct constraints set.
     */
    @Override
    public void initialize() {
        log.debug("{}Initializing KBCdrModel for {} >>>", LoggerUtils.tab(), getName());
        LoggerUtils.indent();

        // sets possibly faulty constraints to super class
        // sets correct constraints to super class
        initializeConstraintSets();

        List<Constraint> C = new LinkedList<>(this.getPossiblyFaultyConstraints());
//        if (isReversedConstraintsOrder()) {
//            Collections.reverse(C); // in default, this shouldn't happen
//        }
        // translates user requirements to Choco constraints
        log.trace("{}Translating user requirements to Choco constraints", LoggerUtils.tab());
        List<Constraint> constraints = translator.translateToList(requirement, kb);
        // add user requirements to C
        C.addAll(constraints);
//        if (isReversedConstraintsOrder()) {
//            Collections.reverse(C); // in default, this shouldn't happen
//        }
        this.setPossiblyFaultyConstraints(C);

        // remove all Choco constraints, cause we just need variables and test cases
        model.unpost(model.getCstrs());

        LoggerUtils.outdent();
        log.debug("{}<<< Model {} initialized", LoggerUtils.tab(), getName());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        KBRequirementCdrModel clone = (KBRequirementCdrModel) super.clone();

        clone.requirement = (Requirement) requirement.clone();

        return clone;
    }

    @Override
    public void dispose() {
        super.dispose();
        requirement = null;
        translator = null;
    }
}

