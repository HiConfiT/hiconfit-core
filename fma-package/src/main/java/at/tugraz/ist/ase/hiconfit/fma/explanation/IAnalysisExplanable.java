/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.explanation;

import at.tugraz.ist.ase.hiconfit.fma.analysis.AbstractFMAnalysis;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyType;
import lombok.NonNull;

import java.util.List;

public interface IAnalysisExplanable {
    String getDescriptiveExplanation(@NonNull List<AbstractFMAnalysis<?>> analyses,
                                     @NonNull Class<? extends AbstractFMAnalysis<?>> analysisClass,
                                     @NonNull AnomalyType anomalyType);
}
