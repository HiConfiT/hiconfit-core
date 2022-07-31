/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cdrmodel.test.builder.fm;

import at.tugraz.ist.ase.cdrmodel.test.TestCase;
import lombok.Cleanup;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static at.tugraz.ist.ase.common.IOUtils.getInputStream;
import static org.junit.jupiter.api.Assertions.*;

class TestCaseTest {
    static List<TestCase> testsuite;

    @BeforeAll
    static void setup() throws IOException {
        testsuite = new LinkedList<>();
        FMTestCaseBuilder builder = new FMTestCaseBuilder();

        @Cleanup InputStream is = getInputStream(TestCaseTest.class.getClassLoader(), "FM_10_0_c5_0.testcases");

        @Cleanup BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        br.readLine(); // read the first line and ignore it

        // Read all test cases
        String line;
        while ((line = br.readLine()) != null) {
            TestCase testCase = builder.buildTestCase(line);
            testsuite.add(testCase);
        }
    }

    @Test
    public void testTestCase () {
        assertAll(() -> assertEquals(5, testsuite.size()),
                () -> assertEquals("~gui_builder & uml & sdi & ~mdi", testsuite.get(0).getTestcase()),
                () -> assertEquals("~gui_builder & diagram_builder & ~uml", testsuite.get(1).getTestcase()),
                () -> assertEquals("~interface & ~gui_builder & diagram_builder & ~uml & sdi", testsuite.get(2).getTestcase()),
                () -> assertEquals("~interface & ~diagram_builder & uml & mdi", testsuite.get(3).getTestcase()),
                () -> assertEquals("~mdi & interface", testsuite.get(4).getTestcase()),
                () -> assertEquals("gui_builder", testsuite.get(0).getAssignments().get(0).getVariable()),
                () -> assertEquals("false", testsuite.get(0).getAssignments().get(0).getValue()),
                () -> assertEquals("uml", testsuite.get(0).getAssignments().get(1).getVariable()),
                () -> assertEquals("true", testsuite.get(0).getAssignments().get(1).getValue()),
                () -> assertEquals("sdi", testsuite.get(0).getAssignments().get(2).getVariable()),
                () -> assertEquals("true", testsuite.get(0).getAssignments().get(2).getValue()),
                () -> assertEquals("mdi", testsuite.get(0).getAssignments().get(3).getVariable()),
                () -> assertEquals("false", testsuite.get(0).getAssignments().get(3).getValue()),
                () -> assertEquals("~gui_builder & diagram_builder & ~uml", testsuite.get(1).toString()));

        assertNotNull(testsuite.get(0).getChocoConstraints());
        assertNotNull(testsuite.get(0).getNegChocoConstraints());
    }

    @Test
    public void shouldCloneable() throws CloneNotSupportedException {
        TestCase tc = testsuite.get(0);

        assertDoesNotThrow(tc::clone);

        TestCase tc2 = (TestCase) tc.clone();

        assertAll(() -> assertEquals(tc.getTestcase(), tc2.getTestcase(), "Testcase"),
                () -> assertNotSame(tc.getAssignments(), tc2.getAssignments(), "Assignments"),
                () -> assertNotSame(tc.getAssignments().get(0), tc2.getAssignments().get(0), "Assignments"),
                () -> assertNotSame(tc.getAssignments().get(1), tc2.getAssignments().get(1), "Assignments"),
                () -> assertNotSame(tc.getAssignments().get(2), tc2.getAssignments().get(2), "Assignments"),
                () -> assertNotSame(tc.getAssignments().get(3), tc2.getAssignments().get(3), "Assignments"),
                () -> assertEquals(tc.getAssignments().size(), tc2.getAssignments().size(), "Assignment size"),
                () -> assertEquals(tc.getAssignments().get(0), tc2.getAssignments().get(0), "Assignments"),
                () -> assertEquals(tc.getAssignments().get(1), tc2.getAssignments().get(1), "Assignments"),
                () -> assertEquals(tc.getAssignments().get(2), tc2.getAssignments().get(2), "Assignments"),
                () -> assertEquals(tc.getAssignments().get(3), tc2.getAssignments().get(3), "Assignments"),
                () -> assertEquals(tc.isViolated(), tc2.isViolated(), "isViolated"));

        tc2.setTestcase("new testcase");
        assertNotEquals(tc.getTestcase(), tc2.getTestcase());

        tc2.setAssignments(Collections.emptyList());

        assertAll(() -> assertEquals(4, tc.getAssignments().size()),
                () -> assertEquals(0, tc2.getAssignments().size()));
    }
}