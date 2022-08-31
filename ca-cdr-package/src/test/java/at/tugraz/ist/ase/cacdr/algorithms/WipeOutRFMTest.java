/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms;

import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.cdrmodel.fm.FMCdrModel;
import at.tugraz.ist.ase.cdrmodel.test_model.model.TestModel7;
import at.tugraz.ist.ase.cdrmodel.test_model.model.TestModel8;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import at.tugraz.ist.ase.fm.core.FeatureModelException;
import at.tugraz.ist.ase.fm.core.RelationshipType;
import at.tugraz.ist.ase.kb.core.Constraint;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static at.tugraz.ist.ase.common.ConstraintUtils.convertToString;
import static at.tugraz.ist.ase.eval.PerformanceEvaluator.reset;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WipeOutRFMTest {
    @Test
    void test1() throws IOException {
        TestModel7 testCaseModel = new TestModel7();
        testCaseModel.initialize();

        System.out.println("=========================================");
        System.out.println("Constraints translated from the text file:");
        System.out.println(convertToString(testCaseModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testCaseModel);

        List<Constraint> CF = new LinkedList<>(testCaseModel.getPossiblyFaultyConstraints());

        WipeOutR_FM wipeOut = new WipeOutR_FM(checker);

        reset();
        List<Constraint> newCF = wipeOut.run(CF);

        assertEquals(newCF, testCaseModel.getNonRedundantConstraints());

        System.out.println("Result constraints:");
        newCF.forEach(System.out::println);
    }

    @Test
    void test2() throws IOException {
        TestModel8 testCaseModel = new TestModel8();
        testCaseModel.initialize();

        System.out.println("=========================================");
        System.out.println("Constraints translated from the text file:");
        System.out.println(convertToString(testCaseModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testCaseModel);

        List<Constraint> CF = new LinkedList<>(testCaseModel.getPossiblyFaultyConstraints());

        WipeOutR_FM wipeOut = new WipeOutR_FM(checker);

        reset();
        List<Constraint> newCF = wipeOut.run(CF);

        assertEquals(newCF, testCaseModel.getNonRedundantConstraints());

        System.out.println("Result constraints:");
        newCF.forEach(System.out::println);
    }

    @Test
    void test3() throws FeatureModelException {
        FeatureModel fm = new FeatureModel();
        fm.addFeature("survey", "survey");
        fm.addFeature("pay", "pay");
        fm.addFeature("ABtesting", "ABtesting");
        fm.addFeature("statistics", "statistics");
        fm.addFeature("qa", "qa");
        fm.addFeature("license", "license");
        fm.addFeature("nonlicense", "nonlicense");
        fm.addFeature("multiplechoice", "multiplechoice");
        fm.addFeature("singlechoice", "singlechoice");
        fm.addRelationship(RelationshipType.MANDATORY, fm.getFeature("survey"), Collections.singletonList(fm.getFeature("pay")));
        fm.addRelationship(RelationshipType.OPTIONAL, fm.getFeature("ABtesting"), Collections.singletonList(fm.getFeature("survey")));
        fm.addRelationship(RelationshipType.MANDATORY, fm.getFeature("survey"), Collections.singletonList(fm.getFeature("statistics")));
        fm.addRelationship(RelationshipType.MANDATORY, fm.getFeature("survey"), Collections.singletonList(fm.getFeature("qa")));
        fm.addRelationship(RelationshipType.ALTERNATIVE, fm.getFeature("pay"), List.of(fm.getFeature("license"), fm.getFeature("nonlicense")));
        fm.addRelationship(RelationshipType.OR, fm.getFeature("qa"), List.of(fm.getFeature("multiplechoice"), fm.getFeature("singlechoice")));
        fm.addRelationship(RelationshipType.OPTIONAL, fm.getFeature("ABtesting"), Collections.singletonList(fm.getFeature("statistics")));
        fm.addConstraint(RelationshipType.REQUIRES, fm.getFeature("ABtesting"), Collections.singletonList(fm.getFeature("statistics")));
        fm.addConstraint(RelationshipType.EXCLUDES, fm.getFeature("ABtesting"), Collections.singletonList(fm.getFeature("nonlicense")));
        fm.addConstraint(RelationshipType.REQUIRES, fm.getFeature("ABtesting"), Collections.singletonList(fm.getFeature("survey")));

        FMCdrModel testCaseModel = new FMCdrModel(fm, true, false, true);
        testCaseModel.initialize();

        System.out.println("=========================================");
        System.out.println("Constraints translated from the text file:");
        System.out.println(convertToString(testCaseModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testCaseModel);

        List<Constraint> CF = new LinkedList<>(testCaseModel.getPossiblyFaultyConstraints());

        WipeOutR_FM wipeOut = new WipeOutR_FM(checker);

        reset();
        List<Constraint> newCF = wipeOut.run(CF);

        System.out.println("Result constraints:");
        newCF.forEach(System.out::println);

        // test
        CF.remove(0); // remove requires(ABtesting, survey)
        CF.remove(1); // remove requires(ABtesting, statistics)
        CF.remove(1); // remove optional(ABtesting, statistics)
        assertEquals(CF, newCF);
    }
}