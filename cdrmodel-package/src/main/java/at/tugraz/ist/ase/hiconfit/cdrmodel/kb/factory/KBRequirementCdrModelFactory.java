/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2023-2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cdrmodel.kb.factory;

import at.tugraz.ist.ase.hiconfit.cacdr_core.Requirement;
import at.tugraz.ist.ase.hiconfit.cdrmodel.AbstractCDRModel;
import at.tugraz.ist.ase.hiconfit.cdrmodel.IRequirementSetable;
import at.tugraz.ist.ase.hiconfit.cdrmodel.kb.KBRequirementCdrModel;
import at.tugraz.ist.ase.hiconfit.kb.core.KB;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Factory for creating a CDR model for KB requirement diagnosis/conflict detection task
 * If cfInConflicts, then:
 *     + C = CF + Requirement
 *     + B = {}
 * else:
 *     + C = Requirement
 *     + B = CF
 * Output model can be used for the following algorithms: FastDiag, QuickXPlain, HSDAG
 */
@Getter
@Setter
public class KBRequirementCdrModelFactory extends KBCdrModelFactory implements IRequirementSetable {

    protected Requirement requirement;

    public KBRequirementCdrModelFactory(@NonNull KB kb,
                                        Requirement requirement,
                                        boolean cfInConflicts,
                                        boolean reversedConstraintsOrder) {
        super(kb, reversedConstraintsOrder);
        this.cfInConflicts = cfInConflicts;
        this.requirement = requirement;
    }

    public static KBRequirementCdrModelFactory getInstance(@NonNull KB kb,
                                                           Requirement requirement,
                                                           boolean cfInConflicts,
                                                           boolean reversedConstraintsOrder) {
        return new KBRequirementCdrModelFactory(kb, requirement, cfInConflicts, reversedConstraintsOrder);
    }

    public static KBRequirementCdrModelFactory getInstance(@NonNull KB kb,
                                                           Requirement requirement) {
        return getInstance(kb, requirement, false, false);
    }

    @Override
    public AbstractCDRModel createModel() {
        checkArgument(requirement != null, "Requirement cannot be null");

        KBRequirementCdrModel diagModel
            = new KBRequirementCdrModel(kb, requirement, cfInConflicts, reversedConstraintsOrder);
        diagModel.initialize();

        return diagModel;
    }
}
