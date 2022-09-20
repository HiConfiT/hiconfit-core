/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cdrmodel.test.builder.fm;

import at.tugraz.ist.ase.cdrmodel.test.ITestCase;
import at.tugraz.ist.ase.cdrmodel.test.TestCase;
import at.tugraz.ist.ase.cdrmodel.test.builder.ITestCaseBuildable;
import at.tugraz.ist.ase.cdrmodel.test.reader.XMLTestSuiteReader;
import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.kb.core.Assignment;
import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class XMLTestCaseBuilder implements ITestCaseBuildable {
    @Override
    public ITestCase buildTestCase(@NonNull Object testcase) {
        Preconditions.checkArgument(testcase instanceof Element, "The test case must be an XML Element");
        Element testcaseEle = (Element) testcase;

        LoggerUtils.indent();

        List<Assignment> assignments = splitTestCase(testcaseEle);

        TestCase testCase = TestCase.builder()
                .testcase(testCaseNodeToString(testcaseEle))
                .assignments(assignments)
                .build();

        LoggerUtils.outdent();
        log.debug("{}<<< Built test case [testcase={}]", LoggerUtils.tab(), testCase);

        return testCase;
    }

    private List<Assignment> splitTestCase(Element testcase) {
        List<Assignment> assignments = new LinkedList<>();
        for (int clauseIndex = 0; clauseIndex < testcase.getElementsByTagName(XMLTestSuiteReader.TAG_CLAUSE).getLength(); clauseIndex++) {
            Element clause = (Element) testcase.getElementsByTagName(XMLTestSuiteReader.TAG_CLAUSE).item(clauseIndex);

            String variable;
            String value;

            NodeList variableList = clause.getElementsByTagName(XMLTestSuiteReader.TAG_VARIABLE);
            NodeList valueList = clause.getElementsByTagName(XMLTestSuiteReader.TAG_VALUE);
            if (variableList.getLength() != 1 || valueList.getLength() != 1) {
                throw new RuntimeException("There is a clause with not exactly one variable and/or value!");
            }

            Element variableEle = (Element) variableList.item(0);
            Element valueEle = (Element) valueList.item(0);

            variable = variableEle.getChildNodes().item(0).getNodeValue();
            value = valueEle.getChildNodes().item(0).getNodeValue();

            if (!(value.equals("true") || value.equals("false"))) {
                throw new RuntimeException("Assignment to a variable must be boolean!");
            }

            Assignment assignment = Assignment.builder()
                    .variable(variable)
                    .value(value)
                    .build();
            assignments.add(assignment);

            log.trace("{}Parsed assignment [clause={}, assignment={}]", LoggerUtils.tab(), variable, assignment);
        }

        return assignments;
    }

    private String testCaseNodeToString(Element testcase) {
        NodeList clauses = testcase.getElementsByTagName(XMLTestSuiteReader.TAG_CLAUSE);

        Element firstClause = (Element) clauses.item(0);
        StringBuilder sb = new StringBuilder()
                .append(clauses.item(0).getChildNodes().item(0).getNodeValue())
                .append(" = ")
                .append(clauses.item(0).getChildNodes().item(1).getNodeValue());

        for (int clauseIndex = 1; clauseIndex < clauses.getLength(); clauseIndex++) {
            sb.append(" & ")
                    .append(clauses.item(clauseIndex).getChildNodes().item(0).getNodeValue())
                    .append(" = ")
                    .append(clauses.item(clauseIndex).getChildNodes().item(1).getNodeValue());
        }

        return sb.toString();
    }
}
