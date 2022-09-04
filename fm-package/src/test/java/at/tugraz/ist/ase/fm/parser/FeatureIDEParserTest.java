/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.parser;

import at.tugraz.ist.ase.fm.builder.ConstraintBuilder;
import at.tugraz.ist.ase.fm.builder.FeatureBuilder;
import at.tugraz.ist.ase.fm.builder.RelationshipBuilder;
import at.tugraz.ist.ase.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.fm.core.CTConstraint;
import at.tugraz.ist.ase.fm.core.Feature;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import lombok.Cleanup;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FeatureIDEParserTest {
    static FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel;

    @Test
    @SuppressWarnings("unchecked")
    void test() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/bamboobike_featureide.xml");
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = (FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint>) FMParserFactory.getInstance(new FeatureBuilder(), new RelationshipBuilder(), new ConstraintBuilder()).getParser(fileFM.getName());
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
    @SuppressWarnings("unchecked")
    void test1() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/linux-2.6.33.3_simple.xml");
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = (FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint>) FMParserFactory.getInstance(new FeatureBuilder(), new RelationshipBuilder(), new ConstraintBuilder()).getParser(fileFM.getName());
        featureModel = parser.parse(fileFM);

        assertAll(() -> assertNotNull(featureModel));
    }

    @Test
    @SuppressWarnings("unchecked")
    void test2() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/linux-2.6.33.3.xml");
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = (FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint>) FMParserFactory.getInstance(new FeatureBuilder(), new RelationshipBuilder(), new ConstraintBuilder()).getParser(fileFM.getName());
        featureModel = parser.parse(fileFM);

        assertAll(() -> assertNotNull(featureModel));
    }

    @Test
    @SuppressWarnings("unchecked")
    void test3() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/model1.xml");
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = (FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint>) FMParserFactory.getInstance(new FeatureBuilder(), new RelationshipBuilder(), new ConstraintBuilder()).getParser(fileFM.getName());
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
    @SuppressWarnings("unchecked")
    void test4() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/complex_featureide_model.xml");
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = (FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint>) FMParserFactory.getInstance(new FeatureBuilder(), new RelationshipBuilder(), new ConstraintBuilder()).getParser(fileFM.getName());
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
                	ADA
                	ADB
                	BA
                	BB
                	BC
                	CA
                	CB
                	CC
                	DA
                	DB
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
                	or(AD, ADA, ADB)
                	mandatory(B, BA)
                	optional(B, BB)
                	optional(B, BC)
                	or(C, CA, CB, CC)
                	alternative(D, DA, DB)
                	or(DA, DAA, DAB)
                CONSTRAINTS:
                	AB -> ~CB
                	AC -> ~CC
                	~(ADA /\\ AB)
                	requires(BC, ADB)
                	BC \\/ CA -> CB
                	DAA -> ~(AB /\\ AC)
                	AB -> AC \\/ ~DAB
                	DB -> ~(~AB)
                	DB -> ~(~(~(~AC)))
                	DAA -> AB \\/ ~AC
                	ADA <-> BB /\\ DAB
                """;

        assertAll(() -> assertNotNull(featureModel),
                () -> assertEquals(expected, featureModel.toString()));
    }
}