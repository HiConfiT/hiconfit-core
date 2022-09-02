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
public class OptionalRelationship<F extends Feature> extends AbstractRelationship<F> implements Cloneable {

    @Builder
    public OptionalRelationship(@NonNull F from, @NonNull F to) {
        super(from, Collections.singletonList(to));
    }

    @Override
    protected void convertToConfRule() {
        this.confRule = String.format("optional(%s, %s)", getParent(), getChild());
    }

    @Override
    public OptionalRelationship<F> clone() {
        return new OptionalRelationship<>(getParent(), getChild());
    }
}