/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr_core.builder.fm;

import at.tugraz.ist.ase.hiconfit.cacdr_core.TestSuite;
import at.tugraz.ist.ase.hiconfit.cacdr_core.reader.XMLTestSuiteReader;
import at.tugraz.ist.ase.hiconfit.cacdr_core.writer.XMLTestSuiteWriter;
import at.tugraz.ist.ase.hiconfit.common.IOUtils;
import lombok.Cleanup;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class XMLTestSuiteTest {
    private static TestSuite testSuite;

    @BeforeAll
    static void setUp() throws IOException {
        XMLTestSuiteReader factory = new XMLTestSuiteReader();
        XMLTestCaseBuilder testCaseFactory = new XMLTestCaseBuilder();
        @Cleanup InputStream is = IOUtils.getInputStream(TestSuiteTest.class.getClassLoader(), "testcases.xml");

        testSuite = factory.read(is, testCaseFactory);
    }

    @Test
    public void testSize() {
        assertEquals(3, testSuite.size());
    }

    @Test
    public void testToString() {
        String expected = """
                root
                root & something
                root & ~anything""";

        System.out.println(testSuite.toString());
        assertEquals(expected, testSuite.toString());
    }

    @Test
    public void testWrite() throws IOException, ParserConfigurationException, TransformerException {
        String fileName = "written_testcases.xml";
        String filePath = "src/test/resources/" + fileName;

        XMLTestSuiteWriter writer = new XMLTestSuiteWriter();
        writer.write(testSuite.getTestCases(), filePath);

        XMLTestSuiteReader factory = new XMLTestSuiteReader();
        XMLTestCaseBuilder testCaseFactory = new XMLTestCaseBuilder();
        @Cleanup InputStream is = IOUtils.getInputStream(TestSuiteTest.class.getClassLoader(), fileName);

        TestSuite reReadTestSuite = factory.read(is, testCaseFactory);
        assertEquals(testSuite, reReadTestSuite);
    }
}
