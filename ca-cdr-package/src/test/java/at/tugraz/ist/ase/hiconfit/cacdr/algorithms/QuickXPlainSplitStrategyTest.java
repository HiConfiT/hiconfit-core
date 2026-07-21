/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2026
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr.algorithms;

import at.tugraz.ist.ase.hiconfit.cacdr.algorithms.hs.labeler.QuickXPlainLabeler;
import at.tugraz.ist.ase.hiconfit.cacdr.algorithms.hs.parameters.QuickXPlainParameters;
import at.tugraz.ist.ase.hiconfit.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.hiconfit.cacdr.eval.CAEvaluator;
import at.tugraz.ist.ase.hiconfit.cdrmodel.AbstractCDRModel;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test_model.ITestModel;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test_model.model.*;
import at.tugraz.ist.ase.hiconfit.common.ConstraintUtils;
import at.tugraz.ist.ase.hiconfit.common.RatioSplitPointStrategy;
import at.tugraz.ist.ase.hiconfit.common.SplitPointStrategy;
import at.tugraz.ist.ase.hiconfit.eval.PerformanceEvaluator;
import at.tugraz.ist.ase.hiconfit.kb.core.Constraint;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static at.tugraz.ist.ase.hiconfit.cacdr.eval.CAEvaluator.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Covers the injectable split-point strategy on {@link QuickXPlain}.
 * <p>
 * Baseline reproduction and the correctness invariant are the two things that must hold: the
 * split point affects efficiency only, never the conflict returned.
 */
class QuickXPlainSplitStrategyTest {

    /** Supplies a freshly initialized model, so runs never share solver state. */
    @FunctionalInterface
    private interface ModelSupplier {
        AbstractCDRModel get() throws Exception;
    }

    private static final List<ModelSupplier> MODELS = List.of(
            TestModel1::new, TestModel2::new, TestModel3::new, TestModel4::new, TestModel5::new);

    /** Counts computeK invocations, to prove the strategy actually reaches the recursion. */
    private static final class CountingStrategy implements SplitPointStrategy {
        private final SplitPointStrategy delegate;
        private int calls = 0;

        CountingStrategy(SplitPointStrategy delegate) {
            this.delegate = delegate;
        }

        @Override
        public int computeK(int size) {
            calls++;
            return delegate.computeK(size);
        }
    }

    private record Run(Set<Constraint> conflict, Map<String, Long> counters) {}

    private static final List<String> TRACKED_COUNTERS = List.of(
            ConstraintUtils.COUNTER_SPLIT_SET,
            COUNTER_CONSISTENCY_CHECKS,
            QuickXPlain.COUNTER_QUICKXPLAIN_CALLS,
            COUNTER_LEFT_BRANCH_CALLS,
            COUNTER_RIGHT_BRANCH_CALLS,
            COUNTER_UNION_OPERATOR);

    /**
     * Runs QuickXPlain on a fresh model. A null strategy leaves the instance untouched, exercising
     * the default field value rather than the setter.
     */
    private static Run run(ModelSupplier supplier, SplitPointStrategy strategy, boolean applyAtAllLevels)
            throws Exception {
        AbstractCDRModel model = supplier.get();
        model.initialize();

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(model);
        QuickXPlain quickXPlain = new QuickXPlain(checker);
        if (strategy != null) {
            quickXPlain.setSplitStrategy(strategy, applyAtAllLevels);
        }

        CAEvaluator.reset();
        Set<Constraint> conflict = quickXPlain.findConflictSet(
                model.getPossiblyFaultyConstraints(), model.getCorrectConstraints());

        Map<String, Long> counters = new LinkedHashMap<>();
        TRACKED_COUNTERS.forEach(
                name -> counters.put(name, PerformanceEvaluator.getCounter(name).getValue()));
        return new Run(conflict, counters);
    }

    private static Set<Constraint> expectedConflict(ModelSupplier supplier) throws Exception {
        AbstractCDRModel model = supplier.get();
        model.initialize();
        return ((ITestModel) model).getExpectedFirstConflict();
    }

    // ---------------------------------------------------------------- baseline reproduction

    @Test
    void defaultsReproduceBaselineConflict() throws Exception {
        for (ModelSupplier supplier : MODELS) {
            assertEquals(expectedConflict(supplier), run(supplier, null, true).conflict());
        }
    }

    @Test
    void explicitMidpointStrategyReproducesBaselineConflict() throws Exception {
        for (ModelSupplier supplier : MODELS) {
            assertEquals(run(supplier, null, true).conflict(),
                    run(supplier, SplitPointStrategy.MIDPOINT, true).conflict());
        }
    }

    /**
     * The efficiency comparison against the pre-change baseline is only fair if every counter is
     * untouched, not just the conflict set.
     */
    @Test
    void explicitMidpointStrategyReproducesBaselineCounters() throws Exception {
        for (ModelSupplier supplier : MODELS) {
            assertEquals(run(supplier, null, true).counters(),
                    run(supplier, SplitPointStrategy.MIDPOINT, true).counters());
        }
    }

