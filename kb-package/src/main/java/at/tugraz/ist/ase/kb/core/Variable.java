/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.kb.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
public abstract class Variable implements Cloneable {

    protected String name;
    protected Domain domain;

    public Variable(@NonNull String name, @NonNull Domain domain) {
        this.name = name;
        this.domain = domain;
    }

    public abstract String getValue();

    public abstract int getChocoValue();

    public boolean isAssignable(@NonNull String value) {
        return domain.contains(value) || value.equals("NULL") || value.isEmpty();
    }

    public Object clone() throws CloneNotSupportedException {
        Variable clone = (Variable) super.clone();

        clone.domain = (Domain) domain.clone();

        return clone;
    }

    public void dispose() {
        name = null;
        domain = null;
    }
}
