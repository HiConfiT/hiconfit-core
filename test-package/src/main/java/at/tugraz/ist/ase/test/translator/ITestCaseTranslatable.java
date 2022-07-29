/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.test.translator;

import at.tugraz.ist.ase.test.ITestCase;
import org.chocosolver.solver.Model;

public interface ITestCaseTranslatable {
    /**
     * Translates a test case to Choco constraints.
     */
    void translate(ITestCase testCase, Model model);
}
