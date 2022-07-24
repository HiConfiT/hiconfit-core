/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cdrmodel.fm;

import at.tugraz.ist.ase.cdrmodel.CDRModel;
import at.tugraz.ist.ase.cdrmodel.IChocoModel;
import at.tugraz.ist.ase.common.LoggerUtils;
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
 * An extension class of {@link CDRModel} for a diagnosis task of feature models.
 */
@Slf4j
public class FMDiagnosisModel extends CDRModel implements IChocoModel {

    @Getter
    private Model model;
    private FeatureModel featureModel;
    private FMKB fmkb;

    @Getter
    private final boolean rootConstraints;

    @Getter
    private final boolean reversedConstraintsOrder;

    /**
     * A constructor
     * On the basic of a given {@link FeatureModel}, it creates
     * corresponding variables and constraints for the model.
     *
     * @param fm a {@link FeatureModel}
     * @param rootConstraints true if the root constraint (f0 = true) should be added
     * @param reversedConstraintsOrder true if the order of constraints should be reversed before adding to the possibly faulty constraints
     */
    public FMDiagnosisModel(@NonNull FeatureModel fm,
                            boolean rootConstraints, boolean reversedConstraintsOrder) {
        super(fm.getName());

        this.featureModel = fm;

        this.fmkb = new FMKB(fm, false);
        this.model = fmkb.getModelKB();

        this.rootConstraints = rootConstraints;
        this.reversedConstraintsOrder = reversedConstraintsOrder;
    }

    /**
     * This function adds constraints to the possibly faulty constraints set, the correct constraints set.
     */
    @Override
    public void initialize() {
        log.debug("{}Initializing FMDiagnosisModel for {} >>>", LoggerUtils.tab(), getName());
        LoggerUtils.indent();

        // sets possibly faulty constraints to super class
        log.trace("{}Adding possibly faulty constraints", LoggerUtils.tab());
        List<Constraint> C = new LinkedList<>(fmkb.getConstraintList());
        if (isReversedConstraintsOrder()) {
            Collections.reverse(C); // in default, this shouldn't happen
        }
        this.setPossiblyFaultyConstraints(C);

        // sets correct constraints to super class
        if (isRootConstraints()) {
            log.trace("{}Adding correct constraints", LoggerUtils.tab());
            // {f0 = true}
//            int startIdx = model.getNbCstrs();
//            String f0 = fmkb.getVariable(0).getName();
//            BoolVar f0Var = (BoolVar) getVariable(model, f0);
//            model.addClauseTrue(f0Var);
//
//            Constraint constraint = new Constraint(f0 + " = true");
//            constraint.addChocoConstraints(model, startIdx, model.getNbCstrs() - 1, false);

            Constraint constraint = fmkb.getRootConstraint();

            if (constraint != null) {
                this.setCorrectConstraints(Collections.singletonList(constraint));
            }
        }

        // remove all Choco constraints, cause we just need variables and test cases
        model.unpost(model.getCstrs());

        LoggerUtils.outdent();
        log.debug("{}<<< Model {} initialized", LoggerUtils.tab(), getName());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        FMDiagnosisModel clone = (FMDiagnosisModel) super.clone();

        clone.fmkb = new FMKB(this.featureModel, false);
        clone.model = clone.fmkb.getModelKB();

        clone.initialize();

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
