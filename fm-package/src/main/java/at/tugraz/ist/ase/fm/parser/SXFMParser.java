/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.parser;

import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.fm.core.Feature;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import at.tugraz.ist.ase.fm.core.RelationshipType;
import constraints.BooleanVariable;
import constraints.PropositionalFormula;
import fm.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * A parser for the SPLOT format
 * <p>
 * Using "fmapi" library of Generative Software Development Lab (<a href="http://gsd.uwaterloo.ca/">http://gsd.uwaterloo.ca/</a>)
 * University of Waterloo
 * Waterloo, Ontario, Canada
 * <p>
 * For further details of this library, we refer to <a href="http://52.32.1.180:8080/SPLOT/sxfm.html">http://52.32.1.180:8080/SPLOT/sxfm.html</a>
 */
@Slf4j
public class SXFMParser implements FeatureModelParser {

    /**
     * Checks whether the format of the given file is SPLOT format
     *
     * @param filePath - a {@link File}
     * @return true - if the format of the given file is SPLOT format
     *          false - otherwise
     */
    @Override
    public boolean checkFormat(@NonNull File filePath) {
        // first, check the extension of file
        checkArgument(filePath.getName().endsWith(".sxfm") || filePath.getName().endsWith(".splx"), "The file is not in SPLOT format!");
        // second, check the structure of file
        try {
            // read the file
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(filePath.toString());
            Element rootEle = doc.getDocumentElement();

            // if it has three tag "feature_model", "feature_tree" and "constraints"
            if (rootEle.getTagName().equals("feature_model") &&
                    rootEle.getElementsByTagName("feature_tree").getLength() > 0 &&
                    rootEle.getElementsByTagName("constraints").getLength() > 0) {
                return true;
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            return false; // if it raises an exception, it's not SPLOT format
        }
        return false;
    }

    /**
     * This function parses the given {@link File} into a {@link FeatureModel}.
     *
     * @param filePath - a {@link File}
     * @return a {@link FeatureModel}
     * @throws FeatureModelParserException when error occurs in parsing
     */
    @Override
    public FeatureModel parse(@NonNull File filePath) throws FeatureModelParserException {
        checkArgument(checkFormat(filePath), "The format of file is not SPLOT format or there are errors in the file!");

        log.trace("{}Parsing the feature model file [file={}] >>>", LoggerUtils.tab(), filePath.getName());
        LoggerUtils.indent();

        FeatureModel featureModel;
        try {
            fm.FeatureModel sxfm = new XMLFeatureModel(filePath.toString(), XMLFeatureModel.USE_VARIABLE_NAME_AS_ID);

            // Load the XML file and create
            sxfm.loadModel();

            // create the feature model
            featureModel = new FeatureModel();

            featureModel.setName(filePath.getName());
            // convert features
            convertFeatures(sxfm, featureModel);

            if (featureModel.getNumOfFeatures() == 0) {
                throw new FeatureModelParserException("Couldn't parse any features in the feature model file!");
            }

            // convert relationships
            convertRelationships(sxfm, featureModel);

            // convert constraints
            convertConstraints(sxfm, featureModel);
        } catch (FeatureModelException | at.tugraz.ist.ase.fm.core.FeatureModelException ex) {
            throw new FeatureModelParserException(ex.getMessage());
        }

        LoggerUtils.outdent();
        log.debug("{}<<< Parsed feature model [file={}, fm={}]", LoggerUtils.tab(), filePath.getName(), featureModel);
        return featureModel;
    }

    /**
     * Iterate nodes to take features.
     *
     * @param sxfm - a {@link fm.FeatureModel}
     */
    private void convertFeatures(fm.FeatureModel sxfm, FeatureModel fm) {
        log.trace("{}Generating features >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        Queue<FeatureTreeNode> queue = new LinkedList<>();
        queue.add(sxfm.getRoot());

        FeatureTreeNode node;
        while (!queue.isEmpty()) {
            node = queue.remove();

            if ((node instanceof RootNode)
                    || (node instanceof SolitaireFeature)
                    || (node instanceof GroupedFeature)) {
                String name = node.getName();
                String id = node.getID();

                fm.addFeature(name, id);
            }

            exploreChildren(queue, node);
        }

        LoggerUtils.outdent();
    }

    private void exploreChildren(Queue<FeatureTreeNode> queue, FeatureTreeNode node) {
        IntStream.range(0, node.getChildCount()).mapToObj(i -> (FeatureTreeNode) node.getChildAt(i))
                .forEachOrdered(queue::add);
        /*for (int i = 0; i < node.getChildCount(); i++) {
            FeatureTreeNode child = (FeatureTreeNode) node.getChildAt(i);
            queue.add(child);
        }*/
    }

    /**
     * Iterates nodes to take the relationships between features.
     *
     * @param sxfm - a {@link fm.FeatureModel}
     * @param fm - a {@link FeatureModel}
     * @throws FeatureModelParserException a ParserException
     */
    private void convertRelationships(fm.FeatureModel sxfm, FeatureModel fm) throws FeatureModelParserException {
        log.trace("{}Generating relationships >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        try {
            Queue<FeatureTreeNode> queue = new LinkedList<>();
            queue.add(sxfm.getRoot());

            FeatureTreeNode node;
            while (!queue.isEmpty()) {
                node = queue.remove();

                Feature leftSide;
                List<Feature> rightSide;
                RelationshipType type;

                if (node instanceof SolitaireFeature) {
                    if (((SolitaireFeature) node).isOptional()) { // OPTIONAL
                        type = RelationshipType.OPTIONAL;
                        leftSide = fm.getFeature(node.getID());
                        rightSide = Collections.singletonList(fm.getFeature(((FeatureTreeNode) node.getParent()).getID()));
                    } else { // MANDATORY
                        type = RelationshipType.MANDATORY;
                        leftSide = fm.getFeature(((FeatureTreeNode) node.getParent()).getID());
                        rightSide = Collections.singletonList(fm.getFeature(node.getID()));
                    }
                    fm.addRelationship(type, leftSide, rightSide);
                } else if (node instanceof FeatureGroup) {
                    leftSide = fm.getFeature(((FeatureTreeNode) node.getParent()).getID());
                    rightSide = getChildren(node);

                    checkState(rightSide.size() > 0, "OR and ALT relationships must have at least one child.");

                    if (((FeatureGroup) node).getMax() == 1) { // ALTERNATIVE
                        type = RelationshipType.ALTERNATIVE;
                    } else { // OR
                        type = RelationshipType.OR;
                    }
                    fm.addRelationship(type, leftSide, rightSide);
                }

                exploreChildren(queue, node);
            }
        } catch (at.tugraz.ist.ase.fm.core.FeatureModelException ex) {
            throw new FeatureModelParserException(ex.getMessage());
        }

        LoggerUtils.outdent();
    }

    /**
     * Converts constraints on the file into constraints in {@link FeatureModel}
     *
     * @param sxfm - a {@link fm.FeatureModel}
     * @param fm - a {@link FeatureModel}
     * @throws FeatureModelParserException a ParserException
     */
    private void convertConstraints(fm.FeatureModel sxfm, FeatureModel fm) throws FeatureModelParserException, at.tugraz.ist.ase.fm.core.FeatureModelException {
        log.trace("{}Generating constraints >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        for (PropositionalFormula formula : sxfm.getConstraints()) {
            BooleanVariable[] variables = formula.getVariables().toArray(new BooleanVariable[0]);

            if (variables.length == 2) {

                BooleanVariable leftSide = variables[0];
                BooleanVariable rightSide = variables[1];

                // take type
                RelationshipType type;
                if ((leftSide.isPositive() && !rightSide.isPositive())
                        || ((!leftSide.isPositive() && rightSide.isPositive()))) { // REQUIRES
                    type = RelationshipType.REQUIRES;
                } else if (!leftSide.isPositive() && !rightSide.isPositive()) { // EXCLUDES
                    type = RelationshipType.EXCLUDES;
                } else {
                    throw new FeatureModelParserException(formula + " is not supported constraints!");
                }

                Feature left = fm.getFeature(leftSide.getID());
                List<Feature> rightSideList = Collections.singletonList(fm.getFeature(rightSide.getID()));

                fm.addConstraint(type, left, rightSideList);
            } else {
                // take type
                RelationshipType type = RelationshipType.ThreeCNF;

                List<String> threecnf_constraints = new LinkedList<>();

                Arrays.stream(variables).forEachOrdered(variable -> {
                    if (variable.isPositive()) {
                        threecnf_constraints.add(sxfm.getNodeByID(variable.getID()).getName());
                    } else {
                        threecnf_constraints.add("~" + sxfm.getNodeByID(variable.getID()).getName());
                    }
                });

                fm.addConstraint(type, String.join(" | ", threecnf_constraints));
            }
        }
        LoggerUtils.outdent();
    }

    /**
     * Gets an array of names of child features.
     *
     * @param node - a node {@link FeatureTreeNode}
     * @return an array of names of child features.
     */
    private List<Feature> getChildren(FeatureTreeNode node) throws FeatureModelParserException {
        List<Feature> features = new LinkedList<>();
        for (int i = 0; i < node.getChildCount(); i++) {
            FeatureTreeNode child = (FeatureTreeNode)node.getChildAt(i);

            String name = child.getName();
            String id = child.getID();
            if (name.isEmpty()) {
                throw new FeatureModelParserException("The feature name could not be blank! [" + child + "]");
            }
            Feature feature = new Feature(name, id);
            features.add(feature);
        }
        return features;
    }
}

