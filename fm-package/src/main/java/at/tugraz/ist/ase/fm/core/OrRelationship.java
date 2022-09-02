/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Represents an Or relationship.
 * <p>
 * This class should be immutable.
 */
public class OrRelationship extends AbstractRelationship implements Cloneable {

    @Builder
    public OrRelationship(@NonNull Feature parent, @NonNull List<Feature> children) {
        super(parent, children);

        checkArgument(children.size() >= 1, "OR relationship's children must have more than one feature");
    }

    @Override
    protected void convertToConfRule() {
        this.confRule = String.format("or(%s, %s)", getParent(), getChildren().stream().map(Feature::getName).collect(Collectors.joining(", ")));
    }

    public OrRelationship clone() throws CloneNotSupportedException {
        return (OrRelationship) super.clone();
    }
}