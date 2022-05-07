/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core;

import at.tugraz.ist.ase.fm.parser.FeatureModelParserException;
import at.tugraz.ist.ase.fm.parser.SXFMParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FeatureModelTest {
    static FeatureModel featureModel;

    @BeforeAll
    static void setUp() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/FM_10_0.splx");
        SXFMParser parser = new SXFMParser();
        featureModel = parser.parse(fileFM);
    }

//    @Test
//    void testAddFeature() {
//        assertDoesNotThrow(() -> featureModel.addFeature("test", null));
//    }

    @Test
    public void testGetName() {
        assertEquals(featureModel.getName(), "FM_10_0.splx");
    }

    @Test
    public void testGetFeature() throws FeatureModelException {
        Feature f1 = featureModel.getBfFeatures().get(4);

        Feature f2 = featureModel.getFeature(4);
        Feature f3 = featureModel.getFeature("F3");

        assertAll(() -> assertEquals(f1, f2),
                () -> assertEquals(f1, f3));
    }

    @Test
    public void testGetNumOfFeatures() {
        assertEquals(9, featureModel.getNumOfFeatures());
    }

    @Test
    public void testIsMandatoryFeature() {
        assertAll(() -> assertFalse(featureModel.isMandatoryFeature(featureModel.getFeature(0))),
                () -> assertFalse(featureModel.isMandatoryFeature(featureModel.getFeature(1))),
                () -> assertTrue(featureModel.isMandatoryFeature(featureModel.getFeature(2))),
                () -> assertFalse(featureModel.isMandatoryFeature(featureModel.getFeature(3))),
                () -> assertFalse(featureModel.isMandatoryFeature(featureModel.getFeature(4))),
                () -> assertFalse(featureModel.isMandatoryFeature(featureModel.getFeature(5))),
                () -> assertFalse(featureModel.isMandatoryFeature(featureModel.getFeature(6))),
                () -> assertFalse(featureModel.isMandatoryFeature(featureModel.getFeature(7))),
                () -> assertFalse(featureModel.isMandatoryFeature(featureModel.getFeature(8))));
    }

    @Test
    public void testIsOptionalFeature() {
        assertAll(() -> assertTrue(featureModel.isOptionalFeature(featureModel.getFeature(1))),
                () -> assertFalse(featureModel.isOptionalFeature(featureModel.getFeature(2))),
                () -> assertTrue(featureModel.isOptionalFeature(featureModel.getFeature(3))),
                () -> assertTrue(featureModel.isOptionalFeature(featureModel.getFeature(4))),
                () -> assertTrue(featureModel.isOptionalFeature(featureModel.getFeature(5))),
                () -> assertTrue(featureModel.isOptionalFeature(featureModel.getFeature(6))),
                () -> assertTrue(featureModel.isOptionalFeature(featureModel.getFeature(7))),
                () -> assertTrue(featureModel.isOptionalFeature(featureModel.getFeature(8))));
    }

