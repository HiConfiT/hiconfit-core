/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.kb.core;

import at.tugraz.ist.ase.common.LoggerUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.Model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;

@Getter
@Slf4j
public class Constraint {
    private final String constraint;
    private final List<org.chocosolver.solver.constraints.Constraint> chocoConstraints;
    private final List<org.chocosolver.solver.constraints.Constraint> negChocoConstraints;

    public Constraint(@NonNull String constraint) {
        this.constraint = constraint;
        chocoConstraints = new LinkedList<>();
        negChocoConstraints = new LinkedList<>();

        log.trace("{}Created Constraint [cstr={}]", LoggerUtils.tab(), constraint);
    }

    public void addChocoConstraints(@NonNull Model model, int startIdx, int endIdx, boolean hasNegativeConstraints) {
        org.chocosolver.solver.constraints.Constraint[] constraints = model.getCstrs();

        checkElementIndex(startIdx, constraints.length, "startIdx must be within the range of constraints");
        checkElementIndex(endIdx, constraints.length, "endIdx must be within the range of constraints");
        checkArgument(startIdx <= endIdx, "startIdx must be <= endIdx");

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

    public void addChocoConstraint(@NonNull org.chocosolver.solver.constraints.Constraint constraint) {
        chocoConstraints.add(constraint);

        log.trace("{}Added a Choco constraint to Constraint [choco_cstr={}, cstr={}]", LoggerUtils.tab(), constraint, this);
    }

    public void addNegChocoConstraint(@NonNull org.chocosolver.solver.constraints.Constraint constraint) {
        negChocoConstraints.add(constraint);

        log.trace("{}Added a negative Choco constraint to Constraint [choco_cstr={}, cstr={}]", LoggerUtils.tab(), constraint, this);
    }

    public boolean contains(@NonNull org.chocosolver.solver.constraints.Constraint constraint) {
        return chocoConstraints.contains(constraint);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Constraint that)) return false;
        return Objects.equals(constraint, that.constraint);
    }

    @Override
    public int hashCode() {
        // shouldn't add choco constraints to hashcode
        // since in a multi-thread scenario, a clone constraint will have different choco constraint objects
        return Objects.hash(constraint);
    }

    @Override
    public String toString() {
        return constraint;
    }
}
