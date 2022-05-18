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
 * A parser for the FeatureIDE format
 */
@Beta
@Slf4j
public class FeatureIDEParser implements FeatureModelParser {

    /**
     * Check whether the format of the given file is FeatureIDE format
     *
     * @param filePath - a {@link File}
     * @return true - if the format of the given file is FeatureIDE format
     *          false - otherwise
     */
    @Override
    public boolean checkFormat(@NonNull File filePath) {
        // first, check the extension of file
        checkArgument(filePath.getName().endsWith(".xml"), "The file is not in FeatureIDE format!");

        // second, check the structure of file
        try {
            // read the file
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(filePath.toString());
            Element rootEle = doc.getDocumentElement();

            // if it has three tag "featureModel", "struct" and "constraints"
            if (rootEle.getTagName().equals("featureModel") &&
                    rootEle.getElementsByTagName("struct").getLength() > 0 &&
                    rootEle.getElementsByTagName("constraints").getLength() > 0) {
                return true;
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            return false; // if it occurs an exception, it's not FeatureIDE format
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
        checkArgument(checkFormat(filePath), "The format of file is not FeatureIDE format or there are errors in the file!");

        log.trace("{}Parsing the feature model file [file={}] >>>", LoggerUtils.tab(), filePath.getName());
        LoggerUtils.indent();

        FeatureModel featureModel;
        try {
            // read XML file
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(filePath.toString());
            doc.getDocumentElement().normalize();
            Element rootEle = doc.getDocumentElement();

            checkState(rootEle != null, "DocumentBuilder couldn't parse the document! There are errors in the file.");

            // create the feature model
            featureModel = new FeatureModel();
            featureModel.setName(filePath.getName());

            convertStructNodes(rootEle, featureModel);

            if (featureModel.getNumOfFeatures() == 0) {
                throw new FeatureModelParserException("Couldn't parse any features in the feature model file!");
            }

            convertConstraintNodes(rootEle, featureModel);
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            throw new FeatureModelParserException(ex.getMessage());
        }

        LoggerUtils.outdent();
        log.debug("{}<<< Parsed feature model [file={}, fm={}]", LoggerUtils.tab(), filePath.getName(), featureModel);
        return featureModel;
    }

    /**
     * Take the "struct" node and convert its child nodes into features
     * and relationships in the {@link FeatureModel}.
     *
     * @param rootEle - a XML root element
     * @param fm - a {@link FeatureModel}
     */
    private void convertStructNodes(Element rootEle, FeatureModel fm) throws FeatureModelParserException {
        log.trace("{}Generating features and relationships >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        NodeList struct = rootEle.getElementsByTagName("struct");

        examineAStructNode(struct.item(0), fm);

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
    private void examineAStructNode(Node node, FeatureModel fm) throws FeatureModelParserException {
        try {
            NodeList children = node.getChildNodes();
            Element parentElement = (Element) node;
            // create features for child nodes
            List<Feature> childrenFeatures = createChildFeaturesIfAbsent(node, fm);

            // convert relationships
            if (!node.getNodeName().equals("struct")) {
                // relationships
                Feature leftSide;
                List<Feature> rightSide;
                RelationshipType type;

                switch (node.getNodeName()) {
                    case "and":
                        for (int i = 0; i < children.getLength(); i++) {
                            Node child = children.item(i);

                            if (isCorrectNode(child)) {
                                Element childElement = (Element) child;

                                if (childElement.getAttribute("mandatory").equals("true")) {
                                    // MANDATORY
                                    leftSide = fm.getFeature(parentElement.getAttribute("name"));
                                    rightSide = Collections.singletonList(fm.getFeature(childElement.getAttribute("name")));
                                    type = RelationshipType.MANDATORY;
                                } else {
                                    // OPTIONAL
                                    leftSide = fm.getFeature(childElement.getAttribute("name"));
                                    rightSide = Collections.singletonList(fm.getFeature(parentElement.getAttribute("name")));
                                    type = RelationshipType.OPTIONAL;
                                }

                                fm.addRelationship(type, leftSide, rightSide);
                            }
                        }

                        break;
                    case "or":
                        checkState(childrenFeatures.size() > 0, "OR node must have at least one child feature!");

                        leftSide = fm.getFeature(parentElement.getAttribute("name"));
                        rightSide = childrenFeatures;
                        type = RelationshipType.OR;

                        fm.addRelationship(type, leftSide, rightSide);
                        break;
                    case "alt":
                        checkState(childrenFeatures.size() > 0, "ALT node must have at least one child feature!");

                        leftSide = fm.getFeature(parentElement.getAttribute("name"));
                        rightSide = childrenFeatures;
                        type = RelationshipType.ALTERNATIVE;

                        fm.addRelationship(type, leftSide, rightSide);
                        break;
                }
            }

            // examine sub-nodes
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (isCorrectNode(child)) {
                    examineAStructNode(child, fm);
                }
            }
        } catch (Exception e) {
            throw new FeatureModelParserException(e.getMessage());
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

                Feature childFeature;
                try {
                    // first, try to get the feature with id=name
                    childFeature = fm.getFeature(name);
                } catch (FeatureModelException e) {

                    // create new feature
                    fm.addFeature(name, name);

                    try {
                        // try to get again
                        childFeature = fm.getFeature(name);
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
                && (node.getNodeName().equals("and")
                || node.getNodeName().equals("or")
                || node.getNodeName().equals("alt")
                || node.getNodeName().equals("feature"));
    }

    /**
     * Take "rule" nodes and convert them into constraints in {@link FeatureModel}.
     *
     * @param rootEle - the root element
     * @param fm - a {@link FeatureModel}
     */
    private void convertConstraintNodes(Element rootEle, FeatureModel fm) throws FeatureModelParserException {
        log.trace("{}Generating constraints >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        NodeList rules = rootEle.getElementsByTagName("rule");

        for (int i = 0; i < rules.getLength(); i++) {
            examineARuleNode(rules.item(i), fm);
        }

        LoggerUtils.outdent();
    }

    /**
     * Examine a "rule" node to convert into a constraint
     *
     * @param node - an XML node
     * @param fm - a {@link FeatureModel}
     * @throws FeatureModelParserException - if the node is not a "rule" node
     */
    private void examineARuleNode(Node node, FeatureModel fm) throws FeatureModelParserException {
        try {
            Node n = node.getChildNodes().item(1);

            Feature left;
            List<Feature> rightSideList;
            RelationshipType type;

            String constraintType = n.getNodeName();
            switch (constraintType) {
                case "not" -> {
                    type = RelationshipType.ThreeCNF;

                    fm.addConstraint(type, "~" + n.getChildNodes().item(1).getTextContent());
                }
                case "imp" -> {
                    left = fm.getFeature(n.getChildNodes().item(1).getTextContent());
                    rightSideList = Collections.singletonList(fm.getFeature(n.getChildNodes().item(3).getTextContent()));
                    type = RelationshipType.REQUIRES;

                    fm.addConstraint(type, left, rightSideList);
                }
                case "disj" -> {
                    NodeList n1 = n.getChildNodes();
                    List<String> clauses = new LinkedList<>();

                    disjExplore(n1, clauses); // explore the disjunction rule

                    if (clauses.size() == 2) {
                        // requires or excludes
                        if (clauses.get(0).startsWith("~") && clauses.get(1).startsWith("~")) { // excludes
                            left = fm.getFeature(clauses.get(0).substring(1));
                            rightSideList = Collections.singletonList(fm.getFeature(clauses.get(1).substring(1)));
                            type = RelationshipType.EXCLUDES;
                        } else { // requires
                            if (clauses.get(0).startsWith("~")) {
                                left = fm.getFeature(clauses.get(0).substring(1));
                                rightSideList = Collections.singletonList(fm.getFeature(clauses.get(1)));
                            } else {// if (clauses.get(1).startsWith("~")) {
                                left = fm.getFeature(clauses.get(1).substring(1));
                                rightSideList = Collections.singletonList(fm.getFeature(clauses.get(0)));
                            }
                            type = RelationshipType.REQUIRES;
                        }

                        fm.addConstraint(type, left, rightSideList);
                    } else {
                        // 3CNF
                        type = RelationshipType.ThreeCNF;
                        fm.addConstraint(type, String.join(" | ", clauses));
                    }
                }
                default -> throw new FeatureModelParserException("Unexpected constraint type: " + constraintType);
            }
        } catch (FeatureModelException e) {
            throw new FeatureModelParserException(e.getMessage());
        }
    }

    private void disjExplore(NodeList nodeList, List<String> clauses) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node n = nodeList.item(i);

            switch (n.getNodeName()) {
                case "disj" -> disjExplore(n.getChildNodes(), clauses);
                case "var" -> clauses.add(n.getTextContent());
                case "not" -> clauses.add("~" + n.getChildNodes().item(1).getTextContent());
            }
        }
    }
}
