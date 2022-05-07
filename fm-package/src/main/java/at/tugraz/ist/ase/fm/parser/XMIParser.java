/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.parser;

import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.fm.core.Feature;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import at.tugraz.ist.ase.fm.core.FeatureModelException;
import at.tugraz.ist.ase.fm.core.RelationshipType;
import com.google.common.annotations.Beta;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * A parser for the XMI format (a format of v.control)
 */
@Beta
@Slf4j
public class XMIParser implements FeatureModelParser {

    private Element rootEle = null;

    /**
     * Check whether the format of the given file is v.control format
     *
     * @param filePath - a {@link File}
     * @return true - if the format of the given file is v.control format
     *          false - otherwise
     */
    @Override
    public boolean checkFormat(@NonNull File filePath) {
        // first, check the extension of file
        checkArgument(filePath.getName().endsWith(".xmi"), "The file is not in XMI format!");

        // second, check the structure of file
        try {
            // read the file
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(filePath.toString());
            Element rootEle = doc.getDocumentElement();


            // if it has three tag "xmi:XMI", "models" and "constraints"
            if (rootEle.getTagName().equals("xmi:XMI") &&
                    rootEle.getElementsByTagName("models").getLength() > 0 &&
                    rootEle.getElementsByTagName("constraints").getLength() > 0) {
                return true;
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            return false; // if it occurs an exception, it's not v.control format
        }
        return false;
    }

    /**
     * This function parse the given {@link File} into a {@link FeatureModel}.
     *
     * @param filePath - a {@link File}
     * @return a {@link FeatureModel}
     * @throws FeatureModelParserException when error occurs in parsing
     */
    @Override
    public FeatureModel parse(@NonNull File filePath) throws FeatureModelParserException {
        checkArgument(checkFormat(filePath), "The format of file is not XMI format or there are errors in the file!");

        log.trace("{}Parsing the feature model file [file={}] >>>", LoggerUtils.tab, filePath.getName());
        LoggerUtils.indent();

        FeatureModel featureModel;
        try {
            // read XMI file
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(filePath.toString());
            doc.getDocumentElement().normalize();
            rootEle = doc.getDocumentElement();

            checkState(rootEle != null, "DocumentBuilder couldn't parse the document! There are errors in the file.");

            // create the feature model
            featureModel = new FeatureModel();
            featureModel.setName(filePath.getName());

            convertModelsNode(rootEle, featureModel);

            if (featureModel.getNumOfFeatures() == 0) {
                throw new FeatureModelParserException("Couldn't parse any features in the feature model file!");
            }

            convertConstraintsNodes(rootEle, featureModel);

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            throw new FeatureModelParserException(ex.getMessage());
        }

        LoggerUtils.outdent();
        log.debug("{}<<< Parsed feature model [file={}, fm={}]", LoggerUtils.tab, filePath.getName(), featureModel);
        return featureModel;
    }

    /**
     * Take the "models" node and convert its child nodes into features
     * and relationships in the {@link FeatureModel}.
     *
     * @param rootEle - a XML root element
     * @param fm - a {@link FeatureModel}
     */
    private void convertModelsNode(Element rootEle, FeatureModel fm) throws FeatureModelParserException {
        log.trace("{}Generating features and relationships >>>", LoggerUtils.tab);
        LoggerUtils.indent();

        NodeList models = rootEle.getElementsByTagName("models");

        examineModelsNode(models.item(0), fm);

        LoggerUtils.outdent();
    }

    /**
     * Examine a XML node to convert child nodes into features, and relationships
     * of a {@link FeatureModel}.
     *
     * @param node - a XML node
     * @param fm - a {@link FeatureModel}
     * @throws FeatureModelParserException when error occurs in parsing
     */
    private void examineModelsNode(Node node, FeatureModel fm) throws FeatureModelParserException {
        try {
            NodeList children = node.getChildNodes();
            Element parentElement = (Element) node;
            // create features for child nodes
            List<Feature> childrenFeatures = createChildFeaturesIfAbsent(node, fm);

            // convert relationships
            if (!node.getNodeName().equals("models")) {
                // relationships
                Feature leftSide;
                List<Feature> rightSide;
                RelationshipType type;

                switch (parentElement.getAttribute("xsi:type")) {
                    case "com.prostep.vcontrol.model.feature:Feature":
                        for (int i = 0; i < children.getLength(); i++) {
                            Node child = children.item(i);

                            if (isCorrectNode(child)) {
                                Element childElement = (Element) child;

                                if (childElement.getAttribute("optional").equals("false")) {
                                    // MANDATORY
                                    leftSide = fm.getFeature(parentElement.getAttribute("id"));
                                    rightSide = Collections.singletonList(fm.getFeature(childElement.getAttribute("id")));
                                    type = RelationshipType.MANDATORY;
                                } else {
                                    // OPTIONAL
                                    leftSide = fm.getFeature(childElement.getAttribute("id"));
                                    rightSide = Collections.singletonList(fm.getFeature(parentElement.getAttribute("id")));
                                    type = RelationshipType.OPTIONAL;
                                }
                                fm.addRelationship(type, leftSide, rightSide);
                            }
                        }
                        break;
                    case "com.prostep.vcontrol.model.feature:FeatureGroup":
                        checkState(childrenFeatures.size() > 0, "OR and ALT relationships must have at least one child feature");

                        leftSide = fm.getFeature(parentElement.getAttribute("id"));
                        rightSide = childrenFeatures;

                        if (parentElement.getAttribute("max").isEmpty()) { // ALTERNATIVE
                            type = RelationshipType.ALTERNATIVE;
                        } else { // OR
                            type = RelationshipType.OR;
                        }

                        fm.addRelationship(type, leftSide, rightSide);
                        break;
                    default:
                        throw new FeatureModelParserException("Unexpected relationship type: " + parentElement.getAttribute("xsi:type"));
                }
            }

            // examine sub-nodes
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (isCorrectNode(child)) {
                    examineModelsNode(child, fm);
                }
            }
        } catch (Exception e) {
            throw new FeatureModelParserException("There exists errors in the feature model file!");
        }
    }

