/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cdrmodel.test;

import lombok.*;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/***
 * Represents a test suite, i.e., a list of test cases.
 */
@Getter @Setter
@EqualsAndHashCode
public class TestSuite implements Cloneable {
    private List<ITestCase> testCases; // list of test cases

    @Builder
    public TestSuite(@NonNull List<ITestCase> testCases) {
        this.testCases = testCases;
    }

    /**
     * Gets the number of test cases.
     * @return the number of test cases.
     */
    public int size() {
        return this.testCases.size();
    }

    /**
     * Gets a corresponding {@link TestCase} object of a textual testcase.
     * @param testcase a textual testcase.
     * @return a corresponding {@link TestCase} object.
     */
    public ITestCase getTestCase(@NonNull String testcase) {
        for (ITestCase tc: testCases) {
            if (tc.toString().equals(testcase)) {
                return tc;
            }
        }
        throw new IllegalArgumentException("TestSuite does not contain the given test case [testcase=" + testcase + "]");
    }

    @Override
    public String toString() {
        return testCases.stream()
                .map(ITestCase::toString)
                .collect(Collectors.joining("\n"));
    }

    public Object clone() throws CloneNotSupportedException {
        TestSuite clone = (TestSuite) super.clone();
        // copy test cases
        List<ITestCase> testCases = new LinkedList<>();
        for (ITestCase testCase : this.testCases) {
            ITestCase cloneTestCase = (ITestCase) ((TestCase)testCase).clone();
            testCases.add(cloneTestCase);
        }
        clone.setTestCases(testCases);
        return clone;
    }

    public void dispose() {
        testCases = null;
    }
}
