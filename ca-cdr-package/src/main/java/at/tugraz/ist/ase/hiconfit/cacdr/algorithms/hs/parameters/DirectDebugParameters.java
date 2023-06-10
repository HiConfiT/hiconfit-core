/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr.algorithms.hs.parameters;

import at.tugraz.ist.ase.hiconfit.cacdr_core.ITestCase;
import at.tugraz.ist.ase.hiconfit.kb.core.Constraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Set;

/**
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
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
