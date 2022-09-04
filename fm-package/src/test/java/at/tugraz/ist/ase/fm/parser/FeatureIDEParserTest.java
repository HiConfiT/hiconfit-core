/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.parser;

import at.tugraz.ist.ase.fm.builder.FeatureBuilder;
import at.tugraz.ist.ase.fm.builder.RelationshipBuilder;
import at.tugraz.ist.ase.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.fm.core.Feature;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import lombok.Cleanup;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FeatureIDEParserTest {
    static FeatureModel<Feature, AbstractRelationship<Feature>> featureModel;

    @Test
    void test() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/bamboobike_featureide.xml");
//        FeatureModelParser parser = FMParserFactory.getInstance().getParser(fileFM.getName());
        @Cleanup("dispose")
        FeatureIDEParser<Feature, AbstractRelationship<Feature>> parser = new FeatureIDEParser<>(new FeatureBuilder(), new RelationshipBuilder());
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
    void test1() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/linux-2.6.33.3_simple.xml");
//        FMParserFactory factory = FMParserFactory.getInstance();
//        FeatureModelParser parser = factory.getParser(FMFormat.FEATUREIDE);
        @Cleanup("dispose")
        FeatureIDEParser<Feature, AbstractRelationship<Feature>> parser = new FeatureIDEParser<>(new FeatureBuilder(), new RelationshipBuilder());
        featureModel = parser.parse(fileFM);

        assertAll(() -> assertNotNull(featureModel));
    }

    @Test
    void test2() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/linux-2.6.33.3.xml");
//        FMParserFactory factory = FMParserFactory.getInstance();
//        FeatureModelParser parser = factory.getParser(FMFormat.FEATUREIDE);
        @Cleanup("dispose")
        FeatureIDEParser<Feature, AbstractRelationship<Feature>> parser = new FeatureIDEParser<>(new FeatureBuilder(), new RelationshipBuilder());
        featureModel = parser.parse(fileFM);

        assertAll(() -> assertNotNull(featureModel));
    }

    @Test
    void test3() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/model1.xml");
//        FMParserFactory factory = FMParserFactory.getInstance();
//        FeatureModelParser parser = factory.getParser(FMFormat.FEATUREIDE);
        @Cleanup("dispose")
        FeatureIDEParser<Feature, AbstractRelationship<Feature>> parser = new FeatureIDEParser<>(new FeatureBuilder(), new RelationshipBuilder());
        featureModel = parser.parse(fileFM);

        assertAll(() -> assertNotNull(featureModel));
    }
}