/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.kb.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DomainTest {
    static Domain d1;
    static Domain d2;
    static Domain d3;
    static Domain d4;
    static Domain d5;
    static Domain d6;

    @BeforeAll
    static void setUp() {
        d1 = new Domain("mb_cpuslotD",
                List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));

        d2 = new Domain("mb_cpuslotD_1",
                DomainType.INT,
                List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"),
                List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));

        d3 = new Domain("mb_cpuslotD_2"); // create a bool domain

        d4 = Domain.builder()
                .name("mb_cpuslotD")
                .values(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"))
                .build();

        d5 = Domain.builder()
                .name("mb_cpuslotD_1")
                .values(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"))
                .chocoValues(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11))
                .build();

        d6 = Domain.builder()
                .name("mb_cpuslotD_2")
                .build(); // create a bool domain
    }

    @Test
    void testConstructor() {
        assertAll(() -> assertEquals("mb_cpuslotD", d1.getName()),
                () -> assertEquals(DomainType.INT, d1.getType()),
                () -> assertEquals(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"), d1.getValues()),
                () -> assertEquals(11, d1.getValues().size()),
                () -> assertEquals(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10), d1.getChocoValues()),
                () -> assertEquals("mb_cpuslotD = [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ]", d1.toString()),
                () -> assertEquals(5, d1.indexOf("5")),
                () -> assertEquals("6", d1.getValue(6)),
                () -> assertTrue(d1.isDomainOf("mb_cpuslotD")),
                () -> assertFalse(d1.isDomainOf("mb_cpuslotD_1")));

        assertAll(() -> assertEquals("mb_cpuslotD_1", d2.getName()),
                () -> assertEquals(DomainType.INT, d2.getType()),
                () -> assertEquals(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"), d2.getValues()),
                () -> assertEquals(11, d2.getValues().size()),
                () -> assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), d2.getChocoValues()),
                () -> assertEquals("mb_cpuslotD_1 = [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ]", d2.toString()),
                () -> assertEquals(5, d2.indexOf("5")),
                () -> assertEquals("5", d2.getValue(6)));

        assertAll(() -> assertEquals("mb_cpuslotD_2", d3.getName()),
                () -> assertEquals(DomainType.BOOL, d3.getType()),
                () -> assertEquals(List.of("false", "true"), d3.getValues()),
                () -> assertEquals(2, d3.getValues().size()),
                () -> assertEquals(List.of(0, 1), d3.getChocoValues()),
                () -> assertEquals("mb_cpuslotD_2 = [ false, true ]", d3.toString()),
                () -> assertEquals(1, d3.indexOf("true")),
                () -> assertEquals("false", d3.getValue(0)));
    }

    @Test
    void testBuilder() {
        assertAll(() -> assertEquals("mb_cpuslotD", d4.getName()),
                () -> assertEquals(DomainType.INT, d4.getType()),
                () -> assertEquals(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"), d4.getValues()),
                () -> assertEquals(11, d4.getValues().size()),
                () -> assertEquals(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10), d4.getChocoValues()),
                () -> assertEquals("mb_cpuslotD = [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ]", d4.toString()));

        assertAll(() -> assertEquals("mb_cpuslotD_1", d5.getName()),
                () -> assertEquals(DomainType.INT, d5.getType()),
                () -> assertEquals(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"), d5.getValues()),
                () -> assertEquals(11, d5.getValues().size()),
                () -> assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), d5.getChocoValues()),
                () -> assertEquals("mb_cpuslotD_1 = [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ]", d5.toString()));

        assertAll(() -> assertEquals("mb_cpuslotD_2", d6.getName()),
                () -> assertEquals(DomainType.BOOL, d6.getType()),
                () -> assertEquals(List.of("false", "true"), d6.getValues()),
                () -> assertEquals(2, d6.getValues().size()),
                () -> assertEquals(List.of(0, 1), d6.getChocoValues()),
                () -> assertEquals("mb_cpuslotD_2 = [ false, true ]", d6.toString()));
    }

    @Test
    void testEquals() {
        assertAll(() -> assertEquals(d1, d4),
                () -> assertEquals(d2, d5),
                () -> assertEquals(d3, d6));
    }

    @Test
    void testClone() throws CloneNotSupportedException {
        Domain d7 = (Domain) d1.clone();

        assertAll(() -> assertEquals(d1, d7),
                () -> assertEquals("mb_cpuslotD", d7.getName()),
                () -> assertEquals(DomainType.INT, d7.getType()),
                () -> assertEquals(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"), d7.getValues()),
                () -> assertEquals(11, d7.getValues().size()),
                () -> assertEquals(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10), d7.getChocoValues()),
                () -> assertEquals("mb_cpuslotD = [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ]", d7.toString()),
                () -> assertEquals(5, d7.indexOf("5")),
                () -> assertEquals("6", d7.getValue(6)),
                () -> assertTrue(d7.isDomainOf("mb_cpuslotD")),
                () -> assertFalse(d7.isDomainOf("mb_cpuslotD_1")));
    }
}