/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a relationship/constraint of a feature model.
 */
@Getter
@EqualsAndHashCode
public class Relationship {

    protected final RelationshipType type;

    protected String confRule;

    @EqualsAndHashCode.Exclude
    private final List<String> constraints = new LinkedList<>();

    public Relationship(RelationshipType type) {
        this.type = type;
    }

    /**
     * Checks whether the relationship is optional.
     * @return true if the relationship is OPTIONAL or OR, false otherwise.
     */
    public boolean isOptional() {
        return type == RelationshipType.OPTIONAL || type == RelationshipType.OR;
    }

    /**
     * Checks whether the type of relationship/constraint is the given {@link RelationshipType} type.
     * @param type a {@link RelationshipType} type
     * @return the type of relationship/constraint.
     */
    public boolean isType(RelationshipType type) {
        return this.type == type;
    }

    /**
     * Checks whether the given {@link Feature} belongs to the left part of the relationship/constraint.
     *
     * @param feature a {@link Feature}
     * @return true if yes, false otherwise.
     */
    public boolean presentAtLeftSide(@NonNull Feature feature) {
        return false;
    }

    /**
     * Checks whether the given {@link Feature} belongs to the right part of the relationship/constraint.
     *
     * @param feature a {@link Feature}
     * @return true if yes, false otherwise.
     */
    public boolean presentAtRightSide(@NonNull Feature feature) {
        return false;
    }

    public boolean contains(@NonNull Feature feature) {
        return false;
    }


    /**
     * Adds a textual Choco constraint.
     * @param constraint a textual Choco constraint.
     */
    public void setConstraint(@NonNull String constraint) {
        constraints.add(constraint);
    }

    @Override
    public String toString() {
        return confRule;
    }
}

