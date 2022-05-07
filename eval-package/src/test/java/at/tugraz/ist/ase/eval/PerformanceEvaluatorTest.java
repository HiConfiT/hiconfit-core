/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.eval;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static at.tugraz.ist.ase.eval.PerformanceEvaluator.*;
import static org.junit.jupiter.api.Assertions.*;

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

        assertThrows(IllegalStateException.class, () -> getTimer("TIMER_ALL").getElapsedTime());

        start(TIMER_ALL);
        start(TIMER_FIRST);

        incrementCounter(COUNTER_FINDCONFLICT_CALLS);
        incrementCounter(COUNTER_FINDCONFLICT_CALLS);
        incrementCounter(COUNTER_FINDCONFLICT_CALLS);

        stop(TIMER_FIRST);
        start(TIMER_FIRST);

        incrementCounter(COUNTER_FASTDIAG_CALLS);
        incrementCounter(COUNTER_FASTDIAG_CALLS);
        incrementCounter(COUNTER_FASTDIAG_CALLS);
        incrementCounter(COUNTER_FASTDIAG_CALLS);
        incrementCounter(COUNTER_FASTDIAG_CALLS);

        incrementCounter(COUNTER_CONSISTENCY_CHECKS, 10);
        incrementCounter(COUNTER_CONSISTENCY_CHECKS, 11);

        stop(TIMER_FIRST);
        start(TIMER_FIRST);

        incrementCounter(COUNTER_SIZE_CONSISTENCY_CHECKS, 100);
        incrementCounter(COUNTER_SIZE_CONSISTENCY_CHECKS, 101);
        incrementCounter(COUNTER_SIZE_CONSISTENCY_CHECKS, 100);

        incrementCounter(COUNTER_UNION_OPERATOR);
        incrementCounter(COUNTER_UNION_OPERATOR);
        incrementCounter(COUNTER_UNION_OPERATOR);

        incrementCounter(COUNTER_ADD_OPERATOR);
        incrementCounter(COUNTER_ADD_OPERATOR);
        incrementCounter(COUNTER_ADD_OPERATOR);

        stop(TIMER_FIRST);
        stop(TIMER_ALL);

        String results = PerformanceEvaluator.getEvaluationResults();
        System.out.println(results);

        assertAll(() -> assertEquals(3, getCounter(COUNTER_FINDCONFLICT_CALLS).getValue()),
                () -> assertEquals(5, getCounter(COUNTER_FASTDIAG_CALLS).getValue()),
                () -> assertEquals(21, getCounter(COUNTER_CONSISTENCY_CHECKS).getValue()),
                () -> assertEquals(301, getCounter(COUNTER_SIZE_CONSISTENCY_CHECKS).getValue()),
                () -> assertEquals(3, getCounter(COUNTER_UNION_OPERATOR).getValue()),
                () -> assertEquals(3, getCounter(COUNTER_ADD_OPERATOR).getValue()));
    }
}