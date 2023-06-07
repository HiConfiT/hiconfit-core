/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.configurator;

import at.tugraz.ist.ase.hiconfit.common.IOUtils;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.Feature;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import at.tugraz.ist.ase.hiconfit.fm.parser.FMParserFactory;
import at.tugraz.ist.ase.hiconfit.fm.parser.FeatureModelParser;
import at.tugraz.ist.ase.hiconfit.fm.parser.FeatureModelParserException;
import at.tugraz.ist.ase.hiconfit.heuristics.ValueVariableOrdering;
import at.tugraz.ist.ase.hiconfit.heuristics.io.ValueVariableOrderingReader;
import at.tugraz.ist.ase.hiconfit.kb.camera.CameraKB;
import at.tugraz.ist.ase.hiconfit.kb.core.Assignment;
import at.tugraz.ist.ase.hiconfit.kb.core.Requirement;
import at.tugraz.ist.ase.hiconfit.kb.core.Solution;
import at.tugraz.ist.ase.hiconfit.kb.core.translator.camera.CameraSolutionTranslator;
import at.tugraz.ist.ase.hiconfit.kb.core.translator.fm.FMSolutionTranslator;
import at.tugraz.ist.ase.hiconfit.kb.core.writer.TxtSolutionWriter;
import at.tugraz.ist.ase.hiconfit.kb.fm.FMKB;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Cleanup;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConfiguratorTest {

    private final static CameraKB cameraKB = new CameraKB(false);

    private static FMKB<Feature, AbstractRelationship<Feature>, CTConstraint> kb;
    private static FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel;

    @Test
    void testCameraKB() throws IOException, CsvValidationException {
        InputStream is = IOUtils.getInputStream(this.getClass().getClassLoader(), "vvo_camera.csv");

        ValueVariableOrderingReader reader = new ValueVariableOrderingReader();
        ValueVariableOrdering vvo = reader.read(is, cameraKB);

        System.out.println(vvo);

        Configurator configurator = new Configurator(cameraKB, false, new CameraSolutionTranslator());

        // identify first 5 solutions without the given VVO
        configurator.findSolutions(false, 5, new TxtSolutionWriter("./conf/camera_withoutVVO/"));

        // identify first 5 solutions with the given VVO
        configurator.findSolutions(false, 5, vvo, new TxtSolutionWriter("./conf/camera_withVVO/"));
    }

    @Test
    void testFMKB() throws FeatureModelParserException, IOException, CsvValidationException {
        // read the feature model
        File fileFM = new File("src/test/resources/pizzas.xml");

        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
        featureModel = parser.parse(fileFM);

        // convert the feature model into FMKB
        kb = new FMKB<>(featureModel, true);

        // read the value variable ordering
        InputStream is = IOUtils.getInputStream(this.getClass().getClassLoader(), "vvo_pizzas.csv");

        ValueVariableOrderingReader reader = new ValueVariableOrderingReader();
        ValueVariableOrdering vvo = reader.read(is, kb);

        System.out.println(vvo);

        Configurator configurator = new Configurator(kb, true, new FMSolutionTranslator());

        // identify first 5 solutions without the given VVO
        configurator.findSolutions(false, 5, new TxtSolutionWriter("./conf/pizzas_withoutVVO/"));

        // identify first 5 solutions with the given VVO
        configurator.findSolutions(false, 5, vvo, new TxtSolutionWriter("./conf/pizzas_withVVO/"));
    }

    @Test
    void testFindSolutions() throws FeatureModelParserException {
        // read the feature model
        File fileFM = new File("src/test/resources/pizzas.xml");

        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
        featureModel = parser.parse(fileFM);

        // convert the feature model into FMKB
        kb = new FMKB<>(featureModel, true);

        Configurator configurator = new Configurator(kb, true, new FMSolutionTranslator());

        int counter = 0;
        for (int i = 0; i < 100; i++) {
            configurator.findSolutions(false, 1);
            System.out.println(++counter + " " + configurator.getLastestSolution());
        }

        // check uniqueness of configurations
        List<Solution> solutions = configurator.getSolutions();
        for (int i = 0; i < solutions.size() - 1; i++) {
            for (int j = i + 1; j < solutions.size(); j++) {
                assertNotEquals(solutions.get(i), solutions.get(j));
                if (solutions.get(i).equals(solutions.get(j))) {
                    System.out.println("Solution " + i + " and " + j + " are the same");
                }
            }
        }
    }

    @Test
    void testFind() throws FeatureModelParserException {
        // read the feature model
        File fileFM = new File("src/test/resources/pizzas.xml");

        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
        featureModel = parser.parse(fileFM);

        // convert the feature model into FMKB
        kb = new FMKB<>(featureModel, true);

        FMKB<Feature, AbstractRelationship<Feature>, CTConstraint> checkKB = new FMKB<>(featureModel, true);

        Configurator checker = new Configurator(checkKB, true, new FMSolutionTranslator());

        Configurator configurator = new Configurator(kb, true, new FMSolutionTranslator());
        configurator.initializeWithKB();

        configurator.find(43, 0);

        configurator.reset();
        assert configurator.getNumberSolutions() == 42;

//        int counter = 0;
        for (Solution s : configurator.getSolutions()) {
//            System.out.print(++counter + " " + s + " - ");

            assertTrue(checker.isConsistent(s));
//            if (checker.isConsistent(s)) {
//                System.out.println("Consistent");
//            } else {
//                System.out.println("Inconsistent");
//            }
        }
    }

    @Test
    void testFindWithRequirement() throws FeatureModelParserException {
        // read the feature model
        File fileFM = new File("src/test/resources/pizzas.xml");

        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
        featureModel = parser.parse(fileFM);

        // convert the feature model into FMKB
        kb = new FMKB<>(featureModel, true);

        FMKB<Feature, AbstractRelationship<Feature>, CTConstraint> checkKB = new FMKB<>(featureModel, true);

        Configurator checker = new Configurator(checkKB, true, new FMSolutionTranslator());

        Configurator configurator = new Configurator(kb, true, new FMSolutionTranslator());
        configurator.initializeWithKB();

        configurator.setRequirement(Requirement.requirementBuilder()
                .assignments(List.of(new Assignment("Salami", "true")))
                .build());

        while (configurator.find(1, 0)) {

        }

        configurator.reset();
        assert configurator.getNumberSolutions() == 24;

//        int counter = 0;
        for (Solution s : configurator.getSolutions()) {
//            System.out.print(++counter + " " + s + " - ");

            assertTrue(checker.isConsistent(s));
//            if (checker.isConsistent(s)) {
//                System.out.println("Consistent");
//            } else {
//                System.out.println("Inconsistent");
//            }
        }
    }

    @Test
    void testFindWithNotKB() throws FeatureModelParserException {
        // read the feature model
        File fileFM = new File("src/test/resources/pizzas.xml");

        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
        featureModel = parser.parse(fileFM);

        // convert the feature model into FMKB
        kb = new FMKB<>(featureModel, true);

        FMKB<Feature, AbstractRelationship<Feature>, CTConstraint> checkKB = new FMKB<>(featureModel, true);

        Configurator checker = new Configurator(checkKB, true, new FMSolutionTranslator());

        Configurator configurator = new Configurator(kb, true, new FMSolutionTranslator());
        configurator.initializeWithNotKB();

        while (configurator.find(1, 0)) {

        }

        configurator.reset();
        assert configurator.getNumberSolutions() == 2560;

//        int counter = 0;
        for (Solution s : configurator.getSolutions()) {
//            System.out.print(++counter + " " + s + " - ");

            assertFalse(checker.isConsistent(s));
//            if (checker.isConsistent(s)) {
//                System.out.println("Consistent");
//            } else {
//                System.out.println("Inconsistent");
//            }
        }
//        System.out.println("Number of solutions: " + configurator.getNumberSolutions());
    }

    @Test
    void testFindWithNotKBInCompactMode() throws FeatureModelParserException {
        // read the feature model
        File fileFM = new File("src/test/resources/pizzas.xml");

        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
        featureModel = parser.parse(fileFM);

        // convert the feature model into FMKB
        kb = new FMKB<>(featureModel, true);

        FMKB<Feature, AbstractRelationship<Feature>, CTConstraint> checkKB = new FMKB<>(featureModel, true);

        Configurator checker = new Configurator(checkKB, true, new FMSolutionTranslator());

        Configurator configurator = new Configurator(kb, true, new FMSolutionTranslator());

        configurator.findAllSolutions(true,0);
        assert configurator.getNumberSolutions() == 2560;

//        int counter = 0;
        for (Solution s : configurator.getSolutions()) {
//            System.out.print(++counter + " " + s + " - ");

            assertFalse(checker.isConsistent(s));
//            if (checker.isConsistent(s)) {
//                System.out.println("Consistent");
//            } else {
//                System.out.println("Inconsistent");
//            }
        }
    }
}