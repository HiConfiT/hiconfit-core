/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core;

import at.tugraz.ist.ase.fm.builder.ConstraintBuilder;
import at.tugraz.ist.ase.fm.builder.FeatureBuilder;
import at.tugraz.ist.ase.fm.builder.RelationshipBuilder;
import at.tugraz.ist.ase.fm.translator.ConfRuleTranslator;
import at.tugraz.ist.ase.fm.translator.IConfRuleTranslatable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FeatureTest {

    static FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> fm;
    static Feature root;
    static Feature f1;
    static Feature f2;
    static Feature f3;
    static Feature f4;
    static Feature f5;
    static Feature f6;

    static IConfRuleTranslatable translator = new ConfRuleTranslator();

    @BeforeAll
    static void setUp() {
        fm = new FeatureModel<>("test", new FeatureBuilder(), new RelationshipBuilder(translator), new ConstraintBuilder(translator));
        root = fm.addRoot("root", "root");
        f1 = fm.addFeature("F1", "ID1");
        f2 = fm.addFeature("F2", "ID2");
        f3 = fm.addFeature("F3", "ID3");
        f4 = fm.addFeature("F4", "ID4");
        f5 = fm.addFeature("F5", "ID5");
        f6 = fm.addFeature("F6", "ID6");

        fm.addOptionalRelationship(root, f1);
        fm.addMandatoryRelationship(root, f2);
        fm.addOrRelationship(f1, List.of(f3, f4));
        fm.addAlternativeRelationship(f2, List.of(f5, f6));
    }

    @Test
    public void testFeature() {
        assertAll(() -> assertEquals("F1", f1.getName()),
                () -> assertEquals("ID1", f1.getId()),
                () -> assertEquals("F1", f1.toString()));
    }

    @Test
    public void testException() {
        assertAll(() -> assertThrows(NullPointerException.class, () -> new Feature(null, null)),
                () -> assertThrows(NullPointerException.class, () -> new Feature("f", null)));
    }

    @Test
    void testIsLeaf() {
        assertAll(() -> assertFalse(f1.isLeaf()),
                () -> assertFalse(f2.isLeaf()),
                () -> assertTrue(f3.isLeaf()),
                () -> assertTrue(f4.isLeaf()),
                () -> assertTrue(f5.isLeaf()),
                () -> assertTrue(f6.isLeaf()));
    }

    @Test
    void isDuplicate() {
        Feature f7 = Feature.builder()
                .name("F1")
                .id("ID1")
                .build();
        Feature f8 = new Feature("F1", "ID2");

        assertAll(() -> assertTrue(f1.isDuplicate(f7)),
                () -> assertFalse(f1.isDuplicate(f8)),
                () -> assertTrue(f1.isIdDuplicate("ID1")),
                () -> assertFalse(f1.isIdDuplicate("ID2")),
                () -> assertTrue(f1.isNameDuplicate("F1")),
                () -> assertFalse(f1.isNameDuplicate("F2")));
    }

    @Test
    void isRoot() {
        assertAll(() -> assertTrue(root.isRoot()),
                () -> assertFalse(f1.isRoot()));
    }

    @Test
    void isAbstract() {
        Feature f = Feature.builder()
                .name("F1")
                .id("ID")
                .build();
        f.setAbstract(true);

        assertAll(() -> assertTrue(root.isAbstract()),
                () -> assertFalse(f1.isAbstract()),
                () -> assertTrue(f.isAbstract()));
    }

    @Test
    void isMandatory() {
        assertAll(() -> assertFalse(root.isMandatory()),
                () -> assertFalse(f1.isMandatory()),
                () -> assertTrue(f2.isMandatory()),
                () -> assertFalse(f3.isMandatory()),
                () -> assertFalse(f4.isMandatory()),
                () -> assertFalse(f5.isMandatory()),
                () -> assertFalse(f6.isMandatory()));
    }

    @Test
    void isOptional() {
        assertAll(() -> assertFalse(root.isOptional()),
                () -> assertTrue(f1.isOptional()),
                () -> assertFalse(f2.isOptional()),
                () -> assertTrue(f3.isOptional()),
                () -> assertTrue(f4.isOptional()),
                () -> assertTrue(f5.isOptional()),
                () -> assertTrue(f6.isOptional()));
    }

    @Test
    void setParent() {
        Feature f = Feature.builder()
                .name("F1")
                .id("ID")
                .build();

        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> root.setParent(f1)),
                () -> assertDoesNotThrow(() -> f.setParent(f1)));
    }

    @Test
    void testGetChildren() {
        assertAll(() -> assertEquals(2, root.getChildren().size()),
                () -> assertEquals(List.of(f1, f2), root.getChildren()),
                () -> assertEquals(f1, root.getChildren().get(0)),
                () -> assertEquals(f2, root.getChildren().get(1)),
                () -> assertEquals(2, f1.getChildren().size()),
                () -> assertEquals(List.of(f3, f4), f1.getChildren()),
                () -> assertEquals(f3, f1.getChildren().get(0)),
                () -> assertEquals(f4, f1.getChildren().get(1)),
                () -> assertEquals(2, f2.getChildren().size()),
                () -> assertEquals(List.of(f5, f6), f2.getChildren()),
                () -> assertEquals(f5, f2.getChildren().get(0)),
                () -> assertEquals(f6, f2.getChildren().get(1)),
                () -> assertEquals(0, f3.getChildren().size()),
                () -> assertEquals(0, f4.getChildren().size()),
                () -> assertEquals(0, f5.getChildren().size()),
                () -> assertEquals(0, f6.getChildren().size()));
    }

    @Test
    void testCloneWithRelationships() throws CloneNotSupportedException {
        Feature fClone1 = (Feature) f1.clone();

        assertAll(() -> assertNotSame(f1, fClone1),
                () -> assertEquals(f1, fClone1),
                () -> assertEquals(f1.toString(), fClone1.toString()),
                () -> assertEquals(f1.isRoot, fClone1.isRoot),
                () -> assertEquals(f1.isAbstract, fClone1.isAbstract),
                () -> assertEquals(f1.name, fClone1.name),
                () -> assertEquals(f1.id, fClone1.id),
                () -> assertEquals(f1.parent, fClone1.parent),
                () -> assertSame(f1.parent, fClone1.parent),
                () -> assertEquals(f1.getChildren(), fClone1.getChildren()),
                () -> assertEquals(f1.getRelationshipsAsParent(), fClone1.getRelationshipsAsParent()),
                () -> assertNotSame(f1.getRelationshipsAsParent(), fClone1.getRelationshipsAsParent()),
                () -> assertEquals(f1.getRelationshipsAsParent().size(), fClone1.getRelationshipsAsParent().size()),
                () -> assertEquals(f1.getRelationshipsAsParent().get(0).toString(), fClone1.getRelationshipsAsParent().get(0).toString()),
                () -> assertEquals(f1.getRelationshipsAsParent().get(0), fClone1.getRelationshipsAsParent().get(0)),
                () -> assertNotSame(f1.getRelationshipsAsParent().get(0), fClone1.getRelationshipsAsParent().get(0)));
    }
}