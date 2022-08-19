/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.kb.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AssignmentTest {
    @Test
    public void testAssignment() {
        Assignment assignment = new Assignment("F1", "true");

        System.out.println(assignment);

        assertAll(()-> assertEquals(new Assignment("F1", "true"), assignment),
                ()-> assertEquals("F1", assignment.getVariable()),
                ()-> assertEquals("true", assignment.getValue()));
    }

    @Test
    public void shouldCloneable() throws CloneNotSupportedException {
        Assignment assignment = new Assignment("F1", "true");
        Assignment clone = (Assignment) assignment.clone();

        assertAll(()-> assertEquals(assignment, clone),
                ()-> assertEquals(assignment.getVariable(), clone.getVariable()),
                ()-> assertEquals(assignment.getValue(), clone.getValue()));

        clone.setVariable("F2");
        clone.setValue("false");

        assertAll(() -> assertEquals("true", assignment.getValue()),
                () -> assertEquals("F1", assignment.getVariable()),
                () -> assertEquals("false", clone.getValue()),
                () -> assertEquals("F2", clone.getVariable()));
    }
}