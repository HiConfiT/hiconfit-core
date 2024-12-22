/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cdrmodel.fm;

import at.tugraz.ist.ase.hiconfit.cacdr_core.Requirement;
import at.tugraz.ist.ase.hiconfit.cacdr_core.translator.ISolutionTranslatable;
import at.tugraz.ist.ase.hiconfit.cacdr_core.translator.fm.FMSolutionTranslator;
import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.Feature;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import at.tugraz.ist.ase.hiconfit.kb.core.Constraint;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

/**
 * An extension class of {@link FMCdrModel} for a diagnosis task of feature models, in which:
 * If cfInConflicts, then:
 *     + C = CF + Requirement
 *     + B = { f0 = true } - rootConstraints = true
 * else:
 *     + C = Requirement
 *     + B = { f0 = true } + CF - rootConstraints = true
 * + reversedConstraintsOrder = false
 * + hasNegativeConstraints = false
 */
@Slf4j
public class FMRequirementCdrModel<F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint>
        extends FMCdrModel<F, R, C> {

    protected Requirement requirement;
    @Setter
    protected ISolutionTranslatable solutionTranslator = new FMSolutionTranslator();

    /**
     * A constructor
     * On the basic of a given {@link FeatureModel}, it creates
     * corresponding variables and constraints for the model.
     *
     * @param fm a {@link FeatureModel}
     */
    public FMRequirementCdrModel(@NonNull FeatureModel<F, R, C> fm,
                                 @NonNull Requirement requirement,
                                 boolean hasNegativeConstraints,
                                 boolean rootConstraints,
                                 boolean cfInConflicts,
                                 boolean reversedConstraintsOrder) {
        super(fm, hasNegativeConstraints, rootConstraints, cfInConflicts, reversedConstraintsOrder);
        this.requirement = requirement;
    }

    /**
     * This function creates a Choco models, variables, constraints
     * for a corresponding feature models. Besides, test cases are
     * also translated to Choco constraints.
     */
    @Override
    public void initialize() {
        log.debug("{}Initializing FMModel for {} >>>", LoggerUtils.tab(), getName());
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
        List<Constraint> constraints = solutionTranslator.translateToList(requirement, fmkb);
        // add user requirements to C
        C.addAll(constraints);
//        if (isReversedConstraintsOrder()) {
//            Collections.reverse(C); // in default, this shouldn't happen
//        }
        this.setPossiblyFaultyConstraints(C);

        // remove all Choco constraints
        model.unpost(model.getCstrs());

        LoggerUtils.outdent();
        log.debug("{}<<< Model {} initialized", LoggerUtils.tab(), getName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object clone() throws CloneNotSupportedException {
        FMRequirementCdrModel<F, R, C> clone = (FMRequirementCdrModel<F, R, C>) super.clone();

        clone.requirement = (Requirement) requirement.clone();

        return clone;
    }

    @Override
    public void dispose() {
        super.dispose();
        requirement = null;
        solutionTranslator = null;
    }
}