    /**
     * Gets or creates the children {@link Feature}s of a given XML node.
     *
     * @param node - a XML node
     * @return a list of children {@link Feature}s a given XML node
     */
    private List<Feature> createChildFeaturesIfAbsent(Node node, FeatureModel fm) throws FeatureModelParserException {
        NodeList children = node.getChildNodes();
        List<Feature> features = new LinkedList<>();

        for (int i = 0; i < children.getLength(); i++)
        {
            Node child = children.item(i);
            if (isCorrectNode(child)) {
                Element childElement = (Element) child;
                String name = childElement.getAttribute("name");
                String id = childElement.getAttribute("id");

                Feature childFeature;
                try {
                    // first, try to get the feature with id=name
                    childFeature = fm.getFeature(id);
                } catch (FeatureModelException e) {

                    // create new feature
                    fm.addFeature(name, id);

                    try {
                        // try to get again
                        childFeature = fm.getFeature(id);
                    } catch (FeatureModelException ex) {
                        throw new FeatureModelParserException(e.getMessage());
                    }
                }

                features.add(childFeature);
            }
        }

        return features;
    }

    /**
     * Check whether a {@link Node} is a Element node
     * and the node name is "and" or "or" or "alt" or "feature"
     *
     * @param node - a {@link Node}
     * @return true if it's correct, false otherwise
     */
    private boolean isCorrectNode(Node node) {
        return node.getNodeType() == Node.ELEMENT_NODE
                && (node.getNodeName().equals("rootFeature")
                || node.getNodeName().equals("children"));
    }

    /**
     * Take "constraints" nodes and convert them into constraints in {@link FeatureModel}.
     *
     * @param rootEle - the root element
     * @param fm - a {@link FeatureModel}
     * @throws FeatureModelParserException - if there exists errors in the feature model file
     */
    private void convertConstraintsNodes(Element rootEle, FeatureModel fm) throws FeatureModelParserException {
        log.trace("{}Generating constraints >>>", LoggerUtils.tab);
        LoggerUtils.indent();

        NodeList constraints = rootEle.getElementsByTagName("constraints");

        for (int i = 0; i < constraints.getLength(); i++) {
            examineAConstraintsNode(constraints.item(i), fm);
        }

        LoggerUtils.outdent();
    }

    /**
     * Examine a "rule" node to convert into a constraint
     *
     * @param node - an XML node
     * @param fm - a {@link FeatureModel}
     * @throws FeatureModelParserException - if there exists errors in the feature model file
     */
    private void examineAConstraintsNode(Node node, FeatureModel fm) throws FeatureModelParserException {
        try {
            Node n = node.getChildNodes().item(1);
            Element ele = (Element) n;

            Element leftOperand = (Element) (n.getChildNodes().item(1));
            Element rightOperand = (Element) (n.getChildNodes().item(3));

            Feature left = fm.getFeature(leftOperand.getAttribute("element"));
            List<Feature> rightSideList = Collections.singletonList(fm.getFeature(rightOperand.getAttribute("element")));

            RelationshipType type;
            String constraintType = ele.getAttribute("xsi:type");
            if (constraintType.equals("com.prostep.vcontrol.model.terms:ImpliesTerm")) {
                type = RelationshipType.REQUIRES;
            } else if (constraintType.equals("com.prostep.vcontrol.model.terms:ExcludesTerm")) {
                type = RelationshipType.EXCLUDES;
            } else {
                throw new FeatureModelParserException("Unexpected constraint type: " + constraintType);
            }

            fm.addConstraint(type, left, rightSideList);
        } catch (Exception e) {
            throw new FeatureModelParserException(e.getMessage());
        }
    }
}