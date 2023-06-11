/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr.algorithms;

import at.tugraz.ist.ase.hiconfit.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.hiconfit.cacdr_core.Requirement;
import at.tugraz.ist.ase.hiconfit.cacdr_core.builder.RequirementBuilder;
import at.tugraz.ist.ase.hiconfit.cdrmodel.fm.FMModelWithRequirement;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.Feature;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import at.tugraz.ist.ase.hiconfit.fm.parser.FMParserFactory;
import at.tugraz.ist.ase.hiconfit.fm.parser.FeatureModelParser;
import at.tugraz.ist.ase.hiconfit.fm.parser.FeatureModelParserException;
import at.tugraz.ist.ase.hiconfit.kb.core.Constraint;
import com.google.common.collect.Iterators;
import lombok.Cleanup;
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
        String var_value_combination = "Appearance=false,BrightnessAndLock=false,Displays1=false,UniversalAccess1=false,Displays=true";

        File file = new File("src/test/resources/ubuntu.sxfm");
        FMParserFactory<Feature, AbstractRelationship<Feature>, CTConstraint> factory = FMParserFactory.getInstance();
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = factory.getParser(file.getName());
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(file);

        RequirementBuilder builder = new RequirementBuilder();
        Requirement userRequirement = builder.build(var_value_combination);

        // CHECK CONSISTENCY
        FMModelWithRequirement<Feature, AbstractRelationship<Feature>, CTConstraint> diagModel
                = new FMModelWithRequirement<>(featureModel, userRequirement, false, true, true,  false);
        diagModel.initialize();

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
        String var_value_combination = "Appearance=false,BrightnessAndLock=false,Displays1=true,UniversalAccess1=false,Displays=false,Low=true,Normal=true,High=true,AcceptanceDelay=true,Short=false";

        File file = new File("src/test/resources/ubuntu.sxfm");
        FMParserFactory<Feature, AbstractRelationship<Feature>, CTConstraint> factory = FMParserFactory.getInstance();
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = factory.getParser(file.getName());
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(file);

        RequirementBuilder builder = new RequirementBuilder();
        Requirement userRequirement = builder.build(var_value_combination);

        // CHECK CONSISTENCY
        FMModelWithRequirement<Feature, AbstractRelationship<Feature>, CTConstraint> diagModel
                = new FMModelWithRequirement<>(featureModel, userRequirement, false, true, true,  false);
        diagModel.initialize();

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
        String var_value_combination = "Appearance=false,BrightnessAndLock=false,Displays1=false,UniversalAccess1=false,Displays=false,Low=false,Normal=false,High=false,AcceptanceDelay=false,Short=false";

        File file = new File("src/test/resources/ubuntu.sxfm");
        FMParserFactory<Feature, AbstractRelationship<Feature>, CTConstraint> factory = FMParserFactory.getInstance();
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = factory.getParser(file.getName());
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(file);

        RequirementBuilder builder = new RequirementBuilder();
        Requirement userRequirement = builder.build(var_value_combination);

        // CHECK CONSISTENCY
        FMModelWithRequirement<Feature, AbstractRelationship<Feature>, CTConstraint> diagModel
                = new FMModelWithRequirement<>(featureModel, userRequirement, false, true, true,  false);
        diagModel.initialize();

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
        String var_value_combination = "Appearance=false,BrightnessAndLock=false,Displays1=false,UniversalAccess1=false,Displays=true";

        File file = new File("src/test/resources/ubuntu.sxfm");
        FMParserFactory<Feature, AbstractRelationship<Feature>, CTConstraint> factory = FMParserFactory.getInstance();
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = factory.getParser(file.getName());
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(file);

        RequirementBuilder builder = new RequirementBuilder();
        Requirement userRequirement = builder.build(var_value_combination);

        // CHECK CONSISTENCY
        FMModelWithRequirement<Feature, AbstractRelationship<Feature>, CTConstraint> diagModel
                = new FMModelWithRequirement<>(featureModel, userRequirement, false, true, false, false);
        diagModel.initialize();

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
        String var_value_combination = "Appearance=false,BrightnessAndLock=false,Displays1=true,UniversalAccess1=false,Displays=false,Low=true,Normal=true,High=true,AcceptanceDelay=true,Short=false";

        File file = new File("src/test/resources/ubuntu.sxfm");
        FMParserFactory<Feature, AbstractRelationship<Feature>, CTConstraint> factory = FMParserFactory.getInstance();
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = factory.getParser(file.getName());
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(file);

        RequirementBuilder builder = new RequirementBuilder();
        Requirement userRequirement = builder.build(var_value_combination);

        // CHECK CONSISTENCY
        FMModelWithRequirement<Feature, AbstractRelationship<Feature>, CTConstraint> diagModel
                = new FMModelWithRequirement<>(featureModel, userRequirement, false, true, false, false);
        diagModel.initialize();

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
        String var_value_combination = "Appearance=false,BrightnessAndLock=false,Displays1=false,UniversalAccess1=false,Displays=false,Low=false,Normal=false,High=false,AcceptanceDelay=false,Short=false";

        File file = new File("src/test/resources/ubuntu.sxfm");
        FMParserFactory<Feature, AbstractRelationship<Feature>, CTConstraint> factory = FMParserFactory.getInstance();
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = factory.getParser(file.getName());
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(file);

        RequirementBuilder builder = new RequirementBuilder();
        Requirement userRequirement = builder.build(var_value_combination);

        // CHECK CONSISTENCY
        FMModelWithRequirement<Feature, AbstractRelationship<Feature>, CTConstraint> diagModel
                = new FMModelWithRequirement<>(featureModel, userRequirement, false, true, false,  false);
        diagModel.initialize();

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