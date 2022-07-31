/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cdrmodel.fm;

import at.tugraz.ist.ase.cdrmodel.test.TestSuite;
import at.tugraz.ist.ase.cdrmodel.test.builder.fm.FMTestCaseBuilder;
import at.tugraz.ist.ase.cdrmodel.test.reader.TestSuiteReader;
import at.tugraz.ist.ase.cdrmodel.test.translator.fm.FMTestCaseTranslator;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import at.tugraz.ist.ase.fm.parser.FMFormat;
import at.tugraz.ist.ase.fm.parser.FeatureModelParser;
import at.tugraz.ist.ase.fm.parser.FeatureModelParserException;
import at.tugraz.ist.ase.fm.parser.factory.FMParserFactory;
import com.google.common.collect.Iterators;
import com.google.common.io.Files;
import lombok.Cleanup;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static at.tugraz.ist.ase.common.IOUtils.getInputStream;
import static org.junit.jupiter.api.Assertions.*;

class FMDebuggingModelTest {

    static FMDebuggingModel model;
    static TestSuite testSuite;

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

    @BeforeAll
    static void init() throws FeatureModelParserException, IOException {
        File fileFM = new File("src/test/resources/survey.fm4conf");

        FMFormat fmFormat = FMFormat.getFMFormat(Files.getFileExtension(fileFM.getName()));
        FeatureModelParser parser = FMParserFactory.getInstance().getParser(fmFormat);
        FeatureModel fm = parser.parse(fileFM);

        TestSuiteReader builder = new TestSuiteReader();
        FMTestCaseBuilder testCaseFactory = new FMTestCaseBuilder();
        @Cleanup InputStream is = getInputStream(FMDebuggingModelTest.class.getClassLoader(), "survey.testcases");

        testSuite = builder.read(is, testCaseFactory);

        FMTestCaseTranslator translator = new FMTestCaseTranslator();
        model = new FMDebuggingModel(fm, testSuite, translator, true, false);
        model.initialize();
    }

    @Test
    void testFMDebuggingModel() {
        assertAll(() -> {
                    for (int i = 0; i < expectedConstraints.size(); i++) {
                        List<String> expected = expectedConstraints.get(i);
                        for (int j = 0; j < expected.size(); j++) {
                            assertEquals(expected.get(j), Iterators.get(model.getPossiblyFaultyConstraints().iterator(), i).getChocoConstraints().get(j).toString());
                        }
                    }},
                () -> {
                    for (int j = 0; j < model.getPossiblyFaultyConstraints().size(); j++) {
                        assertEquals(0, Iterators.get(model.getPossiblyFaultyConstraints().iterator(), j).getNegChocoConstraints().size());
                    }
                },
                () -> assertEquals(model.getTestcases().size(), testSuite.size()),
                () -> assertEquals(model.getTestCase("nonlicense").getChocoConstraints().size(), 1),
                () -> assertEquals("ARITHM ([nonlicense = 1])", model.getTestCase("nonlicense").getChocoConstraints().get(0).toString()),
                () -> assertEquals(model.getTestCase("nonlicense").getNegChocoConstraints().size(), 1),
                () -> assertEquals("ARITHM ([not(nonlicense) = 1])", model.getTestCase("nonlicense").getNegChocoConstraints().get(0).toString()),
                () -> assertEquals(model.getTestCase("license & ~statistics").getChocoConstraints().size(), 2),
                () -> assertEquals("ARITHM ([license = 1])", model.getTestCase("license & ~statistics").getChocoConstraints().get(0).toString()),
                () -> assertEquals("ARITHM ([not(statistics) = 1])", model.getTestCase("license & ~statistics").getChocoConstraints().get(1).toString()),
                () -> assertEquals(model.getTestCase("license & ~statistics").getNegChocoConstraints().size(), 1),
                () -> assertEquals("ARITHM ([statistics + not(license) >= 1])", model.getTestCase("license & ~statistics").getNegChocoConstraints().get(0).toString()),
                () -> assertEquals(model.getTestCase("pay").getChocoConstraints().size(), 1),
                () -> assertEquals("ARITHM ([pay = 1])", model.getTestCase("pay").getChocoConstraints().get(0).toString()),
                () -> assertEquals(model.getTestCase("pay").getNegChocoConstraints().size(), 1),
                () -> assertEquals("ARITHM ([not(pay) = 1])", model.getTestCase("pay").getNegChocoConstraints().get(0).toString()),
                () -> assertEquals(model.getTestCase("~singlechoice").getChocoConstraints().size(), 1),
                () -> assertEquals("ARITHM ([not(singlechoice) = 1])", model.getTestCase("~singlechoice").getChocoConstraints().get(0).toString()),
                () -> assertEquals(model.getTestCase("~singlechoice").getNegChocoConstraints().size(), 1),
                () -> assertEquals("ARITHM ([singlechoice = 1])", model.getTestCase("~singlechoice").getNegChocoConstraints().get(0).toString()),
                () -> assertEquals(model.getTestCase("multiplechoice & ~singlechoice").getChocoConstraints().size(), 2),
                () -> assertEquals("ARITHM ([multiplechoice = 1])", model.getTestCase("multiplechoice & ~singlechoice").getChocoConstraints().get(0).toString()),
                () -> assertEquals("ARITHM ([not(singlechoice) = 1])", model.getTestCase("multiplechoice & ~singlechoice").getChocoConstraints().get(1).toString()),
                () -> assertEquals(model.getTestCase("multiplechoice & ~singlechoice").getNegChocoConstraints().size(), 1),
                () -> assertEquals("ARITHM ([singlechoice + not(multiplechoice) >= 1])", model.getTestCase("multiplechoice & ~singlechoice").getNegChocoConstraints().get(0).toString()));
    }

