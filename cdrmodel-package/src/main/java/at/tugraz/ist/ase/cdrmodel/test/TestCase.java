/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cdrmodel.test;

import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.kb.core.Assignment;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;

/**
 * Represents a test case.
 */
@Slf4j
@Getter @Setter
public class TestCase implements ITestCase {
    private String testcase; // a test case
    private List<Assignment> assignments; // the list of assignments
    private List<Constraint> chocoConstraints; // the list of Choco constraints which are translated from this test case
    private List<Constraint> negChocoConstraints; // a list of NEGATIVE Choco constraints

    private boolean isViolated; // represents the violation of this test case with the knowledge base

    @Builder
    public TestCase(@NonNull String testcase, @NonNull List<Assignment> assignments) {
        this.testcase = testcase;
        this.assignments = assignments;
        this.chocoConstraints = new LinkedList<>();
        this.negChocoConstraints = new LinkedList<>();
        this.isViolated = false;
    }

    // TODO: integrate the aggregated test cases

    /**
     * Adds a set of Choco constraints to the {@link TestCase} object.
     * @param startIdx the index of the first constraint.
     * @param endIdx the index of the last constraint.
     */
    @Deprecated
    public void addChocoConstraints(@NonNull Model model, int startIdx, int endIdx, boolean hasNegativeConstraints) {
        Constraint[] constraints = model.getCstrs();

        checkElementIndex(startIdx, constraints.length, "startIdx must be within the range of constraints");
        checkElementIndex(endIdx, constraints.length, "endIdx must be within the range of constraints");
        checkArgument(startIdx <= endIdx, "startIdx must be <= endIdx");

//        int index = startIdx;
//        while (index <= endIdx) {
//            addChocoConstraint(constraints[index]);
//            index++;
//        }
        if (hasNegativeConstraints) {
            endIdx = endIdx - 2;
        } else {
            endIdx = endIdx - 1;
        }

        int index = startIdx;
        while (index <= endIdx) {
            addChocoConstraint(constraints[index]);
            if (hasNegativeConstraints) {
                addNegChocoConstraint(constraints[index]);
            }
            index++;
        }

        addChocoConstraint(constraints[index]);
        if (hasNegativeConstraints) {
            addNegChocoConstraint(constraints[index + 1]);
        }
    }

    /**
     * Adds a Choco constraint translated from this test case.
     * @param constraint a Choco constraint
     */
    public void addChocoConstraint(@NonNull Constraint constraint) {
//        if (chocoConstraints == null) {
//            chocoConstraints = new LinkedList<>();
//        }
        chocoConstraints.add(constraint);

        log.trace("{}Added a Choco constraint to TestCase [choco_cstr={}, testcase={}]", LoggerUtils.tab(), constraint, this);
    }

    /**
     * Adds a negative Choco constraint
     * @param neg_constraint a Choco constraint
     */
    public void addNegChocoConstraint(@NonNull Constraint neg_constraint) {
//        if (negChocoConstraints == null) {
//            negChocoConstraints = new LinkedList<>();
//        }
        negChocoConstraints.add(neg_constraint);

        log.trace("{}Added a negative Choco constraint to TestCase [choco_cstr={}, testcase={}]", LoggerUtils.tab(), neg_constraint, this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestCase that)) return false;
        return Objects.equals(testcase, that.testcase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testcase);
    }

    @Override
    public String toString() {
        return testcase;
    }

    public Object clone() throws CloneNotSupportedException {
        TestCase clone = (TestCase) super.clone();
        // copy assignments, constraints and negConstraints
        List<Assignment> assignments = new LinkedList<>();
        for (Assignment assignment : this.assignments) {
            Assignment cloneAssignment = (Assignment) assignment.clone();
            assignments.add(cloneAssignment);
        }
        clone.setAssignments(assignments);

        clone.chocoConstraints = new LinkedList<>();
        clone.negChocoConstraints = new LinkedList<>();

        // should not clone chocoConstraints and negChocoConstraints
        // should add new generated chocoConstraints and negChocoConstraints
//            List<Constraint> constraints = null;
//            if (this.chocoConstraints != null) {
//                constraints = new ArrayList<>(this.chocoConstraints);
//            }
//            List<Constraint> negConstraints = null;
//            if (this.negChocoConstraints != null) {
//                negConstraints = new ArrayList<>(this.negChocoConstraints);
//            }

//            clone.setChocoConstraints(constraints);
//            clone.setNegChocoConstraints(negConstraints);
        return clone;
    }

    public void dispose() {
        if (chocoConstraints != null) {
            chocoConstraints.clear();
        }
        if (negChocoConstraints != null) {
            negChocoConstraints.clear();
        }
        assignments = null;
    }
}