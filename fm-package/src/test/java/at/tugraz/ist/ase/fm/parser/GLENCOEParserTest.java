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

class GLENCOEParserTest {
    static FeatureModel<Feature, AbstractRelationship<Feature>> featureModel;

    @Test
    void test() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/bamboobike.gfm.json");
//        FeatureModelParser parser = FMParserFactory.getInstance().getParser(fileFM.getName());
        @Cleanup("dispose")
        GLENCOEParser<Feature, AbstractRelationship<Feature>> parser = new GLENCOEParser<>(new FeatureBuilder(), new RelationshipBuilder());
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
}