//    @Test
//    public void testGetLeftSideOfRequiresConstraint() {
//    }
//
    @Test
    public void testGetRightSideOfRelationships() throws FeatureModelException {
        Feature f1 = featureModel.getFeature(0);
        Feature f2 = featureModel.getFeature(2);
        List<Feature> f1s = featureModel.getRightSideOfRelationships(f1);
        List<Feature> f2s = featureModel.getRightSideOfRelationships(f2);

        assertAll(() -> assertEquals("F1", f1s.get(0).toString()),
                () -> assertEquals("F2", f1s.get(1).toString()),
                () -> assertEquals("F3", f1s.get(2).toString()),
                () -> assertEquals("F4", f1s.get(3).toString()),
                () -> assertEquals("F5", f1s.get(4).toString()),
                () -> assertEquals("F6", f1s.get(5).toString()),
                () -> assertEquals("F7", f1s.get(6).toString()),
                () -> assertEquals("F8", f2s.get(0).toString()));
    }

    @Test
    public void testGetRelationshipsWith() {
        Feature f1 = featureModel.getFeature(1);
        Feature f2 = featureModel.getFeature(2);

        System.out.println(f1);
        System.out.println(f2);

        List<Relationship> r1List = featureModel.getRelationshipsWith(f1);
        List<Relationship> r2List = featureModel.getRelationshipsWith(f2);

        List<Relationship> allRelationships = featureModel.getRelationships();
        List<Relationship> allConstraints = featureModel.getConstraints();

        assertAll(() -> assertEquals(allRelationships.get(0), r1List.get(0)),
                () -> assertEquals(allConstraints.get(1), r1List.get(1)),
                () -> assertEquals(allConstraints.get(2), r1List.get(2)),
                () -> assertEquals(allRelationships.get(1), r2List.get(0)),
                () -> assertEquals(allRelationships.get(4), r2List.get(1)));
    }

    @Test
    public void testGetMandatoryParents() throws FeatureModelException {
        // TODO - 3CNF
        Feature f1 = featureModel.getFeature("F6");
        Feature f2 = featureModel.getFeature("F2");

        List<Feature> featureList = featureModel.getMandatoryParents(f1);

        assertEquals(1, featureList.size());
        assertEquals(f2, featureList.get(0));
    }

    @Test
    public void testGetMandatoryParent() {
    }

    @Test
    public void testGetRelationshipByConstraint() {
    }

    @Test
    public void testGetNumOfRelationships() {
        assertEquals(featureModel.getNumOfRelationships(), 5);
    }

    @Test
    public void testGetNumOfRelationshipsWithSpecifyType() {
        assertAll(() -> assertEquals(1, featureModel.getNumOfRelationships(RelationshipType.MANDATORY)),
                () -> assertEquals(2, featureModel.getNumOfRelationships(RelationshipType.OPTIONAL)),
                () -> assertEquals(1, featureModel.getNumOfRelationships(RelationshipType.ALTERNATIVE)),
                () -> assertEquals(1, featureModel.getNumOfRelationships(RelationshipType.OR)));
    }

    @Test
    public void testGetConstraints() {
        List<Relationship> constraints = featureModel.getConstraints();

        assertAll(() -> assertEquals(RelationshipType.REQUIRES, constraints.get(0).getType()),
                () -> assertEquals(RelationshipType.EXCLUDES, constraints.get(1).getType()),
                () -> assertEquals(RelationshipType.ThreeCNF, constraints.get(2).getType()),

                () -> assertEquals("requires(F8, F6)", constraints.get(0).getConfRule()),
                () -> assertEquals("excludes(F1, F4)", constraints.get(1).getConfRule()),
                () -> assertEquals("3cnf(~F1, F7, F8)", constraints.get(2).getConfRule()),

                () -> assertEquals(constraints.get(0), constraints.get(0)));
    }

    @Test
    public void testGetNumOfConstraints() {
        assertEquals(4, featureModel.getNumOfConstraints());
    }

    @Test
    public void testTestToString() {

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
                String.format("\t%s\n", "optional(F1, FM_10_0)") +
                String.format("\t%s\n", "mandatory(FM_10_0, F2)") +
                String.format("\t%s\n", "or(FM_10_0, F3, F4, F5)") +
                String.format("\t%s\n", "alternative(FM_10_0, F6, F7)") +
                String.format("\t%s\n", "optional(F8, F2)") +
                "CONSTRAINTS:\n" +
                String.format("\t%s\n", "requires(F8, F6)") +
                String.format("\t%s\n", "excludes(F1, F4)") +
                String.format("\t%s\n", "3cnf(~F1, F7, F8)") +
                String.format("\t%s\n", "requires(F2, F6)");

        assertEquals(st, featureModel.toString());
    }

    @Test
    void testSmartwatch() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/smartwatch.sxfm");
        SXFMParser parser = new SXFMParser();
        FeatureModel featureModel = parser.parse(fileFM);

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
                String.format("\t%s\n", "optional(Camera, Smartwatch)") +
                String.format("\t%s\n", "optional(Compass, Smartwatch)") +
                String.format("\t%s\n", "or(Connector, GPS, Cellular, Wifi, Bluetooth)") +
                String.format("\t%s\n", "alternative(Screen, Analog, High Resolution, E-ink)") +
                "CONSTRAINTS:\n" +
                String.format("\t%s\n", "requires(Camera, High Resolution)") +
                String.format("\t%s\n", "requires(Compass, GPS)") +
                String.format("\t%s\n", "excludes(Analog, Cellular)") +
                String.format("\t%s\n", "3cnf(~Analog, Cellular, Wifi)");

        assertAll(() -> assertEquals(12, featureModel.getNumOfFeatures()),
                () -> assertEquals(6, featureModel.getNumOfRelationships()),
                () -> assertEquals(4, featureModel.getNumOfConstraints()),
                () -> assertEquals(st, featureModel.toString()));
    }
}