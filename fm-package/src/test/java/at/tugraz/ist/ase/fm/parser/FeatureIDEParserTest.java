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
                """;
//        CONSTRAINTS:
//        requires(Drop Handlebar, Male)
//        excludes(Engine, Back-pedal)

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

        assertAll(() -> assertNotNull(featureModel));
    }
}