    /** Flooring makes ratio 0.5 a drop-in for midpoint, counters included. */
    @Test
    void ratioOneHalfReproducesMidpointExactly() throws Exception {
        for (ModelSupplier supplier : MODELS) {
            Run midpoint = run(supplier, SplitPointStrategy.MIDPOINT, true);
            Run half = run(supplier, new RatioSplitPointStrategy(0.5), true);

            assertEquals(midpoint.conflict(), half.conflict());
            assertEquals(midpoint.counters(), half.counters());
        }
    }

    // ---------------------------------------------------------------- correctness invariant

    private static final double[] RATIOS = {0.1, 0.25, 0.4, 0.6, 0.75, 0.9};

    @Test
    void ratioStrategiesReturnSameConflictAsMidpoint_allLevels() throws Exception {
        assertConflictInvariantHolds(true);
    }

    @Test
    void ratioStrategiesReturnSameConflictAsMidpoint_topLevelOnly() throws Exception {
        assertConflictInvariantHolds(false);
    }

    private static void assertConflictInvariantHolds(boolean applyAtAllLevels) throws Exception {
        for (ModelSupplier supplier : MODELS) {
            Set<Constraint> expected = run(supplier, SplitPointStrategy.MIDPOINT, true).conflict();

            for (double ratio : RATIOS) {
                assertEquals(expected,
                        run(supplier, new RatioSplitPointStrategy(ratio), applyAtAllLevels).conflict(),
                        "ratio " + ratio + ", applyAtAllLevels=" + applyAtAllLevels);
            }
        }
    }

    /** Ratios at the extremes must still clamp to a real split, terminate, and stay correct. */
    @Test
    void extremeRatiosTerminateAndStayCorrect() throws Exception {
        for (ModelSupplier supplier : MODELS) {
            Set<Constraint> expected = run(supplier, SplitPointStrategy.MIDPOINT, true).conflict();

            for (double ratio : new double[]{0.0, 0.001, 0.999, 1.0}) {
                assertEquals(expected,
                        run(supplier, new RatioSplitPointStrategy(ratio), true).conflict(),
                        "ratio " + ratio);
            }
        }
    }

    // ---------------------------------------------------------------- the knob is actually wired

    /**
     * Without this, a strategy silently ignored inside qx would still pass every correctness test.
     * Counting computeK calls proves the strategy reaches the recursion, and that
     * applyAtAllLevels=false confines it to exactly the top-level split.
     */
    @Test
    void applyAtAllLevelsControlsHowDeepTheStrategyReaches() throws Exception {
        List<Integer> allLevelCalls = new ArrayList<>();
        List<Integer> topOnlyCalls = new ArrayList<>();

        for (ModelSupplier supplier : MODELS) {
            CountingStrategy allLevels = new CountingStrategy(new RatioSplitPointStrategy(0.9));
            CountingStrategy topOnly = new CountingStrategy(new RatioSplitPointStrategy(0.9));

            Set<Constraint> a = run(supplier, allLevels, true).conflict();
            Set<Constraint> b = run(supplier, topOnly, false).conflict();

            assertEquals(a, b, "flag must not change the conflict");

            // top-level-only: the strategy is consulted for the first split and never again
            assertTrue(topOnly.calls <= 1, "topOnly consulted " + topOnly.calls + " times");
            assertTrue(allLevels.calls >= topOnly.calls);

            allLevelCalls.add(allLevels.calls);
            topOnlyCalls.add(topOnly.calls);
        }

        int totalAll = allLevelCalls.stream().mapToInt(Integer::intValue).sum();
        int totalTop = topOnlyCalls.stream().mapToInt(Integer::intValue).sum();

        assertTrue(totalTop > 0, "strategy never reached qx at all: " + topOnlyCalls);
        assertTrue(totalAll > totalTop,
                "strategy never applied below the top level: all=" + allLevelCalls + " top=" + topOnlyCalls);
    }

    // ---------------------------------------------------------------- labeler clone safety

    /** HSDAG/HSTree clone labelers via getInstance; the split policy must survive that. */
    @Test
    void labelerClonesInheritSplitStrategy() throws Exception {
        TestModel1 model = new TestModel1();
        model.initialize();

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(model);
        QuickXPlainParameters params = QuickXPlainParameters.builder()
                .C(model.getPossiblyFaultyConstraints())
                .B(model.getCorrectConstraints()).build();

        QuickXPlainLabeler original = new QuickXPlainLabeler(checker, params);
        SplitPointStrategy strategy = new RatioSplitPointStrategy(0.8);
        original.setSplitStrategy(strategy, false);

        QuickXPlainLabeler clone = (QuickXPlainLabeler) original.getInstance(checker);

        assertAll(
                () -> assertSame(strategy, clone.getSplitStrategy()),
                () -> assertFalse(clone.isApplyAtAllLevels()),
                // a default labeler still clones to the default policy
                () -> assertSame(SplitPointStrategy.MIDPOINT,
                        ((QuickXPlainLabeler) new QuickXPlainLabeler(checker, params).getInstance(checker))
                                .getSplitStrategy())
        );
    }
}
