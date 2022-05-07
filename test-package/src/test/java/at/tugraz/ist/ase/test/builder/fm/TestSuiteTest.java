/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.test.builder.fm;

import at.tugraz.ist.ase.test.TestSuite;
import at.tugraz.ist.ase.test.builder.TestSuiteBuilder;
import lombok.Cleanup;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static at.tugraz.ist.ase.common.IOUtils.getInputStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestSuiteTest {
    private static TestSuite testSuite;

    @BeforeAll
    static void setUp() throws IOException {
        TestSuiteBuilder factory = new TestSuiteBuilder();
        FMTestCaseBuilder testCaseFactory = new FMTestCaseBuilder();
        @Cleanup InputStream is = getInputStream(TestSuiteTest.class.getClassLoader(), "FM_10_0_c5_0.testcases");

        testSuite = factory.buildTestSuite(is, testCaseFactory);
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
}