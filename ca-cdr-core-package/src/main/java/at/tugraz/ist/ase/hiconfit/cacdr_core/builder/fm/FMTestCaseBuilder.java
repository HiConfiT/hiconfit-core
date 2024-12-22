/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr_core.builder.fm;

import at.tugraz.ist.ase.hiconfit.cacdr_core.Assignment;
import at.tugraz.ist.ase.hiconfit.cacdr_core.ITestCase;
import at.tugraz.ist.ase.hiconfit.cacdr_core.TestCase;
import at.tugraz.ist.ase.hiconfit.cacdr_core.builder.ITestCaseBuildable;
import at.tugraz.ist.ase.hiconfit.cacdr_core.factory.Assignments;
import at.tugraz.ist.ase.hiconfit.cacdr_core.factory.TestCases;
import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class FMTestCaseBuilder implements ITestCaseBuildable {
    @Override
    public ITestCase buildTestCase(@NonNull Object testcase) {
        Preconditions.checkArgument(testcase instanceof String, "The test case must be a string");
        String testcaseStr = (String) testcase;

        log.trace("{}Building test case [testcase={}] >>>", LoggerUtils.tab(), testcase);
        LoggerUtils.indent();

        List<Assignment> assignments = splitTestCase(testcaseStr);

//        TestCase testCase = TestCase.builder()
//                .testcase(testcaseStr)
//                .assignments(assignments)
//                .build();
        TestCase testCase = TestCases.fromAssignments(testcaseStr, assignments);

        LoggerUtils.outdent();
        log.debug("{}<<< Built test case [testcase={}]", LoggerUtils.tab(), testCase);

        return testCase;
    }

    protected List<Assignment> splitTestCase(String testcase) {
        List<Assignment> assignments = new LinkedList<>();
        String[] clauses = testcase.split(" & ");

        for (String clause: clauses) {
            // TODO check if the clause is valid
//            String variable;
//            String value;
//            if (clause.startsWith("~")) {
//                value = "false";
//                variable = clause.substring(1);
//            } else {
//                value = "true";
//                variable = clause;
//            }
//            Assignment assignment = Assignment.builder()
//                    .variable(variable)
//                    .value(value)
//                    .build();
            Assignment assignment = Assignments.fromClause(clause);

            assignments.add(assignment);

            log.trace("{}Parsed assignment [clause={}, assignment={}]", LoggerUtils.tab(), clause, assignment);
        }
        return assignments;
    }
}