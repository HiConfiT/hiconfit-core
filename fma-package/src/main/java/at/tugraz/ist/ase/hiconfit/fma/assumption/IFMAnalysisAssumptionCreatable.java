/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.assumption;

import at.tugraz.ist.ase.hiconfit.cacdr_core.ITestCase;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.Feature;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import lombok.NonNull;

import java.util.List;

public interface IFMAnalysisAssumptionCreatable {
    <F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint>
    List<ITestCase> createAssumptions(@NonNull FeatureModel<F, R, C> fm);
}
