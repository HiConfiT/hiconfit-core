/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.parser;

import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.fm.builder.IConstraintBuildable;
import at.tugraz.ist.ase.fm.builder.IFeatureBuildable;
import at.tugraz.ist.ase.fm.builder.IRelationshipBuildable;
import at.tugraz.ist.ase.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.fm.core.CTConstraint;
import at.tugraz.ist.ase.fm.core.Feature;
import at.tugraz.ist.ase.fm.core.FeatureModel;
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
import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * A parser for the FeatureIDE format
 */
@Slf4j
public class FeatureIDEParser<F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint> implements FeatureModelParser<F, R, C> {

    public static final String FILE_EXTENSION = ".xml";

    // Main tags
    public static final String TAG_ROOT = "featureModel";
    public static final String TAG_STRUCT = "struct";
    public static final String TAG_CONSTRAINT = "rule";

    // Feature tags
    public static final String TAG_AND = "and";
    public static final String TAG_OR = "or";
    public static final String TAG_ALT = "alt";
    public static final String TAG_FEATURE = "feature";

    // Constraint tags
    public static final String TAG_VAR = "var";
    public static final String TAG_NOT = "not";
    public static final String TAG_IMP = "imp";
    public static final String TAG_DISJ = "disj";
    public static final String TAG_CONJ = "conj";
    public static final String TAG_EQ = "eq";

    // Feature attributes
    public static final String ATTRIB_NAME = "name";
    public static final String ATTRIB_MANDATORY = "mandatory";
    public static final String ATTRIB_ABSTRACT = "abstract";

    // Attribute values
    public static final String VALUE_TRUE = "true";
    public static final String VALUE_FALSE = "false";

    private FeatureModel<F, R, C> fm;
    private Element rootEle;

    private IFeatureBuildable featureBuilder;
    private IRelationshipBuildable relationshipBuilder;
    private IConstraintBuildable constraintBuilder;

    public FeatureIDEParser(@NonNull IFeatureBuildable featureBuilder,
                            @NonNull IRelationshipBuildable relationshipBuilder,
                            @NonNull IConstraintBuildable constraintBuilder) {
        this.featureBuilder = featureBuilder;
        this.relationshipBuilder = relationshipBuilder;
        this.constraintBuilder = constraintBuilder;
    }

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
        checkArgument(filePath.getName().endsWith(FILE_EXTENSION), "The file is not in FeatureIDE format!");

