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
 * Represents a Mandatory relationship.
 * <p>
 * This class should be immutable.
 */
public class MandatoryRelationship extends AbstractRelationship implements Cloneable {

    @Builder
    public MandatoryRelationship(@NonNull Feature parent, @NonNull Feature child) {
        super(parent, Collections.singletonList(child));

//        checkArgument(child.size() == 1, "Mandatory relationship's children must have exactly one feature");
    }

    @Override
    protected void convertToConfRule() {
        this.confRule = String.format("mandatory(%s, %s)", getParent(), getChild());
    }

    public MandatoryRelationship clone() throws CloneNotSupportedException {
        return (MandatoryRelationship) super.clone();
    }
}
