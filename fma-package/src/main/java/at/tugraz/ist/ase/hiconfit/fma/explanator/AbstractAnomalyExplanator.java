/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.explanator;

import at.tugraz.ist.ase.hiconfit.cacdr_core.ITestCase;
import at.tugraz.ist.ase.hiconfit.cdrmodel.fm.FMDebuggingModel;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.hiconfit.kb.core.Constraint;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;
import java.util.Set;

/**
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 * @author: Tamim Burgstaller (tamim.burgstaller@student.tugraz.at)
 */
public abstract class AbstractAnomalyExplanator<T extends ITestCase, F extends AnomalyAwareFeature> {
    protected FMDebuggingModel<F, AbstractRelationship<F>, CTConstraint> debuggingModel;

    protected T assumption;

    @Getter
    protected List<Set<Constraint>> diagnoses = null;

    public AbstractAnomalyExplanator(@NonNull FMDebuggingModel<F, AbstractRelationship<F>, CTConstraint> debuggingModel,
                                     T assumption) {
        this.debuggingModel = debuggingModel;
        this.assumption = assumption;
    }

    public abstract void identify();
}
