/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomUtilsTest {
    @Test
    @DisplayName("Test random utils")
    public void testRandomUtils() {
        Assertions.assertAll(() -> assertTrue(RandomUtils.getRandomBoolean()),
                () -> assertTrue(RandomUtils.getRandomBoolean()),
                () -> assertFalse(RandomUtils.getRandomBoolean()),
                () -> assertFalse(RandomUtils.getRandomBoolean()),
                () -> assertFalse(RandomUtils.getRandomBoolean()),
                () -> assertFalse(RandomUtils.getRandomBoolean()),
                () -> assertTrue(RandomUtils.getRandomBoolean()),
                () -> assertFalse(RandomUtils.getRandomBoolean()),
                () -> assertTrue(RandomUtils.getRandomBoolean()),
                () -> assertFalse(RandomUtils.getRandomBoolean()),
                () -> assertEquals(4, RandomUtils.getRandomInt(10)),
                () -> assertEquals(2, RandomUtils.getRandomInt(10)),
                () -> assertEquals(0, RandomUtils.getRandomInt(10)),
                () -> assertEquals(2, RandomUtils.getRandomInt(10)),
                () -> assertEquals(0, RandomUtils.getRandomInt(10)),
                () -> assertEquals(4, RandomUtils.getRandomInt(10)),
                () -> assertEquals(8, RandomUtils.getRandomInt(10)),
                () -> assertEquals(6, RandomUtils.getRandomInt(10)),
                () -> assertEquals(3, RandomUtils.getRandomInt(10)),
                () -> assertEquals(4, RandomUtils.getRandomInt(10)));
    }

    @Test
    @DisplayName("Should a new random numbers with a new seed")
    public void testSetSeed() {
        RandomUtils.setSeed(10);
        Assertions.assertAll(() -> assertTrue(RandomUtils.getRandomBoolean()),
                () -> assertFalse(RandomUtils.getRandomBoolean()),
                () -> assertFalse(RandomUtils.getRandomBoolean()),
                () -> assertFalse(RandomUtils.getRandomBoolean()),
                () -> assertFalse(RandomUtils.getRandomBoolean()),
                () -> assertTrue(RandomUtils.getRandomBoolean()),
                () -> assertFalse(RandomUtils.getRandomBoolean()),
                () -> assertFalse(RandomUtils.getRandomBoolean()),
                () -> assertTrue(RandomUtils.getRandomBoolean()),
                () -> assertFalse(RandomUtils.getRandomBoolean()),
                () -> assertEquals(3, RandomUtils.getRandomInt(10)),
                () -> assertEquals(9, RandomUtils.getRandomInt(10)),
                () -> assertEquals(1, RandomUtils.getRandomInt(10)),
                () -> assertEquals(8, RandomUtils.getRandomInt(10)),
                () -> assertEquals(5, RandomUtils.getRandomInt(10)),
                () -> assertEquals(0, RandomUtils.getRandomInt(10)),
                () -> assertEquals(6, RandomUtils.getRandomInt(10)),
                () -> assertEquals(3, RandomUtils.getRandomInt(10)),
                () -> assertEquals(3, RandomUtils.getRandomInt(10)),
                () -> assertEquals(8, RandomUtils.getRandomInt(10)));
    }
}