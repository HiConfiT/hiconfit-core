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
 * <p>
 * Could represent:
 * + a CSP, SAT clause, e.g., F1 = true.
 * + a preference of a user requirement, e.g., Modell = limousine.
 */
@Builder
@Getter @Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Assignment implements Cloneable {
    private @NonNull String variable;
    private @NonNull String value;

    // a copy constructor
//    public Assignment(Assignment assignment) {
//        this.variable = assignment.variable;
//        this.value = assignment.value;
//    }

    @Override
    public String toString() {
        return variable + "=" + value;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
