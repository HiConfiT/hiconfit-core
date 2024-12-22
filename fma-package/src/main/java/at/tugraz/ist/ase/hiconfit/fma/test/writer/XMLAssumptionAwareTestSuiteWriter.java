/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.test.writer;

import at.tugraz.ist.ase.hiconfit.cacdr_core.ITestCase;
import at.tugraz.ist.ase.hiconfit.cacdr_core.format.XMLTestSuiteFormat;
import at.tugraz.ist.ase.hiconfit.cacdr_core.writer.XMLTestSuiteWriter;
import at.tugraz.ist.ase.hiconfit.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.hiconfit.fma.test.AssumptionAwareTestCase;
import at.tugraz.ist.ase.hiconfit.fma.test.format.XMLAssumptionAwareTestSuiteFormat;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.util.List;

/**
 * XMLAssumptionAwareTestSuiteWriter
 * @author: Tamim Burgstaller
 * @param <F>
 */
@Slf4j
public class XMLAssumptionAwareTestSuiteWriter<F extends AnomalyAwareFeature> extends XMLTestSuiteWriter {
    @Override
    public void write(@NonNull List<ITestCase> testCases, @NonNull String path) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        Element rootEle = doc.createElement(XMLTestSuiteFormat.TAG_ROOT);
        doc.appendChild(rootEle);

        for (ITestCase testCase : testCases) {
            Element testCaseEle = doc.createElement(XMLTestSuiteFormat.TAG_TESTCASE);
            AssumptionAwareTestCase<F> assumptionAwareTestCase = (AssumptionAwareTestCase<F>) testCase;
            testCaseEle.setAttribute(XMLAssumptionAwareTestSuiteFormat.ATT_ANOMALY, assumptionAwareTestCase.getAnomalyType().toString());

            for (F anomalyAwareFeature : assumptionAwareTestCase.getAssumptions()) {
                Element assumptionEle = doc.createElement(XMLAssumptionAwareTestSuiteFormat.TAG_ASSUMPTION);
                assumptionEle.setAttribute(XMLAssumptionAwareTestSuiteFormat.ATT_NAME, anomalyAwareFeature.getName());
                assumptionEle.setAttribute(XMLAssumptionAwareTestSuiteFormat.ATT_ID, anomalyAwareFeature.getId());

                testCaseEle.appendChild(assumptionEle);
            }

//            for (Assignment assignment : testCase.getAssignments()) {
//                Element clauseEle = doc.createElement(XMLTestSuiteFormat.TAG_CLAUSE);
//                clauseEle.setAttribute(XMLTestSuiteFormat.TAG_VARIABLE,  assignment.getVariable());
//                clauseEle.setAttribute(XMLTestSuiteFormat.TAG_VALUE, assignment.getValue());
//
//                testCaseEle.appendChild(clauseEle);
//            }
            addChild(testCase, doc, testCaseEle);

            rootEle.appendChild(testCaseEle);
        }

//        TransformerFactory transformerFactory = TransformerFactory.newInstance();
//        transformerFactory.setAttribute("indent-number", 4);
//        Transformer transformer = transformerFactory.newTransformer();
//        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//        DOMSource domSource = new DOMSource(doc);
//        StreamResult streamResult = new StreamResult(new File(path));
//
//        transformer.transform(domSource, streamResult);
        transformToFile(path, doc);
    }
}
