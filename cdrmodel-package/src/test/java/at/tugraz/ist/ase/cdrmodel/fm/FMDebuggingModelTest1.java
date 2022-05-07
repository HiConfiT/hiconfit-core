/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cdrmodel.fm;

import at.tugraz.ist.ase.test.ITestCase;
import at.tugraz.ist.ase.test.TestSuite;
import at.tugraz.ist.ase.test.builder.TestSuiteBuilder;
import at.tugraz.ist.ase.test.builder.fm.FMTestCaseBuilder;
import at.tugraz.ist.ase.test.translator.fm.FMTestCaseTranslator;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import at.tugraz.ist.ase.fm.parser.FMFormat;
import at.tugraz.ist.ase.fm.parser.FeatureModelParser;
import at.tugraz.ist.ase.fm.parser.factory.FMParserFactory;
import at.tugraz.ist.ase.kb.core.Constraint;
import com.google.common.io.Files;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.chocosolver.solver.Model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import static at.tugraz.ist.ase.common.IOUtils.getInputStream;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FMDebuggingModelTest1 {
    static FeatureModel featureModel;
    static TestSuite testSuite;

    static FMDebuggingModel debuggingModel;

    @SneakyThrows
    @BeforeAll
    static void setUp() {
        File fileFM = new File("src/test/resources/FM_10_0.splx");
        FMFormat fmFormat = FMFormat.getFMFormat(Files.getFileExtension(fileFM.getName()));
        FeatureModelParser parser = FMParserFactory.getInstance().getParser(fmFormat);
        featureModel = parser.parse(fileFM);

        TestSuiteBuilder factory = new TestSuiteBuilder();
        FMTestCaseBuilder testCaseFactory = new FMTestCaseBuilder();
        @Cleanup InputStream is = getInputStream(FMDebuggingModelTest.class.getClassLoader(), "FM_10_0.testcases");

        testSuite = factory.buildTestSuite(is, testCaseFactory);

        debuggingModel = new FMDebuggingModel(featureModel, testSuite, new FMTestCaseTranslator(),
                true, false);
        debuggingModel.initialize();
    }

    @Test
    void testModel() {
        Model model = debuggingModel.getModel();

        assertAll(() -> assertEquals(0, model.getNbCstrs()));
//                () -> assertEquals(model.getCstrs()[0].toString(), "ARITHM ([FM_10_0 + not(F1) >= 1])"),
//                () -> assertEquals(model.getCstrs()[1].toString(), "ARITHM ([FM_10_0 + not(F2) >= 1])"),
//                () -> assertEquals(model.getCstrs()[2].toString(), "ARITHM ([F2 + not(FM_10_0) >= 1])"),
//                () -> assertEquals(model.getCstrs()[3].toString(), "ARITHM ([FM_10_0 + not(F3) >= 1])"),
//                () -> assertEquals(model.getCstrs()[4].toString(), "ARITHM ([FM_10_0 + not(F4) >= 1])"),
//                () -> assertEquals(model.getCstrs()[5].toString(), "ARITHM ([FM_10_0 + not(F5) >= 1])"),
//                () -> assertEquals(model.getCstrs()[6].toString(), "SUM ([not(FM_10_0) + F5 + F4 + F3 >= 1])"),
//                () -> assertEquals(model.getCstrs()[7].toString(), "ARITHM ([FM_10_0 + not(F6) >= 1])"),
//                () -> assertEquals(model.getCstrs()[8].toString(), "ARITHM ([FM_10_0 + not(F7) >= 1])"),
//                () -> assertEquals(model.getCstrs()[9].toString(), "ARITHM ([not(F6) + not(F7) >= 1])"),
//                () -> assertEquals(model.getCstrs()[10].toString(), "SUM ([not(FM_10_0) + F7 + F6 >= 1])"),
//                () -> assertEquals(model.getCstrs()[11].toString(), "ARITHM ([F2 + not(F8) >= 1])"),
//                () -> assertEquals(model.getCstrs()[12].toString(), "ARITHM ([F6 + not(F8) >= 1])"),
//                () -> assertEquals(model.getCstrs()[13].toString(), "ARITHM ([not(F1) + not(F4) >= 1])"),
//                () -> assertEquals(model.getCstrs()[14].toString(), "SUM ([not(F1) + F7 + F8 >= 1])"),
//                () -> assertEquals(model.getCstrs()[15].toString(), "ARITHM ([FM_10_0 = 1])"));
    }

    @Test
    void testConstraints() {
        assertAll(() -> assertEquals(1, debuggingModel.getCorrectConstraints().size()),
                () -> {
                    List<Constraint> constraints = debuggingModel.getCorrectConstraints().stream().toList();
                    assertEquals(1, constraints.get(0).getChocoConstraints().size());
                    assertEquals("ARITHM ([FM_10_0 = 1])", constraints.get(0).getChocoConstraints().get(0).toString());
                },
                () -> assertEquals(8, debuggingModel.getPossiblyFaultyConstraints().size()),
                ()-> {
                    List<Constraint> constraints = debuggingModel.getPossiblyFaultyConstraints().stream().toList();
                    assertEquals(1, constraints.get(0).getChocoConstraints().size());
                    assertEquals("ARITHM ([FM_10_0 + not(F1) >= 1])", constraints.get(0).getChocoConstraints().get(0).toString());
                    assertEquals(2, constraints.get(1).getChocoConstraints().size());
                    assertEquals("ARITHM ([FM_10_0 + not(F2) >= 1])", constraints.get(1).getChocoConstraints().get(0).toString());
                    assertEquals("ARITHM ([F2 + not(FM_10_0) >= 1])", constraints.get(1).getChocoConstraints().get(1).toString());
                    assertEquals(4, constraints.get(2).getChocoConstraints().size());
                    assertEquals("ARITHM ([FM_10_0 + not(F3) >= 1])", constraints.get(2).getChocoConstraints().get(0).toString());
                    assertEquals("ARITHM ([FM_10_0 + not(F4) >= 1])", constraints.get(2).getChocoConstraints().get(1).toString());
                    assertEquals("ARITHM ([FM_10_0 + not(F5) >= 1])", constraints.get(2).getChocoConstraints().get(2).toString());
                    assertEquals("SUM ([not(FM_10_0) + F5 + F4 + F3 >= 1])", constraints.get(2).getChocoConstraints().get(3).toString());
                    assertEquals(6, constraints.get(3).getChocoConstraints().size());
                    assertEquals("BV_1 = [0,1]=>ARITHM ([F6 = [0,1] + F7 = [0,1] = 1]), !BV_1 = [0,1]=>ARITHM ([PropNotEqualXY_C(F6, F7)])", constraints.get(3).getChocoConstraints().get(0).toString());
                    assertEquals("ARITHM ([FM_10_0 + not(F6) >= 1])", constraints.get(3).getChocoConstraints().get(1).toString());
                    assertEquals("ARITHM ([FM_10_0 + not(F7) >= 1])", constraints.get(3).getChocoConstraints().get(2).toString());
                    assertEquals("ARITHM ([not(FM_10_0) + BV_1 >= 1])", constraints.get(3).getChocoConstraints().get(3).toString());
                    assertEquals("ARITHM ([BV_1 + not(F6) >= 1])", constraints.get(3).getChocoConstraints().get(4).toString());
                    assertEquals("ARITHM ([BV_1 + not(F7) >= 1])", constraints.get(3).getChocoConstraints().get(5).toString());
                    assertEquals(1, constraints.get(4).getChocoConstraints().size());
                    assertEquals("ARITHM ([F2 + not(F8) >= 1])", constraints.get(4).getChocoConstraints().get(0).toString());
                    assertEquals(1, constraints.get(5).getChocoConstraints().size());
                    assertEquals("ARITHM ([F6 + not(F8) >= 1])", constraints.get(5).getChocoConstraints().get(0).toString());
                    assertEquals(1, constraints.get(6).getChocoConstraints().size());
                    assertEquals("ARITHM ([not(F1) + not(F4) >= 1])", constraints.get(6).getChocoConstraints().get(0).toString());
                    assertEquals(1, constraints.get(7).getChocoConstraints().size());
                    assertEquals("SUM ([not(F1) + F7 + F8 >= 1])", constraints.get(7).getChocoConstraints().get(0).toString());
                });
    }

    @Test
    void testTestCases() {
        ITestCase t1 = debuggingModel.getTestCase("FM_10_0");
        assertEquals(t1.getChocoConstraints().get(0).toString(),
                "ARITHM ([FM_10_0 = 1])");

        ITestCase t2 = debuggingModel.getTestCase("F1");
        assertEquals(t2.getChocoConstraints().get(0).toString(),
                "ARITHM ([F1 = 1])");

        ITestCase t3 = debuggingModel.getTestCase("~F6");
        assertEquals(t3.getChocoConstraints().get(0).toString(),
                "ARITHM ([not(F6) = 1])");

        ITestCase t4 = debuggingModel.getTestCase("~F1 & F6");
        assertEquals(t4.getChocoConstraints().size(), 2);
        assertEquals(t4.getChocoConstraints().get(0).toString(),
                "ARITHM ([F6 = 1])");
        assertEquals(t4.getChocoConstraints().get(1).toString(),
                "ARITHM ([not(F1) = 1])");

        ITestCase t5 = debuggingModel.getTestCase("~F1 & ~F6");
        assertEquals(t5.getChocoConstraints().size(), 2);
        assertEquals(t5.getChocoConstraints().get(0).toString(),
                "ARITHM ([not(F1) = 1])");
        assertEquals(t5.getChocoConstraints().get(1).toString(),
                "ARITHM ([not(F6) = 1])");

        ITestCase t6 = debuggingModel.getTestCase("~F1 & F2 & ~F6 & ~F3 & F4 & F5");
        assertEquals(t6.getChocoConstraints().size(), 6);
        assertEquals(t6.getChocoConstraints().get(0).toString(),
                "ARITHM ([F2 = 1])");
        assertEquals(t6.getChocoConstraints().get(1).toString(),
                "ARITHM ([F4 = 1])");
        assertEquals(t6.getChocoConstraints().get(2).toString(),
                "ARITHM ([F5 = 1])");
        assertEquals(t6.getChocoConstraints().get(3).toString(),
                "ARITHM ([not(F1) = 1])");
        assertEquals(t6.getChocoConstraints().get(4).toString(),
                "ARITHM ([not(F3) = 1])");
        assertEquals(t6.getChocoConstraints().get(5).toString(),
                "ARITHM ([not(F6) = 1])");
    }
}