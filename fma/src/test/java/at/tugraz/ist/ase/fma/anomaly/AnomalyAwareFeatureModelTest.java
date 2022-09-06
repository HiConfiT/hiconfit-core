/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.anomaly;

import at.tugraz.ist.ase.fm.builder.ConstraintBuilder;
import at.tugraz.ist.ase.fm.builder.RelationshipBuilder;
import at.tugraz.ist.ase.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.fm.core.CTConstraint;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import at.tugraz.ist.ase.fm.translator.ConfRuleTranslator;
import at.tugraz.ist.ase.fm.translator.IConfRuleTranslatable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnomalyAwareFeatureModelTest {
    static FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> fm;
    static AnomalyAwareFeature root;
    static AnomalyAwareFeature pay;
    static AnomalyAwareFeature ABtesting;
    static AnomalyAwareFeature statistics;
    static AnomalyAwareFeature qa;
    static AnomalyAwareFeature license;
    static AnomalyAwareFeature nonlicense;
    static AnomalyAwareFeature multiplechoice;
    static AnomalyAwareFeature singlechoice;

    static IConfRuleTranslatable translator = new ConfRuleTranslator();

    @BeforeAll
    static void setUp() {
        fm = new FeatureModel<>("test", new AnomalyAwareFeatureBuilder(), new RelationshipBuilder(translator), new ConstraintBuilder(translator));
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

        fm.addRequires(ABtesting, statistics);
        fm.addExcludes(ABtesting, nonlicense);
        fm.addRequires(ABtesting, root);
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
                CONSTRAINTS:
                	requires(ABtesting, statistics)
                	excludes(ABtesting, nonlicense)
                	requires(ABtesting, survey)
                """;

        assertEquals(expected, fm.toString());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testClone() throws CloneNotSupportedException {
        FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> fm2 = (FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>) fm.clone();
        assertEquals(fm.toString(), fm2.toString());
    }
}