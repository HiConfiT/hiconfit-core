/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms;

import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.kb.core.Constraint;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

import static at.tugraz.ist.ase.cacdr.eval.CAEvaluator.COUNTER_CONSISTENCY_CHECKS;
import static at.tugraz.ist.ase.eval.PerformanceEvaluator.*;

/**
 * Implementation of the WipeOutR_FM algorithm.
 * <p>
 * // Func WipeOutR_FM(CF = {c1..cn}) :
 * // CFΔ = CF
 * // for all cα ∈ CF do
 * //   if inconsistent(CFΔ - {cα} ∪ {¬cα}) then
 * //      CFΔ ← CFΔ − {cα}
 * //   end if
 * // end for
 * // return(CFΔ)
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Slf4j
public class WipeOutR_FM extends IConsistencyAlgorithm {

    // for evaluation
    public static final String TIMER_WIPEOUTR_FM = "Timer for WipeOutR_FM";

    public WipeOutR_FM(@NonNull ChocoConsistencyChecker checker) {
        super(checker);
    }

    /**
     * // Func WipeOutR_FM(CF = {c1..cn}) :
     * // CFΔ = CF
     * // for all cα ∈ CF do
     * //   if inconsistent(CFΔ - {cα} ∪ {¬cα}) then
     * //      CFΔ ← CFΔ − {cα}
     * //   end if
     * // end for
     * // return(CFΔ)
     */
    public List<Constraint> run(@NonNull List<Constraint> CF) {
        log.debug("{}Detecting redundancies for [CF={}] >>>", LoggerUtils.tab(), CF);
        LoggerUtils.indent();

        List<Constraint> CF_alpha = new LinkedList<>(CF); // CFΔ = CF

        start(TIMER_WIPEOUTR_FM);
        for (Constraint cstr : CF) { // for all cα ∈ CF do

            log.trace("{}Checking redundancy of [cstr={}]", LoggerUtils.tab(), cstr);
            LoggerUtils.indent();

            incrementCounter(COUNTER_CONSISTENCY_CHECKS);
            // if inconsistent(CFΔ - {cα} ∪ {¬cα}) then
            if (!checker.isConsistent(CF_alpha, cstr)) {
                CF_alpha.remove(cstr); // CFΔ ← CFΔ − {cα}

                log.trace("{}Redundancy detected: [cstr={}]", LoggerUtils.tab(), cstr);
            }

            LoggerUtils.outdent();
        }
        stop(TIMER_WIPEOUTR_FM);

        LoggerUtils.outdent();
        log.debug("{}<<< Return non-redundant feature model [CF_alpha={}]", LoggerUtils.tab(), CF_alpha);

        return CF_alpha; // return(CFΔ)
    }
}
