/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.kb.fm;

import at.tugraz.ist.ase.fm.core.FeatureModel;
import at.tugraz.ist.ase.fm.parser.FMFormat;
import at.tugraz.ist.ase.fm.parser.FeatureModelParser;
import at.tugraz.ist.ase.fm.parser.FeatureModelParserException;
import at.tugraz.ist.ase.fm.parser.factory.FMParserFactory;
import at.tugraz.ist.ase.kb.core.BoolVariable;
import at.tugraz.ist.ase.kb.core.Variable;
import org.chocosolver.solver.variables.BoolVar;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FMKBTest3 {
    static FMKB kb;
    static FeatureModel featureModel;

    @BeforeAll
    static void setUp() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/survey.fm4conf");
        FMParserFactory factory = FMParserFactory.getInstance();
        FeatureModelParser parser = factory.getParser(FMFormat.DESCRIPTIVE);
        featureModel = parser.parse(fileFM);

        kb = new FMKB(featureModel, false);

        kb.getConstraintList().forEach(c -> {
            System.out.println(c);
            c.getChocoConstraints().forEach(System.out::println);
            System.out.println("not " + c);
            c.getNegChocoConstraints().forEach(System.out::println);
        });
    }

    @Test
    void testVariables() {
        List<String> expectedVariables = List.of("survey", "pay", "ABtesting",
                "statistics", "qa", "license", "nonlicense", "multiplechoice", "singlechoice");

        assertAll(() -> assertEquals(9, kb.getNumVariables()),
                () -> assertEquals("survey.fm4conf", kb.getName()),
                () -> {
                    for (int i = 0; i < expectedVariables.size(); i++) {
                        assertEquals(expectedVariables.get(i), kb.getVariable(i).getName());
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
                List.of("ARITHM ([survey + not(pay) >= 1])", "ARITHM ([pay + not(survey) >= 1])"),
                List.of("ARITHM ([survey + not(ABtesting) >= 1])", "ARITHM ([ABtesting + not(survey) >= 1])"),
                List.of("ARITHM ([survey + not(statistics) >= 1])"),
                List.of("ARITHM ([survey + not(qa) >= 1])", "ARITHM ([qa + not(survey) >= 1])"),
                List.of("BV_1 = [0,1]=>ARITHM ([license = [0,1] + nonlicense = [0,1] = 1]), !BV_1 = [0,1]=>ARITHM ([PropNotEqualXY_C(license, nonlicense)])",
//                        "BV_2 = [0,1]=>ARITHM ([PropNotEqualXY_C(license, nonlicense)]), !BV_2 = [0,1]=>ARITHM ([license = [0,1] + nonlicense = [0,1] = 1])",
                        "ARITHM ([pay + not(license) >= 1])",
                        "ARITHM ([pay + not(nonlicense) >= 1])",
                        "ARITHM ([not(pay) + BV_1 >= 1])",
                        "ARITHM ([BV_1 + not(license) >= 1])",
                        "ARITHM ([BV_1 + not(nonlicense) >= 1])"),
                List.of("ARITHM ([qa + not(multiplechoice) >= 1])", "ARITHM ([qa + not(singlechoice) >= 1])",
                        "SUM ([not(qa) + singlechoice + multiplechoice >= 1])"),
                List.of("ARITHM ([statistics + not(ABtesting) >= 1])"),
                List.of("ARITHM ([not(ABtesting) + not(nonlicense) >= 1])")
                );

        assertEquals(8, kb.getNumConstraints());

        assertAll(() -> {
            for (int i = 0; i < expectedConstraints.size(); i++) {
                List<String> expected = expectedConstraints.get(i);
                for (int j = 0; j < expected.size(); j++) {
                    assertEquals(expected.get(j), kb.getConstraint(i).getChocoConstraints().get(j).toString());
                }
            }},
                () -> {
                for (int j = 0; j < kb.getConstraintList().size(); j++) {
                    assertEquals(0, kb.getConstraint(j).getNegChocoConstraints().size());
                }
            }
        );
    }
}