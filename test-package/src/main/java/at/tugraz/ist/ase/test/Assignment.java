/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.test;

import lombok.*;

/**
 * Represents an assignment of a value to a variable.
 *
 * Could represent:
 * + a CSP, SAT clause, e.g., F1 = true.
 * + a preference of a user requirement, e.g., Modell = limousine.
 */
@Builder
@Getter @Setter
@EqualsAndHashCode
public class Assignment implements Cloneable {
    private @NonNull String variable;
    private @NonNull String value;

    @Override
    public String toString() {
        return variable + "=" + value;
    }

    @Override
    public Assignment clone() {
        try {
            return (Assignment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
