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
public class FastDiagV2Parameters extends AbstractHSParameters {
    private Set<Constraint> AC;

    @Builder
    public FastDiagV2Parameters(@NonNull Set<Constraint> C, @NonNull Set<Constraint> AC) {
        super(C);
        this.AC = AC;
    }

    @Override
    public String toString() {
        return "FastDiagV2Parameters{" +
                "C=" + getC() +
                ", B=" + AC +
                "}";
    }

    @Override
    public void dispose() {
        super.dispose();
        AC = null;
    }
}
