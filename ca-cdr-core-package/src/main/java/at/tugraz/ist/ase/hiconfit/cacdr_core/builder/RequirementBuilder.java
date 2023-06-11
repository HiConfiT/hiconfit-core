/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr_core.builder;

import at.tugraz.ist.ase.hiconfit.cacdr_core.Assignment;
import at.tugraz.ist.ase.hiconfit.cacdr_core.Requirement;
import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class RequirementBuilder implements IRequirementBuildable {

    /**
     * Example of a requirement: ""QFMT_V1=true,TELCLOCK=true,SELECT_MEMORY_MODEL=true,PERF_COUNTERS=true,HT_IRQ=false,DEBUG_PAGEALLOC=true"
     * @param stringUR a string of requirements
     * @return a {@link Requirement} object
     */
    @Override
    public Requirement build(@NonNull String stringUR) {
        log.trace("{}Building user requirement from [ur={}] >>>", LoggerUtils.tab(), stringUR);
        LoggerUtils.indent();

        List<Assignment> assignments = new LinkedList<>();

        String[] tokens = stringUR.split(",");

        for (String token : tokens) {
            String[] items = token.split("=");

            String variable = items[0];
            String value = items[1];

            Assignment ur = Assignment.builder().variable(variable).value(value).build();

            assignments.add(ur);
        }

        Requirement requirement = Requirement.requirementBuilder()
                .assignments(assignments)
                .build();

        LoggerUtils.outdent();
        log.trace("{}Built a user requirement [ur={}]", LoggerUtils.tab(), assignments);

        return requirement;
    }
}
