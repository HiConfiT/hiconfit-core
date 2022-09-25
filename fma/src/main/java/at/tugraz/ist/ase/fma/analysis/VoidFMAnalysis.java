/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.analysis;

import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.cdrmodel.fm.FMDebuggingModel;
import at.tugraz.ist.ase.cdrmodel.test.ITestCase;
import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.fm.core.CTConstraint;
import at.tugraz.ist.ase.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.fma.anomaly.AnomalyType;
import at.tugraz.ist.ase.fma.explanator.VoidFMExplanator;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Analysis checks if a feature model is void.
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 * @author: Tamim Burgstaller (tamim.burgstaller@student.tugraz.at)
 */
@Slf4j
public class VoidFMAnalysis extends AbstractFMAnalysis<ITestCase> {

    public VoidFMAnalysis(@NonNull FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> debuggingModel,
                          @NonNull ITestCase assumption) {
        super(debuggingModel, assumption);
    }

    @Override
    @SuppressWarnings("unchecked")
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
            explanator = new VoidFMExplanator((FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>) model, assumption);

            explanator.identify();
            log.trace("{}Identified diagnoses for [assumption=[{}]]", LoggerUtils.tab(), assumption);
        }

        LoggerUtils.outdent();
        log.debug("{}Analyzed Void feature model with [assumption=[{}]]", LoggerUtils.tab(), assumption);
        return non_violated;
    }
}
