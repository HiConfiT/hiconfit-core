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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a relationship of a feature model.
 * <p>
 * This class should be immutable.
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class AbstractRelationship implements Cloneable {

    @Getter
    protected Feature parent;
    @Getter
    protected List<Feature> children = new LinkedList<>();

    @EqualsAndHashCode.Include
    protected String confRule = null;

    public AbstractRelationship(@NonNull Feature parent, @NonNull Collection<Feature> children) {
        this.parent = parent;
        this.children.addAll(children);

        convertToConfRule();
    }

//    /**
//     * Checks whether the relationship is optional.
//     * @return true if the relationship is OPTIONAL or OR, false otherwise.
//     */
//    public boolean isOptional() {
//        return type == RelationshipType.OPTIONAL || type == RelationshipType.OR;
//    }

    /**
     * Returns the first child.
     */
    public Feature getChild() {
        if (children.isEmpty()) {
            return null;
        }
        return children.get(0);
    }

//    /**
//     * Adds a child to the relationship.
//     * Supports this operation just in case of a mutable relationship.
//     */
//    public void addChild(Feature child) {
//        children.add(child);
//    }

    public boolean isGroup() {
        return children.size() > 1;
    }

    /**
     * Checks whether the given {@link Feature} belongs to the left part of the relationship/constraint.
     *
     * @param feature a {@link Feature}
     * @return true if yes, false otherwise.
     */
    public boolean isParent(@NonNull Feature feature) {
        return parent.equals(feature);
    }

    /**
     * Checks whether the given {@link Feature} belongs to the right part of the relationship/constraint.
     *
     * @param feature a {@link Feature}
     * @return true if yes, false otherwise.
     */
    public boolean isChild(@NonNull Feature feature) {
        return children.parallelStream().anyMatch(f -> f.equals(feature));
    }

    public boolean contains(@NonNull Feature feature) {
        return isParent(feature) || isChild(feature);
    }

    protected abstract void convertToConfRule();

    @Override
    public String toString() {
        if (confRule == null) {
            convertToConfRule();
        }
        return confRule;
    }

    public void dispose() {
        parent = null;
        children.clear();
        children = null;
    }

    public AbstractRelationship clone() throws CloneNotSupportedException {
        AbstractRelationship clone = (AbstractRelationship) super.clone();

        // copy children
        clone.children = new LinkedList<>(children);
        clone.convertToConfRule();

        return clone;
    }
}

