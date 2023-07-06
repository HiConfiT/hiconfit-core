/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.explanator;

import at.tugraz.ist.ase.hiconfit.cacdr.algorithms.hs.HSDAG;
import at.tugraz.ist.ase.hiconfit.cacdr.algorithms.hs.HSDAGPruningEngine;
import at.tugraz.ist.ase.hiconfit.cacdr.algorithms.hs.labeler.DirectDebugLabeler;
import at.tugraz.ist.ase.hiconfit.cacdr.algorithms.hs.parameters.DirectDebugParameters;
import at.tugraz.ist.ase.hiconfit.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.hiconfit.cacdr_core.ITestCase;
import at.tugraz.ist.ase.hiconfit.cdrmodel.fm.FMDebuggingModel;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyAwareFeature;
import lombok.NonNull;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 * @author: Tamim Burgstaller (tamim.burgstaller@student.tugraz.at)
 */
public class DeadFeatureExplanator<T extends ITestCase, F extends AnomalyAwareFeature>
        extends AbstractAnomalyExplanator<T, F> {

    public DeadFeatureExplanator(@NonNull FMDebuggingModel<F, AbstractRelationship<F>, CTConstraint> debuggingModel,
                                 T assumption) {
        super(debuggingModel, assumption);
    }

    public void identify() {
        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(debuggingModel);

        Set<ITestCase> TC = new LinkedHashSet<>(Collections.singletonList(assumption));

        // run the hsdag to find diagnoses
        DirectDebugParameters params = DirectDebugParameters.builder()
                .C(debuggingModel.getPossiblyFaultyConstraints())
                .B(debuggingModel.getCorrectConstraints())
                .TV(Collections.emptySet())
                .TC(TC).build();
        DirectDebugLabeler directDebug = new DirectDebugLabeler(checker, params);

        HSDAG hsdag = new HSDAG(directDebug);
        hsdag.setPruningEngine(new HSDAGPruningEngine(hsdag));

        hsdag.construct();

        diagnoses = hsdag.getDiagnoses();
    }
}
