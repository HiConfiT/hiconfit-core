/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.explanation;

import at.tugraz.ist.ase.hiconfit.cacdr_core.ITestCase;
import at.tugraz.ist.ase.hiconfit.fma.analysis.AbstractFMAnalysis;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyType;
import lombok.NonNull;

import java.util.List;

public interface IAnalysisExplanable {
    <T extends ITestCase, F extends AnomalyAwareFeature>
    String getDescriptiveExplanation(@NonNull List<AbstractFMAnalysis<T, F>> analyses,
                                     @NonNull Class<?> analysisClass,
                                     @NonNull AnomalyType anomalyType);
}
