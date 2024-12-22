/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2023-2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr.algorithms;

import at.tugraz.ist.ase.hiconfit.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.hiconfit.cacdr_core.Requirement;
import at.tugraz.ist.ase.hiconfit.cacdr_core.factory.Requirements;
import at.tugraz.ist.ase.hiconfit.cdrmodel.fm.factory.FMCdrModels;
import at.tugraz.ist.ase.hiconfit.fm.factory.FeatureModels;
import at.tugraz.ist.ase.hiconfit.fm.parser.FeatureModelParserException;
import at.tugraz.ist.ase.hiconfit.kb.core.Constraint;
import com.google.common.collect.Iterators;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Set;

import static at.tugraz.ist.ase.hiconfit.cacdr.eval.CAEvaluator.printPerformance;
import static at.tugraz.ist.ase.hiconfit.eval.PerformanceEvaluator.reset;
import static at.tugraz.ist.ase.hiconfit.eval.PerformanceEvaluator.setCommonTimer;
import static org.junit.jupiter.api.Assertions.*;

class FMModelWithRequirementTest {
    @Test
    void shouldInconsistent_CFinC() throws FeatureModelParserException {
        val var_value_combination = "Appearance=false,BrightnessAndLock=false,Displays1=false,UniversalAccess1=false,Displays=true";

        val file = new File("src/test/resources/ubuntu.sxfm");
        val featureModel = FeatureModels.fromFile(file);

//        RequirementBuilder builder = new RequirementBuilder();
//        Requirement userRequirement = builder.build(var_value_combination);
        Requirement userRequirement = Requirements.fromString(var_value_combination);

        // CHECK CONSISTENCY
        val diagModel = FMCdrModels.createRequirementCdrModel(featureModel, userRequirement, true);

        System.out.println("\tNumber of constraints: " + diagModel.getAllConstraints().size());

        // FIND DIAGNOSIS
        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(diagModel);

        Set<Constraint> C = diagModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = diagModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        reset();
        setCommonTimer(FastDiagV3.TIMER_FASTDIAGV3);
        Set<Constraint> firstDiagnosis = fastDiag.findDiagnosis(C, B);

        if (!firstDiagnosis.isEmpty()) {

            System.out.println("\tinconsistent");

            System.out.println("\t\t=========================================");
            System.out.println("\t\tDiagnoses found by FastDiagV3:");
            System.out.println(firstDiagnosis);
            System.out.println("\t\tCardinality: " + firstDiagnosis.size());
            printPerformance();
        } else {
            System.out.println("\tconsistent");
        }

        assertAll(() -> assertFalse(firstDiagnosis.isEmpty()),
                () -> assertEquals(1, firstDiagnosis.size()),
                () -> assertEquals("Displays=true", Iterators.get(firstDiagnosis.iterator(), 0).getConstraint())
        );
    }

    @Test
    void shouldInconsistent1_CFinC() throws FeatureModelParserException {
        val var_value_combination = "Appearance=false,BrightnessAndLock=false,Displays1=true,UniversalAccess1=false,Displays=false,Low=true,Normal=true,High=true,AcceptanceDelay=true,Short=false";

        val file = new File("src/test/resources/ubuntu.sxfm");
        val featureModel = FeatureModels.fromFile(file);

//        RequirementBuilder builder = new RequirementBuilder();
//        Requirement userRequirement = builder.build(var_value_combination);
        Requirement userRequirement = Requirements.fromString(var_value_combination);

        // CHECK CONSISTENCY
        val diagModel = FMCdrModels.createRequirementCdrModel(featureModel, userRequirement, true);

        System.out.println("\tNumber of constraints: " + diagModel.getAllConstraints().size());

        // FIND DIAGNOSIS
        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(diagModel);

        Set<Constraint> C = diagModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = diagModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        reset();
        setCommonTimer(FastDiagV3.TIMER_FASTDIAGV3);
        Set<Constraint> firstDiagnosis = fastDiag.findDiagnosis(C, B);

        if (!firstDiagnosis.isEmpty()) {

            System.out.println("\tinconsistent");

            System.out.println("\t\t=========================================");
            System.out.println("\t\tDiagnoses found by FastDiagV3:");
            System.out.println(firstDiagnosis);
            System.out.println("\t\tCardinality: " + firstDiagnosis.size());
            printPerformance();
        } else {
            System.out.println("\tconsistent");
        }

        assertAll(() -> assertFalse(firstDiagnosis.isEmpty()),
                () -> assertEquals(4, firstDiagnosis.size()),
                () -> assertEquals("Displays=false", Iterators.get(firstDiagnosis.iterator(), 0).getConstraint()),
                () -> assertEquals("Low=true", Iterators.get(firstDiagnosis.iterator(), 1).getConstraint()),
                () -> assertEquals("High=true", Iterators.get(firstDiagnosis.iterator(), 2).getConstraint()),
                () -> assertEquals("AcceptanceDelay=true", Iterators.get(firstDiagnosis.iterator(), 3).getConstraint())
        );
    }

