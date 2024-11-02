/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2023-2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cdrmodel.fm.factory;

import at.tugraz.ist.ase.hiconfit.cdrmodel.AbstractCDRModel;
import at.tugraz.ist.ase.hiconfit.cdrmodel.ICdrModelFactory;
import at.tugraz.ist.ase.hiconfit.cdrmodel.fm.FMCdrModel;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.Feature;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Factory for creating a CDR model for a diagnosis/conflict detection task
 * If cfInConflicts, then:
 *     + C = CF
 *     + B = { f0 = true } - rootConstraints = true
 * else:
 *     + C = {}
 *     + B = { f0 = true } + CF - rootConstraints = true
 * + reversedConstraintsOrder = false
 * + hasNegativeConstraints = false
 * Output model can be used for the following algorithms: FastDiag, QuickXPlain, HSDAG
 */
@Getter
@Setter
public class FMCdrModelFactory implements ICdrModelFactory {

    protected @NonNull FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel;

    protected boolean hasNegativeConstraints = false;
    protected boolean rootConstraints = true;
    protected boolean cfInConflicts;
    protected boolean reversedConstraintsOrder = false;

    public FMCdrModelFactory(@NonNull FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel,
                             boolean cfInConflicts) {
        this.featureModel = featureModel;
        this.cfInConflicts = cfInConflicts;
    }

    public static FMCdrModelFactory getInstance(@NonNull FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel,
                                  boolean cfInConflicts) {
        return new FMCdrModelFactory(featureModel, cfInConflicts);
    }

    public static FMCdrModelFactory getInstance(@NonNull FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel) {
        return getInstance(featureModel, false);
    }

    @Override
    public AbstractCDRModel createModel() {
        FMCdrModel<Feature, AbstractRelationship<Feature>, CTConstraint> diagModel
                = new FMCdrModel<>(featureModel, hasNegativeConstraints,
                rootConstraints, cfInConflicts, reversedConstraintsOrder);
        diagModel.initialize();

        return diagModel;
    }
}
