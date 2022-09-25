/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma;

import at.tugraz.ist.ase.fma.analysis.AbstractFMAnalysis;
import at.tugraz.ist.ase.fma.monitor.IAnalysisMonitor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 * @author: Tamim Burgstaller (tamim.burgstaller@student.tugraz.at)
 */
@NoArgsConstructor
public class FMAnalyzer {
    @Getter
    private final List<AbstractFMAnalysis<?>> analyses = new LinkedList<>();

    @Setter
    protected IAnalysisMonitor monitor = null;

    public void addAnalysis(@NonNull AbstractFMAnalysis<?> analysis) {
        analyses.add(analysis);
    }

    public void run(boolean withDiagnosis) {
        ForkJoinPool pool = ForkJoinPool.commonPool();

        if (monitor != null) {
            monitor.setNumberOfTasks(analyses.size());
        }

        for (AbstractFMAnalysis<?> analysis : analyses) {
            analysis.setWithDiagnosis(withDiagnosis);
            pool.execute(analysis);
        }

        for (AbstractFMAnalysis<?> analysis : analyses) {
            analysis.join();

            if (monitor != null) {
                monitor.done();
            }
        }

        pool.shutdown();
    }

    public void reset() {
        analyses.clear();
    }
}
