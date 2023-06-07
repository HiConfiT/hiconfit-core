/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
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
 * Represents an Or relationship.
 * <p>
 * This class should be immutable.
 */
public class OrRelationship<F extends Feature> extends AbstractRelationship<F> implements Cloneable {

    @Builder
    public OrRelationship(@NonNull F from, @NonNull List<F> to, @NonNull IConfRuleTranslatable translator) {
        super(from, to, translator);

        checkArgument(to.size() >= 1, "OR relationship's children must have more than one feature");
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
        return false;
    }

    @Override
    public boolean isOr() {
        return true;
    }

    @Override
    public boolean isGroup() {
        return true;
    }

    @Override
    public OrRelationship<F> clone() {
        return new OrRelationship<>(getParent(), getChildren(), confRuleTranslator);
    }
}
