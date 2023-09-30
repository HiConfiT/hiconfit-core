/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.kb.core;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConstraintTest {
    static Constraint c1;
    static Constraint c2;
    static Constraint c3;

    static org.chocosolver.solver.constraints.Constraint chocoCstr1;
    static org.chocosolver.solver.constraints.Constraint chocoCstr2;
    static org.chocosolver.solver.constraints.Constraint chocoCstr3;
    static org.chocosolver.solver.constraints.Constraint chocoCstr4;

    @BeforeAll
    static void setUp() {
        Model model = new Model();

        IntVar x = model.intVar("x", 0, 10);

        chocoCstr1 = model.arithm(x, "=", 1);
        chocoCstr2 = model.arithm(x, "=", 2);
        chocoCstr3 = model.arithm(x, "=", 3);
        chocoCstr4 = model.arithm(x, "=", 4);

        c1 = new Constraint("c1", List.of(x.getName())); // create a constraint using the constructor
        c1.addChocoConstraint(chocoCstr1);
        c1.addChocoConstraint(chocoCstr2);
        c1.addNegChocoConstraint(chocoCstr3);
        c1.addNegChocoConstraint(chocoCstr4);

        c2 = new Constraint("c1", List.of(x.getName()));
        c3 = Constraint.builder()
                .constraint("c2")
                .variables(List.of(x.getName()))
                .build(); // create a constraint using the builder
    }

    @Test
    void testConstraint() {
        assertAll(() -> assertEquals("c1", c1.getConstraint()),
                () -> assertEquals("ARITHM ([x = 1])", c1.getChocoConstraints().get(0).toString()),
                () -> assertEquals("ARITHM ([x = 2])", c1.getChocoConstraints().get(1).toString()),
                () -> assertEquals(2, c1.getChocoConstraints().size()),
                () -> assertEquals("ARITHM ([x = 3])", c1.getNegChocoConstraints().get(0).toString()),
                () -> assertEquals("ARITHM ([x = 4])", c1.getNegChocoConstraints().get(1).toString()),
                () -> assertEquals(2, c1.getNegChocoConstraints().size()),
                () -> assertEquals("c1", c1.toString()),
                () -> assertNotEquals("c2", c2.toString()),
                () -> assertEquals("c2", c3.toString()),
                () -> assertTrue(c1.contains(chocoCstr1)),
                () -> assertFalse(c1.contains(chocoCstr3)),
                () -> assertEquals(c1, c2),
                () -> assertNotEquals(c1, c3));
    }
}