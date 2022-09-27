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

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Represents an Alternative relationship.
 * <p>
 * This class should be immutable.
 */
public class AlternativeRelationship<F extends Feature> extends AbstractRelationship<F> implements Cloneable {

    @Builder
    public AlternativeRelationship(@NonNull F from, @NonNull List<F> to, @NonNull IConfRuleTranslatable translator) {
        super(from, to, translator);

        checkArgument(to.size() >= 1, "Alternative relationship's children must have more than one feature");
    }

    @Override
    public boolean isMandatory() {
        return false;
    }

    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public boolean isAlternative() {
        return true;
    }

    @Override
    public boolean isOr() {
        return false;
    }

    @Override
    public boolean isGroup() {
        return true;
    }

    @Override
    public AlternativeRelationship<F> clone() {
        return new AlternativeRelationship<>(getParent(), getChildren(), confRuleTranslator);
    }
}
