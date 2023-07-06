/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.analysis;

import at.tugraz.ist.ase.hiconfit.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.hiconfit.cacdr_core.ITestCase;
import at.tugraz.ist.ase.hiconfit.cdrmodel.fm.FMDebuggingModel;
import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyType;
import at.tugraz.ist.ase.hiconfit.fma.explanator.VoidFMExplanator;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Analysis checks if a feature model is void.
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 * @author: Tamim Burgstaller (tamim.burgstaller@student.tugraz.at)
 */
@Slf4j
public class VoidFMAnalysis<T extends ITestCase, F extends AnomalyAwareFeature>
        extends AbstractFMAnalysis<T, F> {

    public VoidFMAnalysis(@NonNull FMDebuggingModel<F, AbstractRelationship<F>, CTConstraint> debuggingModel,
                          @NonNull T assumption) {
        super(debuggingModel, assumption);
    }

    @Override
    protected Boolean analyze() {
        log.trace("{}Analyzing Void feature model with [assumption=[{}]]", LoggerUtils.tab(), assumption);
        LoggerUtils.indent();

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(model);

        // inconsistent( CF ∪ { c0 })
        non_violated = checker.isConsistent(model.getAllConstraints(), assumption);
        log.trace("{}Checked assumption [assumption=[{}], non_violated={}]", LoggerUtils.tab(), assumption, non_violated);

        // set anomaly type to the feature
        if (!non_violated) {
            setAnomalyType(AnomalyType.VOID);
        }

        if (withDiagnosis && !non_violated) { // create an explanator and execute it
            explanator = new VoidFMExplanator<>((FMDebuggingModel<F, AbstractRelationship<F>, CTConstraint>) model, assumption);

            explanator.identify();
            log.trace("{}Identified diagnoses for [assumption=[{}]]", LoggerUtils.tab(), assumption);
        }

        LoggerUtils.outdent();
        log.debug("{}Analyzed Void feature model with [assumption=[{}]]", LoggerUtils.tab(), assumption);
        return non_violated;
    }
}
