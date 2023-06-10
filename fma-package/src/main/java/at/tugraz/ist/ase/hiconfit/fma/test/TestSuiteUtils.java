/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.test;

import at.tugraz.ist.ase.hiconfit.cacdr_core.ITestCase;
import at.tugraz.ist.ase.hiconfit.cacdr_core.TestSuite;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyType;
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
