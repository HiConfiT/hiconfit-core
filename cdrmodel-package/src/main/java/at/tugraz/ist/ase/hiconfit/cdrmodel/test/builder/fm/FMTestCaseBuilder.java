/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cdrmodel.test.builder.fm;

import at.tugraz.ist.ase.hiconfit.cdrmodel.test.TestCase;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test.builder.ITestCaseBuildable;
import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import at.tugraz.ist.ase.hiconfit.kb.core.Assignment;
import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class FMTestCaseBuilder implements ITestCaseBuildable {
    public TestCase buildTestCase(@NonNull Object testcase) {
        Preconditions.checkArgument(testcase instanceof String, "The test case must be a string");
        String testcaseStr = (String) testcase;

        log.trace("{}Building test case [testcase={}] >>>", LoggerUtils.tab(), testcase);
        LoggerUtils.indent();

        List<Assignment> assignments = splitTestCase(testcaseStr);

        TestCase testCase = TestCase.builder()
                .testcase(testcaseStr)
                .assignments(assignments)
                .build();

        LoggerUtils.outdent();
        log.debug("{}<<< Built test case [testcase={}]", LoggerUtils.tab(), testCase);

        return testCase;
    }

    private List<Assignment> splitTestCase(String testcase) {
        List<Assignment> assignments = new LinkedList<>();
        String[] clauses = testcase.split(" & ");

        for (String clause: clauses) {
            String variable;
            String value;
            if (clause.startsWith("~")) {
                value = "false";
                variable = clause.substring(1);
            } else {
                value = "true";
                variable = clause;
            }
            Assignment assignment = Assignment.builder()
                    .variable(variable)
                    .value(value)
                    .build();

            assignments.add(assignment);

            log.trace("{}Parsed assignment [clause={}, assignment={}]", LoggerUtils.tab(), clause, assignment);
        }
        return assignments;
    }
}