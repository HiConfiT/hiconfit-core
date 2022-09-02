/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core;

import at.tugraz.ist.ase.common.LoggerUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a feature of a feature model
 * <p>
 * This class should be immutable.
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Slf4j
public class Feature implements Cloneable {

    /**
     * Name of feature
     */
    @EqualsAndHashCode.Include
    protected @NonNull String name;

    /**
     * ID of feature
     */
    @EqualsAndHashCode.Include
    protected @NonNull String id;

    protected boolean isRoot = false;

    protected Feature parent = null;
    protected List<AbstractRelationship> relationships = new LinkedList<>();

    @Setter
    protected boolean isAbstract = false;

    /**
     * Constructor for the root feature.
     */
    public static Feature createRoot(@NonNull String name, @NonNull String id) {
        Feature root = new Feature(name, id);
        root.isRoot = true;
        root.isAbstract = true;

        log.trace("{}Created root feature with [name={}, id={}]", LoggerUtils.tab(), name, id);
        return root;
    }

    /**
     * Constructor for the child feature.
     */
    @Builder
    public Feature(@NonNull String name, @NonNull String id) {
        this.name = name;
        this.id = id;

        log.trace("{}Created feature with [name={}, id={}, parent={}]", LoggerUtils.tab(), name, id, parent);
    }

    public boolean isIdDuplicate(@NonNull String id) {
        return this.id.equals(id);
    }

    public boolean isNameDuplicate(@NonNull String name) {
        return this.name.equals(name);
    }

    public boolean isDuplicate(@NonNull Feature feature) {
        return isIdDuplicate(feature.id) && isNameDuplicate(feature.name);
    }

    public boolean isMandatory() {
        return parent != null
                && parent.relationships.parallelStream().anyMatch(r -> r instanceof MandatoryRelationship && r.isChild(this));
    }

    // TODO - check
    public boolean isOptional() {
        return parent != null
                && parent.relationships.parallelStream().anyMatch(r -> r instanceof OptionalRelationship && r.isChild(this));
    }

    public boolean isLeaf() {
        return relationships.isEmpty();
    }

    /**
     * Adds a parent to this feature.
     */
    public void setParent(Feature parent) {
        if (isRoot()) {
            throw new IllegalArgumentException("The root feature cannot have parent.");
        } else {
            this.parent = parent;

            log.trace("{}Added parent feature with [parent={}, child={}]", LoggerUtils.tab(), parent, this);
        }
    }

    public void addRelationship(AbstractRelationship relationship) {
        relationships.add(relationship);
        relationship.getChildren().forEach(child -> child.setParent(this));
    }

    public List<Feature> getChildren() {
        return relationships.stream().flatMap(relationship -> relationship.getChildren().stream()).collect(Collectors.toCollection(LinkedList::new));
        /*List<Feature> children = new LinkedList<>();
        for (AbstractRelationship relationship : relationships) {
            children.addAll(relationship.getChildren());
        }
        return children;*/
    }

    /**
     * Returns the name of the feature.
     *
     * @return name of the feature
     */
    @Override
    public String toString() {
        return name;
    }

    public void dispose() {
        this.parent = null;
        this.relationships.clear();
        this.relationships = null;
    }

    public Object clone() throws CloneNotSupportedException {
        Feature clone = (Feature) super.clone();

        // copy relationships
        clone.relationships = new LinkedList<>();
        for (AbstractRelationship relationship : relationships) {
            clone.relationships.add(relationship.clone());
        }

        return clone;
    }
}
