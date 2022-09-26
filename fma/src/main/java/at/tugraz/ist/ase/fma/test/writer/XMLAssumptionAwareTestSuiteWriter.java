/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.test.writer;

import at.tugraz.ist.ase.cdrmodel.test.ITestCase;
import at.tugraz.ist.ase.cdrmodel.test.reader.XMLTestSuiteReader;
import at.tugraz.ist.ase.cdrmodel.test.writer.ITestSuiteWritable;
import at.tugraz.ist.ase.fm.core.Feature;
import at.tugraz.ist.ase.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.fma.test.AssumptionAwareTestCase;
import at.tugraz.ist.ase.fma.test.reader.XMLAssumptionAwareTestSuiteReader;
import at.tugraz.ist.ase.kb.core.Assignment;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

@Slf4j
public class XMLAssumptionAwareTestSuiteWriter implements ITestSuiteWritable {
    @Override
    public void write(@NonNull List<ITestCase> testCases, @NonNull String path) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        Element rootEle = doc.createElement(XMLTestSuiteReader.TAG_ROOT);
        doc.appendChild(rootEle);

        for (ITestCase testCase : testCases) {
            Element testCaseEle = doc.createElement(XMLTestSuiteReader.TAG_TESTCASE);
            AssumptionAwareTestCase assumptionAwareTestCase = (AssumptionAwareTestCase) testCase;

            for (AnomalyAwareFeature anomalyAwareFeature : assumptionAwareTestCase.getAssumptions()) {
                Element assumptionEle = doc.createElement(XMLAssumptionAwareTestSuiteReader.TAG_ASSUMPTION);
                assumptionEle.setAttribute(XMLAssumptionAwareTestSuiteReader.TAG_NAME, anomalyAwareFeature.getName());
                assumptionEle.setAttribute(XMLAssumptionAwareTestSuiteReader.TAG_ID, anomalyAwareFeature.getId());

                testCaseEle.appendChild(assumptionEle);
            }

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
