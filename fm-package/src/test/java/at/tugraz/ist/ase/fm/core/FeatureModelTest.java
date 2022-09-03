/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core;

import at.tugraz.ist.ase.fm.builder.FeatureBuilder;
import at.tugraz.ist.ase.fm.builder.RelationshipBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FeatureModelTest {
    static FeatureModel<Feature, AbstractRelationship<Feature>> fm;
    static Feature root;
    static Feature pay;
    static Feature ABtesting;
    static Feature statistics;
    static Feature qa;
    static Feature license;
    static Feature nonlicense;
    static Feature multiplechoice;
    static Feature singlechoice;

    @BeforeAll
    static void setUp() {
        fm = new FeatureModel<>("test", new FeatureBuilder(), new RelationshipBuilder());

        root = fm.addRoot("survey", "survey");
        // the order of adding features should be breadth-first
        pay = fm.addFeature("pay", "pay");
        ABtesting = fm.addFeature("ABtesting", "ABtesting");
        statistics = fm.addFeature("statistics", "statistics");
        qa = fm.addFeature("qa", "qa");
        license = fm.addFeature("license", "license");
        nonlicense = fm.addFeature("nonlicense", "nonlicense");
        multiplechoice = fm.addFeature("multiplechoice", "multiplechoice");
        singlechoice = fm.addFeature("singlechoice", "singlechoice");

        fm.addMandatoryRelationship(root, pay);
        fm.addOptionalRelationship(root, ABtesting);
        fm.addMandatoryRelationship(root, statistics);
        fm.addMandatoryRelationship(root, qa);
        fm.addAlternativeRelationship(pay, List.of(license, nonlicense));
        fm.addOrRelationship(qa, List.of(multiplechoice, singlechoice));
        fm.addOptionalRelationship(ABtesting, statistics);

//        fm.addConstraint(RelationshipType.REQUIRES, fm.getFeature("ABtesting"), Collections.singletonList(fm.getFeature("statistics")));
//        fm.addConstraint(RelationshipType.EXCLUDES, fm.getFeature("ABtesting"), Collections.singletonList(fm.getFeature("nonlicense")));
//        fm.addConstraint(RelationshipType.REQUIRES, fm.getFeature("ABtesting"), Collections.singletonList(fm.getFeature("survey")));
    }

    @Test
    void testAddFeatureWithoutRoot() {
        FeatureModel<Feature, AbstractRelationship<Feature>> anotherFM = new FeatureModel<>("test", new FeatureBuilder(), new RelationshipBuilder());
        assertThrows(IllegalStateException.class, () -> anotherFM.addFeature("feature", "feature"));
    }

    @Test
    void testAddRootWhenOnceAlreadyExist() {
        FeatureModel<Feature, AbstractRelationship<Feature>> anotherFM = new FeatureModel<>("test", new FeatureBuilder(), new RelationshipBuilder());
        anotherFM.addRoot("survey", "survey");
        assertThrows(IllegalArgumentException.class, () -> anotherFM.addRoot("feature", "feature"));
    }

    @Test
    void testAddFeatureWithTheSameNameAndID() {
        assertThrows(IllegalArgumentException.class, () -> fm.addFeature("ABtesting", "123"));
        assertThrows(IllegalArgumentException.class, () -> fm.addFeature("123", "singlechoice"));
    }

    @Test
    void testAddWrongRelationships() {
        Feature f1 = new Feature("f1", "f1");
        Feature f2 = new Feature("f2", "f2");

        // f1, f2 are not in the feature model
        assertThrows(IllegalArgumentException.class, () -> fm.addAlternativeRelationship(root, List.of(f1, f2)));
        assertThrows(IllegalArgumentException.class, () -> fm.addMandatoryRelationship(f1, ABtesting));
    }

    @Test
    void testToString() {
        String expected = """
                FEATURES:
                	survey
                	pay
                	ABtesting
                	statistics
                	qa
                	license
                	nonlicense
                	multiplechoice
                	singlechoice
                RELATIONSHIPS:
                	mandatory(survey, pay)
                	optional(survey, ABtesting)
                	mandatory(survey, statistics)
                	mandatory(survey, qa)
                	alternative(pay, license, nonlicense)
                	or(qa, multiplechoice, singlechoice)
                	optional(ABtesting, statistics)
                """;

        assertEquals(expected, fm.toString());
    }

    @Test
    void testGetName() {
        assertEquals(fm.getName(), "survey");
    }

    @Test
    void testGetFeature() {
        Feature f1 = fm.getBfFeatures().get(4);

        Feature f2 = fm.getFeature(4);
        Feature f3 = fm.getFeature("qa");

        assertAll(() -> assertEquals(f1, f2),
                () -> assertEquals(f1, f3),
                () -> assertThrows(IndexOutOfBoundsException.class, () -> fm.getFeature(-1)),
                () -> assertDoesNotThrow(() -> fm.getFeature(0)),
                () -> assertDoesNotThrow(() -> fm.getFeature(8)),
                () -> assertThrows(IndexOutOfBoundsException.class, () -> fm.getFeature(9)),
                () -> assertThrows(IllegalArgumentException.class, () -> fm.getFeature("")),
                () -> assertThrows(IllegalArgumentException.class, () -> fm.getFeature("no exists")),
                () -> assertThrows(NullPointerException.class, () -> fm.getFeature(null)));
    }

    @Test
    void testGetNumOfFeatures() {
        assertEquals(9, fm.getNumOfFeatures());
    }

    @Test
    void testConnectionBetweenParentAndChildren() {
        assertAll(() -> assertEquals(fm.getRoot(), fm.getRelationships().get(0).getParent()),
                () -> assertEquals(fm.getFeature("pay"), fm.getRelationships().get(0).getChild()),
                () -> assertEquals(fm.getFeature("pay"), fm.getRelationships().get(4).getParent()),
                () -> assertEquals(fm.getFeature("license"), fm.getRelationships().get(4).getChildren().get(0)),
                () -> assertEquals(fm.getFeature("nonlicense"), fm.getRelationships().get(4).getChildren().get(1))
                );
    }

    @Test
    @SuppressWarnings("unchecked")
    void testNumOfRelationships() {
        assertAll(() -> assertEquals(7, fm.getNumOfRelationships()),
                () -> assertEquals(3, fm.getNumOfRelationships(MandatoryRelationship.class)),
                () -> assertEquals(2, fm.getNumOfRelationships(OptionalRelationship.class)),
                () -> assertEquals(1, fm.getNumOfRelationships(AlternativeRelationship.class)),
                () -> assertEquals(1, fm.getNumOfRelationships(OrRelationship.class))
        );
    }

    @Test
    void testClone() throws CloneNotSupportedException {
        FeatureModel<Feature, AbstractRelationship<Feature>> clone = (FeatureModel<Feature, AbstractRelationship<Feature>>) fm.clone();
        assertAll(
                () -> assertEquals(fm.getName(), clone.getName()),
                () -> assertEquals(fm.getNumOfFeatures(), clone.getNumOfFeatures()),
                () -> assertEquals(fm.getFeature(0), clone.getFeature(0)),
                () -> assertEquals(fm.getFeature(1), clone.getFeature(1)),
                () -> assertEquals(fm.getFeature(2), clone.getFeature(2)),
                () -> assertEquals(fm.getFeature(3), clone.getFeature(3)),
                () -> assertEquals(fm.getFeature(4), clone.getFeature(4)),
                () -> assertEquals(fm.getFeature(5), clone.getFeature(5)),
                () -> assertEquals(fm.getFeature(6), clone.getFeature(6)),
                () -> assertEquals(fm.getFeature(7), clone.getFeature(7)),
                () -> assertEquals(fm.getFeature(8), clone.getFeature(8)),
                () -> assertEquals(fm.getNumOfRelationships(), clone.getNumOfRelationships()),
                () -> assertEquals(fm.getRelationships().get(0), clone.getRelationships().get(0)),
                () -> assertEquals(fm.getRelationships().get(1), clone.getRelationships().get(1)),
                () -> assertEquals(fm.getRelationships().get(2), clone.getRelationships().get(2)),
                () -> assertEquals(fm.getRelationships().get(3), clone.getRelationships().get(3)),
                () -> assertEquals(fm.getRelationships().get(4), clone.getRelationships().get(4)),
                () -> assertEquals(fm.getRelationships().get(5), clone.getRelationships().get(5)),
                () -> assertEquals(fm.getRelationships().get(6), clone.getRelationships().get(6)),
                () -> assertEquals(fm.getNumOfRelationships(MandatoryRelationship.class), clone.getNumOfRelationships(MandatoryRelationship.class)),
                () -> assertEquals(fm.getNumOfRelationships(OptionalRelationship.class), clone.getNumOfRelationships(OptionalRelationship.class)),
                () -> assertEquals(fm.getNumOfRelationships(AlternativeRelationship.class), clone.getNumOfRelationships(AlternativeRelationship.class)),
                () -> assertEquals(fm.getNumOfRelationships(OrRelationship.class), clone.getNumOfRelationships(OrRelationship.class))
        );
    }



//
//    @Test
//    public void testIsMandatoryFeature() {
//        assertAll(() -> assertFalse(featureModel.isMandatoryFeature(featureModel.getFeature(0))),
//                () -> assertFalse(featureModel.isMandatoryFeature(featureModel.getFeature(1))),
//                () -> assertTrue(featureModel.isMandatoryFeature(featureModel.getFeature(2))),
//                () -> assertFalse(featureModel.isMandatoryFeature(featureModel.getFeature(3))),
//                () -> assertFalse(featureModel.isMandatoryFeature(featureModel.getFeature(4))),
//                () -> assertFalse(featureModel.isMandatoryFeature(featureModel.getFeature(5))),
//                () -> assertFalse(featureModel.isMandatoryFeature(featureModel.getFeature(6))),
//                () -> assertFalse(featureModel.isMandatoryFeature(featureModel.getFeature(7))),
//                () -> assertFalse(featureModel.isMandatoryFeature(featureModel.getFeature(8))));
//    }
//
//    @Test
//    public void testIsOptionalFeature() {
//        assertAll(() -> assertTrue(featureModel.isOptionalFeature(featureModel.getFeature(1))),
//                () -> assertFalse(featureModel.isOptionalFeature(featureModel.getFeature(2))),
//                () -> assertTrue(featureModel.isOptionalFeature(featureModel.getFeature(3))),
//                () -> assertTrue(featureModel.isOptionalFeature(featureModel.getFeature(4))),
//                () -> assertTrue(featureModel.isOptionalFeature(featureModel.getFeature(5))),
//                () -> assertTrue(featureModel.isOptionalFeature(featureModel.getFeature(6))),
//                () -> assertTrue(featureModel.isOptionalFeature(featureModel.getFeature(7))),
//                () -> assertTrue(featureModel.isOptionalFeature(featureModel.getFeature(8))));
//    }
//
//    @Test
//    public void testGetRightSideOfRelationships() throws FeatureModelException {
//        Feature f1 = featureModel.getFeature(0);
//        Feature f2 = featureModel.getFeature(2);
//        List<Feature> f1s = featureModel.getRightSideOfRelationships(f1);
//        List<Feature> f2s = featureModel.getRightSideOfRelationships(f2);
//
//        assertAll(() -> assertEquals("F1", f1s.get(0).toString()),
//                () -> assertEquals("F2", f1s.get(1).toString()),
//                () -> assertEquals("F3", f1s.get(2).toString()),
//                () -> assertEquals("F4", f1s.get(3).toString()),
//                () -> assertEquals("F5", f1s.get(4).toString()),
//                () -> assertEquals("F6", f1s.get(5).toString()),
//                () -> assertEquals("F7", f1s.get(6).toString()),
//                () -> assertEquals("F8", f2s.get(0).toString()));
//    }
//
//    @Test
//    public void testGetRelationshipsWith() {
//        Feature f1 = featureModel.getFeature(1);
//        Feature f2 = featureModel.getFeature(2);
//
//        System.out.println(f1);
//        System.out.println(f2);
//
//        List<Relationship> r1List = featureModel.getRelationshipsWith(f1);
//        List<Relationship> r2List = featureModel.getRelationshipsWith(f2);
//
//        List<Relationship> allRelationships = featureModel.getRelationships();
//        List<Relationship> allConstraints = featureModel.getConstraints();
//
//        assertAll(() -> assertEquals(allRelationships.get(0), r1List.get(0)),
//                () -> assertEquals(allConstraints.get(1), r1List.get(1)),
//                () -> assertEquals(allConstraints.get(2), r1List.get(2)),
//                () -> assertEquals(allRelationships.get(1), r2List.get(0)),
//                () -> assertEquals(allRelationships.get(4), r2List.get(1)));
//    }
//
//    @Test
//    public void testGetMandatoryParents() throws FeatureModelException {
//        // TODO - 3CNF
//        Feature f1 = featureModel.getFeature("F6");
//        Feature f2 = featureModel.getFeature("F2");
//
//        List<Feature> featureList = featureModel.getMandatoryParents(f1);
//
//        assertEquals(1, featureList.size());
//        assertEquals(f2, featureList.get(0));
//    }
//
//    @Test
//    public void testGetMandatoryParent() {
//    }
//
//    @Test
//    public void testGetRelationshipByConstraint() {
//    }
//
//    @Test
//    public void testGetNumOfRelationships() {
//        assertEquals(featureModel.getNumOfRelationships(), 5);
//    }
//
//    @Test
//    public void testGetNumOfRelationshipsWithSpecifyType() {
//        assertAll(() -> assertEquals(1, featureModel.getNumOfRelationships(RelationshipType.MANDATORY)),
//                () -> assertEquals(2, featureModel.getNumOfRelationships(RelationshipType.OPTIONAL)),
//                () -> assertEquals(1, featureModel.getNumOfRelationships(RelationshipType.ALTERNATIVE)),
//                () -> assertEquals(1, featureModel.getNumOfRelationships(RelationshipType.OR)));
//    }
//
//    @Test
//    public void testGetConstraints() {
//        List<Relationship> constraints = featureModel.getConstraints();
//
//        assertAll(() -> assertEquals(RelationshipType.REQUIRES, constraints.get(0).getType()),
//                () -> assertEquals(RelationshipType.EXCLUDES, constraints.get(1).getType()),
//                () -> assertEquals(RelationshipType.ThreeCNF, constraints.get(2).getType()),
//
//                () -> assertEquals("requires(F8, F6)", constraints.get(0).getConfRule()),
//                () -> assertEquals("excludes(F1, F4)", constraints.get(1).getConfRule()),
//                () -> assertEquals("3cnf(~F1, F7, F8)", constraints.get(2).getConfRule()),
//
//                () -> assertEquals(constraints.get(0), constraints.get(0)));
//    }
//
//    @Test
//    public void testGetNumOfConstraints() {
//        assertEquals(4, featureModel.getNumOfConstraints());
//    }
//

//

}