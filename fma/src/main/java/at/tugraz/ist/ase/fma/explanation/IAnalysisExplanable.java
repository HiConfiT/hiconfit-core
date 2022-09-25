/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.explanation;

import at.tugraz.ist.ase.fma.analysis.AbstractFMAnalysis;
import at.tugraz.ist.ase.fma.anomaly.AnomalyType;
import lombok.NonNull;

import java.util.List;

public interface IAnalysisExplanable {
    String getDescriptiveExplanation(@NonNull List<AbstractFMAnalysis<?>> analyses,
                                     @NonNull Class<? extends AbstractFMAnalysis<?>> analysisClass,
                                     @NonNull AnomalyType anomalyType);
}
