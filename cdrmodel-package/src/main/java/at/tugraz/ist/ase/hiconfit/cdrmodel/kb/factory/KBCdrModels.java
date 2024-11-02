/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cdrmodel.kb.factory;

import at.tugraz.ist.ase.hiconfit.cacdr_core.Requirement;
import at.tugraz.ist.ase.hiconfit.cdrmodel.kb.KBCdrModel;
import at.tugraz.ist.ase.hiconfit.cdrmodel.kb.KBRequirementCdrModel;
import at.tugraz.ist.ase.hiconfit.kb.core.KB;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.val;

@UtilityClass
public class KBCdrModels {

    /**
     * Create a CDR model for a KB diagnosis/conflict detection task
     * + rootConstraints = true
     * + reversedConstraintsOrder = false
     * + hasNegativeConstraints = false
     * @param kb knowledge base
     * @return a FMCdrModel instance
     */
    public KBCdrModel createCdrModel(@NonNull KB kb) {
        val CdrModelFactory = KBCdrModelFactory.getInstance(kb);

        return (KBCdrModel)CdrModelFactory.createModel();
    }

    /**
     * Create a CDR model for a KB requirement diagnosis/conflict detection task
     * @param kb knowledge base
     * @param requirement requirement
     * @return a FMRequirementCdrModel instance
     */
    public KBRequirementCdrModel createRequirementCdrModel(@NonNull KB kb,
                                                           @NonNull Requirement requirement) {
        val requirementCdrModelFactory = KBRequirementCdrModelFactory.getInstance(kb, requirement);

        return (KBRequirementCdrModel)requirementCdrModelFactory.createModel();
    }
}
