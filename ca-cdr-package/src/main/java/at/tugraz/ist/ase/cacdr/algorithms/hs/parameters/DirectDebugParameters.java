/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hs.parameters;

import at.tugraz.ist.ase.cdrmodel.test.ITestCase;
import at.tugraz.ist.ase.kb.core.Constraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Set;

@Getter
public class DirectDebugParameters extends AbstractHSParameters {
    private final Set<Constraint> B;
    private final Set<ITestCase> TV;
    private final Set<ITestCase> TC;
    @Setter
    private Set<ITestCase> TCp;

    @Builder
    public DirectDebugParameters(@NonNull Set<Constraint> C, @NonNull Set<Constraint> B, @NonNull Set<ITestCase> TV, @NonNull Set<ITestCase> TC) {
        super(C);
        this.B = B;
        this.TV = TV;
        this.TC = TC;
    }

    @Override
    public String toString() {
        return "DirectDebugParameters{" +
                "C=" + getC() +
                ", B=" + B +
                ", TV=" + TV +
                ", TC=" + TC +
                ", TCp=" + TCp +
                "}";
    }
}
