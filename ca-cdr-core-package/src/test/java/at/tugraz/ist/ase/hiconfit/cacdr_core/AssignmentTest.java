/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr_core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssignmentTest {
    @Test
    void testAssignment() {
        Assignment assignment = new Assignment("F1", "true");

        System.out.println(assignment);

        assertAll(()-> assertEquals(new Assignment("F1", "true"), assignment),
                ()-> assertEquals("F1", assignment.getVariable()),
                ()-> assertEquals("true", assignment.getValue()));
    }

    @Test
    void testAssignmentWithBuilder() {
        Assignment assignment = Assignment.builder()
                .variable("F1")
                .value("true")
                .build();

        System.out.println(assignment);

        assertAll(()-> assertEquals(new Assignment("F1", "true"), assignment),
                ()-> assertEquals("F1", assignment.getVariable()),
                ()-> assertEquals("true", assignment.getValue()));
    }

    @Test
    void testEquals() {
        Assignment assignment1 = new Assignment("F1", "true");
        Assignment assignment2 = new Assignment("F1", "true");

        assertAll(()-> assertEquals(assignment1, assignment2),
                ()-> assertEquals(assignment1.getVariable(), assignment2.getVariable()),
                ()-> assertEquals(assignment1.getValue(), assignment2.getValue()));
    }

    @Test
    void testCompareTo() {
        Assignment assignment1 = new Assignment("F1", "true");
        Assignment assignment2 = new Assignment("F1", "true");

        assertEquals(0, assignment1.compareTo(assignment2));
    }

    @Test
    void testNotEquals() {
        Assignment assignment1 = new Assignment("F1", "true");
        Assignment assignment2 = assignment1.withValue("false");

        assertAll(()-> assertNotEquals(assignment1, assignment2),
                ()-> assertEquals(assignment1.getVariable(), assignment2.getVariable()),
                ()-> assertNotEquals(assignment1.getValue(), assignment2.getValue()));
    }

    private static class AssignmentChild extends Assignment {
        public AssignmentChild(String variable, String value) {
            super(variable, value);
        }
    }

    @Test
    void testAssignmentChild() {
        Assignment assignment = new AssignmentChild("F1", "true");

        System.out.println(assignment);

        assertAll(()-> assertEquals(new AssignmentChild("F1", "true"), assignment),
                ()-> assertEquals("F1", assignment.getVariable()),
                ()-> assertEquals("true", assignment.getValue()));
    }

//    @Test
//    void shouldCloneable() throws CloneNotSupportedException {
//        Assignment assignment = new Assignment("F1", "true");
//        Assignment clone = (Assignment) assignment.clone();
//
//        assertAll(()-> assertEquals(assignment, clone),
//                ()-> assertEquals(assignment.getVariable(), clone.getVariable()),
//                ()-> assertEquals(assignment.getValue(), clone.getValue()));
//
//        clone.setVariable("F2");
//        clone.setValue("false");
//
//        assertAll(() -> assertEquals("true", assignment.getValue()),
//                () -> assertEquals("F1", assignment.getVariable()),
//                () -> assertEquals("false", clone.getValue()),
//                () -> assertEquals("F2", clone.getVariable()));
//    }
}