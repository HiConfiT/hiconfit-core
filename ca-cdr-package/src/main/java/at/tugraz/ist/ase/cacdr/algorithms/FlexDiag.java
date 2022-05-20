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
import com.google.common.collect.Sets;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static at.tugraz.ist.ase.cacdr.eval.CAEvaluator.*;
import static at.tugraz.ist.ase.common.ConstraintUtils.split;

/**
 * Implementation of FlexDiag algorithm.
 *
 * <ul>
 *     <li>A. Felfernig, R. Walter, J. Galindo, D. Benavides, S.P. Erdeniz, M. Atas and S. Reiterer.
 *     Anytime Diagnosis for Reconfiguration. In Journal of Intelligent Information Systems, vol. 51,
 *     pp. 161-182, 2018.</li>
 * </ul>
 *
 * // FlexDiag Algorithm
 * //--------------------
 * // Func FlexDiag(S, AC = C ∪ Rp ∪ S, m): ∆
 * // if isEmpty(S) or inconsistent(AC - C) return Φ
 * // else return FlexD(Φ, C, AC, m)
 *
 * // Func FlexD(D,S = {s1..sq},AC,m): ∆
 * // if D != Φ and consistent(AC) return Φ;
 * // if size(S) <= m return S;
 * // k = q/2;
 * // S1 = {c1..ck}; S2 = {ck+1..cq};
 * // D1 = FlexD(S1, S2, AC - S1, m);
 * // D2 = FlexD(D1, S1, AC - D1, m);
 * // return(D1 ∪ D2);
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Slf4j
public class FlexDiag extends IConsistencyAlgorithm {
    // for evaluation
    public static final String TIMER_FLEXDIAG = "Timer for FlexDiag";
    public static final String COUNTER_FLEXDIAG_CALLS = "The number of FlexDiag calls";

    public FlexDiag(@NonNull ChocoConsistencyChecker checker) {
        super(checker);
    }

    /**
     * This function will activate FlexDiag algorithm if there exists at least one constraint,
     * which induces an inconsistency with AC - C. Otherwise, it returns an empty set.
     *
     * // Func FlexDiag(S, AC = C ∪ Rp ∪ S, m): ∆
     * // if isEmpty(S) or inconsistent(AC - C) return Φ
     * // else return FlexD(Φ, C, AC, m)
     *
     * @param S a consideration set of constraints. Need to inverse the order of the possibly faulty constraint set.
     * @param AC a background knowledge
     * @param m the value which controls the diagnosis quality
     * @return a diagnosis or an empty set
     */
    public Set<Constraint> findDiagnosis(@NonNull Set<Constraint> S, @NonNull Set<Constraint> AC, int m) {
        log.debug("{}Identifying diagnosis for [S={}, AC={}, {}] >>>", LoggerUtils.tab(), S, AC, m);
        LoggerUtils.indent();

        Set<Constraint> ACwithoutS = Sets.difference(AC, S); incrementCounter(COUNTER_DIFFERENT_OPERATOR);

        // if isEmpty(S) or inconsistent(AC - S) return Φ
        if (S.isEmpty() || checker.isConsistent(AC) ||
                (!ACwithoutS.isEmpty() && !checker.isConsistent(ACwithoutS))) {

            LoggerUtils.outdent();
            log.debug("{}<<< No diagnosis found", LoggerUtils.tab());

            return Collections.emptySet();
        } else { // else return FlexD(Φ, C, AC, m)
            incrementCounter(COUNTER_FLEXDIAG_CALLS);
            start(TIMER_FLEXDIAG);
            Set<Constraint> Δ = flexd(Collections.emptySet(), S, AC, m);
            stop(TIMER_FLEXDIAG);

            LoggerUtils.outdent();
            log.debug("{}<<< Found diagnosis [diag={}]", LoggerUtils.tab(), Δ);

            return Δ;
        }
    }

    /**
     * The implementation of FlexD procedure.
     *
     * // Func FlexD(D,S = {s1..sq},AC,m): ∆
     * // if D != Φ and consistent(AC) return Φ;
     * // if size(S) <= m return S;
     * // k = q/2;
     * // S1 = {s1..sk}; S2 = {sk+1..sq};
     * // D1 = FlexD(S1, S2, AC - S1, m);
     * // D2 = FlexD(D1, S1, AC - D1, m);
     * // return(D1 ∪ D2);
     *
     * @param D check to skip redundant consistency checks
     * @param S a consideration set of constraints
     * @param AC all constraints
     * @param m the parameter m, which controls the diagnosis quality
     * @return a diagnosis or an empty set
     */
    private Set<Constraint> flexd(Set<Constraint> D, Set<Constraint> S, Set<Constraint> AC, int m) {
        log.trace("{}FlexD [D={}, S={}, AC={}, m={}] >>>", LoggerUtils.tab(), D, S, AC, m);
        LoggerUtils.indent();

        // if D != Φ and consistent(AC) return Φ;
        if ( !D.isEmpty() ) {
            incrementCounter(COUNTER_CONSISTENCY_CHECKS);
            if (checker.isConsistent(AC)) {
                log.trace("{}<<< return Φ", LoggerUtils.tab());
                LoggerUtils.outdent();

                return Collections.emptySet();
            }
        }

        // if size(S) <= m return S;
        int q = S.size();
        if (q <= m) {
            LoggerUtils.outdent();
            log.trace("{}<<< return [{}]", LoggerUtils.tab(), S);

            return S;
        }

        // S1 = {s1..sk}; S2 = {sk+1..sq};
        Set<Constraint> S1 = new LinkedHashSet<>();
        Set<Constraint> S2 = new LinkedHashSet<>();
        split(S, S1, S2);
        log.trace("{}Split S into [S1={}, S2={}]", LoggerUtils.tab(), S1, S2);

        // D1 = FlexD(S2, S1, AC - S2, m);
        Set<Constraint> ACwithoutS2 = Sets.difference(AC, S2); incrementCounter(COUNTER_DIFFERENT_OPERATOR);
        incrementCounter(COUNTER_LEFT_BRANCH_CALLS);
        incrementCounter(COUNTER_FLEXDIAG_CALLS);
        Set<Constraint> D1 = flexd(S2, S1, ACwithoutS2, m);

        // D2 = FlexD(D1, S2, AC - D1, m);
        Set<Constraint> ACwithoutD1 = Sets.difference(AC, D1); incrementCounter(COUNTER_DIFFERENT_OPERATOR);
        incrementCounter(COUNTER_RIGHT_BRANCH_CALLS);
        incrementCounter(COUNTER_FLEXDIAG_CALLS);
        Set<Constraint> D2 = flexd(D1, S2, ACwithoutD1, m);

        LoggerUtils.outdent();
        log.trace("{}<<< return [D1={} ∪ D2={}]", LoggerUtils.tab(), D1, D2);

        // return(D1 ∪ D2);
        incrementCounter(COUNTER_UNION_OPERATOR);
        return Sets.union(D1, D2);
    }
}
