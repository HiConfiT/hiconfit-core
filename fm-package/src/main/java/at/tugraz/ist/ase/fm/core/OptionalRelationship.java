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

import java.util.Collections;

/**
 * Represents an Optional relationship.
 * <p>
 * This class should be immutable.
 */
public class OptionalRelationship extends AbstractRelationship implements Cloneable {

    @Builder
    public OptionalRelationship(@NonNull Feature parent, @NonNull Feature child) {
        super(parent, Collections.singletonList(child));

//        checkArgument(children.size() == 1, "Optional relationship's children must have exactly one feature");
    }

    @Override
    protected void convertToConfRule() {
        this.confRule = String.format("optional(%s, %s)", getParent(), getChild());
    }

    public OptionalRelationship clone() throws CloneNotSupportedException {
        return (OptionalRelationship) super.clone();
    }
}