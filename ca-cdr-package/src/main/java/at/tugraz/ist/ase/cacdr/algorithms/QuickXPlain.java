/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
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
import static at.tugraz.ist.ase.common.IOUtils.*;

/**
 * Implementation of QuickXplain algorithm using Set structures.
 *
 * <ul>
 *     <li>"Ulrich Junker"</li>
 * </ul>
 * //QuickXPlain Algorithm
 * //--------------------
 * //QuickXPlain(C={c1,c2,…, cm}, B): CS
 * //IF consistent(B∪C) return "No conflict";
 * //IF isEmpty(C) return Φ;
 * //ELSE return QX(Φ, C, B);
 *
 * //func QX(Δ, C={c1,c2, …, cq}, B): CS
 * //IF (Δ != Φ AND inconsistent(B)) return Φ;
 * //IF singleton(C) return C;
 * //k=n/2;
 * //C1 <-- {c1, …, ck}; C2 <-- {ck+1, …, cq};
 * //CS1 <-- QX(C2, C1, B ∪ C2);
 * //CS2 <-- QX(CS1, C2, B ∪ CS1);
 * //return (CS1 ∪ CS2)
 *
 * #08.2020 - Viet-Man Le: using Set structures to store constraints instead of List
 *
 * @author Muslum Atas (muesluem.atas@ist.tugraz.at)
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Slf4j
public class QuickXPlain extends IConsistencyAlgorithm {

    // for evaluation
    public static final String TIMER_QUICKXPLAIN = "Timer for QX ";
    public static final String COUNTER_QUICKXPLAIN_CALLS = "The number of QX calls:";

    public QuickXPlain(@NonNull ChocoConsistencyChecker checker) {
        super(checker);
    }

    /**
     * //QuickXPlain(C={c1,c2,…, cm}, B): CS
     * //IF consistent(B∪C) return "No conflict";
     * //IF isEmpty(C) return Φ;
     * //ELSE return QX(Φ, C, B);
     *
     * @param C a consideration set. Need to inverse the order of the possibly faulty constraint set.
     * @param B a background knowledge
     * @return a conflict set or an empty set
     */
    public Set<Constraint> findConflictSet(@NonNull Set<Constraint> C, @NonNull Set<Constraint> B) {
        log.debug("{}Identifying conflict for [C={}, B={}] >>>", LoggerUtils.tab(), C, B);
        LoggerUtils.indent();

        Set<Constraint> BwithC = Sets.union(B, C); incrementCounter(COUNTER_UNION_OPERATOR);

        //IF (is empty(C) or consistent(B ∪ C)) return Φ
        if (C.isEmpty() || checker.isConsistent(BwithC)) {

            LoggerUtils.outdent();
            log.debug("{}<<< No conflict found", LoggerUtils.tab());

            return Collections.emptySet();
        } else { //ELSE return QX(Φ, C, B)
            incrementCounter(COUNTER_QUICKXPLAIN_CALLS);
            start(TIMER_QUICKXPLAIN + getThreadString() + ": ");
            Set<Constraint> cs = qx(Collections.emptySet(), C, B);
            stop(TIMER_QUICKXPLAIN + getThreadString() + ": ");

            LoggerUtils.outdent();
            log.debug("{}<<< Found conflict [conflict={}]", LoggerUtils.tab(), cs);

            return cs;
        }
    }

    /**
     * //func QX(Δ, C={c1,c2, …, cq}, B): CS
     * //IF (Δ != Φ AND inconsistent(B)) return Φ;
     * //IF singleton(C) return C;
     * //k = q/2;
     * //C1 <-- {c1, …, ck}; C2 <-- {ck+1, …, cq};
     * //CS1 <-- QX(C2, C1, B ∪ C2);
     * //CS2 <-- QX(CS1, C2, B ∪ CS1);
     * //return (CS1 ∪ CS2)
     *
     * @param D check to skip redundant consistency checks
     * @param C a consideration set of constraints
     * @param B a background knowledge
     * @return a conflict set or an empty set
     */
    private Set<Constraint> qx(Set<Constraint> D, Set<Constraint> C, Set<Constraint> B) {
        log.trace("{}QX [D={}, C={}, B={}] >>>", LoggerUtils.tab(), D, C, B);
        LoggerUtils.indent();

        //IF (Δ != Φ AND inconsistent(B)) return Φ;
        if ( !D.isEmpty() ) {
            incrementCounter(COUNTER_CONSISTENCY_CHECKS);
            if (!checker.isConsistent(B)) {
                LoggerUtils.outdent();
                log.trace("{}<<< return Φ", LoggerUtils.tab());

                return Collections.emptySet();
            }
        }

        // if singleton(C) return C;
        int q = C.size();
        if (q == 1) {
            LoggerUtils.outdent();
            log.trace("{}<<< return [{}]", LoggerUtils.tab(), C);

            return C;
        }

        // C1 = {c1..ck}; C2 = {ck+1..cq};
        Set<Constraint> C1 = new LinkedHashSet<>();
        Set<Constraint> C2 = new LinkedHashSet<>();
        split(C, C1, C2);
        log.trace("{}Split C into [C1={}, C2={}]", LoggerUtils.tab(), C1, C2);

        // CS1 <-- QX(C2, C1, B ∪ C2);
        Set<Constraint> BwithC2 = Sets.union(B, C2); incrementCounter(COUNTER_UNION_OPERATOR);
        incrementCounter(COUNTER_LEFT_BRANCH_CALLS);
        incrementCounter(COUNTER_QUICKXPLAIN_CALLS);
        Set<Constraint> CS1 = qx(C2, C1, BwithC2);

        // CS2 <-- QX(CS1, C2, B ∪ CS1);
        Set<Constraint> BwithCS1 = Sets.union(B, CS1); incrementCounter(COUNTER_UNION_OPERATOR);
        incrementCounter(COUNTER_RIGHT_BRANCH_CALLS);
        incrementCounter(COUNTER_QUICKXPLAIN_CALLS);
        Set<Constraint> CS2 = qx(CS1, C2, BwithCS1);

        LoggerUtils.outdent();
        log.trace("{}<<< return [CS1={} ∪ CS2={}]", LoggerUtils.tab(), CS1, CS2);

        //return (CS1 ∪ CS2)
        incrementCounter(COUNTER_UNION_OPERATOR);
        return Sets.union(CS1, CS2);
    }
}
