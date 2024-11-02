/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2023-2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cdrmodel.fm.factory;

import at.tugraz.ist.ase.hiconfit.cacdr_core.Requirement;
import at.tugraz.ist.ase.hiconfit.cdrmodel.AbstractCDRModel;
import at.tugraz.ist.ase.hiconfit.cdrmodel.IRequirementSetable;
import at.tugraz.ist.ase.hiconfit.cdrmodel.fm.FMRequirementCdrModel;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.Feature;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Factory for creating a CDR model for requirement diagnosis/conflict detection task
 * If cfInConflicts, then:
 *     + C = CF + Requirement
 *     + B = { f0 = true } - rootConstraints = true
 * else:
 *     + C = Requirement
 *     + B = { f0 = true } + CF - rootConstraints = true
 * + reversedConstraintsOrder = false
 * + hasNegativeConstraints = false
 * Output model can be used for the following algorithms: FastDiag, QuickXPlain, HSDAG
 */
@Getter
@Setter
public class FMRequirementCdrModelFactory extends FMCdrModelFactory implements IRequirementSetable {

    protected Requirement requirement;

    public FMRequirementCdrModelFactory(@NonNull FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel,
                                        Requirement requirement,
                                        boolean cfInConflicts) {
        super(featureModel, cfInConflicts);
        this.requirement = requirement;
    }

    public static FMRequirementCdrModelFactory getInstance(@NonNull FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel,
                                                           Requirement requirement,
                                                           boolean cfInConflicts) {
        return new FMRequirementCdrModelFactory(featureModel, requirement, cfInConflicts);
    }

    public static FMRequirementCdrModelFactory getInstance(@NonNull FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel,
                                                           Requirement requirement) {
        return getInstance(featureModel, requirement, false);
    }

    @Override
    public AbstractCDRModel createModel() {
        checkArgument(requirement != null, "Requirement cannot be null");

        FMRequirementCdrModel<Feature, AbstractRelationship<Feature>, CTConstraint> diagModel
            = new FMRequirementCdrModel<>(featureModel, requirement, hasNegativeConstraints,
                rootConstraints, cfInConflicts, reversedConstraintsOrder);
        diagModel.initialize();

        return diagModel;
    }
}
