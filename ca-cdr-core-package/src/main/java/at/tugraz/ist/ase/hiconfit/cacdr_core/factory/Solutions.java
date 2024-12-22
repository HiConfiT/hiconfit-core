/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr_core.factory;

import at.tugraz.ist.ase.hiconfit.cacdr_core.Assignment;
import at.tugraz.ist.ase.hiconfit.cacdr_core.Solution;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Solutions {

    public Solution fromAssignments(List<Assignment> assignments) {
        return Solution.builder()
                .assignments(assignments)
                .build();
    }

}
