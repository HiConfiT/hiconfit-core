/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.kb.core;

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

    @Override
    public String toString() {
        return variable + "=" + value;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
