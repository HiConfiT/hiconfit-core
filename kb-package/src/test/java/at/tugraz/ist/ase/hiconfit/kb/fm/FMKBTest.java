/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
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
import at.tugraz.ist.ase.hiconfit.fm.parser.FeatureModelParserException;
import at.tugraz.ist.ase.hiconfit.fm.parser.SXFMParser;
import at.tugraz.ist.ase.hiconfit.fm.translator.ConfRuleTranslator;
import at.tugraz.ist.ase.hiconfit.kb.core.BoolVariable;
import at.tugraz.ist.ase.hiconfit.kb.core.Variable;
import org.chocosolver.solver.variables.BoolVar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FMKBTest {
    static FMKB<Feature, AbstractRelationship<Feature>, CTConstraint> kb;
    static FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel;

    @BeforeAll
    static void setUp() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/smartwatch.sxfm");

        FeatureBuilder featureBuilder = new FeatureBuilder();
        ConfRuleTranslator confRuleTranslator = new ConfRuleTranslator();
        RelationshipBuilder relationshipBuilder = new RelationshipBuilder(confRuleTranslator);
        ConstraintBuilder constraintBuilder = new ConstraintBuilder(confRuleTranslator);

        SXFMParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = new SXFMParser<>(featureBuilder, relationshipBuilder, constraintBuilder);
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
        List<String> expectedVariables = List.of("Smartwatch", "Connector", "Screen",
                "Camera", "Compass", "GPS", "Cellular", "Wifi", "Bluetooth", "Analog",
                "High Resolution", "E-ink");

        assertAll(() -> Assertions.assertEquals(12, kb.getNumVariables()),
                () -> Assertions.assertEquals("smartwatch.sxfm", kb.getName()),
                () -> {
                    for (int i = 0; i < expectedVariables.size(); i++) {
                        Assertions.assertEquals(expectedVariables.get(i), kb.getVariable(i).getName());
                        assertEquals(expectedVariables.get(i), kb.getBoolVar(expectedVariables.get(i)).getName());
                    }
                });
    }

    @Test
    void testSolver() {
        kb.getModelKB().getSolver().solve();

        for (Variable v : kb.getVariableList()) {
            BoolVar bv = ((BoolVariable) v).getChocoVar();
            String expectedResults = bv.getValue() == 1 ? "true" : "false";

            assertEquals(expectedResults.equals("true"), kb.getBoolValue(bv.getName(), expectedResults));
        }
        kb.getModelKB().getSolver().reset();
    }

    @Test
    void testConstraints() {
        List<List<String>> expectedConstraints = List.of(
                List.of("ARITHM ([Smartwatch + not(Connector) >= 1])", "ARITHM ([Connector + not(Smartwatch) >= 1])"),
                List.of("ARITHM ([Smartwatch + not(Screen) >= 1])", "ARITHM ([Screen + not(Smartwatch) >= 1])"),
                List.of("ARITHM ([Smartwatch + not(Camera) >= 1])"),
                List.of("ARITHM ([Smartwatch + not(Compass) >= 1])"),
                List.of("ARITHM ([Connector + not(GPS) >= 1])", "ARITHM ([Connector + not(Cellular) >= 1])",
                        "ARITHM ([Connector + not(Wifi) >= 1])", "ARITHM ([Connector + not(Bluetooth) >= 1])",
                        "SUM ([not(Connector) + Bluetooth + Wifi + Cellular + GPS >= 1])"),
                List.of("BV_1 = [0,1]=>SUM ([E-ink + High Resolution + Analog = 1]), !BV_1 = [0,1]=>SUM ([E-ink + High Resolution + Analog != 1])",
                        "BV_2 = [0,1]=>SUM ([E-ink + High Resolution + Analog != 1]), !BV_2 = [0,1]=>SUM ([E-ink + High Resolution + Analog = 1])",
                        "ARITHM ([Screen + not(Analog) >= 1])",
                        "ARITHM ([Screen + not(High Resolution) >= 1])",
                        "ARITHM ([Screen + not(E-ink) >= 1])",
                        "ARITHM ([not(Screen) + BV_1 >= 1])",
                        "ARITHM ([BV_1 + not(Analog) >= 1])",
                        "ARITHM ([BV_1 + not(High Resolution) >= 1])",
                        "ARITHM ([BV_1 + not(E-ink) >= 1])"),
                List.of("ARITHM ([High Resolution + not(Camera) >= 1])"),
                List.of("ARITHM ([GPS + not(Compass) >= 1])"),
                List.of("ARITHM ([not(Cellular) + not(Analog) >= 1])"),
                List.of("SUM ([not(Analog) + Wifi + Cellular >= 1])")
                );

        List<List<String>> expectedNegConstraints = List.of(
                List.of("ARITHM ([Smartwatch + Connector >= 1])", "ARITHM ([not(Smartwatch) + not(Connector) >= 1])"),
                List.of("ARITHM ([Smartwatch + Screen >= 1])", "ARITHM ([not(Smartwatch) + not(Screen) >= 1])"),
                List.of("ARITHM ([Camera = 1])", "ARITHM ([not(Smartwatch) = 1])"),
                List.of("ARITHM ([Compass = 1])", "ARITHM ([not(Smartwatch) = 1])"),
                List.of("ARITHM ([not(Connector) + not(GPS) >= 1])", "ARITHM ([not(Connector) + not(Cellular) >= 1])",
                        "ARITHM ([not(Connector) + not(Wifi) >= 1])", "ARITHM ([not(Connector) + not(Bluetooth) >= 1])",
                        "SUM ([Bluetooth + Wifi + Cellular + GPS + Connector >= 1])"),
                List.of("BV_2 = [0,1]=>SUM ([E-ink + High Resolution + Analog != 1]), !BV_2 = [0,1]=>SUM ([E-ink + High Resolution + Analog = 1])",
                        "ARITHM ([not(Screen) + BV_2 >= 1])",
                        "SUM ([E-ink + High Resolution + Analog + Screen >= 1])",
                        "SUM ([BV_2 + E-ink + High Resolution + Analog >= 1])"),
                List.of("ARITHM ([Camera = 1])", "ARITHM ([not(High Resolution) = 1])"),
                List.of("ARITHM ([Compass = 1])", "ARITHM ([not(GPS) = 1])"),
                List.of("ARITHM ([Cellular = 1])", "ARITHM ([Analog = 1])"),
                List.of("ARITHM ([Analog = 1])", "ARITHM ([not(Cellular) = 1])", "ARITHM ([not(Wifi) = 1])")
                );

        List<List<String>> expectedVariables = List.of(
                List.of("Smartwatch", "Connector"),
                List.of("Smartwatch", "Screen"),
                List.of("Smartwatch", "Camera"),
                List.of("Smartwatch", "Compass"),
                List.of("Connector", "GPS", "Cellular", "Wifi", "Bluetooth"),
                List.of("Screen", "Analog", "High Resolution", "E-ink"),
                List.of("Camera", "High Resolution"),
                List.of("Compass", "GPS"),
                List.of("Analog", "Cellular"),
                List.of("Analog", "Cellular", "Wifi")
        );

        Assertions.assertEquals(10, kb.getNumConstraints());

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
            }},
                () -> {
            for (int i = 0; i < expectedVariables.size(); i++) {
                List<String> expected = expectedVariables.get(i);
                for (int j = 0; j < expected.size(); j++) {
                    Assertions.assertEquals(expected.get(j), kb.getConstraint(i).getVariables().get(j));
                }
            }}
        );
    }
}