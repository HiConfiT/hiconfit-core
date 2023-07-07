/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr_core;

import lombok.*;

/**
 * Represents an assignment of a value to a variable.
 * <p>
 * Could represent:
 * + a CSP, SAT clause, e.g., F1 = true.
 * + a preference of a user requirement, e.g., Modell = limousine.
 */
@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Assignment implements Cloneable, Comparable<Assignment> {
    @With
    protected final @NonNull String variable;
    @With
    protected final @NonNull String value;

    @Override
    public String toString() {
        return variable + "=" + value;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    // khong can thiet lam
    @Override
    public int compareTo(Assignment o) {
        return this.toString().compareTo(o.toString());
    }
}
