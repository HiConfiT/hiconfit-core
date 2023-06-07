/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.common;

import at.tugraz.ist.ase.kb.core.Constraint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static at.tugraz.ist.ase.common.ConstraintUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class ConstraintUtilsTest {
    static Constraint c1;
    static Constraint c2;
    static Constraint c3;
    static Constraint c4;

    static Set<Constraint> diag1;
    static Set<Constraint> diag2;
    static Set<Constraint> diag3;
    static Set<Constraint> diag4;
    static Set<Constraint> diag5;
    static Set<Constraint> diag6;

    static Set<Constraint> diag7;

    static List<Set<Constraint>> allDiag;

    @BeforeAll
    static void setUp() {
        c1 = new Constraint("c1");
        c2 = new Constraint("c2");
        c3 = new Constraint("c3");
        c4 = new Constraint("c4");

        diag1 = Set.of(c1, c2, c3);
        diag2 = Set.of(c2, c3);

        allDiag = List.of(diag1, diag2);

        diag3 = Set.of(c2, c3);
        diag4 = Set.of(c1, c3); // no way
        diag5 = Set.of(c2, c4);
        diag6 = Set.of(c1, c2, c3, c4); // no new diagnosis, new conflict set

        diag7 = Set.of(c1, c4);
    }

    @Test
    void testConvertToString() {
        System.out.println("Constraints:");
        System.out.println(convertToString(diag1, "\n", "\t", false));

        System.out.println("Diagnoses:");
        System.out.println(convertToStringWithMessage(allDiag, "Diagnosis", "\t", ", ", true));
    }

    @Test
    void testContains() {

        assertAll(() -> assertTrue(containsAll(allDiag, diag1), "[containsAll] allDiag doesn't contain diag1"),
                () -> assertTrue(containsAll(allDiag, diag2), "[containsAll] allDiag doesn't contain diag2"),
                () -> assertTrue(containsAll(allDiag, diag3), "[containsAll] allDiag contains diag3"),
                () -> assertTrue(containsAll(allDiag, diag4), "[containsAll] allDiag doesn't contain diag4"),
                () -> assertFalse(containsAll(allDiag, diag5), "[containsAll] allDiag contains diag5"),
                () -> assertFalse(containsAll(allDiag, diag6), "[containsAll] allDiag contains diag6"),

                () -> assertTrue(allDiag.contains(diag1), "allDiag doesn't contain diag1"),
                () -> assertTrue(allDiag.contains(diag2), "allDiag doesn't contain diag2"),
                () -> assertTrue(allDiag.contains(diag3), "allDiag contains diag3"), // Prove that couldn't use contains to check
                () -> assertFalse(allDiag.contains(diag4), "allDiag contain diag4"));// Prove that couldn't use contains to check
    }

    @Test
    void testIsMinimal() {
        assertAll(() -> assertFalse(isMinimal(diag3, allDiag)),
                () -> assertTrue(isMinimal(diag4, allDiag)));

        assertAll(() -> assertTrue(containsAll(allDiag, diag4)), // no way
                () -> assertTrue(isMinimal(diag4, allDiag)));

        assertAll(() -> assertFalse(containsAll(allDiag, diag5)), // new diagnosis, new conflict set
                () -> assertTrue(isMinimal(diag5, allDiag)));

        assertAll(() -> assertFalse(containsAll(allDiag, diag6)), // no new diagnosis, new conflict set
                () -> assertFalse(isMinimal(diag6, allDiag)));

        assertAll(() -> assertTrue(hasIntersection(diag1, diag2)),
                () -> assertFalse(hasIntersection(diag2, diag7)));
    }
}