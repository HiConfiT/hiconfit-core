/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
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
import at.tugraz.ist.ase.fm.core.ast.ASTNode;
import at.tugraz.ist.ase.fm.core.ast.ImpliesOperator;
import at.tugraz.ist.ase.fm.core.ast.OrOperator;
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
import java.util.Queue;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * A parser for the FeatureIDE format
 * <p>
 * Supports arbitrary constraints
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

        convertConstraintNodes(rootEle);

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

        Queue<Node> queue = new LinkedList<>();
        queue.add(struct.item(0));

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            queue.addAll(examineAStructNode(node));
        }

        LoggerUtils.outdent();
    }

    /**
     * Examine an XML node to convert child nodes into features, and relationships
     * of a {@link FeatureModel}.
     *
     * @param node - a XML node
     */
    private List<Node> examineAStructNode(Node node) {
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

                            F leftSide = fm.getFeature(parentElement.getAttribute(ATTRIB_NAME));
                            F rightSide = fm.getFeature(childElement.getAttribute(ATTRIB_NAME));

                            if (childElement.getAttribute(ATTRIB_MANDATORY).equals(VALUE_TRUE)) {
                                // MANDATORY
                                fm.addMandatoryRelationship(leftSide, rightSide);
                            } else {
                                // OPTIONAL
                                fm.addOptionalRelationship(leftSide, rightSide);
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
        List<Node> subNodes = new LinkedList<>();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (isCorrectNode(child)) {
                subNodes.add(child);
            }
        }
        return subNodes;
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
     * Check whether a {@link Node} is an Element node
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

    /**
     * Take "rule" nodes and convert them into constraints in {@link FeatureModel}.
     *
     * @param rootEle - the root element
     */
    private void convertConstraintNodes(Element rootEle) throws FeatureModelParserException {
        log.trace("{}Generating constraints >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        NodeList rules = rootEle.getElementsByTagName(TAG_CONSTRAINT);

        for (int i = 0; i < rules.getLength(); i++) {
            ASTNode formula = examineARuleNode(rules.item(i).getChildNodes().item(1));
            if (formula.getFeatures().size() == 2
                    && (formula instanceof OrOperator) || (formula instanceof ImpliesOperator)) {
                formula = constraintBuilder.convertToRequiresOrExcludes(formula);
            }
            fm.addConstraint(constraintBuilder.buildConstraint(formula));
        }

        LoggerUtils.outdent();
    }

    /**
     * Examine a "rule" node to convert into a constraint
     *
     * @param node - an XML node
     * @throws FeatureModelParserException - if the node is not a "rule" node
     */
    private ASTNode examineARuleNode(Node node) throws FeatureModelParserException {
        String constraintType = node.getNodeName();
        ASTNode ast;
        switch (constraintType) {
            case TAG_VAR -> ast = constraintBuilder.buildOperand(fm.getFeature(node.getTextContent()));
            case TAG_NOT -> {
                ASTNode right = examineARuleNode(node.getChildNodes().item(1));
                ast = constraintBuilder.buildNot(right);
            }
            case TAG_IMP -> {
                ASTNode left = examineARuleNode(node.getChildNodes().item(1));
                ASTNode right = examineARuleNode(node.getChildNodes().item(3));

                ast = constraintBuilder.buildImplies(left, right);
            }
            case TAG_EQ -> {
                ASTNode left = examineARuleNode(node.getChildNodes().item(1));
                ASTNode right = examineARuleNode(node.getChildNodes().item(3));
                ast = constraintBuilder.buildEquivalence(left, right);
            }
            case TAG_DISJ -> {
                NodeList n1 = node.getChildNodes();

                if (n1.getLength() == 1) {
                    ast = examineARuleNode(n1.item(1));
                } else {
                    ASTNode left = examineARuleNode(n1.item(1));
                    ASTNode right = examineARuleNode(n1.item(3));

                    ast = constraintBuilder.buildOr(left, right);
                }
            }
            case TAG_CONJ -> {
                NodeList n1 = node.getChildNodes();

                if (n1.getLength() == 1) {
                    ast = examineARuleNode(n1.item(1));
                } else {
                    ASTNode left = examineARuleNode(n1.item(1));
                    ASTNode right = examineARuleNode(n1.item(3));
                    ast = constraintBuilder.buildAnd(left, right);
                }
            }
            default -> throw new FeatureModelParserException("Unexpected constraint type: " + constraintType);
        }
        return ast;
    }

    public void dispose() {
        fm = null;
        rootEle = null;
        featureBuilder = null;
        relationshipBuilder = null;
        constraintBuilder = null;
    }
}
