/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.kb.core;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VariableTest {
    static Variable v1;
    static Variable v2;

    @BeforeAll
    static void setUp() {
        Model model = new Model();

        Domain d1 = Domain.builder()
                .name("mb_cpuslotD")
                .values(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"))
                .build();
        IntVar intVar = model.intVar("mb_cpuslotD", d1.getIntValues());

        v1 = IntVariable.builder()
                .name("mb_cpuslotD")
                .domain(d1)
                .chocoVar(intVar)
                .build();

        Domain d2 = Domain.builder()
                .name("mb_cpuslotD_2")
                .build();
        BoolVar boolVar = model.boolVar("mb_cpuslotD_2");

        v2 = BoolVariable.builder()
                .name("mb_cpuslotD_2")
                .domain(d2)
                .chocoVar(boolVar)
                .build();

        model.arithm(intVar, "=", 10).post();
        model.addClauseFalse(boolVar);
        model.getSolver().solve();
    }

    @Test
    void testConstructor() {
        assertAll(() -> assertEquals("mb_cpuslotD", v1.getName()),
                () -> assertEquals("mb_cpuslotD", v1.getDomain().getName()),
                () -> assertEquals(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"), v1.getDomain().getValues()),
                () -> assertEquals(11, v1.getDomain().size()),
                () -> assertEquals(DomainType.INT, v1.getDomain().getType()),
                () -> assertEquals("10", v1.getValue()),
                () -> assertEquals(10, v1.getChocoValue()));

        assertAll(() -> assertEquals("mb_cpuslotD_2", v2.getName()),
                () -> assertEquals("mb_cpuslotD_2", v2.getDomain().getName()),
                () -> assertEquals(List.of("false", "true"), v2.getDomain().getValues()),
                () -> assertEquals(2, v2.getDomain().size()),
                () -> assertEquals(DomainType.BOOL, v2.getDomain().getType()),
                () -> assertEquals("false", v2.getValue()),
                () -> assertEquals(0, v2.getChocoValue()));
    }

    @Test
    void testAssignable() {
        assertAll(() -> assertTrue(v1.isAssignable("10")),
                () -> assertTrue(v1.isAssignable("0")),
                () -> assertFalse(v1.isAssignable("11")),
                () -> assertFalse(v1.isAssignable("-1")),
                () -> assertThrows(NullPointerException.class, () -> v1.isAssignable(null)),
                () -> assertFalse(v1.isAssignable("a")),
                () -> assertFalse(v1.isAssignable("a1")),
                () -> assertFalse(v1.isAssignable("1a")),
                () -> assertTrue(v1.isAssignable("NULL")),
                () -> assertTrue(v1.isAssignable("")));

        assertAll(() -> assertTrue(v2.isAssignable("true")),
                () -> assertTrue(v2.isAssignable("false")),
                () -> assertTrue(v2.isAssignable("")),
                () -> assertThrows(NullPointerException.class, () -> v2.isAssignable(null)),
                () -> assertFalse(v2.isAssignable("a")),
                () -> assertFalse(v2.isAssignable("a1")),
                () -> assertFalse(v2.isAssignable("1a")));
    }

    @Test
    void testCloneable() throws CloneNotSupportedException {
        Variable v3 = (Variable) v1.clone();
        assertAll(() -> assertEquals("mb_cpuslotD", v3.getName()),
                () -> assertEquals("mb_cpuslotD", v3.getDomain().getName()),
                () -> assertEquals(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"), v3.getDomain().getValues()),
                () -> assertEquals(11, v3.getDomain().size()),
                () -> assertEquals(DomainType.INT, v3.getDomain().getType()),
                () -> assertEquals("10", v3.getValue()),
                () -> assertEquals(10, v3.getChocoValue()));
    }
}