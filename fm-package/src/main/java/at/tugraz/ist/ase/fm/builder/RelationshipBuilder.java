/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.builder;

import at.tugraz.ist.ase.fm.core.*;
import at.tugraz.ist.ase.fm.translator.IConfRuleTranslatable;
import lombok.NonNull;

import java.util.List;

/**
 * Provides methods for creating the relationship of a basic feature model
 */
public class RelationshipBuilder implements IRelationshipBuildable {

    private final IConfRuleTranslatable translator;

    public RelationshipBuilder(@NonNull IConfRuleTranslatable translator) {
        this.translator = translator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <F extends Feature, R extends AbstractRelationship<F>> R buildMandatoryRelationship(@NonNull F from, @NonNull F to) {
        return (R) new MandatoryRelationship<>(from, to, translator);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <F extends Feature, R extends AbstractRelationship<F>> R buildOptionalRelationship(@NonNull F from, @NonNull F to) {
        return (R) new OptionalRelationship<>(from, to, translator);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <F extends Feature, R extends AbstractRelationship<F>> R buildOrRelationship(@NonNull F from, @NonNull List<F> to) {
        return (R) new OrRelationship<>(from, to, translator);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <F extends Feature, R extends AbstractRelationship<F>> R buildAlternativeRelationship(@NonNull F from, @NonNull List<F> to) {
        return (R) new AlternativeRelationship<>(from, to, translator);
    }
}
