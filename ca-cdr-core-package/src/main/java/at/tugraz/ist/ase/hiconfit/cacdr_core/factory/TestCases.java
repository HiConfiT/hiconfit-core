/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr_core.factory;

import at.tugraz.ist.ase.hiconfit.cacdr_core.Assignment;
import at.tugraz.ist.ase.hiconfit.cacdr_core.TestCase;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class TestCases {

    /**
     * Create a test case
     * @param testcaseStr the test case string
     * @param assignments the list of assignments
     * @return a {@link TestCase} object
     */
    public TestCase fromAssignments(@NonNull String testcaseStr, @NonNull List<Assignment> assignments) {
        return TestCase.builder()
                .testcase(testcaseStr)
                .assignments(assignments)
                .build();
    }
}
