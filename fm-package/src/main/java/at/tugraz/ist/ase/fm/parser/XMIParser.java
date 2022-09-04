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
import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * A parser for the XMI format (a format of v.control)
 */
@Beta
@Slf4j
public class XMIParser<F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint> implements FeatureModelParser<F, R, C> {

    public static final String FILE_EXTENSION = ".xmi";

    // Main tags
    public static final String TAG_ROOT = "xmi:XMI";
    public static final String TAG_STRUCT = "models";
    public static final String TAG_CONSTRAINT = "constraints";

    // Feature tags
    public static final String TAG_ROOT_FEATURE = "rootFeature";
    public static final String TAG_CHILDREN = "children";

    // Feature types
    public static final String TYPE_FEATURE = "com.prostep.vcontrol.model.feature:Feature";
    public static final String TYPE_FEATURE_GROUP = "com.prostep.vcontrol.model.feature:FeatureGroup";

    // Constraint types
    public static final String TYPE_IMPLIES = "com.prostep.vcontrol.model.terms:ImpliesTerm";
    public static final String TYPE_EXCLUDES = "com.prostep.vcontrol.model.terms:ExcludesTerm";

    // Feature attributes
    public static final String ATTRIB_NAME = "name";
    public static final String ATTRIB_ID = "id";
    public static final String ATTRIB_TYPE = "xsi:type";
    public static final String ATTRIB_OPTIONAL = "optional";
    public static final String ATTRIB_MAX = "max";

    // Constraint attributes
    public static final String ATTRIB_ELEMENT = "element";

    // Attribute values
    public static final String VALUE_FALSE = "false";

    private FeatureModel<F, R, C> fm;
    private Element rootEle;

    private IFeatureBuildable featureBuilder;
    private IRelationshipBuildable relationshipBuilder;
    private IConstraintBuildable constraintBuilder;

    public XMIParser(@NonNull IFeatureBuildable featureBuilder,
                     @NonNull IRelationshipBuildable relationshipBuilder,
                     @NonNull IConstraintBuildable constraintBuilder) {
        this.featureBuilder = featureBuilder;
        this.relationshipBuilder = relationshipBuilder;
        this.constraintBuilder = constraintBuilder;
    }

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
        checkArgument(filePath.getName().endsWith(FILE_EXTENSION), "The file is not in XMI format!");

