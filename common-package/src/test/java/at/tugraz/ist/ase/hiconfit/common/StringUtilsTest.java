/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringUtilsTest {
    @Test
    void replaceSpecialCharactersByUnderscore() {
        String st = "This is a test_string $%^&*";
        String expected = "This_is_a_test_string______";
        String actual = StringUtils.replaceSpecialCharactersByUnderscore(st);
        assertEquals(expected, actual);
    }
}