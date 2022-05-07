/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cdrmodel.test.model;

import at.tugraz.ist.ase.kb.core.Constraint;
import org.chocosolver.solver.Model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestModel1Test {
    static TestModel1 testModel;
    static Constraint c1;
    static Constraint c2;
    static Constraint c3;
    static Constraint c4;
    static Constraint c5;

    @BeforeAll
    static void setUp() throws Exception {
        testModel = new TestModel1();
        testModel.initialize();

        c1 = new Constraint("ARITHM ([x >= 2])");
        c2 = new Constraint("ARITHM ([y >= x + 1])");
        c3 = new Constraint("ARITHM ([x <= 0])");
        c4 = new Constraint("ARITHM ([y = -10])");
        c5 = new Constraint("ARITHM ([y = 10])");
    }

    @Test
    void testModel() {
        Model model = testModel.getModel();

        assertAll(() -> assertEquals(2, model.getNbVars()));
    }

    @Test
    void testConstraints() {
        assertAll(() -> assertTrue(testModel.getCorrectConstraints().isEmpty()),
                () -> assertEquals(4, testModel.getPossiblyFaultyConstraints().size()),
                () -> assertTrue(testModel.getPossiblyFaultyConstraints().contains(c1)),
                () -> assertTrue(testModel.getPossiblyFaultyConstraints().contains(c2)),
                () -> assertTrue(testModel.getPossiblyFaultyConstraints().contains(c3)),
                () -> assertTrue(testModel.getPossiblyFaultyConstraints().contains(c4)),
                () -> assertFalse(testModel.getPossiblyFaultyConstraints().contains(c5)));
    }

    @Test
    void testExpectedResults() {
        assertAll(() -> assertEquals(2, testModel.getExpectedFirstConflict().size()),
                () -> assertTrue(testModel.getExpectedFirstConflict().contains(c1)),
                () -> assertTrue(testModel.getExpectedFirstConflict().contains(c3)),
                () -> assertEquals(2, testModel.getExpectedAllConflicts().size()),
                () -> assertTrue(testModel.getExpectedAllConflicts().get(0).contains(c1), "Expected first conflict"),
                () -> assertTrue(testModel.getExpectedAllConflicts().get(0).contains(c3), "Expected first conflict"),
                () -> assertTrue(testModel.getExpectedAllConflicts().get(1).contains(c2), "Expected second conflict"),
                () -> assertTrue(testModel.getExpectedAllConflicts().get(1).contains(c4), "Expected second conflict"),
                () -> assertEquals(2, testModel.getExpectedFirstDiagnosis().size()),
                () -> assertTrue(testModel.getExpectedFirstDiagnosis().contains(c1), "Expected first diagnosis"),
                () -> assertTrue(testModel.getExpectedFirstDiagnosis().contains(c2), "Expected first diagnosis"),
                () -> assertEquals(4, testModel.getExpectedAllDiagnoses().size()),
                () -> assertTrue(testModel.getExpectedAllDiagnoses().get(0).contains(c1), "Expected first diagnosis"),
                () -> assertTrue(testModel.getExpectedAllDiagnoses().get(0).contains(c2), "Expected first diagnosis"),
                () -> assertTrue(testModel.getExpectedAllDiagnoses().get(1).contains(c4), "Expected second diagnosis"),
                () -> assertTrue(testModel.getExpectedAllDiagnoses().get(1).contains(c1), "Expected second diagnosis"),
                () -> assertTrue(testModel.getExpectedAllDiagnoses().get(2).contains(c3), "Expected third diagnosis"),
                () -> assertTrue(testModel.getExpectedAllDiagnoses().get(2).contains(c2), "Expected third diagnosis"),
                () -> assertTrue(testModel.getExpectedAllDiagnoses().get(3).contains(c3), "Expected fourth diagnosis"),
                () -> assertTrue(testModel.getExpectedAllDiagnoses().get(3).contains(c4), "Expected fourth diagnosis")
        );

    }
}