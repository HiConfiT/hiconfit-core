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

class ClauseTest {

    @Test
    void testClause() {
        Clause c1 = new Clause("A");
        Clause c2 = new Clause("~A");
        Clause c3 = new Clause("A");

        assertAll(() -> assertEquals("A = true", c1.toString()),
                () -> assertEquals("A", c1.getLiteral()),
                () -> assertTrue(c1.isPositive()),
                () -> assertEquals("A", c1.getClause()),
                () -> assertEquals("A = false", c2.toString()),
                () -> assertFalse(c2.isPositive()),
                () -> assertEquals("~A", c2.getClause()),
                () -> assertEquals(c1, c3),
                () -> assertNotEquals(c1, c2));
    }

    @Test
    void testBuilder() {
        Clause c1 = Clause.builder()
                .clause("A")
                .build();
        Clause c2 = Clause.builder()
                .clause("~A")
                .build();

        assertAll(() -> assertEquals("A = true", c1.toString()),
                () -> assertEquals("A", c1.getLiteral()),
                () -> assertTrue(c1.isPositive()),
                () -> assertEquals("A", c1.getClause()),
                () -> assertEquals("A = false", c2.toString()),
                () -> assertFalse(c2.isPositive()),
                () -> assertEquals("~A", c2.getClause()),
                () -> assertNotEquals(c1, c2));
    }
}