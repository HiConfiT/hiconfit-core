/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.test.reader;

import at.tugraz.ist.ase.hiconfit.cacdr_core.ITestCase;
import at.tugraz.ist.ase.hiconfit.cacdr_core.TestSuite;
import at.tugraz.ist.ase.hiconfit.cacdr_core.builder.ITestCaseBuildable;
import at.tugraz.ist.ase.hiconfit.cacdr_core.format.XMLTestSuiteFormat;
import at.tugraz.ist.ase.hiconfit.cacdr_core.reader.ITestSuiteReadable;
import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkState;

@Slf4j
public class XMLAssumptionAwareTestSuiteReader
        implements ITestSuiteReadable {

//    @Getter
//    private final FeatureModel<F, R, C> featureModel;
//
//    public XMLAssumptionAwareTestSuiteReader(FeatureModel<F, R, C> featureModel) {
//        this.featureModel = featureModel;
//    }

    @Override
    public TestSuite read(@NonNull InputStream is, @NonNull ITestCaseBuildable testCaseBuilder) throws IOException {
        try {
            // read the stream
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(is);
            doc.getDocumentElement().normalize();
            Element rootEle = doc.getDocumentElement();

            // if it has two tags "featureModel", "struct"
            checkState(rootEle != null, "DocumentBuilder couldn't parse the document! There are errors in the file.");

            if (!(rootEle.getTagName().equals(XMLTestSuiteFormat.TAG_ROOT) && rootEle.getElementsByTagName(XMLTestSuiteFormat.TAG_TESTCASE).getLength() > 0)) {
                throw new RuntimeException("The file does not contain test cases!");
            }

            log.trace("{}Building test suite from input stream >>>", LoggerUtils.tab());
            LoggerUtils.indent();

            List<ITestCase> testCases;

            Stream<Node> nodeStream = IntStream.range(0, rootEle.getElementsByTagName(XMLTestSuiteFormat.TAG_TESTCASE).getLength())
                    .mapToObj(rootEle.getElementsByTagName(XMLTestSuiteFormat.TAG_TESTCASE)::item);
            testCases = nodeStream.map(testCaseBuilder::buildTestCase).collect(Collectors.toCollection(LinkedList::new));

            TestSuite testSuite = TestSuite.builder()
                    .testCases(testCases)
                    .build();

            LoggerUtils.outdent();
            log.debug("{}<<< Built test suite [testsuite={}]", LoggerUtils.tab(), testSuite);

            return testSuite;
        }
        catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException("An error occurred while reading the file!", e.getCause());
        }
    }
}
