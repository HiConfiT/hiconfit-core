/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.eval;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PerformanceEvaluatorTest {

    public static final String COUNTER_FINDCONFLICT_CALLS = "The number of QX calls:";
    public static final String COUNTER_FASTDIAG_CALLS = "The number of FD calls:";
    public static final String COUNTER_CONSISTENCY_CHECKS = "The number of Consistency checks:";
    public static final String COUNTER_SIZE_CONSISTENCY_CHECKS = "The size of Consistency checks:";
    public static final String COUNTER_UNION_OPERATOR = "The number of union:";
    public static final String COUNTER_ADD_OPERATOR = "The number of add:";

    public static final String TIMER_FIRST = "Time for first:";
    public static final String TIMER_ALL = "Time for all:";

    @Test
    @DisplayName("Test performance evaluation")
    public void testPerformanceEvaluation() {
        PerformanceEvaluator.reset();

        assertThrows(IllegalStateException.class, () -> PerformanceEvaluator.getTimer("TIMER_ALL").getElapsedTime());

        PerformanceEvaluator.start(TIMER_ALL);
        PerformanceEvaluator.start(TIMER_FIRST);

        PerformanceEvaluator.incrementCounter(COUNTER_FINDCONFLICT_CALLS);
        PerformanceEvaluator.incrementCounter(COUNTER_FINDCONFLICT_CALLS);
        PerformanceEvaluator.incrementCounter(COUNTER_FINDCONFLICT_CALLS);

        PerformanceEvaluator.stop(TIMER_FIRST);
        PerformanceEvaluator.start(TIMER_FIRST);

        PerformanceEvaluator.incrementCounter(COUNTER_FASTDIAG_CALLS);
        PerformanceEvaluator.incrementCounter(COUNTER_FASTDIAG_CALLS);
        PerformanceEvaluator.incrementCounter(COUNTER_FASTDIAG_CALLS);
        PerformanceEvaluator.incrementCounter(COUNTER_FASTDIAG_CALLS);
        PerformanceEvaluator.incrementCounter(COUNTER_FASTDIAG_CALLS);

        PerformanceEvaluator.incrementCounter(COUNTER_CONSISTENCY_CHECKS, 10);
        PerformanceEvaluator.incrementCounter(COUNTER_CONSISTENCY_CHECKS, 11);

        PerformanceEvaluator.stop(TIMER_FIRST);
        PerformanceEvaluator.start(TIMER_FIRST);

        PerformanceEvaluator.incrementCounter(COUNTER_SIZE_CONSISTENCY_CHECKS, 100);
        PerformanceEvaluator.incrementCounter(COUNTER_SIZE_CONSISTENCY_CHECKS, 101);
        PerformanceEvaluator.incrementCounter(COUNTER_SIZE_CONSISTENCY_CHECKS, 100);

        PerformanceEvaluator.incrementCounter(COUNTER_UNION_OPERATOR);
        PerformanceEvaluator.incrementCounter(COUNTER_UNION_OPERATOR);
        PerformanceEvaluator.incrementCounter(COUNTER_UNION_OPERATOR);

        PerformanceEvaluator.incrementCounter(COUNTER_ADD_OPERATOR);
        PerformanceEvaluator.incrementCounter(COUNTER_ADD_OPERATOR);
        PerformanceEvaluator.incrementCounter(COUNTER_ADD_OPERATOR);

        PerformanceEvaluator.stop(TIMER_FIRST);
        PerformanceEvaluator.stop(TIMER_ALL);

        String results = PerformanceEvaluator.getEvaluationResults();
        System.out.println(results);

        assertAll(() -> Assertions.assertEquals(3, PerformanceEvaluator.getCounter(COUNTER_FINDCONFLICT_CALLS).getValue()),
                () -> Assertions.assertEquals(5, PerformanceEvaluator.getCounter(COUNTER_FASTDIAG_CALLS).getValue()),
                () -> Assertions.assertEquals(21, PerformanceEvaluator.getCounter(COUNTER_CONSISTENCY_CHECKS).getValue()),
                () -> Assertions.assertEquals(301, PerformanceEvaluator.getCounter(COUNTER_SIZE_CONSISTENCY_CHECKS).getValue()),
                () -> Assertions.assertEquals(3, PerformanceEvaluator.getCounter(COUNTER_UNION_OPERATOR).getValue()),
                () -> Assertions.assertEquals(3, PerformanceEvaluator.getCounter(COUNTER_ADD_OPERATOR).getValue()));
    }
}