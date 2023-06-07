/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cdrmodel.test_model.csp;

import org.chocosolver.solver.Model;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CSPModelsTest {
    @Test
    void testModel1() throws IOException {
        Model model = CSPModels.createModel1();

        assertAll(() -> assertEquals(2, model.getNbVars()),
                () -> assertEquals(4, model.getNbCstrs()),
                () -> assertEquals("ARITHM ([x >= 2])", model.getCstrs()[0].toString()),
                () -> assertEquals("ARITHM ([y >= x + 1])", model.getCstrs()[1].toString()),
                () -> assertEquals("ARITHM ([x <= 0])", model.getCstrs()[2].toString()),
                () -> assertEquals("ARITHM ([y = -10])", model.getCstrs()[3].toString()));
    }

    @Test
    void testModel2() throws IOException {
        Model model = CSPModels.createModel2();

        assertAll(() -> assertEquals(3, model.getNbVars()),
                () -> assertEquals(7, model.getNbCstrs()),
                () -> assertEquals("ARITHM ([v2 = 1])", model.getCstrs()[0].toString()),
                () -> assertEquals("ARITHM ([v2 >= 3])", model.getCstrs()[1].toString()),
                () -> assertEquals("ARITHM ([v3 = 1])", model.getCstrs()[2].toString()),
                () -> assertEquals("ARITHM ([v2 <= 1])", model.getCstrs()[3].toString()),
                () -> assertEquals("ARITHM ([v1 >= 3])", model.getCstrs()[4].toString()),
                () -> assertEquals("ARITHM ([v3 >= 3])", model.getCstrs()[5].toString()),
                () -> assertEquals("ARITHM ([v1 >= 2])", model.getCstrs()[6].toString()));
    }

    @Test
    void testModel3() throws IOException {
        Model model = CSPModels.createModel3();

        assertAll(() -> assertEquals(3, model.getNbVars()),
                () -> assertEquals(7, model.getNbCstrs()),
                () -> assertEquals("ARITHM ([v2 = 1])", model.getCstrs()[0].toString()),
                () -> assertEquals("ARITHM ([v2 >= 3])", model.getCstrs()[1].toString()),
                () -> assertEquals("ARITHM ([v3 = 1])", model.getCstrs()[2].toString()),
                () -> assertEquals("ARITHM ([v2 <= 1])", model.getCstrs()[3].toString()),
                () -> assertEquals("ARITHM ([v1 >= 3])", model.getCstrs()[4].toString()),
                () -> assertEquals("ARITHM ([v3 >= 1])", model.getCstrs()[5].toString()),
                () -> assertEquals("ARITHM ([v1 <= 1])", model.getCstrs()[6].toString()));
    }

    @Test
    void testModel4() throws IOException {
        Model model = CSPModels.createModel4();

        assertAll(() -> assertEquals(3, model.getNbVars()),
                () -> assertEquals(7, model.getNbCstrs()),
                () -> assertEquals("ARITHM ([v2 = 1])", model.getCstrs()[0].toString()),
                () -> assertEquals("ARITHM ([v2 >= 3])", model.getCstrs()[1].toString()),
                () -> assertEquals("ARITHM ([v3 = 1])", model.getCstrs()[2].toString()),
                () -> assertEquals("ARITHM ([v1 = 2])", model.getCstrs()[3].toString()),
                () -> assertEquals("ARITHM ([v1 >= 2])", model.getCstrs()[4].toString()),
                () -> assertEquals("ARITHM ([v3 >= 1])", model.getCstrs()[5].toString()),
                () -> assertEquals("ARITHM ([v1 <= 2])", model.getCstrs()[6].toString()));
    }

    @Test
    void testModel5() throws IOException {
        Model model = CSPModels.createModel5();

        assertAll(() -> assertEquals(3, model.getNbVars()),
                () -> assertEquals(7, model.getNbCstrs()),
                () -> assertEquals("ARITHM ([v2 = 3])", model.getCstrs()[0].toString()),
                () -> assertEquals("ARITHM ([v2 >= 3])", model.getCstrs()[1].toString()),
                () -> assertEquals("ARITHM ([v3 = 1])", model.getCstrs()[2].toString()),
                () -> assertEquals("ARITHM ([v3 <= 1])", model.getCstrs()[3].toString()),
                () -> assertEquals("ARITHM ([v1 >= 3])", model.getCstrs()[4].toString()),
                () -> assertEquals("ARITHM ([v3 >= 1])", model.getCstrs()[5].toString()),
                () -> assertEquals("ARITHM ([v1 <= 1])", model.getCstrs()[6].toString()));
    }

    @Test
    void testModel6() throws IOException {
        Model model = CSPModels.createModel6();

        assertAll(() -> assertEquals(3, model.getNbVars()),
                () -> assertEquals(7, model.getNbCstrs()),
                () -> assertEquals("ARITHM ([prop(v2.EQ.v1)])", model.getCstrs()[0].toString()),
                () -> assertEquals("ARITHM ([v2 <= 1])", model.getCstrs()[1].toString()),
                () -> assertEquals("ARITHM ([v3 = 1])", model.getCstrs()[2].toString()),
                () -> assertEquals("ARITHM ([v3 >= 1])", model.getCstrs()[3].toString()),
                () -> assertEquals("ARITHM ([v1 >= 3])", model.getCstrs()[4].toString()),
                () -> assertEquals("ARITHM ([v3 >= 1])", model.getCstrs()[5].toString()),
                () -> assertEquals("ARITHM ([v1 = 2])", model.getCstrs()[6].toString()));
    }
}