/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr_core.factory;

import at.tugraz.ist.ase.hiconfit.cacdr_core.ITestCase;
import at.tugraz.ist.ase.hiconfit.cacdr_core.TestSuite;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class TestSuites {

    public TestSuite fromTestCases(List<ITestCase> testCases) {
        return TestSuite.builder()
                .testCases(testCases)
                .build();
    }

}
