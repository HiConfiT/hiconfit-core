/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.parser;

import at.tugraz.ist.ase.fm.core.FeatureModel;
import at.tugraz.ist.ase.fm.parser.factory.FMParserFactory;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class SXFMParserTest {
    static FeatureModel featureModel;

    @Test
    void testUbuntu() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/ubuntu.splx");
        FMParserFactory factory = FMParserFactory.getInstance();
        FeatureModelParser parser = factory.getParser(FMFormat.SXFM);
        featureModel = parser.parse(fileFM);

        assertNotNull(featureModel);
    }

    @Test
    void testBamboo() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/bamboobike_splot.sxfm");
        FMParserFactory factory = FMParserFactory.getInstance();
        FeatureModelParser parser = factory.getParser(FMFormat.SXFM);
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
                	optional(Engine, Bamboo Bike)
                	optional(Drop Handlebar, Bamboo Bike)
                	alternative(Frame, Female, Male, Step-through)
                	or(Brake, Front, Rear, Back-pedal)
                CONSTRAINTS:
                	requires(Drop Handlebar, Male)
                	excludes(Engine, Back-pedal)
                """;

        assertAll(() -> assertNotNull(featureModel),
                () -> assertEquals(expected, featureModel.toString()));
    }
}
