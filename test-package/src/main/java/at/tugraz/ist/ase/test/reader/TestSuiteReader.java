/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.test.reader;

import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.test.ITestCase;
import at.tugraz.ist.ase.test.TestSuite;
import at.tugraz.ist.ase.test.builder.ITestCaseBuildable;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TestSuiteReader implements ITestSuiteReadable {

    @Override
    public TestSuite read(@NonNull InputStream is, @NonNull ITestCaseBuildable testCaseBuilder) throws IOException {
        log.trace("{}Building test suite from input stream >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        @Cleanup BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        List<ITestCase>  testCases;

        br.readLine(); // omit first line

        // Read all test cases
        testCases = br.lines().map(testCaseBuilder::buildTestCase).collect(Collectors.toCollection(LinkedList::new));
        /*while ((line = br.readLine()) != null) {
            ITestCase testCase = testCaseBuilder.buildTestCase(line);
            testCases.add(testCase);
        }*/

        TestSuite testSuite = TestSuite.builder()
                .testCases(testCases)
                .build();

        LoggerUtils.outdent();
        log.debug("{}<<< Built test suite [testsuite={}]", LoggerUtils.tab(), testSuite);
        return testSuite;
    }
}
