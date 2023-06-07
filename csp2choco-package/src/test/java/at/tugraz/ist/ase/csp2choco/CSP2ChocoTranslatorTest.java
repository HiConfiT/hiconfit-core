/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.csp2choco;

import at.tugraz.ist.ase.common.IOUtils;
import org.chocosolver.solver.Model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static at.tugraz.ist.ase.common.ChocoSolverUtils.printConstraintsWithoutFormat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSP2ChocoTranslatorTest {

    static Model model;

    static Model createModel() {
        // create a model
        Model model = new Model("Test Model");
        // Decision variables
        model.intVar("x", -10, 10);
        model.intVar("y", -10, 10);

        return model;
    }

    @BeforeAll
    static void setUp() {
        try {
            ClassLoader classLoader = CSP2ChocoTranslatorTest.class.getClassLoader();
            InputStream inputStream = IOUtils.getInputStream(classLoader, "csp.mzn");

            // create a model with variables
            model = createModel();

            // translate the CSP
            CSP2ChocoTranslator.loadConstraints(inputStream, model);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTranslator() {
        printConstraintsWithoutFormat(model);

        assertAll(() -> assertEquals(3, model.getNbCstrs()),
                () -> assertEquals("ARITHM ([x >= 2])", model.getCstrs()[0].toString()),
                () -> assertEquals("ARITHM ([x <= 0])", model.getCstrs()[1].toString()),
                () -> assertEquals("ARITHM ([x =/= 1])", model.getCstrs()[2].toString()),
                () -> assertEquals(2, model.getNbVars()),
                () -> assertEquals("x", model.getVar(0).getName()),
                () -> assertEquals("y", model.getVar(1).getName()));
    }
}