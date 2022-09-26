/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma;

import at.tugraz.ist.ase.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.fm.core.CTConstraint;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import at.tugraz.ist.ase.fma.analysis.AbstractFMAnalysis;
import at.tugraz.ist.ase.fma.analysis.AnalysisUtils;
import at.tugraz.ist.ase.fma.analysis.VoidFMAnalysis;
import at.tugraz.ist.ase.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.fma.anomaly.AnomalyType;
import at.tugraz.ist.ase.fma.builder.IAnalysisBuildable;
import at.tugraz.ist.ase.fma.monitor.IAnalysisMonitor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 * @author: Tamim Burgstaller (tamim.burgstaller@student.tugraz.at)
 */
public class FMAnalyzer {
    @Getter
    protected final FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> fm;

    @Getter
    protected final List<AbstractFMAnalysis<?>> analyses = new LinkedList<>();

    @Setter
    protected IAnalysisMonitor monitor = null;

    public FMAnalyzer(@NonNull FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> fm) {
        this.fm = fm;
    }

    public void addAnalysis(@NonNull AbstractFMAnalysis<?> analysis) {
        analyses.add(analysis);
    }

    /**
     * Generates analyses and runs them
     */
    public void generateAndRun(@NonNull EnumSet<AnomalyType> anomalyTypes, boolean withDiagnosis) throws CloneNotSupportedException {
        IAnalysisBuildable builder;

        // generate VoidFMAnalysis and execute it
        builder = AnomalyType.VOID.getBuilder();
        builder.build(fm, this);

        execute(withDiagnosis);

        // if VoidFMAnalysis is violated, then no need to run other analyses
        AbstractFMAnalysis<?> voidFMAnalysis = getVoidFMAnalysis();
        if (voidFMAnalysis != null && !voidFMAnalysis.isNon_violated()) {
            return;
        }

        // generate DeadFeatureAnalysis and execute them
        builder = AnomalyType.DEAD.getBuilder();
        builder.build(fm, this);

        execute(withDiagnosis);

        // generate other analyses and execute them
        for (AnomalyType anomalyType : anomalyTypes) {
            if (anomalyType != AnomalyType.VOID && anomalyType != AnomalyType.DEAD) {
                builder = anomalyType.getBuilder();
                builder.build(fm, this);
            }
        }

        execute(withDiagnosis);
    }

    private void execute(boolean withDiagnosis) {
        ForkJoinPool pool = ForkJoinPool.commonPool();

        if (monitor != null) {
            monitor.setNumberOfTasks(analyses.size());
        }

        List<AbstractFMAnalysis<?>> notExecutedAnalyses = AnalysisUtils.getNotExecutedAnalyses(analyses);

        for (AbstractFMAnalysis<?> analysis : notExecutedAnalyses) {
            analysis.setWithDiagnosis(withDiagnosis);
            pool.execute(analysis);
        }

        for (AbstractFMAnalysis<?> analysis : notExecutedAnalyses) {
            analysis.join();

            if (monitor != null) {
                monitor.done();
            }
        }

        pool.shutdown();
    }

    /**
     * Runs the added analyses
     * Uses this functions to execute analyses read from a file
     * @param withDiagnosis if true, the diagnosis is identified
     */
    public void run(boolean withDiagnosis) {
        // run the VoidFMAnalysis first
        boolean isNotVoid = true;
        AbstractFMAnalysis<?> voidFMAnalysis = getVoidFMAnalysis();
        if (voidFMAnalysis != null) {
            ForkJoinPool pool = ForkJoinPool.commonPool();

            if (monitor != null) {
                monitor.setNumberOfTasks(analyses.size());
            }

            voidFMAnalysis.setWithDiagnosis(withDiagnosis);
            pool.execute(voidFMAnalysis);

            voidFMAnalysis.join();

            if (monitor != null) {
                monitor.done(); // done with VoidFMAnalysis
            }

            isNotVoid = voidFMAnalysis.isNon_violated();
            pool.shutdown();
        }

        if (isNotVoid) {
            execute(withDiagnosis);
        }
    }

    public void reset() {
        analyses.clear();
    }

    private AbstractFMAnalysis<?> getVoidFMAnalysis() {
        List<AbstractFMAnalysis<?>> voidFMAnalysis = AnalysisUtils.getAnalyses(analyses, VoidFMAnalysis.class);

        if (voidFMAnalysis.isEmpty()) {
            return null;
        }

        return voidFMAnalysis.get(0);
    }
}
