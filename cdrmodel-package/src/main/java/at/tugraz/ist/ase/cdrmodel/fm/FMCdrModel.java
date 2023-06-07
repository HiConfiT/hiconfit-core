/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cdrmodel.fm;

import at.tugraz.ist.ase.cdrmodel.AbstractCDRModel;
import at.tugraz.ist.ase.cdrmodel.IChocoModel;
import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.fm.core.CTConstraint;
import at.tugraz.ist.ase.fm.core.Feature;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import at.tugraz.ist.ase.kb.core.Constraint;
import at.tugraz.ist.ase.kb.fm.FMKB;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.Model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * An extension class of {@link AbstractCDRModel} for diagnosis tasks as well as
 * analysis operations of feature models, in which:
 * 1. Diagnosis tasks (rootConstraints = true):
 *    If cfInConflicts, then:
 *        + C = CF
 *        + B = { f0 = true } - rootConstraints = true
 *    else:
 *        + C = {}
 *        + B = { f0 = true } + CF - rootConstraints = true
 *    + reversedConstraintsOrder = false
 *    + hasNegativeConstraints = false
 * 2. Feature model redundancy detection - WipeOutR_FM (rootConstraints = false):
 *    If cfInConflicts, then:
 *        + C = CF
 *        + B = {} - rootConstraints = false
 *    else:
 *        + C = {}
 *        + B = CF - rootConstraints = false
 *    + reversedConstraintsOrder = true
 *    + hasNegativeConstraints = true
 */
@Slf4j
public class FMCdrModel<F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint> extends AbstractCDRModel implements IChocoModel {

    @Getter
    protected Model model;
    protected FeatureModel<F, R, C> featureModel;
    protected FMKB<F, R, C> fmkb;

    @Getter
    protected final boolean hasNegativeConstraints;

    @Getter
    protected final boolean rootConstraints;

    @Getter
    protected final boolean cfInConflicts;

    @Getter
    protected final boolean reversedConstraintsOrder;

    /**
     * A constructor
     * On the basic of a given {@link FeatureModel}, it creates
     * corresponding variables and constraints for the model.
     *
     * @param fm a {@link FeatureModel}
     * @param hasNegativeConstraints generate negative constraints if true
     * @param rootConstraints true if the root constraint (f0 = true) should be added
     * @param reversedConstraintsOrder true if the order of constraints should be reversed before adding to the possibly faulty constraints
     */
    public FMCdrModel(@NonNull FeatureModel<F, R, C> fm,
                      boolean hasNegativeConstraints,
                      boolean rootConstraints,
                      boolean cfInConflicts,
                      boolean reversedConstraintsOrder) {
        super(fm.getName());

        this.featureModel = fm;

        this.hasNegativeConstraints = hasNegativeConstraints;
        this.fmkb = new FMKB<>(fm, hasNegativeConstraints);
        this.model = fmkb.getModelKB();

        this.rootConstraints = rootConstraints;
        this.cfInConflicts = cfInConflicts;
        this.reversedConstraintsOrder = reversedConstraintsOrder;
    }

    protected void initializeConstraintSets() {
        // sets possibly faulty constraints to super class
        if (cfInConflicts) {
            log.trace("{}Adding possibly faulty constraints", LoggerUtils.tab());
            List<Constraint> C = new LinkedList<>(fmkb.getConstraintList());
            if (isReversedConstraintsOrder()) {
                Collections.reverse(C); // in default, this shouldn't happen
            }
            this.setPossiblyFaultyConstraints(C);
        }

        // sets correct constraints to super class
        Constraint rootConstraint = null;
        if (isRootConstraints()) {
            // {f0 = true}
//            int startIdx = model.getNbCstrs();
//            String f0 = fmkb.getVariable(0).getName();
//            BoolVar f0Var = (BoolVar) getVariable(model, f0);
//            model.addClauseTrue(f0Var);
//
//            Constraint constraint = new Constraint(f0 + " = true");
//            constraint.addChocoConstraints(model, startIdx, model.getNbCstrs() - 1, false);

            rootConstraint = fmkb.getRootConstraint();
        }

        log.trace("{}Adding correct constraints", LoggerUtils.tab());
        List<Constraint> C = new LinkedList<>();
        if (rootConstraint != null) {
            C.add(rootConstraint);
        }
        if (!cfInConflicts) {
            C.addAll(fmkb.getConstraintList());
        }
        this.setCorrectConstraints(C);
    }

    /**
     * This function adds constraints to the possibly faulty constraints set, the correct constraints set.
     */
    @Override
    public void initialize() {
        log.debug("{}Initializing FMDiagnosisModel for {} >>>", LoggerUtils.tab(), getName());
        LoggerUtils.indent();

        // sets possibly faulty constraints to super class
        // sets correct constraints to super class
        initializeConstraintSets();

        // remove all Choco constraints, because we just need variables and test cases
        model.unpost(model.getCstrs());

        LoggerUtils.outdent();
        log.debug("{}<<< Model {} initialized", LoggerUtils.tab(), getName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object clone() throws CloneNotSupportedException {
        FMCdrModel<F, R, C> clone = (FMCdrModel<F, R, C>) super.clone();

        clone.fmkb = new FMKB<>(this.featureModel, this.hasNegativeConstraints);
        clone.model = clone.fmkb.getModelKB();

        return clone;
    }

    @Override
    public void dispose() {
        super.dispose();
        model = null;
        featureModel = null;
        fmkb.dispose();
        fmkb = null;
    }
}
