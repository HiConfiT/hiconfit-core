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
 * Implementation of an MSS-based FastDiag algorithm.
 * MSS - Maximal Satisfiable Set
 * <p>
 * // MSS-based FastDiag Algorithm
 * //--------------------
 * // B: correctConstraints (background knowledge)
 * // C: possiblyFaultyConstraints
 * //--------------------
 * // Func FastDiag(C, B) : Δ
 * // if isEmpty(C) or consistent(B U C) return Φ
 * // else return C \ FD(Φ, C, B)
 * <p>
 * // Func FD(Δ, C = {c1..cn}, B) : MSS
 * // if Δ != Φ and consistent(B U C) return C;
 * // if singleton(C) return Φ;
 * // k = n/2;
 * // C1 = {c1..ck}; C2 = {ck+1..cn};
 * // Δ1 = FD(C2, C1, B);
 * // Δ2 = FD(C1 - Δ1, C2, B U Δ1);
 * // return Δ1 ∪ Δ2;
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Slf4j
public class FastDiagV3 extends IConsistencyAlgorithm {

    // for evaluation
    public static final String TIMER_FASTDIAGV3 = "Timer for FD V3";
    public static final String COUNTER_FASTDIAGV3_CALLS = "The number of FD V3 calls";

    public FastDiagV3(@NonNull ChocoConsistencyChecker checker) {
        super(checker);
    }

    /**
     * This function will activate FastDiag algorithm if there exists at least one constraint,
     * which induces an inconsistency in B. Otherwise, it returns an empty set.
     * <p>
     * // Func FastDiag(C, B) : Δ
     * // if isEmpty(C) or consistent(B U C) return Φ
     * // else return C \ FD(C, B, Φ)
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
        } else { // else return C \ FD(C, B, Φ)
            incrementCounter(COUNTER_FASTDIAGV3_CALLS);
            start(TIMER_FASTDIAGV3);
            Set<Constraint> mss = fd(Collections.emptySet(), C, B);
            stop(TIMER_FASTDIAGV3);

            incrementCounter(COUNTER_DIFFERENT_OPERATOR);
            Set<Constraint> diag = Sets.difference(C, mss);

            LoggerUtils.outdent();
            log.debug("{}<<< Found diagnosis [diag={}]", LoggerUtils.tab(), diag);

            return diag;
        }
    }

    /**
     * The implementation of MSS-based FastDiag algorithm.
     * The algorithm determines a maximal satisfiable subset MSS (Γ) of C U B.
     * <p>
     * // Func FD(Δ, C = {c1..cn}, B) : MSS
     * // if Δ != Φ and consistent(B U C) return C;
     * // if singleton(C) return Φ;
     * // k = n/2;
     * // C1 = {c1..ck}; C2 = {ck+1..cn};
     * // Δ1 = FD(C2, C1, B);
     * // Δ2 = FD(C1 - Δ1, C2, B U Δ1);
     * // return Δ1 ∪ Δ2;
     *
     * @param Δ check to skip redundant consistency checks
     * @param C a consideration set of constraints
     * @param B a background knowledge
     * @return a maximal satisfiable subset MSS of C U B.
     */
    private Set<Constraint> fd(Set<Constraint> Δ, Set<Constraint> C, Set<Constraint> B) {
        log.debug("{}FD [Δ={}, C={}, B={}] >>>", LoggerUtils.tab(), Δ, C, B);
        LoggerUtils.indent();

        // if Δ != Φ and consistent(B U C) return C;
        if ( !Δ.isEmpty() ) {
            Set<Constraint> BwithC = Sets.union(B, C); incrementCounter(COUNTER_UNION_OPERATOR);

            incrementCounter(COUNTER_CONSISTENCY_CHECKS);
            if (checker.isConsistent(BwithC)) {
                LoggerUtils.outdent();
                log.debug("{}<<< return [{}]", LoggerUtils.tab(), C);

                return C;
            }
        }

        // if singleton(C) return Φ;
        int n = C.size();
        if (n == 1) {
            LoggerUtils.outdent();
            log.debug("{}<<< return Φ", LoggerUtils.tab());

            return Collections.emptySet();
        }

        // C1 = {c1..ck}; C2 = {ck+1..cn};
        Set<Constraint> C1 = new LinkedHashSet<>();
        Set<Constraint> C2 = new LinkedHashSet<>();
        split(C, C1, C2);
        log.trace("{}Split C into [C1={}, C2={}]", LoggerUtils.tab(), C1, C2);

        // Δ1 = FD(C2, C1, B);
        incrementCounter(COUNTER_LEFT_BRANCH_CALLS);
        incrementCounter(COUNTER_FASTDIAGV3_CALLS);
        Set<Constraint> Δ1 = fd(C2, C1, B);

        // Δ2 = FD(C1 - Δ1, C2, B U Δ1);
        Set<Constraint> BwithΔ1 = Sets.union(B, Δ1); incrementCounter(COUNTER_UNION_OPERATOR);
        Set<Constraint> C1withoutΔ1 = Sets.difference(C1, Δ1); incrementCounter(COUNTER_DIFFERENT_OPERATOR);
        incrementCounter(COUNTER_RIGHT_BRANCH_CALLS);
        incrementCounter(COUNTER_FASTDIAGV3_CALLS);
        Set<Constraint> Δ2 = fd(C1withoutΔ1, C2, BwithΔ1);

        LoggerUtils.outdent();
        log.debug("{}<<< return [Δ1={} ∪ Δ2={}]", LoggerUtils.tab(), Δ1, Δ2);

        // return Δ1 ∪ Δ2;
        incrementCounter(COUNTER_UNION_OPERATOR);
        return Sets.union(Δ1, Δ2);
    }
}