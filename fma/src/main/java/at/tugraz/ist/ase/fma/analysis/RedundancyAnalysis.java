/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.analysis;

import at.tugraz.ist.ase.cacdr.algorithms.WipeOutR_FM;
import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.cdrmodel.fm.FMCdrModel;
import at.tugraz.ist.ase.kb.core.Constraint;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.NonNull;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class RedundancyAnalysis extends AbstractFMAnalysis<Boolean> {

    @Getter
    private Set<Constraint> redundantConstraints;

    public RedundancyAnalysis(@NonNull FMCdrModel model) {
        super(model, null);

        redundantConstraints = new LinkedHashSet<>();
    }

    /**
     * @return false - redundant, true - not redundant
     */
    @Override
    protected Boolean analyze() {
        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(model);

        List<Constraint> CF = new LinkedList<>(model.getPossiblyFaultyConstraints());

        WipeOutR_FM wipeOutR_FM = new WipeOutR_FM(checker);

//        reset();
        List<Constraint> newCF = wipeOutR_FM.run(CF);

//        System.out.println("Result constraints:");
//        newCF.forEach(System.out::println);

        Set<Constraint> newCFSet = new LinkedHashSet<>(newCF);
        redundantConstraints = Sets.difference(model.getPossiblyFaultyConstraints(), newCFSet);

        return redundantConstraints.isEmpty();
    }
}
