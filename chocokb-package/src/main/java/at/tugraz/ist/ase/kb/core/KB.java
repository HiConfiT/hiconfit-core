/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.kb.core;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.chocosolver.solver.Model;

import java.util.List;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkElementIndex;

/**
 * Contains the knowledge base for constraint problems.
 */
@Getter
@ToString(onlyExplicitlyIncluded = true)
public abstract class KB {
    @ToString.Include
    protected final String name;
    @ToString.Include
    protected final String source;

    protected final boolean hasNegativeConstraints;

    protected Model modelKB;

    // Variables
    protected List<Variable> variableList;
    // Domains
    protected List<Domain> domainList;
    // Constraints
    protected List<Constraint> constraintList;

    protected KB(String name, String source, boolean hasNegativeConstraints) {
        this.name = name;
        this.source = source;
        this.hasNegativeConstraints = hasNegativeConstraints;
    }

    public void dispose() {
        modelKB = null;
        variableList.clear();
        variableList = null;
        domainList.clear();
        domainList = null;
        constraintList.clear();
        constraintList = null;
    }

    public abstract void reset(boolean hasNegativeConstraints);

    public abstract void defineVariables();
    public abstract void defineConstraints(boolean hasNegativeConstraints);

    // Domains
    public int getNumDomains() {
        return domainList.size();
    }

    public Domain getDomain(@NonNull String variable) {
        for (Domain domain : domainList) {
            if (domain.isDomainOf(variable)) {
                return domain;
            }
        }
        throw new IllegalArgumentException("Variable " + variable + " does not exist");
    }

    // Variables
    public int getNumVariables() {
        return variableList.size();
    }

    public Variable getVariable(int index) {
        checkElementIndex(index, variableList.size(), "Index out of bounds");

        return variableList.get(index);
    }

    public Variable getVariable(@NonNull String name) {
        for (Variable var: variableList) {
            if (var.getName().equals(name)) {
                return var;
            }
        }
        throw new IllegalArgumentException("Variable " + name + " does not exist");
    }

    // Choco vars
    public int getNumChocoVars() {
        return getModelKB().getNbVars();
    }

    public String getValue(@NonNull String var, int index) {
        Domain domain = getDomain(var);

        return domain.getValue(index);
    }

    // Constraints
    public int getNumConstraints() {
        return constraintList.size();
    }

    public Constraint getConstraint(int index) {
        checkElementIndex(index, constraintList.size(), "Index out of bounds");

        return constraintList.get(index);
    }

    // Choco constraints
    public int getNumChocoConstraints() {
        return modelKB.getNbCstrs();
    }

    // Configuration size
    public double getConfigurationSize() {
        return IntStream.range(0, getNumVariables()).mapToDouble(i -> domainList.get(i).size()).reduce(1, (a, b) -> a * b);
    }
}
