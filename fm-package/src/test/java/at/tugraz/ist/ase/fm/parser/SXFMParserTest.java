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

public class SXFMParserTest {
    static FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel;

    @Test
    void testBamboo() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/bamboobike_splot.sxfm");
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
    void testSmartwatch() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/smartwatch.sxfm");
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(fileFM);

        String st = "FEATURES:\n" +
                String.format("\t%s\n", "Smartwatch") +
                String.format("\t%s\n", "Connector") +
                String.format("\t%s\n", "Screen") +
                String.format("\t%s\n", "Camera") +
                String.format("\t%s\n", "Compass") +
                String.format("\t%s\n", "GPS") +
                String.format("\t%s\n", "Cellular") +
                String.format("\t%s\n", "Wifi") +
                String.format("\t%s\n", "Bluetooth") +
                String.format("\t%s\n", "Analog") +
                String.format("\t%s\n", "High Resolution") +
                String.format("\t%s\n", "E-ink") +
                "RELATIONSHIPS:\n" +
                String.format("\t%s\n", "mandatory(Smartwatch, Connector)") +
                String.format("\t%s\n", "mandatory(Smartwatch, Screen)") +
                String.format("\t%s\n", "optional(Smartwatch, Camera)") +
                String.format("\t%s\n", "optional(Smartwatch, Compass)") +
                String.format("\t%s\n", "or(Connector, GPS, Cellular, Wifi, Bluetooth)") +
                String.format("\t%s\n", "alternative(Screen, Analog, High Resolution, E-ink)") +
                "CONSTRAINTS:\n" +
                String.format("\t%s\n", "requires(Camera, High Resolution)") +
                String.format("\t%s\n", "requires(Compass, GPS)") +
                String.format("\t%s\n", "excludes(Analog, Cellular)") +
                String.format("\t%s\n", "((~Analog \\/ Cellular) \\/ Wifi)");

        assertAll(() -> assertEquals(12, featureModel.getNumOfFeatures()),
                () -> assertEquals(6, featureModel.getNumOfRelationships()),
//                () -> assertEquals(4, featureModel.getNumOfConstraints()),
                () -> assertEquals(st, featureModel.toString()));
    }

    @Test
    public void testFM_10_0() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/FM_10_0.splx");
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
        featureModel = parser.parse(fileFM);

        String st = "FEATURES:\n" +
                String.format("\t%s\n", "FM_10_0") +
                String.format("\t%s\n", "F1") +
                String.format("\t%s\n", "F2") +
                String.format("\t%s\n", "F8") +
                String.format("\t%s\n", "F3") +
                String.format("\t%s\n", "F4") +
                String.format("\t%s\n", "F5") +
                String.format("\t%s\n", "F6") +
                String.format("\t%s\n", "F7") +
                "RELATIONSHIPS:\n" +
                String.format("\t%s\n", "optional(FM_10_0, F1)") +
                String.format("\t%s\n", "mandatory(FM_10_0, F2)") +
                String.format("\t%s\n", "or(FM_10_0, F3, F4, F5)") +
                String.format("\t%s\n", "alternative(FM_10_0, F6, F7)") +
                String.format("\t%s\n", "optional(F2, F8)") +
                "CONSTRAINTS:\n" +
                String.format("\t%s\n", "requires(F8, F6)") +
                String.format("\t%s\n", "excludes(F1, F4)") +
                String.format("\t%s\n", "((~F1 \\/ F7) \\/ F8)") +
                String.format("\t%s\n", "requires(F2, F6)");

        assertEquals(st, featureModel.toString());
    }
}
