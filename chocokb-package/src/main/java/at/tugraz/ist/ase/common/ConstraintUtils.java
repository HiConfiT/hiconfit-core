/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.common;

import at.tugraz.ist.ase.kb.core.Constraint;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static at.tugraz.ist.ase.eval.PerformanceEvaluator.incrementCounter;

@UtilityClass
@Slf4j
public final class ConstraintUtils {

    // for evaluation
    public final String COUNTER_UNPOST_CONSTRAINT = "The number of unpost constraints:";
    public final String COUNTER_POST_CONSTRAINT = "The number of post constraints:";
    public final String COUNTER_CONSTAINS_CONSTRAINT = "The number of contains calls:";
    public final String COUNTER_SPLIT_SET = "The number of split set:";

    public String convertToString(@NonNull Set<Constraint> ac) {
        return ac.stream().map(Constraint::toString).collect(Collectors.joining("\n"));
    }

    public String convertToStringWithMessage(@NonNull List<Set<Constraint>> allDiag, @NonNull String mess) {
        if (allDiag.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (Set<Constraint> diag : allDiag) {
            count++;

            sb.append(mess).append(" ").append(count).append(":\n");
            sb.append(convertToString(diag)).append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public void postConstraints(Collection<Constraint> C, Model toModel) {
        C.forEach(c -> {
            c.getChocoConstraints().forEach(toModel::post);
            incrementCounter(COUNTER_POST_CONSTRAINT, c.getChocoConstraints().size());
        });
        log.trace("{}Posted constraints", LoggerUtils.tab);
    }

    public void postConstraint(Constraint cstr, Model toModel, boolean negative) {
        if (negative) {
            cstr.getNegChocoConstraints().forEach(toModel::post);
            incrementCounter(COUNTER_POST_CONSTRAINT, cstr.getNegChocoConstraints().size());
        } else {
            cstr.getChocoConstraints().forEach(toModel::post);
            incrementCounter(COUNTER_POST_CONSTRAINT, cstr.getChocoConstraints().size());
        }
        log.trace("{}Posted constraints", LoggerUtils.tab);
    }

    /**
     * Split a set of {@link Constraint}s into two sets
     *
     * @param C an input set of {@link Constraint}s
     * @param C1 the first output set - needs to be initialized
     * @param C2 the second output set - needs to be initialized
     */
    public void split(Set<Constraint> C, Set<Constraint> C1, Set<Constraint> C2) {
        int k = C.size() / 2; // k = sizeC/2;
        // C1 = {c1..ck}; C2 = {ck+1..cn};
        List<Constraint> firstSubList = new ArrayList<>(C).subList(0, k);
        List<Constraint> secondSubList = new ArrayList<>(C).subList(k, C.size());

        C1.addAll(firstSubList);
        C2.addAll(secondSubList);

        incrementCounter(COUNTER_SPLIT_SET);
    }

    public boolean isMinimal(Set<Constraint> diag, List<Set<Constraint>> allDiag) {
        return allDiag.parallelStream().noneMatch(diag::containsAll);
    }

    public boolean containsAll(List<Set<Constraint>> allDiag, Set<Constraint> diag) {
        return allDiag.parallelStream().anyMatch(adiag -> adiag.containsAll(diag));
    }

    public boolean hasIntersection(Collection<Constraint> col1, Collection<Constraint> col2) {
        return col1.parallelStream().anyMatch(col2::contains);
        /*
        for (Constraint c : col1) {
            if (col2.contains(c)) return true;
        }
        return false;
        */
    }
}
