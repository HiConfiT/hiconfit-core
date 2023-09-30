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

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Constraint {
    @EqualsAndHashCode.Include
    private final String constraint;
    private final List<String> variables;

    private List<org.chocosolver.solver.constraints.Constraint> chocoConstraints;
    private List<org.chocosolver.solver.constraints.Constraint> negChocoConstraints;

    @Builder
    public Constraint(@NonNull String constraint, @NonNull List<String> variables) {
        this.constraint = constraint;
        this.variables = variables;

        chocoConstraints = new LinkedList<>();
        negChocoConstraints = new LinkedList<>();

        log.trace("{}Created Constraint [cstr={}]", LoggerUtils.tab(), constraint);
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

    public Constraint withConstraint(@NonNull String constraint) {
        Constraint cstr = Constraint.builder()
                .constraint(constraint)
                .variables(variables)
                .build();
        cstr.chocoConstraints = chocoConstraints;
        cstr.negChocoConstraints = negChocoConstraints;
        return cstr;
    }

    @Override
    public String toString() {
        return constraint;
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
    }
}
