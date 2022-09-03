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
public abstract class AbstractRelationship<F extends Feature> {

    @Getter
    protected F parent;
    @Getter
    protected List<F> children = new LinkedList<>();

    @EqualsAndHashCode.Include
    protected String confRule = null;

    public AbstractRelationship(@NonNull F from, @NonNull Collection<F> to) {
        this.parent = from;
        this.children.addAll(to);

        convertToConfRule();
    }

    public void makeConnectionBetweenParentAndChildren() {
        parent.addRelationship(this);
        children.forEach(child -> child.setParent(parent));
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof AbstractRelationship<?> that)) return false;
//        return Objects.equals(confRule, that.confRule);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(confRule);
//    }

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
    public F getChild() {
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
    public boolean isParent(@NonNull F feature) {
        return parent.equals(feature);
    }

    /**
     * Checks whether the given {@link Feature} belongs to the right part of the relationship/constraint.
     *
     * @param feature a {@link Feature}
     * @return true if yes, false otherwise.
     */
    public boolean isChild(@NonNull F feature) {
        return children.parallelStream().anyMatch(f -> f.equals(feature));
    }

    public boolean contains(@NonNull F feature) {
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

    public abstract AbstractRelationship<F> clone();
}

