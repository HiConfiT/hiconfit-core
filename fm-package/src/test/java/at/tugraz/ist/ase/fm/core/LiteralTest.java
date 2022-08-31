/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LiteralTest {

    @Test
    void testClause() {
        Literal c1 = new Literal("A");
        Literal c2 = new Literal("~A");
        Literal c3 = new Literal("A");

        assertAll(() -> assertEquals("A = true", c1.toString()),
                () -> assertEquals("A", c1.getVariable()),
                () -> assertTrue(c1.isPositive()),
                () -> assertEquals("A", c1.getLiteral()),
                () -> assertEquals("A = false", c2.toString()),
                () -> assertFalse(c2.isPositive()),
                () -> assertEquals("~A", c2.getLiteral()),
                () -> assertEquals(c1, c3),
                () -> assertNotEquals(c1, c2));
    }

    @Test
    void testBuilder() {
        Literal c1 = Literal.builder()
                .literal("A")
                .build();
        Literal c2 = Literal.builder()
                .literal("~A")
                .build();

        assertAll(() -> assertEquals("A = true", c1.toString()),
                () -> assertEquals("A", c1.getVariable()),
                () -> assertTrue(c1.isPositive()),
                () -> assertEquals("A", c1.getLiteral()),
                () -> assertEquals("A = false", c2.toString()),
                () -> assertFalse(c2.isPositive()),
                () -> assertEquals("~A", c2.getLiteral()),
                () -> assertNotEquals(c1, c2));
    }
}