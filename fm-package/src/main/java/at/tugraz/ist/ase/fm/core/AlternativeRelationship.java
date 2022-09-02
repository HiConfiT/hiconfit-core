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
public class AlternativeRelationship extends AbstractRelationship implements Cloneable {

    @Builder
    public AlternativeRelationship(@NonNull Feature parent, @NonNull List<Feature> children) {
        super(parent, children);

        checkArgument(children.size() >= 1, "Alternative relationship's children must have more than one feature");
    }

    @Override
    protected void convertToConfRule() {
        this.confRule = String.format("alternative(%s, %s)", getParent(), getChildren().stream().map(Feature::getName).collect(Collectors.joining(", ")));
    }

    public AlternativeRelationship clone() throws CloneNotSupportedException {
        return (AlternativeRelationship) super.clone();
    }
}
