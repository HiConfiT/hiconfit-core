/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fm.parser;

import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.Feature;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import lombok.Cleanup;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FeatureIDEParserTest {
    static FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel;

    @Test
    void test() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/bamboobike_featureide.xml");
        FMParserFactory<Feature, AbstractRelationship<Feature>, CTConstraint> factory = FMParserFactory.getInstance();

        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = factory.getParser(fileFM.getName());
        featureModel = parser.parse(fileFM);

        String expected = """
                FEATURES:
                	Bamboo Bike
                	Frame
                	Brake
                	Engine
                	Drop Handlebar
                	Female
                	Male
                	Step-through
                	Front
                	Rear
                	Back-pedal
                RELATIONSHIPS:
                	mandatory(Bamboo Bike, Frame)
                	mandatory(Bamboo Bike, Brake)
                	optional(Bamboo Bike, Engine)
                	optional(Bamboo Bike, Drop Handlebar)
                	alternative(Frame, Female, Male, Step-through)
                	or(Brake, Front, Rear, Back-pedal)
                CONSTRAINTS:
                	requires(Drop Handlebar, Male)
                	excludes(Engine, Back-pedal)
                """;

        assertAll(() -> assertNotNull(featureModel),
                () -> assertEquals(expected, featureModel.toString()));
    }

    @Test
    void test1() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/linux-2.6.33.3_simple.xml");
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
        featureModel = parser.parse(fileFM);

        assertAll(() -> assertNotNull(featureModel));
    }

    @Test
    void test2() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/linux-2.6.33.3.xml");
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
        featureModel = parser.parse(fileFM);

        assertAll(() -> assertNotNull(featureModel));
    }

    @Test
    void test3() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/model1.xml");
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
        featureModel = parser.parse(fileFM);

        String expected = """
                FEATURES:
                	root
                	A
                	B
                	C
                	A1
                	A2
                	B1
                	B2
                	C1
                	C2
                	C1a
                	C1b
                	C1c
                RELATIONSHIPS:
                	mandatory(root, A)
                	optional(root, B)
                	optional(root, C)
                	optional(A, A1)
                	optional(A, A2)
                	optional(B, B1)
                	optional(B, B2)
                	or(C, C1, C2)
                	alternative(C1, C1a, C1b, C1c)
                CONSTRAINTS:
                	requires(A1, A2)
                	requires(A2, A1)
                	excludes(A1, A2)
                """;

        assertAll(() -> assertNotNull(featureModel),
                () -> assertEquals(expected, featureModel.toString()));
    }

    @Test
    void test4() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/complex_featureide_model.xml");
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
        featureModel = parser.parse(fileFM);

        String expected = """
                FEATURES:
                	test1
                	A
                	B
                	C
                	D
                	AA
                	AB
                	AC
                	AD
                	BA
                	BB
                	BC
                	CA
                	CB
                	CC
                	DA
                	DB
                	ADA
                	ADB
                	DAA
                	DAB
                RELATIONSHIPS:
                	mandatory(test1, A)
                	optional(test1, B)
                	optional(test1, C)
                	mandatory(test1, D)
                	mandatory(A, AA)
                	optional(A, AB)
                	optional(A, AC)
                	mandatory(A, AD)
                	mandatory(B, BA)
                	optional(B, BB)
                	optional(B, BC)
                	or(C, CA, CB, CC)
                	alternative(D, DA, DB)
                	or(AD, ADA, ADB)
                	or(DA, DAA, DAB)
                CONSTRAINTS:
                	(AB -> ~CB)
                	(AC -> ~CC)
                	~(ADA /\\ AB)
                	requires(BC, ADB)
                	(BC \\/ (CA -> CB))
                	(DAA -> ~(AB /\\ AC))
                	(AB -> (AC \\/ ~DAB))
                	(DB -> ~(~AB))
                	(DB -> ~(~(~(~AC))))
                	(DAA -> (AB \\/ ~AC))
                	(ADA <-> (BB /\\ DAB))
                """;

        assertAll(() -> assertNotNull(featureModel),
                () -> assertEquals(expected, featureModel.toString()));
    }
}