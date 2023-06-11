/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cdrmodel;

import at.tugraz.ist.ase.hiconfit.cacdr_core.ITestCase;

import java.util.Set;

public interface IDebuggingModel {
    /**
     * Gets the set of test cases.
     * @return the set of test cases.
     */
    Set<ITestCase> getTestcases();

    /**
     * Gets a corresponding {@link ITestCase} object of a textual testcase.
     * @param testcase a textual testcase.
     * @return a corresponding {@link ITestCase} object.
     */
    ITestCase getTestCase(String testcase);
}