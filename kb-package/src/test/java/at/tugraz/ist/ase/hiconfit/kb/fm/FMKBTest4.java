/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.kb.fm;

import at.tugraz.ist.ase.hiconfit.fm.builder.ConstraintBuilder;
import at.tugraz.ist.ase.hiconfit.fm.builder.FeatureBuilder;
import at.tugraz.ist.ase.hiconfit.fm.builder.RelationshipBuilder;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.Feature;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import at.tugraz.ist.ase.hiconfit.fm.parser.FeatureIDEParser;
import at.tugraz.ist.ase.hiconfit.fm.parser.FeatureModelParserException;
import at.tugraz.ist.ase.hiconfit.fm.translator.ConfRuleTranslator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FMKBTest4 {

    static FeatureBuilder featureBuilder = new FeatureBuilder();
    static ConfRuleTranslator confRuleTranslator = new ConfRuleTranslator();
    static RelationshipBuilder relationshipBuilder = new RelationshipBuilder(confRuleTranslator);
    static ConstraintBuilder constraintBuilder = new ConstraintBuilder(confRuleTranslator);

    static FMKB<Feature, AbstractRelationship<Feature>, CTConstraint> kb;
    static FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel;

    @BeforeAll
    static void setUp() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/complex_featureide_model.xml");

        FeatureIDEParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = new FeatureIDEParser<>(featureBuilder, relationshipBuilder, constraintBuilder);
        featureModel = parser.parse(fileFM);

        kb = new FMKB<>(featureModel, true);

        kb.getConstraintList().forEach(c -> {
            System.out.println(c);
            c.getChocoConstraints().forEach(System.out::println);
            System.out.println("not " + c);
            c.getNegChocoConstraints().forEach(System.out::println);
        });
    }

    @Test
    void testVariables() {
        List<String> expectedVariables = List.of("test1", "A", "B", "C", "D",
                "AA", "AB", "AC", "AD", "BA", "BB", "BC", "CA", "CB", "CC", "DA", "DB",
                "ADA", "ADB", "DAA", "DAB");

        assertAll(() -> Assertions.assertEquals(21, kb.getNumVariables()),
                () -> Assertions.assertEquals("complex_featureide_model.xml", kb.getName()),
                () -> {
                    for (int i = 0; i < expectedVariables.size(); i++) {
                        Assertions.assertEquals(expectedVariables.get(i), kb.getVariable(i).getName());
                        assertEquals(expectedVariables.get(i), kb.getBoolVar(expectedVariables.get(i)).getName());
                    }
                });
    }

    @Test
    void testConstraints() {
        List<List<String>> expectedConstraints = List.of(
                List.of("ARITHM ([test1 + not(A) >= 1])", "ARITHM ([A + not(test1) >= 1])"),
                List.of("ARITHM ([test1 + not(B) >= 1])"),
                List.of("ARITHM ([test1 + not(C) >= 1])"),
                List.of("ARITHM ([test1 + not(D) >= 1])", "ARITHM ([D + not(test1) >= 1])"),
                List.of("ARITHM ([A + not(AA) >= 1])", "ARITHM ([AA + not(A) >= 1])"),
                List.of("ARITHM ([A + not(AB) >= 1])"),
                List.of("ARITHM ([A + not(AC) >= 1])"),
                List.of("ARITHM ([A + not(AD) >= 1])", "ARITHM ([AD + not(A) >= 1])"),
                List.of("ARITHM ([B + not(BA) >= 1])", "ARITHM ([BA + not(B) >= 1])"),
                List.of("ARITHM ([B + not(BB) >= 1])"),
                List.of("ARITHM ([B + not(BC) >= 1])"),
                List.of("ARITHM ([C + not(CA) >= 1])", "ARITHM ([C + not(CB) >= 1])",
                        "ARITHM ([C + not(CC) >= 1])",
                        "SUM ([not(C) + CC + CB + CA >= 1])"),
                List.of("BV_1 = [0,1]=>ARITHM ([DA = [0,1] + DB = [0,1] = 1]), !BV_1 = [0,1]=>ARITHM ([PropNotEqualXY_C(DA, DB)])",
                        "BV_2 = [0,1]=>ARITHM ([PropNotEqualXY_C(DA, DB)]), !BV_2 = [0,1]=>ARITHM ([DA = [0,1] + DB = [0,1] = 1])",
                        "ARITHM ([D + not(DA) >= 1])",
                        "ARITHM ([D + not(DB) >= 1])",
                        "ARITHM ([not(D) + BV_1 >= 1])",
                        "ARITHM ([BV_1 + not(DA) >= 1])",
                        "ARITHM ([BV_1 + not(DB) >= 1])"),
                List.of("ARITHM ([AD + not(ADA) >= 1])", "ARITHM ([AD + not(ADB) >= 1])",
                        "SUM ([not(AD) + ADB + ADA >= 1])"),
                List.of("ARITHM ([DA + not(DAA) >= 1])", "ARITHM ([DA + not(DAB) >= 1])",
                        "SUM ([not(DA) + DAB + DAA >= 1])"),
                List.of("ARITHM ([not(AB) + not(CB) >= 1])"),
                List.of("ARITHM ([not(AC) + not(CC) >= 1])"),
                List.of("ARITHM ([not(AB) + not(ADA) >= 1])"),
                List.of("ARITHM ([ADB + not(BC) >= 1])"),
                List.of("SUM ([not(CA) + CB + BC >= 1])"),
                List.of("SUM ([not(DAA) + not(AC) + not(AB) >= 1])"),
                List.of("SUM ([not(DAB) + not(AB) + AC >= 1])"),
                List.of("ARITHM ([AB + not(DB) >= 1])"),
                List.of("ARITHM ([AC + not(DB) >= 1])"),
                List.of("SUM ([not(DAA) + not(AC) + AB >= 1])"),
                List.of("ARITHM ([BB + not(ADA) >= 1])", "ARITHM ([DAB + not(ADA) >= 1])", "SUM ([not(DAB) + not(BB) + ADA >= 1])")
        );

        List<List<String>> expectedNegConstraints = List.of(
                List.of("ARITHM ([test1 + A >= 1])", "ARITHM ([not(test1) + not(A) >= 1])"),
                List.of("ARITHM ([B = 1])", "ARITHM ([not(test1) = 1])"),
                List.of("ARITHM ([C = 1])", "ARITHM ([not(test1) = 1])"),
                List.of("ARITHM ([test1 + D >= 1])", "ARITHM ([not(test1) + not(D) >= 1])"),
                List.of("ARITHM ([A + AA >= 1])", "ARITHM ([not(A) + not(AA) >= 1])"),
                List.of("ARITHM ([AB = 1])", "ARITHM ([not(A) = 1])"),
                List.of("ARITHM ([AC = 1])", "ARITHM ([not(A) = 1])"),
                List.of("ARITHM ([A + AD >= 1])", "ARITHM ([not(A) + not(AD) >= 1])"),
                List.of("ARITHM ([B + BA >= 1])", "ARITHM ([not(B) + not(BA) >= 1])"),
                List.of("ARITHM ([BB = 1])", "ARITHM ([not(B) = 1])"),
                List.of("ARITHM ([BC = 1])", "ARITHM ([not(B) = 1])"),
                List.of("ARITHM ([not(C) + not(CA) >= 1])", "ARITHM ([not(C) + not(CB) >= 1])",
                        "ARITHM ([not(C) + not(CC) >= 1])",
                        "SUM ([CC + CB + CA + C >= 1])"),
                List.of("BV_2 = [0,1]=>ARITHM ([PropNotEqualXY_C(DA, DB)]), !BV_2 = [0,1]=>ARITHM ([DA = [0,1] + DB = [0,1] = 1])",
                        "ARITHM ([not(D) + BV_2 >= 1])",
                        "SUM ([DB + DA + D >= 1])",
                        "SUM ([BV_2 + DB + DA >= 1])"),
                List.of("ARITHM ([not(AD) + not(ADA) >= 1])", "ARITHM ([not(AD) + not(ADB) >= 1])",
                        "SUM ([ADB + ADA + AD >= 1])"),
                List.of("ARITHM ([not(DA) + not(DAA) >= 1])", "ARITHM ([not(DA) + not(DAB) >= 1])",
                        "SUM ([DAB + DAA + DA >= 1])"),
                List.of("ARITHM ([AB = 1])", "ARITHM ([CB = 1])"),
                List.of("ARITHM ([AC = 1])", "ARITHM ([CC = 1])"),
                List.of("ARITHM ([AB = 1])", "ARITHM ([ADA = 1])"),
                List.of("ARITHM ([BC = 1])", "ARITHM ([not(ADB) = 1])"),
                List.of("ARITHM ([CA = 1])", "ARITHM ([not(BC) = 1])", "ARITHM ([not(CB) = 1])"),
                List.of("ARITHM ([AB = 1])", "ARITHM ([AC = 1])", "ARITHM ([DAA = 1])"),
                List.of("ARITHM ([AB = 1])", "ARITHM ([DAB = 1])", "ARITHM ([not(AC) = 1])"),
                List.of("ARITHM ([DB = 1])", "ARITHM ([not(AB) = 1])"),
                List.of("ARITHM ([DB = 1])", "ARITHM ([not(AC) = 1])"),
                List.of("ARITHM ([AC = 1])", "ARITHM ([DAA = 1])", "ARITHM ([not(AB) = 1])"),
                List.of("ARITHM ([BB + ADA >= 1])", "ARITHM ([ADA + DAB >= 1])", "SUM ([not(DAB) + ADA + BB >= 1])",
                        "SUM ([not(BB) + DAB + ADA >= 1])", "SUM ([not(DAB) + not(ADA) + not(BB) >= 1])")
        );

        Assertions.assertEquals(26, kb.getNumConstraints());

        assertAll(() -> {
                    for (int i = 0; i < expectedConstraints.size(); i++) {
                        List<String> expected = expectedConstraints.get(i);
                        for (int j = 0; j < expected.size(); j++) {
                            Assertions.assertEquals(expected.get(j), kb.getConstraint(i).getChocoConstraints().get(j).toString());
                        }
                    }},
                () -> {
                    for (int i = 0; i < expectedNegConstraints.size(); i++) {
                        List<String> expectedNeg = expectedNegConstraints.get(i);
                        for (int j = 0; j < expectedNeg.size(); j++) {
                            Assertions.assertEquals(expectedNeg.get(j), kb.getConstraint(i).getNegChocoConstraints().get(j).toString());
                        }
                    }}
        );
    }
}
