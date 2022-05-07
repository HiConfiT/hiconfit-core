/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cdrmodel;

import at.tugraz.ist.ase.kb.core.Constraint;
import com.google.common.collect.Sets;
import lombok.Getter;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Contains the knowledge base for constraint problems.
 *
 * Supports two types of constraints: Choco constraints (String) and representative constraints ({@link Constraint}).
 */
@Getter
public abstract class CDRModel {

    private final String name;

    /**
     * The set of constraints which we assume to be always correct (background knowledge)
     */
    private Set<Constraint> correctConstraints = new LinkedHashSet<>();

    /**
     * The set of constraints which could be faulty = KB (knowledge base).
     */
    private Set<Constraint> possiblyFaultyConstraints = new LinkedHashSet<>();

    /**
     * The set of Choco constraints which we assume to be always correct (background knowledge)
     */
    private Set<String> correctChocoConstraints = new LinkedHashSet<>();

    /**
     * The set of constraints which could be faulty = KB (knowledge base).
     */
    private Set<String> possiblyFaultyChocoConstraints = new LinkedHashSet<>();

    /**
     * Creates an empty diagnosis model.
     */
    public CDRModel(String name) {
        this.name = name;
    }

    /**
     * Sets the correct constraints (or background knowledge).
     * @param correctConstraints a collection of correct constraints
     */
    public void setCorrectConstraints(Collection<Constraint> correctConstraints) {
        this.correctConstraints = new LinkedHashSet<>(correctConstraints);
    }

    /**
     * Setter for the possibly faulty constraints (or knowledge base).
     * @param possiblyFaultyConstraints a collection of faulty constraints
     */
    public void setPossiblyFaultyConstraints(Collection<Constraint> possiblyFaultyConstraints) {
        this.possiblyFaultyConstraints = new LinkedHashSet<>(possiblyFaultyConstraints);
    }

    /**
     * Getter for all constraints.
     * @return a set of all constraints
     */
    public Set<Constraint> getAllConstraints() {
        return Sets.union(correctConstraints, possiblyFaultyConstraints);
    }

    /**
     * Sets the correct Choco constraints (or background knowledge).
     * @param correctChocoConstraints a collection of correct Choco constraints
     */
    public void setCorrectChocoConstraints(Collection<String> correctChocoConstraints) {
        this.correctChocoConstraints = new LinkedHashSet<>(correctChocoConstraints);
    }

    /**
     * Setter for the possibly faulty Choco constraints (or knowledge base).
     * @param possiblyFaultyChocoConstraints a collection of faulty Choco constraints
     */
    public void setPossiblyFaultyChocoConstraints(Collection<String> possiblyFaultyChocoConstraints) {
        this.possiblyFaultyChocoConstraints = new LinkedHashSet<>(possiblyFaultyChocoConstraints);
    }

    /**
     * Getter for all Choco constraints.
     * @return a set of all Choco constraints
     */
    public Set<String> getAllChocoConstraints() {
        return Sets.union(correctChocoConstraints, possiblyFaultyChocoConstraints);
    }

    /**
     * Call this method to load the constraints for the model.
     * Override this method to add all variables and constraints.
     */
    public abstract void initialize() throws Exception;

    @Override
    public String toString() {
        return "CDRModel{" + " Name=" + this.name +
                ", correctConstraints=" + correctConstraints +
                ", possiblyFaultyConstraints=" + possiblyFaultyConstraints +
                ", correctChocoConstraints=" + correctChocoConstraints +
                ", possiblyFaultyChocoConstraints=" + possiblyFaultyChocoConstraints +
                '}';
    }
}
