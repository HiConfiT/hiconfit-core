/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.test;

import at.tugraz.ist.ase.cdrmodel.test.ITestCase;
import at.tugraz.ist.ase.cdrmodel.test.TestSuite;
import at.tugraz.ist.ase.fma.anomaly.AnomalyType;
import lombok.experimental.UtilityClass;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class TestSuiteUtils {
    public EnumSet<AnomalyType> getAnomalyTypes(TestSuite testSuite) {
        return testSuite.getTestCases().parallelStream()
                .map(TC -> (AnomalyType) ((AssumptionAwareTestCase) TC).getAnomalyType())
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(AnomalyType.class)));
    }

    public TestSuite getSpecificTestCases(TestSuite testSuite, AnomalyType anomalyType) {
        List<ITestCase> testCases = new LinkedList<>();

        for (ITestCase TC : testSuite.getTestCases()) {
            AssumptionAwareTestCase testCase = (AssumptionAwareTestCase) TC;
            if (testCase.getAnomalyType() == anomalyType) {
                testCases.add(TC);
            }
        }

        return TestSuite.builder().testCases(testCases).build();
    }
}
