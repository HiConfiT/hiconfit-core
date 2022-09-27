/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.test.builder;

import at.tugraz.ist.ase.cdrmodel.test.ITestCase;
import at.tugraz.ist.ase.cdrmodel.test.builder.ITestCaseBuildable;
import at.tugraz.ist.ase.cdrmodel.test.format.XMLTestSuiteFormat;
import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.fm.core.CTConstraint;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import at.tugraz.ist.ase.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.fma.anomaly.AnomalyType;
import at.tugraz.ist.ase.fma.anomaly.IAnomalyType;
import at.tugraz.ist.ase.fma.test.AssumptionAwareTestCase;
import at.tugraz.ist.ase.fma.test.format.XMLAssumptionAwareTestSuiteFormat;
import at.tugraz.ist.ase.kb.core.Assignment;
import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class XMLAssumptionAwareTestCaseBuilder implements ITestCaseBuildable {
    @Getter
    private final FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> featureModel;

    public XMLAssumptionAwareTestCaseBuilder(FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> featureModel) {
        this.featureModel = featureModel;
    }

    @Override
    public ITestCase buildTestCase(@NonNull Object testcase) {
        Preconditions.checkArgument(testcase instanceof Element, "The test case must be an XML Element");
        LoggerUtils.indent();

        Element testcaseEle = (Element) testcase;

        // get the anomaly type
        String anomalyString = testcaseEle.getAttribute(XMLAssumptionAwareTestSuiteFormat.ATT_ANOMALY);
        IAnomalyType anomalyType = AnomalyType.valueOf(anomalyString);

        Pair<List<Assignment>, List<AnomalyAwareFeature>> splitTestCases = splitTestCase(testcaseEle);

        AssumptionAwareTestCase testCase = AssumptionAwareTestCase.assumptionAwareTestCaseBuilder()
                .testcase(anomalyString.equals("REDUNDANT") ? "RedundancyAnalysis" : testCaseNodeToString(testcaseEle))
                .anomalyType(anomalyType)
                .assignments(splitTestCases.getValue0())
                .assumptions(splitTestCases.getValue1())
                .build();

        LoggerUtils.outdent();
        log.debug("{}<<< Built test case [testcase={}]", LoggerUtils.tab(), testCase);

        return testCase;
    }

    private Pair<List<Assignment>, List<AnomalyAwareFeature>> splitTestCase(Element testcase) {
        List<AnomalyAwareFeature> assumptions = new LinkedList<>();
        for (int assumptionIndex = 0; assumptionIndex  < testcase.getElementsByTagName(XMLAssumptionAwareTestSuiteFormat.TAG_ASSUMPTION).getLength(); assumptionIndex ++) {
            Element assumptionEle = (Element) testcase.getElementsByTagName(XMLAssumptionAwareTestSuiteFormat.TAG_ASSUMPTION).item(assumptionIndex);

            String name = assumptionEle.getAttribute(XMLAssumptionAwareTestSuiteFormat.ATT_NAME);
            String id = assumptionEle.getAttribute(XMLAssumptionAwareTestSuiteFormat.ATT_ID);

            try {
                AnomalyAwareFeature feature = featureModel.getFeature(id);
                if (!feature.getName().equals(name)) {
                    throw new RuntimeException("Feature has different name in feature model and test cases!");
                }
                assumptions.add(feature);
            } catch (Exception e) {
                throw new RuntimeException("The test cases are incompatible with the feature model!", e.getCause());
            }
        }

        List<Assignment> assignments = new LinkedList<>();
        for (int clauseIndex = 0; clauseIndex < testcase.getElementsByTagName(XMLTestSuiteFormat.TAG_CLAUSE).getLength(); clauseIndex++) {
            Element clause = (Element) testcase.getElementsByTagName(XMLTestSuiteFormat.TAG_CLAUSE).item(clauseIndex);

            String variable = clause.getAttribute(XMLTestSuiteFormat.TAG_VARIABLE);
            String value = clause.getAttribute(XMLTestSuiteFormat.TAG_VALUE);

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

        return new Pair<>(assignments, assumptions);
    }

    private String testCaseNodeToString(Element testcase) {
        NodeList clauses = testcase.getElementsByTagName(XMLTestSuiteFormat.TAG_CLAUSE);

        Element clause = (Element) clauses.item(0);
        String variable = clause.getAttribute(XMLTestSuiteFormat.TAG_VARIABLE);
        String value = clause.getAttribute(XMLTestSuiteFormat.TAG_VALUE);

        StringBuilder sb = new StringBuilder();
        if (value.equals("false")) {
            sb.append("~");
        }
        sb.append(variable);

        for (int clauseIndex = 1; clauseIndex < clauses.getLength(); clauseIndex++) {
            clause = (Element) clauses.item(clauseIndex);
            variable = clause.getAttribute(XMLTestSuiteFormat.TAG_VARIABLE);
            value = clause.getAttribute(XMLTestSuiteFormat.TAG_VALUE);

            sb.append(" & ");
            if (value.equals("false")) {
                sb.append("~");
            }
            sb.append(variable);
        }

        return sb.toString();
    }
}
