/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2023-2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cdrmodel.kb.factory;

import at.tugraz.ist.ase.hiconfit.cdrmodel.AbstractCDRModel;
import at.tugraz.ist.ase.hiconfit.cdrmodel.ICdrModelFactory;
import at.tugraz.ist.ase.hiconfit.cdrmodel.kb.KBCdrModel;
import at.tugraz.ist.ase.hiconfit.kb.core.KB;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Factory for creating a CDR model for a KB diagnosis/conflict detection task
 * 1. Diagnosis/Conflict detection tasks:
 *    + C = KB - cfInConflicts = true
 *    + B = {}
 * Output model can be used for the following algorithms: FastDiag, QuickXPlain, HSDAG
 */
@Getter
@Setter
public class KBCdrModelFactory implements ICdrModelFactory {

    protected @NonNull KB kb;

    protected boolean cfInConflicts = true;
    protected boolean reversedConstraintsOrder;

    public KBCdrModelFactory(@NonNull KB kb,
                             boolean reversedConstraintsOrder) {
        this.kb = kb;
        this.reversedConstraintsOrder = reversedConstraintsOrder;
    }

    public static KBCdrModelFactory getInstance(@NonNull KB kb,
                                                boolean reversedConstraintsOrder) {
        return new KBCdrModelFactory(kb, reversedConstraintsOrder);
    }

    public static KBCdrModelFactory getInstance(@NonNull KB kb) {
        return getInstance(kb, false);
    }

    @Override
    public AbstractCDRModel createModel() {
        KBCdrModel diagModel = new KBCdrModel(kb, cfInConflicts, reversedConstraintsOrder);
        diagModel.initialize();

        return diagModel;
    }
}
