/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.kb.core;

import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.constraints.Constraint;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;

@Slf4j
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Solution implements Cloneable {
    @EqualsAndHashCode.Include
    protected List<Assignment> assignments;
    private List<Constraint> chocoConstraints;
    private List<Constraint> negChocoConstraints;

    @Builder
    public Solution(@NonNull List<Assignment> assignments) {
        this.assignments = assignments;
        this.chocoConstraints = new LinkedList<>();
        this.negChocoConstraints = new LinkedList<>();

        log.trace("{}Created Solution [assignments={}]", LoggerUtils.tab(), assignments);
    }

    public Assignment getAssignment(int index) {
        checkElementIndex(index, assignments.size(), "Index out of bound!");

        return assignments.get(index);
    }

    public Assignment getAssignment(@NonNull String varName) {
        checkArgument(!varName.isEmpty(), "Variable name cannot be empty!");

        for (Assignment assignment : assignments) {
            if (assignment.getVariable().equals(varName)) {
                return assignment;
            }
        }
        throw new IllegalArgumentException("Variable '" + varName + "' doesn't exist!");
    }

    /**
     * Adds a Choco constraint translated from this solution.
     * @param constraint a Choco constraint
     */
    public void addChocoConstraint(@NonNull Constraint constraint) {
        chocoConstraints.add(constraint);

        log.trace("{}Added a Choco constraint to Solution [choco_cstr={}, solution={}]", LoggerUtils.tab(), constraint, this);
    }

    /**
     * Adds a negative Choco constraint
     * @param neg_constraint a Choco constraint
     */
    public void addNegChocoConstraint(@NonNull Constraint neg_constraint) {
        negChocoConstraints.add(neg_constraint);

        log.trace("{}Added a negative Choco constraint to Solution [choco_cstr={}, solution={}]", LoggerUtils.tab(), neg_constraint, this);
    }

    public int size() {
        return assignments.size();
    }

    public double compare1(Solution solution, int numNULL) {
        int counter = 0;
        for (Assignment assignment1 : assignments) {
            Assignment assignment2 = solution.getAssignment(assignment1.getVariable());

            if (assignment1.equals(assignment2)) {
//                System.out.println( (i+1) + ":\t" + assignment1.getValue() + "-" + assignment2.getValue());
                counter++;
            }
        }

        return (double)(counter - numNULL) / (size() - numNULL);
    }

    public double compare(Solution solution) {
        if (assignments.isEmpty() || this.size() != solution.size()) {
            return 0;
        }

        int counter = 0;
        for (int i = 0; i < assignments.size(); i++) {
            Assignment assignment1 = assignments.get(i);
            Assignment assignment2 = solution.getAssignment(i);

            if (!assignment1.equals(assignment2)) {
                System.out.println( (i+1) + ":\t" + assignment1.getValue() + "-" + assignment2.getValue());
                counter++;
            }
        }

        return (double)counter / size();
    }

    public int getNumNULL() {
        return (int) getAssignments().parallelStream().filter(assignment -> assignment.getValue().equals("NULL")).count();
    }

    @Override
    public String toString() {
        return assignments.stream().map(Assignment::toString).collect(Collectors.joining(", "));
    }

    public Object clone() throws CloneNotSupportedException {
        Solution clone = (Solution) super.clone();
        // copy assignments
        List<Assignment> assignments = new LinkedList<>();
        for (Assignment assignment : this.assignments) {
            Assignment cloneAssignment = (Assignment) assignment.clone();
            assignments.add(cloneAssignment);
        }
        clone.assignments = assignments;

        return clone;
    }

    public void dispose() {
        if (chocoConstraints != null) {
            chocoConstraints.clear();
            chocoConstraints = null;
        }
        if (negChocoConstraints != null) {
            negChocoConstraints.clear();
            negChocoConstraints = null;
        }
        assignments = null;
    }
}
