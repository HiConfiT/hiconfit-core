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

    static Relationship optionalRelationship;
    static Relationship mandatoryRelationship;
    static Relationship orRelationship;
    static Relationship alternativeRelationship;
    static Relationship requiresRelationship;
    static Relationship excludesRelationship;
    static Relationship specialRelationship;
    static Feature f1;
    static Feature f2;
    static Feature f3;

    @BeforeAll
    static void setUp() {
        f1 = new Feature("F1", "ID1");
        f2 = new Feature("F2", "ID2");
        f3 = new Feature("F3", "ID3");
        optionalRelationship = new BasicRelationship(RelationshipType.OPTIONAL,
                f1,
                Collections.singletonList(f2));
        mandatoryRelationship = new BasicRelationship(RelationshipType.MANDATORY,
                f1,
                Collections.singletonList(f2));
        orRelationship = new BasicRelationship(RelationshipType.OR,
                f1,
                List.of(f2, f3));
        alternativeRelationship = new BasicRelationship(RelationshipType.ALTERNATIVE,
                f1,
                List.of(f2, f3));
        requiresRelationship = new BasicRelationship(RelationshipType.REQUIRES,
                f1,
                Collections.singletonList(f2));
        excludesRelationship = new BasicRelationship(RelationshipType.EXCLUDES,
                f1,
                Collections.singletonList(f2));
        specialRelationship = new ThreeCNFConstraint(RelationshipType.ThreeCNF,
                "~F1 | F2");
    }

    @Test
    void testConfRule() {
        assertAll(() -> assertEquals("optional(F1, F2)", optionalRelationship.getConfRule()),
                () -> assertEquals("mandatory(F1, F2)", mandatoryRelationship.getConfRule()),
                () -> assertEquals("or(F1, F2, F3)", orRelationship.getConfRule()),
                () -> assertEquals("alternative(F1, F2, F3)", alternativeRelationship.getConfRule()),
                () -> assertEquals("requires(F1, F2)", requiresRelationship.getConfRule()),
                () -> assertEquals("excludes(F1, F2)", excludesRelationship.getConfRule()),
                () -> assertEquals("3cnf(~F1, F2)", specialRelationship.getConfRule()));
    }

    @Test
    public void testException() {
        assertAll(() -> assertThrows(NullPointerException.class,
                        () -> new ThreeCNFConstraint(RelationshipType.ThreeCNF, null)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new BasicRelationship(RelationshipType.MANDATORY, f1, List.of(f2, f3))),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new BasicRelationship(RelationshipType.OPTIONAL, f1, List.of(f2, f3))),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new BasicRelationship(RelationshipType.OR, f1, Collections.singletonList(f2))),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new BasicRelationship(RelationshipType.ALTERNATIVE, f1, Collections.singletonList(f2))),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new BasicRelationship(RelationshipType.REQUIRES, f1, List.of(f2, f3))),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new BasicRelationship(RelationshipType.EXCLUDES, f1, List.of(f2, f3))));
    }

    @Test
    void isOptional() {
        assertAll(() -> assertTrue(optionalRelationship.isOptional()),
                () -> assertFalse(mandatoryRelationship.isOptional()),
                () -> assertTrue(orRelationship.isOptional()),
                () -> assertFalse(alternativeRelationship.isOptional()),
                () -> assertFalse(requiresRelationship.isOptional()),
                () -> assertFalse(excludesRelationship.isOptional()),
                () -> assertFalse(specialRelationship.isOptional()));
    }

    @Test
    void isType() {
        assertAll(() -> assertTrue(optionalRelationship.isType(RelationshipType.OPTIONAL)),
                () -> assertTrue(mandatoryRelationship.isType(RelationshipType.MANDATORY)),
                () -> assertTrue(orRelationship.isType(RelationshipType.OR)),
                () -> assertTrue(alternativeRelationship.isType(RelationshipType.ALTERNATIVE)),
                () -> assertTrue(requiresRelationship.isType(RelationshipType.REQUIRES)),
                () -> assertTrue(excludesRelationship.isType(RelationshipType.EXCLUDES)),
                () -> assertTrue(specialRelationship.isType(RelationshipType.ThreeCNF)),
                () -> assertFalse(optionalRelationship.isType(RelationshipType.MANDATORY)),
                () -> assertFalse(mandatoryRelationship.isType(RelationshipType.OPTIONAL)),
                () -> assertFalse(orRelationship.isType(RelationshipType.MANDATORY)),
                () -> assertFalse(alternativeRelationship.isType(RelationshipType.OR)),
                () -> assertFalse(requiresRelationship.isType(RelationshipType.ALTERNATIVE)),
                () -> assertFalse(excludesRelationship.isType(RelationshipType.REQUIRES)),
                () -> assertFalse(specialRelationship.isType(RelationshipType.EXCLUDES)));
    }

    @Test
    void belongsToLeftSide() {
        assertAll(() -> assertTrue(optionalRelationship.presentAtLeftSide(f1)),
                () -> assertFalse(mandatoryRelationship.presentAtLeftSide(f2)));
    }

    @Test
    void belongsToRightSide() {
        assertAll(() -> assertTrue(optionalRelationship.presentAtRightSide(f2)),
                () -> assertFalse(optionalRelationship.presentAtRightSide(f1)));
    }

    @Test
    void testBasicRelationshipBuilder() {
        BasicRelationship r = BasicRelationship.builder()
                .type(RelationshipType.MANDATORY)
                .leftSide(f1)
                .rightSide( Collections.singletonList(f2))
                .build();

        Relationship r1 = ThreeCNFConstraint.builder()
                .type(RelationshipType.ThreeCNF)
                .constraint3CNF("~F1 | F2")
                .build();

        assertAll(() -> assertNotNull(r),
                () -> assertEquals(RelationshipType.MANDATORY, r.getType()),
                () -> assertEquals(f1, r.getLeftSide()),
                () -> assertEquals(f2, r.getRightSide().get(0)),
                () -> assertEquals(1, r.getRightSide().size()),
                () -> assertEquals("mandatory(F1, F2)", r.getConfRule()),
                () -> assertEquals("3cnf(~F1, F2)", r1.getConfRule()));
    }
}