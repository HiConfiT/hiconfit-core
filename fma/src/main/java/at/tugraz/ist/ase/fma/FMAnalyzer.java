/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma;

import at.tugraz.ist.ase.fma.analysis.AbstractFMAnalysis;
import at.tugraz.ist.ase.fma.explanator.AbstractAnomalyExplanator;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

/**
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 * @author: Tamim Burgstaller (tamim.burgstaller@student.tugraz.at)
 */
public class FMAnalyzer {
    private final Map<AbstractFMAnalysis<?>, AbstractAnomalyExplanator<?>> analyses = new LinkedHashMap<>();

    public FMAnalyzer() {
    }

    public void addAnalysis(AbstractFMAnalysis<?> analysis, AbstractAnomalyExplanator<?> explanator) {
        analyses.put(analysis, explanator);
    }

    public void run() throws ExecutionException, InterruptedException {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        for (AbstractFMAnalysis<?> analysis : analyses.keySet()) {
            pool.execute(analysis);
        }

        List<AbstractAnomalyExplanator<?>> runningTasks = new LinkedList<>();
        for (AbstractFMAnalysis<?> analysis : analyses.keySet()) {
            if (!analysis.get()) {
                AbstractAnomalyExplanator<?> explanator = analyses.get(analysis);

                if (explanator != null) {
                    pool.execute(explanator);

                    runningTasks.add(explanator);
                }
            }
        }

        for (AbstractAnomalyExplanator<?> tasks : runningTasks) {
            tasks.join();
        }

        pool.shutdown();
    }
}
