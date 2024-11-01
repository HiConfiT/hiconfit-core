/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cdrmodel.fm.factory;

import at.tugraz.ist.ase.hiconfit.cacdr_core.TestSuite;
import at.tugraz.ist.ase.hiconfit.cdrmodel.AbstractCDRModel;
import at.tugraz.ist.ase.hiconfit.cdrmodel.fm.FMDebuggingModel;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.Feature;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import lombok.NonNull;

/**
 * Factory for creating a CDR model for debugging task
 * + C = CF
 * + B = { f0 = true }
 * + Test cases
 * + hasNegativeConstraints = false
 * + rootConstraints = true
 * + cfInConflicts = true
 * + reversedConstraintsOrder = false
 * Output model can be used for the following algorithms: DirectDebug
 */
public class FMDebuggingModelFactory extends FMCdrModelFactory {

    protected TestSuite testSuite;

    public FMDebuggingModelFactory(@NonNull FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel,
                                   @NonNull TestSuite testSuite) {
        super(featureModel, true);
        this.testSuite = testSuite;
    }

    public static FMDebuggingModelFactory getInstance(@NonNull FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel,
                                                      @NonNull TestSuite testSuite) {
        return new FMDebuggingModelFactory(featureModel, testSuite);
    }

    @Override
    public AbstractCDRModel createModel() {
        FMDebuggingModel<Feature, AbstractRelationship<Feature>, CTConstraint> diagModel
                = new FMDebuggingModel<>(featureModel, testSuite);
        diagModel.initialize();

        return diagModel;
    }
}