        // second, check the structure of file
        try {
            // read the file
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(filePath.toString());
            rootEle = doc.getDocumentElement();

            // if it has three tag "xmi:XMI", "models" and "constraints"
            if (rootEle.getTagName().equals(TAG_ROOT) &&
                    rootEle.getElementsByTagName(TAG_STRUCT).getLength() > 0 &&
                    rootEle.getElementsByTagName(TAG_CONSTRAINT).getLength() > 0) {
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
    public FeatureModel<F, R, C> parse(@NonNull File filePath) throws FeatureModelParserException {
        checkArgument(checkFormat(filePath), "The format of file is not XMI format or there are errors in the file!");

        log.trace("{}Parsing the feature model file [file={}] >>>", LoggerUtils.tab(), filePath.getName());
        LoggerUtils.indent();

        checkState(rootEle != null, "DocumentBuilder couldn't parse the document! There are errors in the file.");

        // create the feature model
        fm = new FeatureModel<>(filePath.getName(), featureBuilder, relationshipBuilder, constraintBuilder);

        convertModelsNode();

        if (fm.getNumOfFeatures() == 0) {
            throw new FeatureModelParserException("Couldn't parse any features in the feature model file!");
        }

//            convertConstraintsNodes();

        LoggerUtils.outdent();
        log.debug("{}<<< Parsed feature model [file={}, fm={}]", LoggerUtils.tab(), filePath.getName(), fm);
        return fm;
    }

    /**
     * Take the "models" node and convert its child nodes into features
     * and relationships in the {@link FeatureModel}.
     */
    private void convertModelsNode() throws FeatureModelParserException {
        log.trace("{}Generating features and relationships >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        NodeList models = rootEle.getElementsByTagName(TAG_STRUCT);

        examineModelsNode(models.item(0));

        LoggerUtils.outdent();
    }

    /**
     * Examine an XML node to convert child nodes into features, and relationships
     * of a {@link FeatureModel}.
     *
     * @param node - a XML node
     * @throws FeatureModelParserException when error occurs in parsing
     */
    private void examineModelsNode(Node node) throws FeatureModelParserException {
        try {
            NodeList children = node.getChildNodes();
            Element parentElement = (Element) node;
            // create features for child nodes
            List<F> childrenFeatures = createChildFeaturesIfAbsent(node);

            // convert relationships
            if (!node.getNodeName().equals(TAG_STRUCT)) {
                // relationships
                switch (parentElement.getAttribute(ATTRIB_TYPE)) {
                    case TYPE_FEATURE:
                        for (int i = 0; i < children.getLength(); i++) {
                            Node child = children.item(i);

                            if (isCorrectNode(child)) {
                                Element childElement = (Element) child;

                                if (childElement.getAttribute(ATTRIB_OPTIONAL).equals(VALUE_FALSE)) {
                                    // MANDATORY
                                    F leftSide = fm.getFeature(parentElement.getAttribute(ATTRIB_ID));
                                    F rightSide = fm.getFeature(childElement.getAttribute(ATTRIB_ID));

                                    fm.addMandatoryRelationship(leftSide, rightSide);
                                } else {
                                    // OPTIONAL
                                    F leftSide = fm.getFeature(childElement.getAttribute(ATTRIB_ID));
                                    F rightSide = fm.getFeature(parentElement.getAttribute(ATTRIB_ID));

                                    fm.addOptionalRelationship(rightSide, leftSide);
                                }
                            }
                        }
                        break;
                    case TYPE_FEATURE_GROUP:
                        checkState(childrenFeatures.size() > 0, "OR and ALT relationships must have at least one child feature");

                        F parent = fm.getFeature(parentElement.getAttribute(ATTRIB_ID));

                        if (parentElement.getAttribute(ATTRIB_MAX).isEmpty()) { // ALTERNATIVE
                            fm.addAlternativeRelationship(parent, childrenFeatures);
                        } else { // OR
                            fm.addOrRelationship(parent, childrenFeatures);
                        }
                        break;
                    default:
                        throw new FeatureModelParserException("Unexpected relationship type: " + parentElement.getAttribute(ATTRIB_TYPE));
                }
            }

            // examine sub-nodes
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (isCorrectNode(child)) {
                    examineModelsNode(child);
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
    private List<F> createChildFeaturesIfAbsent(Node node) throws FeatureModelParserException {
        NodeList children = node.getChildNodes();
        List<F> features = new LinkedList<>();

        for (int i = 0; i < children.getLength(); i++)
        {
            Node child = children.item(i);
            if (isCorrectNode(child)) {
                Element childElement = (Element) child;
                String name = childElement.getAttribute(ATTRIB_NAME);
                String id = childElement.getAttribute(ATTRIB_ID);

                F childFeature;
                if (!fm.hasRoot()) {
                    childFeature = fm.addRoot(name, id);
                } else {
                    childFeature = fm.addFeature(name, id);
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
                && (node.getNodeName().equals(TAG_ROOT_FEATURE)
                || node.getNodeName().equals(TAG_CHILDREN));
    }

//    /**
//     * Take "constraints" nodes and convert them into constraints in {@link FeatureModel}.
//     *
//     * @param rootEle - the root element
//     * @throws FeatureModelParserException - if there exists errors in the feature model file
//     */
//    private void convertConstraintsNodes(Element rootEle) throws FeatureModelParserException {
//        log.trace("{}Generating constraints >>>", LoggerUtils.tab());
//        LoggerUtils.indent();
//
//        NodeList constraints = rootEle.getElementsByTagName(TAG_CONSTRAINT);
//
//        for (int i = 0; i < constraints.getLength(); i++) {
//            examineAConstraintsNode(constraints.item(i), fm);
//        }
//
//        LoggerUtils.outdent();
//    }
//
//    /**
//     * Examine a "rule" node to convert into a constraint
//     *
//     * @param node - an XML node
//     * @throws FeatureModelParserException - if there exists errors in the feature model file
//     */
//    private void examineAConstraintsNode(Node node) throws FeatureModelParserException {
//        try {
//            Node n = node.getChildNodes().item(1);
//            Element ele = (Element) n;
//
//            Element leftOperand = (Element) (n.getChildNodes().item(1));
//            Element rightOperand = (Element) (n.getChildNodes().item(3));
//
//            F left = fm.getFeature(leftOperand.getAttribute(ATTRIB_ELEMENT));
//            List<F> rightSideList = Collections.singletonList(fm.getFeature(rightOperand.getAttribute(ATTRIB_ELEMENT)));
//
//            RelationshipType type;
//            String constraintType = ele.getAttribute(ATTRIB_TYPE);
//            if (constraintType.equals(TYPE_IMPLY)) {
//                type = RelationshipType.REQUIRES;
//            } else if (constraintType.equals(TYPE_EXCLUDES)) {
//                type = RelationshipType.EXCLUDES;
//            } else {
//                throw new FeatureModelParserException("Unexpected constraint type: " + constraintType);
//            }
//
//            fm.addConstraint(type, left, rightSideList);
//        } catch (Exception e) {
//            throw new FeatureModelParserException(e.getMessage());
//        }
//    }

    public void dispose() {
        fm = null;
        rootEle = null;
        featureBuilder = null;
        relationshipBuilder = null;
    }
}