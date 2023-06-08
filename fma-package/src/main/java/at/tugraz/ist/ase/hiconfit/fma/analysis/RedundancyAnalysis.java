/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.analysis;

import at.tugraz.ist.ase.hiconfit.cacdr.algorithms.WipeOutR_FM;
import at.tugraz.ist.ase.hiconfit.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.hiconfit.cdrmodel.fm.FMCdrModel;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test.ITestCase;
import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.hiconfit.kb.core.Constraint;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Slf4j
public class RedundancyAnalysis extends AbstractFMAnalysis<ITestCase> {

    @Getter
    private Set<Constraint> redundantConstraints;

    public RedundancyAnalysis(@NonNull FMCdrModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> model,
                              @NonNull ITestCase assumption) {
        super(model, assumption);

        redundantConstraints = new LinkedHashSet<>();
    }

    /**
     * @return false - redundant, true - not redundant
     */
    @Override
    protected Boolean analyze() {
        log.trace("{}Analyzing Redundancy", LoggerUtils.tab());
        LoggerUtils.indent();

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(model);

        List<Constraint> CF = new LinkedList<>(model.getPossiblyFaultyConstraints());

        WipeOutR_FM wipeOutR_FM = new WipeOutR_FM(checker);

        List<Constraint> newCF = wipeOutR_FM.run(CF);

        Set<Constraint> newCFSet = new LinkedHashSet<>(newCF);
        redundantConstraints = Sets.difference(model.getPossiblyFaultyConstraints(), newCFSet);

        LoggerUtils.outdent();
        log.debug("{}Analyzed Redundancy", LoggerUtils.tab());

        non_violated = redundantConstraints.isEmpty();
        return non_violated;
    }
}