        // second, check the structure of file
        try {
            // read the file
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(filePath.toString());
            doc.getDocumentElement().normalize();
            rootEle = doc.getDocumentElement();

            // if it has two tags "featureModel", "struct"
            checkState(rootEle != null, "DocumentBuilder couldn't parse the document! There are errors in the file.");

            if (rootEle.getTagName().equals(TAG_ROOT) &&
                    rootEle.getElementsByTagName(TAG_STRUCT).getLength() > 0) {
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
    public FeatureModel<F, R, C> parse(@NonNull File filePath) throws FeatureModelParserException {
        checkArgument(checkFormat(filePath), "The format of file is not FeatureIDE format or there are errors in the file!");

        log.trace("{}Parsing the feature model file [file={}] >>>", LoggerUtils.tab(), filePath.getName());
        LoggerUtils.indent();

        checkState(rootEle != null, "DocumentBuilder couldn't parse the document! There are errors in the file.");

        // create the feature model
        fm = new FeatureModel<>(filePath.getName(), featureBuilder, relationshipBuilder, constraintBuilder);

        convertStructNodes(rootEle);

        if (fm.getNumOfFeatures() == 0) {
            throw new FeatureModelParserException("Couldn't parse any features in the feature model file!");
        }

//        convertConstraintNodes(rootEle, fm);

        LoggerUtils.outdent();
        log.debug("{}<<< Parsed feature model [file={}, fm={}]", LoggerUtils.tab(), filePath.getName(), fm);
        return fm;
    }

    /**
     * Take the "struct" node and convert its child nodes into features
     * and relationships in the {@link FeatureModel}.
     *
     * @param rootEle - a XML root element
     */
    private void convertStructNodes(Element rootEle) {
        log.trace("{}Generating features and relationships >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        NodeList struct = rootEle.getElementsByTagName(TAG_STRUCT);

        examineAStructNode(struct.item(0));

        LoggerUtils.outdent();
    }

    /**
     * Examine an XML node to convert child nodes into features, and relationships
     * of a {@link FeatureModel}.
     *
     * @param node - a XML node
     */
    private void examineAStructNode(Node node) {
        NodeList children = node.getChildNodes();
        Element parentElement = (Element) node;
        // create features for child nodes
        List<F> childrenFeatures = createChildFeaturesIfAbsent(node);

        // convert relationships
        if (!node.getNodeName().equals(TAG_STRUCT)) {
            // relationships
            switch (node.getNodeName()) {
                case TAG_AND:
                    for (int i = 0; i < children.getLength(); i++) {
                        Node child = children.item(i);

                        if (isCorrectNode(child)) {
                            Element childElement = (Element) child;

                            if (childElement.getAttribute(ATTRIB_MANDATORY).equals(VALUE_TRUE)) {
                                // MANDATORY
                                F leftSide = fm.getFeature(parentElement.getAttribute(ATTRIB_NAME));
                                F rightSide = fm.getFeature(childElement.getAttribute(ATTRIB_NAME));

                                fm.addMandatoryRelationship(leftSide, rightSide);
                            } else {
                                // OPTIONAL
                                F leftSide = fm.getFeature(childElement.getAttribute(ATTRIB_NAME));
                                F rightSide = fm.getFeature(parentElement.getAttribute(ATTRIB_NAME));

                                fm.addOptionalRelationship(rightSide, leftSide);
                            }
                        }
                    }

                    break;
                case TAG_OR:
                    checkState(childrenFeatures.size() > 0, "OR node must have at least one child feature!");

                    F leftSide = fm.getFeature(parentElement.getAttribute(ATTRIB_NAME));
                    List<F> rightSide = childrenFeatures;

                    fm.addOrRelationship(leftSide, rightSide);
                    break;
                case TAG_ALT:
                    checkState(childrenFeatures.size() > 0, "ALT node must have at least one child feature!");

                    leftSide = fm.getFeature(parentElement.getAttribute(ATTRIB_NAME));
                    rightSide = childrenFeatures;

                    fm.addAlternativeRelationship(leftSide, rightSide);
                    break;
            }
        }

        // examine sub-nodes
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (isCorrectNode(child)) {
                examineAStructNode(child);
            }
        }
    }

    /**
     * Gets or creates the children {@link Feature}s of a given XML node.
     *
     * @param node - a XML node
     * @return a list of children {@link Feature}s a given XML node
     */
    private List<F> createChildFeaturesIfAbsent(Node node) {
        NodeList children = node.getChildNodes();
        List<F> features = new LinkedList<>();

        for (int i = 0; i < children.getLength(); i++)
        {
            Node child = children.item(i);
            if (isCorrectNode(child)) {
                Element childElement = (Element) child;
                String name = childElement.getAttribute(ATTRIB_NAME);

                F childFeature;
                if (!fm.hasRoot()) {
                    childFeature = fm.addRoot(name, name);
                } else {
                    childFeature = fm.addFeature(name, name);
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
                && (node.getNodeName().equals(TAG_AND)
                || node.getNodeName().equals(TAG_OR)
                || node.getNodeName().equals(TAG_ALT)
                || node.getNodeName().equals(TAG_FEATURE));
    }

//    /**
//     * Take "rule" nodes and convert them into constraints in {@link FeatureModel}.
//     *
//     * @param rootEle - the root element
//     * @param fm - a {@link FeatureModel}
//     */
//    private void convertConstraintNodes(Element rootEle, FeatureModel fm) throws FeatureModelParserException {
//        log.trace("{}Generating constraints >>>", LoggerUtils.tab());
//        LoggerUtils.indent();
//
//        NodeList rules = rootEle.getElementsByTagName(TAG_CONSTRAINT);
//
//        for (int i = 0; i < rules.getLength(); i++) {
//            examineARuleNode(rules.item(i), fm);
//        }
//
//        LoggerUtils.outdent();
//    }
//
//    /**
//     * Examine a "rule" node to convert into a constraint
//     *
//     * @param node - an XML node
//     * @param fm - a {@link FeatureModel}
//     * @throws FeatureModelParserException - if the node is not a "rule" node
//     */
//    private void examineARuleNode(Node node, FeatureModel fm) throws FeatureModelParserException {
//        try {
//            Node n = node.getChildNodes().item(1);
//
//            Feature left;
//            List<Feature> rightSideList;
//            RelationshipType type;
//
//            String constraintType = n.getNodeName();
//            switch (constraintType) {
//                case TAG_NOT -> {
//                    type = RelationshipType.ThreeCNF;
//
//                    fm.addConstraint(type, "~" + n.getChildNodes().item(1).getTextContent());
//                }
//                case TAG_IMP -> {
//                    left = fm.getFeature(n.getChildNodes().item(1).getTextContent());
//                    rightSideList = Collections.singletonList(fm.getFeature(n.getChildNodes().item(3).getTextContent()));
//                    type = RelationshipType.REQUIRES;
//
//                    fm.addConstraint(type, left, rightSideList);
//                }
//                case TAG_DISJ -> {
//                    NodeList n1 = n.getChildNodes();
//                    List<String> clauses = new LinkedList<>();
//
//                    disjExplore(n1, clauses); // explore the disjunction rule
//
//                    if (clauses.size() == 2) {
//                        // requires or excludes
//                        if (clauses.get(0).startsWith("~") && clauses.get(1).startsWith("~")) { // excludes
//                            left = fm.getFeature(clauses.get(0).substring(1));
//                            rightSideList = Collections.singletonList(fm.getFeature(clauses.get(1).substring(1)));
//                            type = RelationshipType.EXCLUDES;
//                        } else { // requires
//                            if (clauses.get(0).startsWith("~")) {
//                                left = fm.getFeature(clauses.get(0).substring(1));
//                                rightSideList = Collections.singletonList(fm.getFeature(clauses.get(1)));
//                            } else {// if (clauses.get(1).startsWith("~")) {
//                                left = fm.getFeature(clauses.get(1).substring(1));
//                                rightSideList = Collections.singletonList(fm.getFeature(clauses.get(0)));
//                            }
//                            type = RelationshipType.REQUIRES;
//                        }
//
//                        fm.addConstraint(type, left, rightSideList);
//                    } else {
//                        // 3CNF
//                        type = RelationshipType.ThreeCNF;
//                        fm.addConstraint(type, String.join(" | ", clauses));
//                    }
//                }
//                default -> throw new FeatureModelParserException("Unexpected constraint type: " + constraintType);
//            }
//        } catch (FeatureModelException e) {
//            throw new FeatureModelParserException(e.getMessage());
//        }
//    }
//
//    private void disjExplore(NodeList nodeList, List<String> clauses) {
//        for (int i = 0; i < nodeList.getLength(); i++) {
//            Node n = nodeList.item(i);
//
//            switch (n.getNodeName()) {
//                case TAG_DISJ -> disjExplore(n.getChildNodes(), clauses);
//                case TAG_VAR -> clauses.add(n.getTextContent());
//                case TAG_NOT -> clauses.add("~" + n.getChildNodes().item(1).getTextContent());
//            }
//        }
//    }

    public void dispose() {
        fm = null;
        rootEle = null;
        featureBuilder = null;
        relationshipBuilder = null;
    }
}
