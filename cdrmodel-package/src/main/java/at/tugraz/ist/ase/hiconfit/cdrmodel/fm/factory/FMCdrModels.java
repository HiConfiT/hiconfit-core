/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cdrmodel.fm.factory;

import at.tugraz.ist.ase.hiconfit.cacdr_core.Requirement;
import at.tugraz.ist.ase.hiconfit.cacdr_core.TestSuite;
import at.tugraz.ist.ase.hiconfit.cdrmodel.fm.FMCdrModel;
import at.tugraz.ist.ase.hiconfit.cdrmodel.fm.FMDebuggingModel;
import at.tugraz.ist.ase.hiconfit.cdrmodel.fm.FMRequirementCdrModel;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.Feature;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.val;

@UtilityClass
public class FMCdrModels {

    /**
     * Create a CDR model for a diagnosis/conflict detection task
     * + rootConstraints = true
     * + reversedConstraintsOrder = false
     * + hasNegativeConstraints = false
     * @param fm feature model
     * @param cfInConflicts CF in conflicts
     * @return a FMCdrModel instance
     */
    public FMCdrModel<Feature, AbstractRelationship<Feature>, CTConstraint>
        createCdrModel(@NonNull FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> fm,
                       boolean cfInConflicts) {
        val CdrModelFactory = FMCdrModelFactory.getInstance(fm, cfInConflicts);

        return (FMCdrModel<Feature, AbstractRelationship<Feature>, CTConstraint>)CdrModelFactory.createModel();
    }

    /**
     * Create a CDR model for a diagnosis/conflict detection task
     * @param fm feature model
     * @param hasNegativeConstraints has negative constraints
     * @param rootConstraints has the root constraint
     * @param cfInConflicts CF in conflicts
     * @param reversedConstraintsOrder reversed constraints order
     * @return a FMCdrModel instance
     */
    public FMCdrModel<Feature, AbstractRelationship<Feature>, CTConstraint>
        createCdrModel(@NonNull FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> fm,
                    boolean hasNegativeConstraints,
                    boolean rootConstraints,
                    boolean cfInConflicts,
                    boolean reversedConstraintsOrder) {
        val CdrModelFactory = FMCdrModelFactory.getInstance(fm, cfInConflicts);
        CdrModelFactory.setHasNegativeConstraints(hasNegativeConstraints);
        CdrModelFactory.setRootConstraints(rootConstraints);
        CdrModelFactory.setReversedConstraintsOrder(reversedConstraintsOrder);

        return (FMCdrModel<Feature, AbstractRelationship<Feature>, CTConstraint>)CdrModelFactory.createModel();
    }

    /**
     * Create a CDR model for a redundancy detection task
     * @param fm feature model
     * @return a FMCdrModel instance
     */
    public FMCdrModel<Feature, AbstractRelationship<Feature>, CTConstraint>
    createRedundancyDetectionModel(@NonNull FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> fm) {
        val RedundancyDetectionModelFactory = FMRedundancyDetectionModelFactory.getInstance(fm);

        return (FMCdrModel<Feature, AbstractRelationship<Feature>, CTConstraint>) RedundancyDetectionModelFactory.createModel();
    }

    /**
     * Create a CDR model for a redundancy detection task
     * @param fm feature model
     * @param cfInConflicts CF in conflicts
     * @return a FMCdrModel instance
     */
    public FMCdrModel<Feature, AbstractRelationship<Feature>, CTConstraint>
        createRedundancyDetectionModel(@NonNull FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> fm,
                                       boolean cfInConflicts) {
        val rdModelFactory = FMRedundancyDetectionModelFactory.getInstance(fm, cfInConflicts);

        return (FMCdrModel<Feature, AbstractRelationship<Feature>, CTConstraint>) rdModelFactory.createModel();
    }

    /**
     * Create a CDR model for a requirement diagnosis/conflict detection task
     * @param fm feature model
     * @param requirement requirement
     * @param cfInConflicts CF in conflicts
     * @return a FMRequirementCdrModel instance
     */
    public FMRequirementCdrModel<Feature, AbstractRelationship<Feature>, CTConstraint>
        createRequirementCdrModel(@NonNull FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> fm,
                                  @NonNull Requirement requirement,
                                  boolean cfInConflicts) {
        val requirementCdrModelFactory = FMRequirementCdrModelFactory.getInstance(fm, requirement, cfInConflicts);

        return (FMRequirementCdrModel<Feature, AbstractRelationship<Feature>, CTConstraint>)requirementCdrModelFactory.createModel();
    }

    /**
     * Create a CDR model for a requirement diagnosis/conflict detection task
     * @param fm feature model
     * @param requirement requirement
     * @return a FMRequirementCdrModel instance
     */
    public FMRequirementCdrModel<Feature, AbstractRelationship<Feature>, CTConstraint>
        createRequirementCdrModel(@NonNull FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> fm,
                                  @NonNull Requirement requirement) {
        val requirementCdrModelFactory = FMRequirementCdrModelFactory.getInstance(fm, requirement);

        return (FMRequirementCdrModel<Feature, AbstractRelationship<Feature>, CTConstraint>)requirementCdrModelFactory.createModel();
    }

    /**
     * Create a CDR model for a debugging task
     * @param fm feature model
     * @param testSuite test suite
     * @return a FMDebuggingModel instance
     */
    public FMDebuggingModel<Feature, AbstractRelationship<Feature>, CTConstraint>
        createDebuggingModel(@NonNull FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> fm,
                             @NonNull TestSuite testSuite) {
        val debuggingModelFactory = FMDebuggingModelFactory.getInstance(fm, testSuite);

        return (FMDebuggingModel<Feature, AbstractRelationship<Feature>, CTConstraint>)debuggingModelFactory.createModel();
    }
}
