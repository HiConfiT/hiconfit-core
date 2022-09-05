/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core;

import at.tugraz.ist.ase.fm.translator.IConfRuleTranslatable;
import lombok.Builder;
import lombok.NonNull;

import java.util.Collections;

/**
 * Represents a Mandatory relationship.
 * <p>
 * This class should be immutable.
 */
public class MandatoryRelationship<F extends Feature> extends AbstractRelationship<F> implements Cloneable {

    @Builder
    public MandatoryRelationship(@NonNull F from, @NonNull F to, @NonNull IConfRuleTranslatable translator) {
        super(from, Collections.singletonList(to), translator);
    }

    @Override
    public boolean isMandatory() {
        return true;
    }

    @Override
    public boolean isOptional() {
        return false;
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
    public MandatoryRelationship<F> clone() {
        return new MandatoryRelationship<>(getParent(), getChild(), confRuleTranslator);
    }
}