    @Test
    void shouldCloneable() throws CloneNotSupportedException {
        FMDebuggingModel clone = (FMDebuggingModel) model.clone();
        clone.initialize();

        assertAll(() -> assertNotSame(model, clone),
                () -> assertNotSame(model.getModel(), clone.getModel()),
                () -> {
                    for (int i = 0; i < model.getTestcases().size(); i++) {
                        assertNotSame(Iterators.get(model.getTestcases().iterator(), i), Iterators.get(clone.getTestcases().iterator(), i));
                    }
                },
                () -> {
                    for (int i = 0; i < model.getTestCase("nonlicense").getChocoConstraints().size(); i++) {
                        assertNotSame(model.getTestCase("nonlicense").getChocoConstraints().get(i), clone.getTestCase("nonlicense").getChocoConstraints().get(i));
                    }
                });

        assertAll(() -> {
                    for (int i = 0; i < expectedConstraints.size(); i++) {
                        List<String> expected = expectedConstraints.get(i);
                        for (int j = 0; j < expected.size(); j++) {
                            assertEquals(expected.get(j), Iterators.get(clone.getPossiblyFaultyConstraints().iterator(), i).getChocoConstraints().get(j).toString());
                        }
                    }},
                () -> {
                    for (int j = 0; j < clone.getPossiblyFaultyConstraints().size(); j++) {
                        assertEquals(0, Iterators.get(clone.getPossiblyFaultyConstraints().iterator(), j).getNegChocoConstraints().size());
                    }
                },
                () -> assertEquals(clone.getTestcases().size(), testSuite.size()),
                () -> assertEquals(clone.getTestCase("nonlicense").getChocoConstraints().size(), 1),
                () -> assertEquals("ARITHM ([nonlicense = 1])", clone.getTestCase("nonlicense").getChocoConstraints().get(0).toString()),
                () -> assertEquals(clone.getTestCase("nonlicense").getNegChocoConstraints().size(), 1),
                () -> assertEquals("ARITHM ([not(nonlicense) = 1])", clone.getTestCase("nonlicense").getNegChocoConstraints().get(0).toString()),
                () -> assertEquals(clone.getTestCase("license & ~statistics").getChocoConstraints().size(), 2),
                () -> assertEquals("ARITHM ([license = 1])", clone.getTestCase("license & ~statistics").getChocoConstraints().get(0).toString()),
                () -> assertEquals("ARITHM ([not(statistics) = 1])", clone.getTestCase("license & ~statistics").getChocoConstraints().get(1).toString()),
                () -> assertEquals(clone.getTestCase("license & ~statistics").getNegChocoConstraints().size(), 1),
                () -> assertEquals("ARITHM ([statistics + not(license) >= 1])", clone.getTestCase("license & ~statistics").getNegChocoConstraints().get(0).toString()),
                () -> assertEquals(clone.getTestCase("pay").getChocoConstraints().size(), 1),
                () -> assertEquals("ARITHM ([pay = 1])", clone.getTestCase("pay").getChocoConstraints().get(0).toString()),
                () -> assertEquals(clone.getTestCase("pay").getNegChocoConstraints().size(), 1),
                () -> assertEquals("ARITHM ([not(pay) = 1])", clone.getTestCase("pay").getNegChocoConstraints().get(0).toString()),
                () -> assertEquals(clone.getTestCase("~singlechoice").getChocoConstraints().size(), 1),
                () -> assertEquals("ARITHM ([not(singlechoice) = 1])", clone.getTestCase("~singlechoice").getChocoConstraints().get(0).toString()),
                () -> assertEquals(clone.getTestCase("~singlechoice").getNegChocoConstraints().size(), 1),
                () -> assertEquals("ARITHM ([singlechoice = 1])", clone.getTestCase("~singlechoice").getNegChocoConstraints().get(0).toString()),
                () -> assertEquals(clone.getTestCase("multiplechoice & ~singlechoice").getChocoConstraints().size(), 2),
                () -> assertEquals("ARITHM ([multiplechoice = 1])", clone.getTestCase("multiplechoice & ~singlechoice").getChocoConstraints().get(0).toString()),
                () -> assertEquals("ARITHM ([not(singlechoice) = 1])", clone.getTestCase("multiplechoice & ~singlechoice").getChocoConstraints().get(1).toString()),
                () -> assertEquals(clone.getTestCase("multiplechoice & ~singlechoice").getNegChocoConstraints().size(), 1),
                () -> assertEquals("ARITHM ([singlechoice + not(multiplechoice) >= 1])", clone.getTestCase("multiplechoice & ~singlechoice").getNegChocoConstraints().get(0).toString()));
    }
}