/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.test.builder;

import at.tugraz.ist.ase.hiconfit.cacdr_core.Assignment;
import at.tugraz.ist.ase.hiconfit.cacdr_core.ITestCase;
import at.tugraz.ist.ase.hiconfit.cacdr_core.builder.ITestCaseBuildable;
import at.tugraz.ist.ase.hiconfit.cacdr_core.format.XMLTestSuiteFormat;
import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyType;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.IAnomalyType;
import at.tugraz.ist.ase.hiconfit.fma.test.AssumptionAwareTestCase;
import at.tugraz.ist.ase.hiconfit.fma.test.format.XMLAssumptionAwareTestSuiteFormat;
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
public class XMLAssumptionAwareTestCaseBuilder<F extends AnomalyAwareFeature>
        implements ITestCaseBuildable {
    @Getter
    private final FeatureModel<F, AbstractRelationship<F>, CTConstraint> featureModel;

    public XMLAssumptionAwareTestCaseBuilder(FeatureModel<F, AbstractRelationship<F>, CTConstraint> featureModel) {
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

        Pair<List<Assignment>, List<F>> splitTestCases = splitTestCase(testcaseEle);
        List<F> assumptions = splitTestCases.getValue1();

        AssumptionAwareTestCase<F> testCase = (AssumptionAwareTestCase<F>) AssumptionAwareTestCase.assumptionAwareTestCaseBuilder()
                .testcase(anomalyString.equals("REDUNDANT") ? "RedundancyAnalysis" : testCaseNodeToString(testcaseEle))
                .anomalyType(anomalyType)
                .assignments(splitTestCases.getValue0())
                .assumptions((List<AnomalyAwareFeature>) splitTestCases.getValue1())
                .build();

        LoggerUtils.outdent();
        log.debug("{}<<< Built test case [testcase={}]", LoggerUtils.tab(), testCase);

        return testCase;
    }

    private Pair<List<Assignment>, List<F>> splitTestCase(Element testcase) {
        List<F> assumptions = new LinkedList<>();
        for (int assumptionIndex = 0; assumptionIndex  < testcase.getElementsByTagName(XMLAssumptionAwareTestSuiteFormat.TAG_ASSUMPTION).getLength(); assumptionIndex ++) {
            Element assumptionEle = (Element) testcase.getElementsByTagName(XMLAssumptionAwareTestSuiteFormat.TAG_ASSUMPTION).item(assumptionIndex);

            String name = assumptionEle.getAttribute(XMLAssumptionAwareTestSuiteFormat.ATT_NAME);
            String id = assumptionEle.getAttribute(XMLAssumptionAwareTestSuiteFormat.ATT_ID);

            try {
                F feature = featureModel.getFeature(id);
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
