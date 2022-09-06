/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.assumption;

import at.tugraz.ist.ase.cdrmodel.test.ITestCase;
import at.tugraz.ist.ase.cdrmodel.test.TestCase;
import at.tugraz.ist.ase.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.fm.core.CTConstraint;
import at.tugraz.ist.ase.fm.core.Feature;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import at.tugraz.ist.ase.kb.core.Assignment;
import lombok.NonNull;

import java.util.LinkedList;
import java.util.List;

/**
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 * @author: Tamim Burgstaller (tamim.burgstaller@student.tugraz.at)
 */
public class FullMandatoryAssumptions implements IFMAnalysisAssumptionCreatable {
    @Override
    public <F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint>
    List<ITestCase> createAssumptions(@NonNull FeatureModel<F, R, C> fm) {
        List<ITestCase> testCases = new LinkedList<>();
        for (int i = 1; i < fm.getNumOfFeatures(); i++) {
            Feature feature = fm.getFeature(i);

            String testcase = fm.getFeature(0).getName() + " = true & " + feature.getName() + " = false";
            List<Assignment> assignments = new LinkedList<>();
            assignments.add(Assignment.builder()
                    .variable(fm.getFeature(0).getName())
                    .value("true")
                    .build());
            assignments.add(Assignment.builder()
                    .variable(feature.getName())
                    .value("false")
                    .build());

            testCases.add(TestCase.builder()
                    .testcase(testcase)
                    .assignments(assignments).build());
        }
        return testCases;
    }
}
