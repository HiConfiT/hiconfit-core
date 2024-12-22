/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr_core;

import at.tugraz.ist.ase.hiconfit.cacdr_core.factory.Requirements;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequirementTest {
    @Test
    void test() {
        Requirement requirement = Requirement.requirementBuilder()
                .assignments(List.of(Assignment.builder().variable("a").value("1").build(),
                        Assignment.builder().variable("b").value("2").build()))
                .build();

        assertEquals(2, requirement.size());
    }

    @Test
    void test1() {
        Requirement requirement = Requirements.fromAssignments(List.of(Assignment.builder().variable("a").value("1").build(),
                Assignment.builder().variable("b").value("2").build()));

        assertEquals(2, requirement.size());
    }
}