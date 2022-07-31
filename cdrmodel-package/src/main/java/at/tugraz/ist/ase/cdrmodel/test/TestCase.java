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
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.constraints.Constraint;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a test case.
 */
@Slf4j
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TestCase implements ITestCase {
    @EqualsAndHashCode.Include
    private final String testcase; // a test case
    private List<Assignment> assignments; // the list of assignments
    private List<Constraint> chocoConstraints; // the list of Choco constraints which are translated from this test case
    private List<Constraint> negChocoConstraints; // a list of NEGATIVE Choco constraints

    @Setter
    private boolean isViolated; // represents the violation of this test case with the knowledge base

    @Builder
    public TestCase(@NonNull String testcase, @NonNull List<Assignment> assignments) {
        this.testcase = testcase;
        this.assignments = assignments;
        this.chocoConstraints = new LinkedList<>();
        this.negChocoConstraints = new LinkedList<>();
        this.isViolated = false;

        log.trace("{}Created TestCase [testcase={}]", LoggerUtils.tab(), testcase);
    }

    // TODO: integrate the aggregated test cases

    /**
     * Adds a Choco constraint translated from this test case.
     * @param constraint a Choco constraint
     */
    public void addChocoConstraint(@NonNull Constraint constraint) {
        chocoConstraints.add(constraint);

        log.trace("{}Added a Choco constraint to TestCase [choco_cstr={}, testcase={}]", LoggerUtils.tab(), constraint, this);
    }

    /**
     * Adds a negative Choco constraint
     * @param neg_constraint a Choco constraint
     */
    public void addNegChocoConstraint(@NonNull Constraint neg_constraint) {
        negChocoConstraints.add(neg_constraint);

        log.trace("{}Added a negative Choco constraint to TestCase [choco_cstr={}, testcase={}]", LoggerUtils.tab(), neg_constraint, this);
    }

    @Override
    public String toString() {
        return testcase;
    }

    public Object clone() throws CloneNotSupportedException {
        TestCase clone = (TestCase) super.clone();
        // copy assignments
        List<Assignment> assignments = new LinkedList<>();
        for (Assignment assignment : this.assignments) {
            Assignment cloneAssignment = (Assignment) assignment.clone();
            assignments.add(cloneAssignment);
        }
        clone.assignments = assignments;

        // should not clone chocoConstraints and negChocoConstraints
        // should add new generated chocoConstraints and negChocoConstraints
        // it means that should re-call initialize() of the CDRModel
        clone.chocoConstraints = new LinkedList<>();
        clone.negChocoConstraints = new LinkedList<>();

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
