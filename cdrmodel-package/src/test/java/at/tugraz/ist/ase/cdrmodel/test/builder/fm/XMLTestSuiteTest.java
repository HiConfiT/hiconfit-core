/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cdrmodel.test.builder.fm;

import at.tugraz.ist.ase.cdrmodel.test.TestSuite;
import at.tugraz.ist.ase.cdrmodel.test.reader.TestSuiteReader;
import at.tugraz.ist.ase.cdrmodel.test.reader.XMLTestSuiteReader;
import lombok.Cleanup;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static at.tugraz.ist.ase.common.IOUtils.getInputStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class XMLTestSuiteTest {
    private static TestSuite testSuite;

    @BeforeAll
    static void setUp() throws IOException {
        XMLTestSuiteReader factory = new XMLTestSuiteReader();
        XMLTestCaseBuilder testCaseFactory = new XMLTestCaseBuilder();
        @Cleanup InputStream is = getInputStream(TestSuiteTest.class.getClassLoader(), "testcases.xml");

        testSuite = factory.read(is, testCaseFactory);
    }

    @Test
    void testSize() {
        assertEquals(3, testSuite.size());
    }

    @Test
    public void testToString() {
        String expected = """
                root
                root & something
                root & ~anything
                """;

        System.out.println(testSuite.toString());
        assertEquals(expected, testSuite.toString());
    }
}
