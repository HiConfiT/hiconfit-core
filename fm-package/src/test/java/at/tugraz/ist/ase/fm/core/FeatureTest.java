/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeatureTest {

    static Feature feature;

    @BeforeAll
    static void setUp() {
        feature = new Feature("F1", "ID");
    }

    @Test
    public void testFeature() {
        assertAll(() -> assertEquals("F1", feature.getName()),
                () -> assertEquals("ID", feature.getId()),
                () -> assertEquals("F1", feature.toString()));
    }

    @Test
    public void testException() {
        assertAll(() -> assertThrows(NullPointerException.class, () -> new Feature(null, null)),
                () -> assertThrows(NullPointerException.class, () -> new Feature("f", null)));
    }

    @Test
    void isDuplicate() {
        Feature f = Feature.builder()
                .name("F1")
                .id("ID").build();
        Feature f1 = new Feature("F1", "ID2");

        assertAll(() -> assertTrue(feature.isDuplicate(f)),
                () -> assertFalse(feature.isDuplicate(f1)),
                () -> assertTrue(feature.isIdDuplicate("ID")),
                () -> assertFalse(feature.isIdDuplicate("ID2")),
                () -> assertTrue(feature.isNameDuplicate("F1")),
                () -> assertFalse(feature.isNameDuplicate("F2")));
    }
}