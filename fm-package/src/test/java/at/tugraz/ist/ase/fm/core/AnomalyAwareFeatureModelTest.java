/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core;

import at.tugraz.ist.ase.fm.builder.RelationshipBuilder;
import at.tugraz.ist.ase.fm.core.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.fm.core.anomaly.AnomalyAwareFeatureBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnomalyAwareFeatureModelTest {
    static FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>> fm;
    static AnomalyAwareFeature root;
    static AnomalyAwareFeature pay;
    static AnomalyAwareFeature ABtesting;
    static AnomalyAwareFeature statistics;
    static AnomalyAwareFeature qa;
    static AnomalyAwareFeature license;
    static AnomalyAwareFeature nonlicense;
    static AnomalyAwareFeature multiplechoice;
    static AnomalyAwareFeature singlechoice;

    @BeforeAll
    static void setUp() {
        fm = new FeatureModel<>("test", new AnomalyAwareFeatureBuilder(), new RelationshipBuilder());
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
    @SuppressWarnings("unchecked")
    void testClone() throws CloneNotSupportedException {
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>> fm2 = (FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>>) fm.clone();
        assertEquals(fm.toString(), fm2.toString());
    }
}