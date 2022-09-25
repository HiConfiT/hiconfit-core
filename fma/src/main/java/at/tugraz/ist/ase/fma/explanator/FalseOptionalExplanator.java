/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.explanator;

import at.tugraz.ist.ase.cacdr.algorithms.hs.HSDAG;
import at.tugraz.ist.ase.cacdr.algorithms.hs.labeler.DirectDebugLabeler;
import at.tugraz.ist.ase.cacdr.algorithms.hs.parameters.DirectDebugParameters;
import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.cdrmodel.fm.FMDebuggingModel;
import at.tugraz.ist.ase.cdrmodel.test.ITestCase;
import at.tugraz.ist.ase.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.fm.core.CTConstraint;
import at.tugraz.ist.ase.fma.anomaly.AnomalyAwareFeature;
import lombok.NonNull;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 * @author: Tamim Burgstaller (tamim.burgstaller@student.tugraz.at)
 */
public class FalseOptionalExplanator extends AbstractAnomalyExplanator {

    public FalseOptionalExplanator(@NonNull FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> debuggingModel,
                                   ITestCase assumption) {
        super(debuggingModel, assumption);
    }

    @Override
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

        hsdag.construct();

        diagnoses = hsdag.getDiagnoses();
    }
}
