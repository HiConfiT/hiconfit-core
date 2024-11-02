/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cdrmodel.kb;

import at.tugraz.ist.ase.hiconfit.cdrmodel.AbstractCDRModel;
import at.tugraz.ist.ase.hiconfit.cdrmodel.IChocoModel;
import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import at.tugraz.ist.ase.hiconfit.kb.core.Constraint;
import at.tugraz.ist.ase.hiconfit.kb.core.KB;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.Model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * An extension class of {@link AbstractCDRModel} for diagnosis tasks of kbs, in which:
 * 1. Diagnosis/Conflict detection tasks:
 *    + C = KB - cfInConflicts = true
 *    + B = {}
 *    + reversedConstraintsOrder = false
 */
@Getter
@Slf4j
public class KBCdrModel extends AbstractCDRModel implements IChocoModel {
    protected Model model;
    protected KB kb;

    protected final boolean cfInConflicts;
    protected final boolean reversedConstraintsOrder;

    public KBCdrModel(@NonNull KB kb,
                      boolean cfInConflicts,
                      boolean reversedConstraintsOrder) {
        super("KBCdrModel");

        this.kb = kb;
        this.model = kb.getModelKB();

        this.cfInConflicts = cfInConflicts;
        this.reversedConstraintsOrder = reversedConstraintsOrder;
    }

    protected void initializeConstraintSets() {
        // sets possibly faulty constraints to super class
        if (cfInConflicts) {
            log.trace("{}Adding possibly faulty constraints", LoggerUtils.tab());
            List<Constraint> C = new LinkedList<>(kb.getConstraintList());
            if (isReversedConstraintsOrder()) {
                Collections.reverse(C); // in default, this shouldn't happen
            }
            this.setPossiblyFaultyConstraints(C);
        }

        log.trace("{}Adding correct constraints", LoggerUtils.tab());
        List<Constraint> C = new LinkedList<>();
        if (!cfInConflicts) {
            C.addAll(kb.getConstraintList());
        }
        this.setCorrectConstraints(C);
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

        // remove all Choco constraints, cause we just need variables and test cases
        model.unpost(model.getCstrs());

        LoggerUtils.outdent();
        log.debug("{}<<< Model {} initialized", LoggerUtils.tab(), getName());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        KBCdrModel clone = (KBCdrModel) super.clone();

        clone.kb = this.kb;
        clone.model = this.model;

        return clone;
    }

    @Override
    public void dispose() {
        super.dispose();
        model = null;
        kb = null;
    }
}

