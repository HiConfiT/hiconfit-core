/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.builder;

import at.tugraz.ist.ase.hiconfit.cacdr_core.ITestCase;
import at.tugraz.ist.ase.hiconfit.cacdr_core.TestSuite;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import at.tugraz.ist.ase.hiconfit.fma.FMAnalyzer;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyAwareFeature;
import lombok.NonNull;

public interface IAnalysisBuildable {

    <T extends ITestCase, F extends AnomalyAwareFeature>
    void build(@NonNull FeatureModel<F, AbstractRelationship<F>, CTConstraint> featureModel,
               @NonNull FMAnalyzer<T, F> analyzer) throws CloneNotSupportedException;

    <T extends ITestCase, F extends AnomalyAwareFeature>
    void build(@NonNull FeatureModel<F, AbstractRelationship<F>, CTConstraint> featureModel,
               @NonNull TestSuite testSuite,
               @NonNull FMAnalyzer<T, F> analyzer) throws CloneNotSupportedException;

}
