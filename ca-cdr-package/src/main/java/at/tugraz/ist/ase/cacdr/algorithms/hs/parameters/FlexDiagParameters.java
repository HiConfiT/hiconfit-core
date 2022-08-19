/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hs.parameters;

import at.tugraz.ist.ase.kb.core.Constraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Set;

@Getter
public class FlexDiagParameters extends AbstractHSParameters {
    private Set<Constraint> AC;
    private final int m;

    @Builder
    public FlexDiagParameters(@NonNull Set<Constraint> S, @NonNull Set<Constraint> AC, int m) {
        super(S);
        this.AC = AC;
        this.m = m;
    }

    @Override
    public String toString() {
        return "FlexDiagParameters{" +
                "S=" + getC() +
                ", AC=" + AC +
                ", m=" + m +
                "}";
    }

    @Override
    public void dispose() {
        super.dispose();
        AC = null;
    }
}
