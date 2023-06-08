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
import at.tugraz.ist.ase.hiconfit.fm.parser.FMParserFactory;
import at.tugraz.ist.ase.hiconfit.fm.parser.FeatureModelParser;
import at.tugraz.ist.ase.hiconfit.fm.parser.FeatureModelParserException;
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

class FMKBTest3 {
    static FMKB<Feature, AbstractRelationship<Feature>, CTConstraint> kb;
    static FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel;

    @BeforeAll
    static void setUp() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/survey.fm4conf");

        FeatureBuilder featureBuilder = new FeatureBuilder();
        ConfRuleTranslator confRuleTranslator = new ConfRuleTranslator();
        RelationshipBuilder relationshipBuilder = new RelationshipBuilder(confRuleTranslator);
        ConstraintBuilder constraintBuilder = new ConstraintBuilder(confRuleTranslator);

        FMParserFactory<Feature, AbstractRelationship<Feature>, CTConstraint> factory = FMParserFactory.getInstance(featureBuilder, relationshipBuilder, constraintBuilder);
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = factory.getParser(fileFM.getName());
        featureModel = parser.parse(fileFM);

        kb = new FMKB<>(featureModel, false);

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

        assertAll(() -> Assertions.assertEquals(9, kb.getNumVariables()),
                () -> Assertions.assertEquals("survey.fm4conf", kb.getName()),
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

        Assertions.assertEquals(8, kb.getNumConstraints());

        assertAll(() -> {
            for (int i = 0; i < expectedConstraints.size(); i++) {
                List<String> expected = expectedConstraints.get(i);
                for (int j = 0; j < expected.size(); j++) {
                    Assertions.assertEquals(expected.get(j), kb.getConstraint(i).getChocoConstraints().get(j).toString());
                }
            }},
                () -> {
                for (int j = 0; j < kb.getConstraintList().size(); j++) {
                    Assertions.assertEquals(0, kb.getConstraint(j).getNegChocoConstraints().size());
                }
            }
        );
    }
}