/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cdrmodel.fm.factory;

import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.Feature;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Factory for creating a CDR model for a redundancy detection task
 * If cfInConflicts, then:
 *    + C = CF
 *    + B = {} - rootConstraints = false
 * else:
 *    + C = {}
 *    + B = CF - rootConstraints = false
 * + reversedConstraintsOrder = true
 * + hasNegativeConstraints = true
 * Output model can be used for the following algorithms: WipeOutR_FM
 */
@Getter
@Setter
public class FMRedundancyDetectionModelFactory extends FMCdrModelFactory {

    public FMRedundancyDetectionModelFactory(@NonNull FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel,
                                             boolean cfInConflicts) {
        super(featureModel, cfInConflicts);
        hasNegativeConstraints = true;
        rootConstraints = false;
        reversedConstraintsOrder = true;
    }

    public static FMRedundancyDetectionModelFactory getInstance(@NonNull FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel,
                                                boolean cfInConflicts) {
        return new FMRedundancyDetectionModelFactory(featureModel, cfInConflicts);
    }

    public static FMRedundancyDetectionModelFactory getInstance(@NonNull FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel) {
        return getInstance(featureModel, true);
    }
}
