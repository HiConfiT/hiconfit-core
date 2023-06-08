/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cdrmodel.test.builder.fm;

import at.tugraz.ist.ase.hiconfit.cdrmodel.test.TestSuite;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test.reader.TestSuiteReader;
import at.tugraz.ist.ase.hiconfit.common.IOUtils;
import lombok.Cleanup;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class TestSuiteTest {
    private static TestSuite testSuite;

    @BeforeAll
    static void setUp() throws IOException {
        TestSuiteReader factory = new TestSuiteReader();
        FMTestCaseBuilder testCaseFactory = new FMTestCaseBuilder();
        @Cleanup InputStream is = IOUtils.getInputStream(TestSuiteTest.class.getClassLoader(), "FM_10_0_c5_0.testcases");

        testSuite = factory.read(is, testCaseFactory);
    }

    @Test
    void testSize() {
        assertEquals(5, testSuite.size());
    }

    @Test
    public void testToString() {
        String expected = """
                ~gui_builder & uml & sdi & ~mdi
                ~gui_builder & diagram_builder & ~uml
                ~interface & ~gui_builder & diagram_builder & ~uml & sdi
                ~interface & ~diagram_builder & uml & mdi
                ~mdi & interface""";

        assertEquals(expected, testSuite.toString());
    }

    @Test
    void shouldCloneable() throws CloneNotSupportedException {
        TestSuite clone = (TestSuite) testSuite.clone();
        assertEquals(testSuite.toString(), clone.toString());
        assertEquals(testSuite.size(), clone.size());

        for (int i = 0; i < testSuite.size(); i++) {
            assertNotSame(clone.getTestCases().get(i), testSuite.getTestCases().get(i));
            assertEquals(clone.getTestCases().get(i).toString(), testSuite.getTestCases().get(i).toString());

            assertNotSame(clone.getTestCases().get(i).getAssignments(), testSuite.getTestCases().get(i).getAssignments());
            assertEquals(clone.getTestCases().get(i).getAssignments().size(), testSuite.getTestCases().get(i).getAssignments().size());

            for (int j = 0; j < clone.getTestCases().get(i).getAssignments().size(); j++) {
                assertNotSame(clone.getTestCases().get(i).getAssignments().get(j), testSuite.getTestCases().get(i).getAssignments().get(j));
                assertEquals(clone.getTestCases().get(i).getAssignments().get(j).toString(), testSuite.getTestCases().get(i).getAssignments().get(j).toString());
            }
        }
    }
}