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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RelationshipTest {

    static AbstractRelationship optionalRelationship;
    static AbstractRelationship mandatoryRelationship;
    static AbstractRelationship orRelationship;
    static AbstractRelationship alternativeRelationship;
//    static AbstractRelationship requiresRelationship;
//    static AbstractRelationship excludesRelationship;
//    static AbstractRelationship specialRelationship;
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

        optionalRelationship = new OptionalRelationship(root, f1);
        mandatoryRelationship = new MandatoryRelationship(root, f2);

        orRelationship = new OrRelationship(f1, List.of(f3, f4));

        alternativeRelationship = new AlternativeRelationship(f2, List.of(f5, f6));

//        requiresRelationship = new BasicRelationship(RelationshipType.REQUIRES,
//                f1,
//                Collections.singletonList(f2));
//        excludesRelationship = new BasicRelationship(RelationshipType.EXCLUDES,
//                f1,
//                Collections.singletonList(f2));
//        specialRelationship = new ThreeCNFConstraint(RelationshipType.ThreeCNF,
//                "~F1 | F2");
    }

    @Test
    void testGetter() {
        assertAll(() -> assertEquals(root, optionalRelationship.getParent()),
                () -> assertEquals(f1, optionalRelationship.getChild()),
                () -> assertEquals(root, mandatoryRelationship.getParent()),
                () -> assertEquals(f2, mandatoryRelationship.getChild()),
                () -> assertEquals(f1, orRelationship.getParent()),
                () -> assertEquals(List.of(f3, f4), orRelationship.getChildren()),
                () -> assertEquals(f2, alternativeRelationship.getParent()),
                () -> assertEquals(List.of(f5, f6), alternativeRelationship.getChildren()));
    }

    @Test
    void testConfRule() {
        assertAll(() -> assertEquals("optional(root, F1)", optionalRelationship.toString()),
                () -> assertEquals("mandatory(root, F2)", mandatoryRelationship.toString()),
                () -> assertEquals("or(F1, F3, F4)", orRelationship.toString()),
                () -> assertEquals("alternative(F2, F5, F6)", alternativeRelationship.toString())
//                () -> assertEquals("requires(F1, F2)", requiresRelationship.getConfRule()),
//                () -> assertEquals("excludes(F1, F2)", excludesRelationship.getConfRule()),
//                () -> assertEquals("3cnf(~F1, F2)", specialRelationship.getConfRule())
        );
    }

    @Test
    void testExceptions() {
        assertThrows(IllegalArgumentException.class, () -> new OrRelationship(root, Collections.emptyList()));
        assertThrows(IllegalArgumentException.class, () -> new AlternativeRelationship(root, Collections.emptyList()));
    }

