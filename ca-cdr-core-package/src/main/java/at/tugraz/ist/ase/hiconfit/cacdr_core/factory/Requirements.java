/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr_core.factory;

import at.tugraz.ist.ase.hiconfit.cacdr_core.Assignment;
import at.tugraz.ist.ase.hiconfit.cacdr_core.Requirement;
import at.tugraz.ist.ase.hiconfit.cacdr_core.builder.RequirementBuilder;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Requirements {

    public Requirement fromAssignments(List<Assignment> assignments) {
        return Requirement.requirementBuilder()
                .assignments(assignments)
                .build();
    }

    /**
     * Example of a requirement: ""QFMT_V1=true,TELCLOCK=true,SELECT_MEMORY_MODEL=true,PERF_COUNTERS=true,HT_IRQ=false,DEBUG_PAGEALLOC=true"
     * @param stringUR a string of requirements
     * @return a {@link Requirement} object
     */
    public Requirement fromString(String stringUR) {
        RequirementBuilder builder = new RequirementBuilder();
        return builder.build(stringUR);
    }

}