    @Test
    void shouldConsistent_CFinC() throws FeatureModelParserException {
        val var_value_combination = "Appearance=false,BrightnessAndLock=false,Displays1=false,UniversalAccess1=false,Displays=false,Low=false,Normal=false,High=false,AcceptanceDelay=false,Short=false";

        val file = new File("src/test/resources/ubuntu.sxfm");
        val featureModel = FeatureModels.fromFile(file);

//        RequirementBuilder builder = new RequirementBuilder();
//        Requirement userRequirement = builder.build(var_value_combination);
        Requirement userRequirement = Requirements.fromString(var_value_combination);

        // CHECK CONSISTENCY
        val diagModel = FMCdrModels.createRequirementCdrModel(featureModel, userRequirement, true);

        System.out.println("\tNumber of constraints: " + diagModel.getAllConstraints().size());

        // FIND DIAGNOSIS
        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(diagModel);

        Set<Constraint> C = diagModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = diagModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        reset();
        setCommonTimer(FastDiagV3.TIMER_FASTDIAGV3);
        Set<Constraint> firstDiagnosis = fastDiag.findDiagnosis(C, B);

        if (!firstDiagnosis.isEmpty()) {

            System.out.println("\tinconsistent");

            System.out.println("\t\t=========================================");
            System.out.println("\t\tDiagnoses found by FastDiagV3:");
            System.out.println(firstDiagnosis);
            System.out.println("\t\tCardinality: " + firstDiagnosis.size());
            printPerformance();
        } else {
            System.out.println("\tconsistent");
        }

        assertTrue(firstDiagnosis.isEmpty());
    }

    @Test
    void shouldInconsistent_CFnotinC() throws FeatureModelParserException {
        val var_value_combination = "Appearance=false,BrightnessAndLock=false,Displays1=false,UniversalAccess1=false,Displays=true";

        val file = new File("src/test/resources/ubuntu.sxfm");
        val featureModel = FeatureModels.fromFile(file);

//        RequirementBuilder builder = new RequirementBuilder();
//        Requirement userRequirement = builder.build(var_value_combination);
        Requirement userRequirement = Requirements.fromString(var_value_combination);

        // CHECK CONSISTENCY
        val diagModel = FMCdrModels.createRequirementCdrModel(featureModel, userRequirement);

        System.out.println("\tNumber of constraints: " + diagModel.getAllConstraints().size());

        // FIND DIAGNOSIS
        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(diagModel);

        Set<Constraint> C = diagModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = diagModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        reset();
        setCommonTimer(FastDiagV3.TIMER_FASTDIAGV3);
        Set<Constraint> firstDiagnosis = fastDiag.findDiagnosis(C, B);

        if (!firstDiagnosis.isEmpty()) {

            System.out.println("\tinconsistent");

            System.out.println("\t\t=========================================");
            System.out.println("\t\tDiagnoses found by FastDiagV3:");
            System.out.println(firstDiagnosis);
            System.out.println("\t\tCardinality: " + firstDiagnosis.size());
            printPerformance();
        } else {
            System.out.println("\tconsistent");
        }

        assertAll(() -> assertFalse(firstDiagnosis.isEmpty()),
                () -> assertEquals(1, firstDiagnosis.size()),
                () -> assertEquals("Displays=true", Iterators.get(firstDiagnosis.iterator(), 0).getConstraint())
        );
    }

