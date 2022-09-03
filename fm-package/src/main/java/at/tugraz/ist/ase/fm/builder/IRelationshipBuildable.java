/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.builder;

import at.tugraz.ist.ase.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.fm.core.Feature;
import lombok.NonNull;

import java.util.List;

public interface IRelationshipBuildable {
    <F extends Feature, R extends AbstractRelationship<F>> R buildMandatoryRelationship(@NonNull F from, @NonNull F to);

    <F extends Feature, R extends AbstractRelationship<F>> R buildOptionalRelationship(@NonNull F from, @NonNull F to);

    <F extends Feature, R extends AbstractRelationship<F>> R buildOrRelationship(@NonNull F from, @NonNull List<F> to);

    <F extends Feature, R extends AbstractRelationship<F>> R buildAlternativeRelationship(@NonNull F from, @NonNull List<F> to);
}
