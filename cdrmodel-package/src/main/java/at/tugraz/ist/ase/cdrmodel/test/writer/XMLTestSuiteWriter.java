/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cdrmodel.test.writer;

import at.tugraz.ist.ase.cdrmodel.test.ITestCase;
import at.tugraz.ist.ase.cdrmodel.test.reader.XMLTestSuiteReader;
import at.tugraz.ist.ase.kb.core.Assignment;
import lombok.NonNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;
import java.util.Properties;

public class XMLTestSuiteWriter implements ITestSuiteWritable {
    @Override
    public void write(@NonNull List<ITestCase> testCases, @NonNull String path) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        Element rootEle = doc.createElement(XMLTestSuiteReader.TAG_ROOT);
        doc.appendChild(rootEle);

        for (ITestCase testCase : testCases) {
            Element testCaseEle = doc.createElement(XMLTestSuiteReader.TAG_TESTCASE);

            for (Assignment assignment : testCase.getAssignments()) {
                Element clauseEle = doc.createElement(XMLTestSuiteReader.TAG_CLAUSE);
                clauseEle.setAttribute(XMLTestSuiteReader.TAG_VARIABLE,  assignment.getVariable());
                clauseEle.setAttribute(XMLTestSuiteReader.TAG_VALUE, assignment.getValue());

                testCaseEle.appendChild(clauseEle);
            }

            rootEle.appendChild(testCaseEle);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", 4);
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource domSource = new DOMSource(doc);
        StreamResult streamResult = new StreamResult(new File(path));

        transformer.transform(domSource, streamResult);
    }
}
