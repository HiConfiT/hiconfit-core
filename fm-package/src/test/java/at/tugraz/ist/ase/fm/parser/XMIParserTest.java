/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.parser;

import at.tugraz.ist.ase.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.fm.core.CTConstraint;
import at.tugraz.ist.ase.fm.core.Feature;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import lombok.Cleanup;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class XMIParserTest {
    static FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel;

    @Test
    void test() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/bamboobike.xmi");
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
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
                	Engine1
                	Engine2
                RELATIONSHIPS:
                	mandatory(Bamboo Bike, Frame)
                	mandatory(Bamboo Bike, Brake)
                	optional(Bamboo Bike, Engine)
                	optional(Bamboo Bike, Drop Handlebar)
                	alternative(Frame, Female, Male, Step-through)
                	or(Brake, Front, Rear, Back-pedal)
                	optional(Engine, Engine1)
                	optional(Engine1, Engine2)
                CONSTRAINTS:
                	excludes(Engine, Back-pedal)
                	requires(Drop Handlebar, Male)
                """;

        assertAll(() -> assertNotNull(featureModel),
                () -> assertEquals(expected, featureModel.toString()));
    }
}