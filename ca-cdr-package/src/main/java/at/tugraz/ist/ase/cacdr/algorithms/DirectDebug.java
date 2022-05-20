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
import at.tugraz.ist.ase.test.ITestCase;
import at.tugraz.ist.ase.kb.core.Constraint;
import com.google.common.collect.Sets;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static at.tugraz.ist.ase.cacdr.eval.CAEvaluator.*;
import static at.tugraz.ist.ase.common.ConstraintUtils.*;

/**
 * Implementation of DirectDebug.
 * The algorithm determines a maximal satisfiable subset MSS (Γ) of C U B U TC.
 *
 * <ul>
 *     <li>Viet-Man Le et al., 2021. DirectDebug: Automated Testing and Debugging of Feature Models.
 *     In Proceedings of the 43rd International Conference on Software Engineering: New Ideas and Emerging Results
 *     (ICSE-NIER 2021). (to appear)</li>
 * </ul>
 *
 * The original algorithm:
 * // Func DirectDebug(δ, C = {c1..cn}, B, Tπ) : Γ
 * // T'π <- Tπ
 * // if δ != Φ and IsConsistent(B U C, Tπ, T'π) return C;
 * // if singleton(C) return Φ;
 * // k = n/2;
 * // C1 = {c1..ck}; C2 = {ck+1..cn};
 * // Γ2 = DirectDebug(δ=C1, C1, B, T'π);
 * // Γ1 = DirectDebug(δ=C1-Γ2, C2, B U Γ2, T'π);
 * // return Γ1 ∪ Γ2;
 *
 * There are two changes in the current version compared to the original algorithm in the paper:
 * // Γ1 = DirectDebug(δ=C1, C1, B, T'π);
 * // Γ2 = DirectDebug(δ=C1-Γ1, C2, B U Γ1, T'π);
 * These changes help to identify diagnoses in the expected order when combining with the HSDAG or HStree algorithms
 * - satisfying the total order of constraints.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Slf4j
public class DirectDebug {

    // for evaluation
    public static final String TIMER_DIRECTDEBUG = "Timer for DirectDebug";
    public static final String COUNTER_DIRECTDEBUG_CALLS = "The number of DirectDebug calls";

    protected final ChocoConsistencyChecker checker;

    /**
     * A constructor with a checker of {@link ChocoConsistencyChecker}.
     * @param checker a checker of {@link ChocoConsistencyChecker}
     */
    public DirectDebug(@NonNull ChocoConsistencyChecker checker) {
        this.checker = checker;
    }

    /**
     * This function will activate DirectDebug algorithm if there exists at least one positive test case,
     * which induces an inconsistency in C U B. Otherwise, it returns an empty set.
     *
     * Note: negative test cases in TΘ should be added (in negated form) to B before calling this function.
     *
     * @param C a consideration set of constraints
     * @param B a background knowledge
     * @param TC a set of test cases
     * @return a diagnosis or an empty set
     */
    public Set<Constraint> findDiagnosis(@NonNull Set<Constraint> C, @NonNull Set<Constraint> B, @NonNull Set<ITestCase> TC)
    {
        log.debug("{}Identifying diagnosis for [C={}, B={}, TC={}] >>>", LoggerUtils.tab(), C, B, TC);
        LoggerUtils.indent();

        Set<Constraint> BwithC = Sets.union(B, C); incrementCounter(COUNTER_UNION_OPERATOR);

        // if isEmpty(C) or consistent(B U C) return Φ
        Set<ITestCase> TCp = checker.isConsistent(BwithC, TC, false);
        if (C.isEmpty() || TCp.isEmpty()) {

            LoggerUtils.outdent();
            log.debug("{}<<< No diagnosis found", LoggerUtils.tab());

            return Collections.emptySet();
        } else{ // else return C \ directDebug(Φ, C, B, T'π)
            incrementCounter(COUNTER_DIRECTDEBUG_CALLS);
            start(TIMER_DIRECTDEBUG);
            Set<Constraint> mss = directDebug(Collections.emptySet(), C, B, TCp);
            stop(TIMER_DIRECTDEBUG);

            incrementCounter(COUNTER_DIFFERENT_OPERATOR);
            Set<Constraint> diag = Sets.difference(C, mss);

            LoggerUtils.outdent();
            log.debug("{}<<< Found diagnosis [diag={}]", LoggerUtils.tab(), diag);

            return diag;
        }
    }

    /**
     * The implementation of DirectDebug algorithm.
     * The algorithm determines a maximal satisfiable subset MSS (Γ) of C U B U TC.
     *
     * // Func DirectDebug(δ, C = {c1..cn}, B, Tπ) : Γ
     * // T'π <- Tπ
     * // if δ != Φ and IsConsistent(B U C, Tπ, T'π) return C;
     * // if singleton(C) return Φ;
     * // k = n/2;
     * // C1 = {c1..ck}; C2 = {ck+1..cn};
     * // Γ1 = DirectDebug(δ=C1, C1, B, T'π);
     * // Γ2 = DirectDebug(δ=C1-Γ1, C2, B U Γ1, T'π);
     * // return Γ1 ∪ Γ2;
     *
     * @param δ check to skip redundant consistency checks
     * @param C a consideration set of constraints
     * @param B a background knowledge
     * @param TC a set of test cases which induce an inconsistency in C U B
     * @return a maximal satisfiable subset MSS of C U B U TC.
     */
    private Set<Constraint> directDebug(Set<Constraint> δ, Set<Constraint> C, Set<Constraint> B, Set<ITestCase> TC) {
        log.trace("{}directDebug [δ={}, C={}, B={}, TC{}] >>>", LoggerUtils.tab(), δ, C, B, TC);
        LoggerUtils.indent();

        // T'π <- Tπ
        Set<ITestCase> TCp = new LinkedHashSet<>(TC);

        // if δ != Φ and IsConsistent(B U C, Tπ, T'π) return C;
        if ( !δ.isEmpty() ) {
            Set<Constraint> BwithC = Sets.union(B, C); incrementCounter(COUNTER_UNION_OPERATOR);

            incrementCounter(COUNTER_CONSISTENCY_CHECKS);
            TCp = checker.isConsistent(BwithC, TC, false);
            if (TCp.isEmpty()) {
                LoggerUtils.outdent();
                log.trace("{}<<< return [{}]", LoggerUtils.tab(), C);

                return C;
            }
        }

        // if singleton(C) return Φ;
        int n = C.size();
        if (n == 1) {
            LoggerUtils.outdent();
            log.trace("{}<<< return Φ", LoggerUtils.tab());

            return Collections.emptySet();
        }

        // C1 = {c1..ck}; C2 = {ck+1..cn};
        Set<Constraint> C1 = new LinkedHashSet<>();
        Set<Constraint> C2 = new LinkedHashSet<>();
        split(C, C1, C2);
        log.trace("{}Split C into [C1={}, C2={}]", LoggerUtils.tab(), C1, C2);

        // Γ1 = DirectDebug(δ=C1, C1, B, T'π);
        incrementCounter(COUNTER_LEFT_BRANCH_CALLS);
        incrementCounter(COUNTER_DIRECTDEBUG_CALLS);
        Set<Constraint> Γ1 = directDebug(C1, C1, B, TCp);

        // Γ2 = DirectDebug(δ=C1-Γ1, C2, B U Γ1, T'π);
        Set<Constraint> BwithΓ1 = Sets.union(Γ1, B); incrementCounter(COUNTER_UNION_OPERATOR);
        Set<Constraint> C1minusΓ1 = Sets.difference(C1, Γ1); incrementCounter(COUNTER_DIFFERENT_OPERATOR);
        incrementCounter(COUNTER_RIGHT_BRANCH_CALLS);
        incrementCounter(COUNTER_DIRECTDEBUG_CALLS);
        Set<Constraint> Γ2 = directDebug(C1minusΓ1, C2, BwithΓ1, TCp);

        LoggerUtils.outdent();
        log.trace("{}<<< return [Γ1={} ∪ Γ2={}]", LoggerUtils.tab(), Γ1, Γ2);

        // return Γ1 ∪ Γ2;
        incrementCounter(COUNTER_UNION_OPERATOR);
        return Sets.union(Γ1, Γ2);
    }
}
