/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FeatureTest {

    static Feature root;
    static Feature f1;
    static Feature f2;
    static Feature f3;
    static Feature f4;
    static Feature f5;
    static Feature f6;

    @BeforeAll
    static void setUp() {
        root = Feature.createRoot("root", "root");
        f1 = new Feature("F1", "ID1");
        f2 = new Feature("F2", "ID2");
        f3 = new Feature("F3", "ID3");
        f4 = new Feature("F4", "ID4");
        f5 = new Feature("F5", "ID5");
        f6 = new Feature("F6", "ID6");

        AbstractRelationship optionalRelationship = OptionalRelationship.builder()
                .parent(root)
                .child(f1)
                .build();
        root.addRelationship(optionalRelationship);
        AbstractRelationship mandatoryRelationship = MandatoryRelationship.builder()
                .parent(root)
                .child(f2)
                .build();
        root.addRelationship(mandatoryRelationship);

        AbstractRelationship orRelationship = OrRelationship.builder()
                .parent(f1)
                .children(List.of(f3, f4))
                .build();
        f1.addRelationship(orRelationship);

        AbstractRelationship alternativeRelationship = AlternativeRelationship.builder()
                .parent(f2)
                .children(List.of(f5, f6))
                .build();
        f2.addRelationship(alternativeRelationship);
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
//        Feature root = Feature.createRoot("root", "root");
//        Feature f1 = new Feature("F1", "ID1");
//        Feature f2 = new Feature("F2", "ID2");
//        Feature f3 = new Feature("F3", "ID3");
//        Feature f4 = new Feature("F4", "ID4");
//        Feature f5 = new Feature("F5", "ID5");
//        Feature f6 = new Feature("F6", "ID6");
//
//        AbstractRelationship optionalRelationship = OptionalRelationship.builder()
//                .parent(root)
//                .child(f1)
//                .build();
//        root.addRelationship(optionalRelationship);
//        AbstractRelationship mandatoryRelationship = MandatoryRelationship.builder()
//                .parent(root)
//                .child(f2)
//                .build();
//        root.addRelationship(mandatoryRelationship);
//
//        AbstractRelationship orRelationship = OrRelationship.builder()
//                .parent(f1)
//                .children(List.of(f3, f4))
//                .build();
//        f1.addRelationship(orRelationship);
//
//        AbstractRelationship alternativeRelationship = AlternativeRelationship.builder()
//                .parent(f2)
//                .children(List.of(f5, f6))
//                .build();
//        f2.addRelationship(alternativeRelationship);

        assertAll(() -> assertEquals(2, root.getChildren().size()),
                () -> assertEquals(List.of(f1, f2), root.getChildren()),
                () -> assertEquals(2, f1.getChildren().size()),
                () -> assertEquals(List.of(f3, f4), f1.getChildren()),
                () -> assertEquals(2, f2.getChildren().size()),
                () -> assertEquals(List.of(f5, f6), f2.getChildren()),
                () -> assertEquals(0, f3.getChildren().size()),
                () -> assertEquals(0, f4.getChildren().size()),
                () -> assertEquals(0, f5.getChildren().size()),
                () -> assertEquals(0, f6.getChildren().size()));
    }

    @Test
    void testCloneWithRelationships() throws CloneNotSupportedException {
//        Feature root = Feature.createRoot("root", "root");
//        Feature f1 = new Feature("F11", "ID1");
//        Feature f2 = new Feature("F21", "ID2");
//        Feature f3 = new Feature("F31", "ID3");
//        Feature f4 = new Feature("F41", "ID4");
//        Feature f5 = new Feature("F51", "ID5");
//        Feature f6 = new Feature("F61", "ID6");
//
//        AbstractRelationship optionalRelationship = OptionalRelationship.builder()
//                .parent(root)
//                .child(f1)
//                .build();
//        root.addRelationship(optionalRelationship);
//        AbstractRelationship mandatoryRelationship = MandatoryRelationship.builder()
//                .parent(root)
//                .child(f2)
//                .build();
//        root.addRelationship(mandatoryRelationship);
//
//        AbstractRelationship orRelationship = OrRelationship.builder()
//                .parent(f1)
//                .children(List.of(f3, f4))
//                .build();
//        f1.addRelationship(orRelationship);
//
//        AbstractRelationship alternativeRelationship = AlternativeRelationship.builder()
//                .parent(f2)
//                .children(List.of(f5, f6))
//                .build();
//        f2.addRelationship(alternativeRelationship);

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
                () -> assertEquals(f1.getRelationships(), fClone1.getRelationships()),
                () -> assertNotSame(f1.getRelationships(), fClone1.getRelationships()),
                () -> assertEquals(f1.getRelationships().size(), fClone1.getRelationships().size()),
                () -> assertEquals(f1.getRelationships().get(0), fClone1.getRelationships().get(0)),
                () -> assertNotSame(f1.getRelationships().get(0), fClone1.getRelationships().get(0)));
//                () -> assertNotSame(f1, fClone.getRelationships().get(0).getChildren().get(0)),
//                () -> assertNotSame(f2, fClone.getRelationships().get(0).getChildren().get(1)),
//                () -> assertNotSame(f3, fClone.getRelationships().get(1).getChildren().get(0)),
//                () -> assertNotSame(f4, fClone.getRelationships().get(1).getChildren().get(1)),
//                () -> assertNotSame(f5, fClone.getRelationships().get(2).getChildren().get(0)),
//                () -> assertNotSame(f6, fClone.getRelationships().get(2).getChildren().get(1)));
    }
}