    @Test
    void shouldInconsistent1() throws FeatureModelParserException {
        val var_value_combination = "Appearance=false,BrightnessAndLock=false,Displays1=true,UniversalAccess1=false,Displays=false,Low=true,Normal=true,High=true,AcceptanceDelay=true,Short=false";

        val file = new File("src/test/resources/ubuntu.sxfm");
        val featureModel = FeatureModels.fromFile(file);

//        RequirementBuilder builder = new RequirementBuilder();
//        Requirement userRequirement = builder.build(var_value_combination);
        Requirement userRequirement = Requirements.fromString(var_value_combination);

        // CHECK CONSISTENCY
        val diagModel = FMCdrModels.createRequirementCdrModel(featureModel, userRequirement);

        System.out.println("\tNumber of constraints: " + diagModel.getAllConstraints().size());

        // FIND DIAGNOSIS
        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(diagModel);

        Set<Constraint> C = diagModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = diagModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        reset();
        setCommonTimer(FastDiagV3.TIMER_FASTDIAGV3);
        Set<Constraint> firstDiagnosis = fastDiag.findDiagnosis(C, B);

        if (!firstDiagnosis.isEmpty()) {

            System.out.println("\tinconsistent");

            System.out.println("\t\t=========================================");
            System.out.println("\t\tDiagnoses found by FastDiagV3:");
            System.out.println(firstDiagnosis);
            System.out.println("\t\tCardinality: " + firstDiagnosis.size());
            printPerformance();
        } else {
            System.out.println("\tconsistent");
        }

        assertAll(() -> assertFalse(firstDiagnosis.isEmpty()),
                () -> assertEquals(4, firstDiagnosis.size()),
                () -> assertEquals("Displays=false", Iterators.get(firstDiagnosis.iterator(), 0).getConstraint()),
                () -> assertEquals("Low=true", Iterators.get(firstDiagnosis.iterator(), 1).getConstraint()),
                () -> assertEquals("High=true", Iterators.get(firstDiagnosis.iterator(), 2).getConstraint()),
                () -> assertEquals("AcceptanceDelay=true", Iterators.get(firstDiagnosis.iterator(), 3).getConstraint())
        );
    }

    @Test
    void shouldConsistent() throws FeatureModelParserException {
        val var_value_combination = "Appearance=false,BrightnessAndLock=false,Displays1=false,UniversalAccess1=false,Displays=false,Low=false,Normal=false,High=false,AcceptanceDelay=false,Short=false";

        val file = new File("src/test/resources/ubuntu.sxfm");
        val featureModel = FeatureModels.fromFile(file);

//        RequirementBuilder builder = new RequirementBuilder();
//        Requirement userRequirement = builder.build(var_value_combination);
        Requirement userRequirement = Requirements.fromString(var_value_combination);

        // CHECK CONSISTENCY
        val diagModel = FMCdrModels.createRequirementCdrModel(featureModel, userRequirement);

        System.out.println("\tNumber of constraints: " + diagModel.getAllConstraints().size());

        // FIND DIAGNOSIS
        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(diagModel);

        Set<Constraint> C = diagModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = diagModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        reset();
        setCommonTimer(FastDiagV3.TIMER_FASTDIAGV3);
        Set<Constraint> firstDiagnosis = fastDiag.findDiagnosis(C, B);

        if (!firstDiagnosis.isEmpty()) {

            System.out.println("\tinconsistent");

            System.out.println("\t\t=========================================");
            System.out.println("\t\tDiagnoses found by FastDiagV3:");
            System.out.println(firstDiagnosis);
            System.out.println("\t\tCardinality: " + firstDiagnosis.size());
            printPerformance();
        } else {
            System.out.println("\tconsistent");
        }

        assertTrue(firstDiagnosis.isEmpty());
    }
}