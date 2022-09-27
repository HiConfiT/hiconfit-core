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

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Represents a feature of a feature model
 * <p>
 * This class should be immutable.
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Slf4j
public class Feature implements Cloneable {

    /**
     * Name of feature
     */
    @Getter
    @EqualsAndHashCode.Include
    protected @NonNull String name;

    /**
     * ID of feature
     */
    @Getter
    @EqualsAndHashCode.Include
    protected @NonNull String id;

    @Getter
    protected boolean isRoot = false;

    @Getter
    protected Feature parent = null;
    protected List<AbstractRelationship<? extends Feature>> relationships = new LinkedList<>();

    @Getter
    @Setter
    protected boolean isAbstract = false;

    /**
     * Constructor for the root feature.
     */
    public static Feature createRoot(@NonNull String name, @NonNull String id) {
        checkArgument(!name.isEmpty(), "Feature name cannot be empty!");
        checkArgument(!id.isEmpty(), "Feature id cannot be empty!");

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
        checkArgument(!name.isEmpty(), "Feature name cannot be empty!");
        checkArgument(!id.isEmpty(), "Feature id cannot be empty!");

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
                && parent.relationships.stream().anyMatch(r -> r.isMandatory() && r.isChild(this));
    }

//    public boolean isOptional() {
//        return parent != null
//                && parent.relationships.parallelStream().anyMatch(r -> r.isOptional() && r.isChild(this));
//    }

    public boolean isOptional() {
        return parent != null
                && parent.relationships.parallelStream()
                        .anyMatch(r -> r.isChild(this)
                                       && (r.isOptional() || r.isOr() || r.isAlternative()));
    }

//    public boolean isOrGroup() {
//        return parent != null
//                && parent.relationships.parallelStream().anyMatch(r -> r.isOr() && r.isChild(this));
//    }
//
//    public boolean isAlternativeGroup() {
//        return parent != null
//                && parent.relationships.parallelStream().anyMatch(r -> r.isAlternative() && r.isChild(this));
//    }

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

    public void addRelationship(AbstractRelationship<? extends Feature> relationship) {
        relationships.add(relationship);
    }

    public List<Feature> getChildren() {
        return relationships.parallelStream().flatMap(r -> r.getChildren().parallelStream()).collect(Collectors.toCollection(LinkedList::new));
        /*List<Feature> children = new LinkedList<>();
        for (AbstractRelationship<? extends Feature> relationship : relationships) {
            children.addAll(relationship.getChildren());
        }
        return children;*/
    }

    public List<AbstractRelationship<? extends Feature>> getRelationshipsAsParent() {
        return relationships;
    }

    public List<AbstractRelationship<? extends Feature>> getRelationshipsAsChild() {
        return parent.getRelationshipsAsParent().parallelStream()
                    .filter(r -> r.isChild(this))
                    .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<AbstractRelationship<? extends Feature>> getAllRelationships() {
        List<AbstractRelationship<? extends Feature>> allRelationships = new LinkedList<>(relationships);
        allRelationships.addAll(getRelationshipsAsChild());
//        parent.getRelationshipsAsParent().forEach(r -> {
//            if (r.isChild(this)) {
//                allRelationships.add(r);
//            }
//        });
        return allRelationships;
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
        for (AbstractRelationship<? extends Feature> relationship : relationships) {
            clone.relationships.add(relationship.clone());
        }

        return clone;
    }
}
