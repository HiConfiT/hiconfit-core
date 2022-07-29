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
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;

@UtilityClass
@Slf4j
public final class ConstraintUtils {

    // for evaluation
    public final String COUNTER_UNPOST_CONSTRAINT = "The number of unpost constraints";
    public final String COUNTER_POST_CONSTRAINT = "The number of post constraints";
    public final String COUNTER_CONSTAINS_CONSTRAINT = "The number of contains calls";
    public final String COUNTER_SPLIT_SET = "The number of split set";

    public String convertToString(@NonNull Set<Constraint> ac, @NonNull String delimiter, boolean brackets) {
        String ex = ac.stream().map(Constraint::toString).collect(Collectors.joining(delimiter));
        return brackets ? "[" + ex + "]" : ex;
    }

    public String convertToString(@NonNull Set<Constraint> ac) {
        return convertToString(ac, "\n", false);
//        return ac.stream().map(Constraint::toString).collect(Collectors.joining("\n"));
    }

    public String convertToStringWithMessage(@NonNull List<Set<Constraint>> allDiag, @NonNull String mess, String tabs, @NonNull String delimiter, boolean brackets) {
        if (allDiag.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (Set<Constraint> diag : allDiag) {
            count++;

            if (tabs != null) {
                sb.append(tabs);
            }

            sb.append(mess).append(" ").append(count).append(": ");

            if (delimiter.equals("\n")) {
                delimiter = delimiter + (tabs != null ? tabs : "");
                sb.append(delimiter);
            }
            sb.append(convertToString(diag, delimiter, brackets)).append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public String convertToStringWithMessage(@NonNull List<Set<Constraint>> allDiag, @NonNull String mess) {
        return convertToStringWithMessage(allDiag, mess, null, "\n", false);
    }

    /**
     * Add Choco constraints from the model to the corresponding list of Choco constraints in a {@link Constraint} object.
     * The function is used when Choco constraints are generated for a feature model.
     * It means that the Choco model uses LogOp and BoolVar.
     * @param negative if true, add constraints to the list negChocoConstraints, otherwise add constraints to the list chocoConstraints.
     * @param constraint a {@link Constraint} object
     * @param model a Choco model
     * @param startIdx the index of the first constraint to copy from the model
     * @param endIdx the index of the last constraint to copy from the model
     */
    public void addChocoConstraintsToConstraint(boolean negative, @NonNull Constraint constraint,
                                                @NonNull Model model, int startIdx, int endIdx) {
//        org.chocosolver.solver.constraints.Constraint[] cstrs = model.getCstrs();
//
//        checkElementIndex(startIdx, cstrs.length, "startIdx must be within the range of constraints");
//        checkElementIndex(endIdx, cstrs.length, "endIdx must be within the range of constraints");
//        checkArgument(startIdx <= endIdx, "startIdx must be <= endIdx");
//
//        int index = startIdx;
//        while (index <= endIdx) {
//            if (negative) {
//                constraint.addNegChocoConstraint(cstrs[index]);
//            } else {
//                constraint.addChocoConstraint(cstrs[index]);
//            }
//
//            index++;
//        }
        List<org.chocosolver.solver.constraints.Constraint> cstrs = ChocoSolverUtils.getConstraints(model, startIdx, endIdx);

        if (negative) {
            cstrs.forEach(constraint::addNegChocoConstraint);
        } else {
            cstrs.forEach(constraint::addChocoConstraint);
        }
    }

    /**
     * Add Choco constraints from the model to a {@link Constraint} object.
     * The function is used when Choco constraints are generated for an integer knowledge base.
     * It means that the Choco model uses IntVar and Arithmetic constraints.
     * @param constraint a {@link Constraint} object
     * @param model a Choco model
     * @param startIdx the index of the first constraint to copy from the model
     * @param endIdx the index of the last constraint to copy from the model
     * @param hasNegativeConstraints if true, there are negative constraints in the Choco constraints
     */
    public void addChocoConstraintsToConstraint(@NonNull Constraint constraint, @NonNull Model model,
                                                int startIdx, int endIdx, boolean hasNegativeConstraints) {
        org.chocosolver.solver.constraints.Constraint[] constraints = model.getCstrs();

        checkElementIndex(startIdx, constraints.length, "startIdx must be within the range of constraints");
        checkElementIndex(endIdx, constraints.length, "endIdx must be within the range of constraints");
        checkArgument(startIdx <= endIdx, "startIdx must be <= endIdx");

        // in case of hasNegativeConstraints is true
        // after posting the negChocoConstraint
        // Ex: there are five constraints in the modelKB: c1, c2, c3, c4, c5
        // c1, c2, c3, c4 will be added to the list of chocoConstraints in the Constraint object
        // c1, c2, c3, c5 will be added to the list of negChocoConstraints in the Constraint object

        if (hasNegativeConstraints) {
            endIdx = endIdx - 2;
        } else {
            endIdx = endIdx - 1;
        }

        // add c1, c2, c3 to two lists in the Constraint object
        if (startIdx <= endIdx) {
            List<org.chocosolver.solver.constraints.Constraint> cstrs = ChocoSolverUtils.getConstraints(model, startIdx, endIdx);

            cstrs.forEach(cstr -> {
                constraint.addChocoConstraint(cstr);
                if (hasNegativeConstraints) {
                    constraint.addNegChocoConstraint(cstr);
                }
            });
        }

        // add c4 to the list of chocoConstraints in the Constraint object
        constraint.addChocoConstraint(constraints[endIdx + 1]);
        if (hasNegativeConstraints) { // add c5 to the list of negChocoConstraints in the Constraint object
            constraint.addNegChocoConstraint(constraints[endIdx + 2]);
        }

//        int index = startIdx;
//        while (index <= endIdx) {
//            constraint.addChocoConstraint(constraints[index]);
//            if (hasNegativeConstraints) {
//                constraint.addNegChocoConstraint(constraints[index]);
//            }
//            index++;
//        }
//
//        // add c4 to the list of chocoConstraints in the Constraint object
//        constraint.addChocoConstraint(constraints[index]);
//        if (hasNegativeConstraints) { // add c5 to the list of negChocoConstraints in the Constraint object
//            constraint.addNegChocoConstraint(constraints[index + 1]);
//        }
    }

    public void postConstraints(Collection<Constraint> C, Model toModel) {
        C.forEach(c -> {
            c.getChocoConstraints().forEach(toModel::post);
            incrementCounter(COUNTER_POST_CONSTRAINT, c.getChocoConstraints().size());
        });
        log.trace("{}Posted constraints", LoggerUtils.tab());
    }

    public void postConstraint(Constraint cstr, Model toModel, boolean negative) {
        if (negative) {
            cstr.getNegChocoConstraints().forEach(toModel::post);
            incrementCounter(COUNTER_POST_CONSTRAINT, cstr.getNegChocoConstraints().size());
        } else {
            cstr.getChocoConstraints().forEach(toModel::post);
            incrementCounter(COUNTER_POST_CONSTRAINT, cstr.getChocoConstraints().size());
        }
        log.trace("{}Posted constraints", LoggerUtils.tab());
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
