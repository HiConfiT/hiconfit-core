/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
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
 * Implementation of the DirectDiag algorithm.
 * //--------------------
 * // B: correctConstraints (background knowledge)
 * // C: possiblyFaultyConstraints
 * //--------------------
 * // Func DirectDiag(C, B) : Δ
 * // if isEmpty(C) or consistent(B U C) return Φ
 * // else return DD(Φ, C, B)
 * //--------------------
 * // Func DD(D, C = {c1..cn}, B) : Δ
 * // if D != Φ and consistent(B U C) return Φ;
 * // if singleton(C) return C;
 * // k = n/2;
 * // C1 = {c1..ck}; C2 = {ck+1..cn};
 * // Δ1 = DD(C1, C1, B);
 * // Δ2 = DD(Δ1, C2, B U C1 - Δ1);
 * // return Δ1 ∪ Δ2;
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Slf4j
public class DirectDiag extends IConsistencyAlgorithm {

    // for evaluation
    public static final String TIMER_DIRECTDIAG = "Timer for DirectDiag";
    public static final String COUNTER_DIRECTDIAG_CALLS = "The number of DirectDiag calls";

    public DirectDiag(@NonNull ChocoConsistencyChecker checker) {
        super(checker);
    }

    /**
     * This function will activate DirectDiag algorithm if there exists at least one constraint,
     * which induces an inconsistency in B. Otherwise, it returns an empty set.
     * <p>
     * // Func DirectDiag(C, B) : Δ
     * // if isEmpty(C) or consistent(B U C) return Φ
     * // else return DD(Φ, C, B)
     *
     * @param C a consideration set of constraints. Need to inverse the order of the possibly faulty constraint set.
     * @param B a background knowledge
     * @return a diagnosis or an empty set
     */
    public Set<Constraint> findDiagnosis(@NonNull Set<Constraint> C, @NonNull Set<Constraint> B) {
        log.debug("{}Identifying diagnosis for [C={}, B={}] >>>", LoggerUtils.tab(), C, B);
        LoggerUtils.indent();

        Set<Constraint> BwithC = Sets.union(B, C); incrementCounter(COUNTER_UNION_OPERATOR);

        // if isEmpty(C) or consistent(B U C) return Φ
        if (C.isEmpty()
                || checker.isConsistent(BwithC)) {

            LoggerUtils.outdent();
            log.debug("{}<<< No diagnosis found", LoggerUtils.tab());

            return Collections.emptySet();
        } else { // else return DD(Φ, C, B)
            incrementCounter(COUNTER_DIRECTDIAG_CALLS);
            start(TIMER_DIRECTDIAG);
            Set<Constraint> diag = dd(Collections.emptySet(), C, B);
            stop(TIMER_DIRECTDIAG);

            LoggerUtils.outdent();
            log.debug("{}<<< Found diagnosis [diag={}]", LoggerUtils.tab(), diag);

            return diag;
        }
    }

    /**
     * The implementation of DirectDiag algorithm.
     * The algorithm determines a diagnosis.
     * <p>
     * // Func FD(D, C = {c1..cn}, B) : D
     * // if D != Φ and consistent(B U C) return Φ;
     * // if singleton(C) return C;
     * // k = n/2;
     * // C1 = {c1..ck}; C2 = {ck+1..cn};
     * // Δ1 = DD(C1, C1, B);
     * // Δ2 = DD(Δ1, C2, B U C1 - Δ1);
     * // return Δ1 ∪ Δ2;
     *
     * @param D check to skip redundant consistency checks
     * @param C a consideration set of constraints
     * @param B a background knowledge
     * @return a diagnosis.
     */
    private Set<Constraint> dd(Set<Constraint> D, Set<Constraint> C, Set<Constraint> B) {
        log.debug("{}DD [D={}, C={}, B={}] >>>", LoggerUtils.tab(), D, C, B);
        LoggerUtils.indent();

        // if D != Φ and consistent(B U C) return C;
        if ( !D.isEmpty() ) {
            Set<Constraint> BwithC = Sets.union(B, C); incrementCounter(COUNTER_UNION_OPERATOR);

            incrementCounter(COUNTER_CONSISTENCY_CHECKS);
            if (checker.isConsistent(BwithC)) {
                LoggerUtils.outdent();
                log.debug("{}<<< return Φ", LoggerUtils.tab());

                return Collections.emptySet();
            }
        }

        // if singleton(C) return C;
        int n = C.size();
        if (n == 1) {
            LoggerUtils.outdent();
            log.debug("{}<<< return [{}]", LoggerUtils.tab(), C);

            return C;
        }

        // C1 = {c1..ck}; C2 = {ck+1..cn};
        Set<Constraint> C1 = new LinkedHashSet<>();
        Set<Constraint> C2 = new LinkedHashSet<>();
        split(C, C1, C2);
        log.trace("{}Split C into [C1={}, C2={}]", LoggerUtils.tab(), C1, C2);

        // Δ1 = DD(C1, C1, B);
        incrementCounter(COUNTER_LEFT_BRANCH_CALLS);
        incrementCounter(COUNTER_DIRECTDIAG_CALLS);
        Set<Constraint> Δ1 = dd(C1, C1, B);

        // Δ2 = DD(Δ1, C2, B U C1 - Δ1);
        Set<Constraint> BwithC1 = Sets.union(B, C1); incrementCounter(COUNTER_UNION_OPERATOR);
        Set<Constraint> BwithC1withoutΔ1 = Sets.difference(BwithC1, Δ1); incrementCounter(COUNTER_DIFFERENT_OPERATOR);
        incrementCounter(COUNTER_RIGHT_BRANCH_CALLS);
        incrementCounter(COUNTER_DIRECTDIAG_CALLS);
        Set<Constraint> Δ2 = dd(Δ1, C2, BwithC1withoutΔ1);

        LoggerUtils.outdent();
        log.debug("{}<<< return [Δ1={} ∪ Δ2={}]", LoggerUtils.tab(), Δ1, Δ2);

        // return Δ1 ∪ Δ2;
        incrementCounter(COUNTER_UNION_OPERATOR);
        return Sets.union(Δ1, Δ2);
    }
}