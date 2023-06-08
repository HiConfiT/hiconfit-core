/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr.algorithms;

import at.tugraz.ist.ase.hiconfit.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.hiconfit.cdrmodel.fm.FMCdrModel;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test_model.model.TestModel7;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test_model.model.TestModel8;
import at.tugraz.ist.ase.hiconfit.common.ConstraintUtils;
import at.tugraz.ist.ase.hiconfit.eval.PerformanceEvaluator;
import at.tugraz.ist.ase.hiconfit.fm.builder.ConstraintBuilder;
import at.tugraz.ist.ase.hiconfit.fm.builder.FeatureBuilder;
import at.tugraz.ist.ase.hiconfit.fm.builder.RelationshipBuilder;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.Feature;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import at.tugraz.ist.ase.hiconfit.fm.translator.ConfRuleTranslator;
import at.tugraz.ist.ase.hiconfit.kb.core.Constraint;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WipeOutRFMTest {
    @Test
    void test1() throws IOException {
        TestModel7 testCaseModel = new TestModel7();
        testCaseModel.initialize();

        System.out.println("=========================================");
        System.out.println("Constraints translated from the text file:");
        System.out.println(ConstraintUtils.convertToString(testCaseModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testCaseModel);

        List<Constraint> CF = new LinkedList<>(testCaseModel.getPossiblyFaultyConstraints());

        WipeOutR_FM wipeOut = new WipeOutR_FM(checker);

        PerformanceEvaluator.reset();
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
        System.out.println(ConstraintUtils.convertToString(testCaseModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testCaseModel);

        List<Constraint> CF = new LinkedList<>(testCaseModel.getPossiblyFaultyConstraints());

        WipeOutR_FM wipeOut = new WipeOutR_FM(checker);

        PerformanceEvaluator.reset();
        List<Constraint> newCF = wipeOut.run(CF);

        assertEquals(newCF, testCaseModel.getNonRedundantConstraints());

        System.out.println("Result constraints:");
        newCF.forEach(System.out::println);
    }

    @Test
    void test3() {
        ConfRuleTranslator translator = new ConfRuleTranslator();
        FeatureBuilder featureBuilder = new FeatureBuilder();
        RelationshipBuilder relationshipBuilder = new RelationshipBuilder(translator);
        ConstraintBuilder constraintBuilder = new ConstraintBuilder(translator);

        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> fm = new FeatureModel<>("test", featureBuilder, relationshipBuilder, constraintBuilder);
        Feature survey = fm.addRoot("survey", "survey");
        Feature pay = fm.addFeature("pay", "pay");
        Feature ABtesting = fm.addFeature("ABtesting", "ABtesting");
        Feature statistics = fm.addFeature("statistics", "statistics");
        Feature qa = fm.addFeature("qa", "qa");
        Feature license = fm.addFeature("license", "license");
        Feature nonlicense = fm.addFeature("nonlicense", "nonlicense");
        Feature multiplechoice = fm.addFeature("multiplechoice", "multiplechoice");
        Feature singlechoice = fm.addFeature("singlechoice", "singlechoice");

        fm.addMandatoryRelationship(survey, pay);
        fm.addOptionalRelationship(survey, ABtesting);
        fm.addMandatoryRelationship(survey, statistics);
        fm.addMandatoryRelationship(survey, qa);
        fm.addAlternativeRelationship(pay, List.of(license, nonlicense));
        fm.addOrRelationship(qa, List.of(multiplechoice, singlechoice));
        fm.addOptionalRelationship(statistics, ABtesting);

        fm.addRequires(ABtesting, statistics);
        fm.addExcludes(ABtesting, nonlicense);
        fm.addRequires(ABtesting, survey);

        FMCdrModel<Feature, AbstractRelationship<Feature>, CTConstraint> testCaseModel = new FMCdrModel<>(fm, true, false, true, true);
        testCaseModel.initialize();

        System.out.println("=========================================");
        System.out.println("Constraints translated from the text file:");
        System.out.println(ConstraintUtils.convertToString(testCaseModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testCaseModel);

        List<Constraint> CF = new LinkedList<>(testCaseModel.getPossiblyFaultyConstraints());

        WipeOutR_FM wipeOut = new WipeOutR_FM(checker);

        PerformanceEvaluator.reset();
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