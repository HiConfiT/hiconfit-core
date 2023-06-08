/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.explanator;

import at.tugraz.ist.ase.hiconfit.common.ConstraintUtils;
import at.tugraz.ist.ase.hiconfit.kb.core.Constraint;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Set;

@UtilityClass
public class ExplanationUtils {
    public String convertToDescriptiveExplanation(List<Set<Constraint>> explanations, String anomaly) {
        return ExplanationColors.EXPLANATION + "\tExplanation(s) for " + anomaly + ":\n"
                + ConstraintUtils.convertToStringWithMessage(explanations, "Diagnosis", "\t\t", ",", true);
    }
}