//    @Test
//    public void testException() {
//        assertAll(() -> assertThrows(NullPointerException.class,
//                        () -> new ThreeCNFConstraint(RelationshipType.ThreeCNF, null)),
//                () -> assertThrows(IllegalArgumentException.class,
//                        () -> new BasicRelationship(RelationshipType.MANDATORY, f1, List.of(f2, f3))),
//                () -> assertThrows(IllegalArgumentException.class,
//                        () -> new BasicRelationship(RelationshipType.OPTIONAL, f1, List.of(f2, f3))),
//                () -> assertDoesNotThrow(() -> new BasicRelationship(RelationshipType.OR, f1, Collections.singletonList(f2))),
//                () -> assertDoesNotThrow(() -> new BasicRelationship(RelationshipType.ALTERNATIVE, f1, Collections.singletonList(f2))),
//                () -> assertThrows(IllegalArgumentException.class,
//                        () -> new BasicRelationship(RelationshipType.REQUIRES, f1, List.of(f2, f3))),
//                () -> assertThrows(IllegalArgumentException.class,
//                        () -> new BasicRelationship(RelationshipType.EXCLUDES, f1, List.of(f2, f3))));
//    }
//
//    @Test
//    void isOptional() {
//        assertAll(() -> assertTrue(optionalRelationship.isOptional()),
//                () -> assertFalse(mandatoryRelationship.isOptional()),
//                () -> assertTrue(orRelationship.isOptional()),
//                () -> assertFalse(alternativeRelationship.isOptional()),
//                () -> assertFalse(requiresRelationship.isOptional()),
//                () -> assertFalse(excludesRelationship.isOptional()),
//                () -> assertFalse(specialRelationship.isOptional()));
//    }

    @Test
    void testIsParent() {
        assertAll(() -> assertTrue(optionalRelationship.isParent(root)),
                () -> assertFalse(mandatoryRelationship.isParent(f2)));
    }

    @Test
    void testIsChild() {
        assertAll(() -> assertTrue(optionalRelationship.isChild(f1)),
                () -> assertFalse(optionalRelationship.isChild(f2)));
    }

    @Test
    void testContains(){
        assertAll(() -> assertTrue(orRelationship.contains(f3)),
                () -> assertFalse(orRelationship.contains(f2)));
    }

    @Test
    void testBuilders() {
        Feature root = Feature.createRoot("root", "root");
        Feature f1 = new Feature("F11", "ID1");
        Feature f2 = new Feature("F21", "ID2");
        Feature f3 = new Feature("F31", "ID3");
        Feature f4 = new Feature("F41", "ID4");
        Feature f5 = new Feature("F51", "ID5");
        Feature f6 = new Feature("F61", "ID6");

        AbstractRelationship optionalRelationship = OptionalRelationship.builder()
                .from(root)
                .to(f1)
                .build();
        AbstractRelationship mandatoryRelationship = MandatoryRelationship.builder()
                .from(root)
                .to(f2)
                .build();

        AbstractRelationship orRelationship = OrRelationship.builder()
                .from(f1)
                .to(List.of(f3, f4))
                .build();

        AbstractRelationship alternativeRelationship = AlternativeRelationship.builder()
                .from(f2)
                .to(List.of(f5, f6))
                .build();

        assertAll(() -> assertEquals("optional(root, F11)", optionalRelationship.toString()),
                () -> assertEquals("mandatory(root, F21)", mandatoryRelationship.toString()),
                () -> assertEquals("or(F11, F31, F41)", orRelationship.toString()),
                () -> assertEquals("alternative(F21, F51, F61)", alternativeRelationship.toString())
//                () -> assertEquals("requires(F1, F2)", requiresRelationship.getConfRule()),
//                () -> assertEquals("excludes(F1, F2)", excludesRelationship.getConfRule()),
//                () -> assertEquals("3cnf(~F1, F2)", specialRelationship.getConfRule())
        );
    }

    @Test
    void testClone() throws CloneNotSupportedException {
        AbstractRelationship cloneRel = mandatoryRelationship.clone();
        AbstractRelationship cloneRel2 = orRelationship.clone();

        assertAll(() -> assertNotSame(mandatoryRelationship,cloneRel),
                () -> assertEquals(mandatoryRelationship, cloneRel),
                () -> assertEquals(mandatoryRelationship.toString(), cloneRel.toString()),
                () -> assertEquals(mandatoryRelationship.getParent(), cloneRel.getParent()),
                () -> assertEquals(mandatoryRelationship.getChild(), cloneRel.getChild()),
                () -> assertNotSame(orRelationship, cloneRel2),
                () -> assertEquals(orRelationship, cloneRel2),
                () -> assertEquals(orRelationship.toString(), cloneRel2.toString()),
                () -> assertEquals(orRelationship.getParent(), cloneRel2.getParent()),
                () -> assertEquals(orRelationship.getChildren().size(), cloneRel2.getChildren().size()),
                () -> assertEquals(orRelationship.getChildren().get(0), cloneRel2.getChildren().get(0)),
                () -> assertEquals(orRelationship.getChildren().get(1), cloneRel2.getChildren().get(1)));
    }
}