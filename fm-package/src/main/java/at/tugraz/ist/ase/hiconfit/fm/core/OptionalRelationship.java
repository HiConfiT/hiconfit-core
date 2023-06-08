/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fm.core;

import at.tugraz.ist.ase.hiconfit.fm.translator.IConfRuleTranslatable;
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
    public OptionalRelationship(@NonNull F from, @NonNull F to, @NonNull IConfRuleTranslatable translator) {
        super(from, Collections.singletonList(to), translator);
    }

    @Override
    public boolean isMandatory() {
        return false;
    }

    @Override
    public boolean isOptional() {
        return true;
    }

    @Override
    public boolean isAlternative() {
        return false;
    }

    @Override
    public boolean isOr() {
        return false;
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public OptionalRelationship<F> clone() {
        return new OptionalRelationship<>(getParent(), getChild(), confRuleTranslator);
    }
}