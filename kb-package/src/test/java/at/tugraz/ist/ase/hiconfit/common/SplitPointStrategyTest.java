/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2026
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.common;

import at.tugraz.ist.ase.hiconfit.eval.PerformanceEvaluator;
import at.tugraz.ist.ase.hiconfit.kb.core.Constraint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SplitPointStrategyTest {

    /** Builds an ordered set c1..cn, so partitions can be asserted positionally. */
    private static Set<Constraint> constraints(int n) {
        Set<Constraint> C = new LinkedHashSet<>();
        for (int i = 1; i <= n; i++) {
            C.add(new Constraint("c" + i, Collections.emptyList()));
        }
        return C;
    }

    private static List<String> names(Set<Constraint> C) {
        List<String> names = new ArrayList<>();
        C.forEach(c -> names.add(c.getConstraint()));
        return names;
    }

    private static long splitCount() {
        return PerformanceEvaluator.getCounter(ConstraintUtils.COUNTER_SPLIT_SET).getValue();
    }

    @BeforeEach
    void resetEvaluator() {
        PerformanceEvaluator.reset(); // counters are global static state
    }

    @Test
    void midpointMatchesLegacyHalving() {
        for (int n = 0; n <= 10; n++) {
            assertEquals(n / 2, SplitPointStrategy.MIDPOINT.computeK(n), "size " + n);
        }
    }

    @Test
    void ratioComputesFlooredFraction() {
        assertAll(
                () -> assertEquals(2, new RatioSplitPointStrategy(0.25).computeK(8)),
                () -> assertEquals(6, new RatioSplitPointStrategy(0.75).computeK(8)),
                () -> assertEquals(3, new RatioSplitPointStrategy(0.3).computeK(10)),
                () -> assertEquals(0, new RatioSplitPointStrategy(0.1).computeK(5)),
                () -> assertEquals(0.25, new RatioSplitPointStrategy(0.25).getRatio())
        );
    }

    /**
     * Flooring is what makes ratio 0.5 a drop-in replacement for the historical midpoint.
     * Rounding would diverge on odd sizes (round(0.5*5)=3 vs 5/2=2).
     */
    @Test
    void ratioAtHalfEqualsMidpointForAllSizes() {
        SplitPointStrategy half = new RatioSplitPointStrategy(0.5);
        for (int n = 0; n <= 20; n++) {
            assertEquals(SplitPointStrategy.MIDPOINT.computeK(n), half.computeK(n), "size " + n);
        }
    }

    @Test
    void splitWithExplicitKPartitionsAtK() {
        Set<Constraint> C1 = new LinkedHashSet<>();
        Set<Constraint> C2 = new LinkedHashSet<>();
        ConstraintUtils.split(constraints(5), C1, C2, 2);

        assertAll(
                () -> assertEquals(List.of("c1", "c2"), names(C1)),
                () -> assertEquals(List.of("c3", "c4", "c5"), names(C2))
        );
    }

    @Test
    void splitClampsToNonEmptyHalves() {
        // k out of range must never yield an empty half when a real split exists
        for (int k : new int[]{-5, 0, 1, 4, 5, 999}) {
            Set<Constraint> C1 = new LinkedHashSet<>();
            Set<Constraint> C2 = new LinkedHashSet<>();
            ConstraintUtils.split(constraints(5), C1, C2, k);

            assertFalse(C1.isEmpty(), "C1 empty for k=" + k);
            assertFalse(C2.isEmpty(), "C2 empty for k=" + k);
            assertEquals(5, C1.size() + C2.size(), "lost constraints for k=" + k);
        }
    }

    /**
     * Regression guard: the no-arg overload must partition exactly as it did before it became a
     * delegate. Sizes 0 and 1 are the cases an unconditional clamp would break - size 0 by
     * throwing, size 1 by swapping the halves.
     */
    @Test
    void splitNoArgIsUnchangedForLegacySizes() {
        assertAll(
                () -> assertLegacySplit(0, List.of(), List.of()),
                () -> assertLegacySplit(1, List.of(), List.of("c1")),
                () -> assertLegacySplit(2, List.of("c1"), List.of("c2")),
                () -> assertLegacySplit(3, List.of("c1"), List.of("c2", "c3")),
                () -> assertLegacySplit(5, List.of("c1", "c2"), List.of("c3", "c4", "c5")),
                () -> assertLegacySplit(8, List.of("c1", "c2", "c3", "c4"),
                        List.of("c5", "c6", "c7", "c8"))
        );
    }

    private static void assertLegacySplit(int n, List<String> expectedC1, List<String> expectedC2) {
        Set<Constraint> C1 = new LinkedHashSet<>();
        Set<Constraint> C2 = new LinkedHashSet<>();
        ConstraintUtils.split(constraints(n), C1, C2);

        assertEquals(expectedC1, names(C1), "C1 for size " + n);
        assertEquals(expectedC2, names(C2), "C2 for size " + n);
    }

    @Test
    void splitIncrementsCounterOncePerCall() {
        long before = splitCount();

        ConstraintUtils.split(constraints(4), new LinkedHashSet<>(), new LinkedHashSet<>());
        assertEquals(before + 1, splitCount(), "no-arg overload");

        ConstraintUtils.split(constraints(4), new LinkedHashSet<>(), new LinkedHashSet<>(), 3);
        assertEquals(before + 2, splitCount(), "k-aware overload");
    }
}
