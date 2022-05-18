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
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * A parser for the Glencoe format
 */
@Beta
@Slf4j
public class GLENCOEParser implements FeatureModelParser {

    /**
     * Check whether the format of the given file is Glencoe format
     *
     * @param filePath - a {@link File}
     * @return true - if the format of the given file is Glencoe format
     *          false - otherwise
     */
    @Override
    public boolean checkFormat(@NonNull File filePath) {
        // first, check the extension of file
        checkArgument(filePath.getName().endsWith(".json"), "The file is not in Glencoe format!");

        // second, check the structure of file
        try {
            // read the file
            InputStream is = new FileInputStream(filePath);

            JSONTokener tokener = new JSONTokener(is);
            JSONObject object = new JSONObject(tokener);

            // if it has three object "features", "tree" and "constraints"
            JSONObject features = object.getJSONObject("features");
            JSONObject tree = object.getJSONObject("tree");
            JSONObject constraints = object.getJSONObject("constraints");

            if (features != null && tree != null && constraints != null) {
                return true; // it is Glencoe format
            }
        } catch (Exception e) {
            return false; // if it occurs an exception, it's not Glencoe format
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
        checkArgument(checkFormat(filePath), "The format of file is not Glencoe format or there are errors in the file!");

        log.trace("{}Parsing the feature model file [file={}] >>>", LoggerUtils.tab(), filePath.getName());
        LoggerUtils.indent();

        FeatureModel featureModel;
        try {
            InputStream is = new FileInputStream(filePath);

            JSONTokener tokener = new JSONTokener(is);
            JSONObject object = new JSONObject(tokener);

            JSONObject features = object.getJSONObject("features");
            JSONObject tree = object.getJSONObject("tree");
            JSONObject constraints = object.getJSONObject("constraints");

            // create the feature model
            featureModel = new FeatureModel();
            featureModel.setName(filePath.getName());

            convertTree(tree, features, featureModel);

            if (featureModel.getNumOfFeatures() == 0) {
                throw new FeatureModelParserException("Couldn't parse any features in the feature model file!");
            }

            convertConstraints(constraints, features, featureModel);
        } catch (IOException | NullPointerException ex) {
            throw new FeatureModelParserException(ex.getMessage());
        }

        LoggerUtils.outdent();
        log.debug("{}<<< Parsed feature model [file={}, fm={}]", LoggerUtils.tab(), filePath.getName(), featureModel);
        return featureModel;
    }

    /**
     * Iterate objects in the {@link JSONObject} of the key "tree" to
     * take the feature names and relationships between features.
     *
     * @param tree - a {@link JSONObject} of the key "tree"
     * @param features - a {@link JSONObject} of the key "features"
     * @param fm - a {@link FeatureModel}
     * @throws FeatureModelParserException when error occurs in parsing
     */
    private void convertTree(JSONObject tree, JSONObject features, FeatureModel fm) throws FeatureModelParserException {
        log.trace("{}Generating features and relationships >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        try {
            String rootId = tree.getString("id");
            JSONObject rootFeature = getFeatureObject(rootId, features);

            checkState(rootFeature != null, "Couldn't find the root feature!");

            String rootName = rootFeature.getString("name");
            fm.addFeature(rootName, rootId);

            examineANode(tree, features, fm);
        } catch (Exception e) {
            throw new FeatureModelParserException(e.getMessage());
        }

        LoggerUtils.outdent();
    }

    /**
     * Examine a node to convert child nodes into features,
     * and relationships of a {@link FeatureModel}.
     *
     * @param node - a {@link JSONObject}
     * @param features - a {@link JSONObject} of the key "features"
     * @param fm - a {@link FeatureModel}
     * @throws FeatureModelParserException when error occurs in parsing
     */
    private void examineANode(JSONObject node, JSONObject features, FeatureModel fm) throws FeatureModelParserException {
        try {
            String parentID = node.getString("id");
            JSONObject parentFeature = getFeatureObject(parentID, features);

            if (node.has("children")) {
                // takes children nodes
                JSONArray children = node.getJSONArray("children");
                // creates child features
                List<Feature> childrenFeatures = createChildFeaturesIfAbsent(node,features, fm);

                // convert relationships
                if (parentFeature.has("type")) {
                    Feature leftSide;
                    List<Feature> rightSide;
                    RelationshipType type;

                    String relationshipType = parentFeature.getString("type");
                    switch (relationshipType) {
                        case "FEATURE":
                            for (Feature childFeature : childrenFeatures) {
                                JSONObject childFeatureObject = getFeatureObject(childFeature.getId(), features);

                                // takes optional
                                if (childFeatureObject.has("optional")) {
                                    if (!childFeatureObject.getBoolean("optional")) {
                                        // MANDATORY
                                        leftSide = fm.getFeature(parentID);
                                        rightSide = Collections.singletonList(childFeature);
                                        type = RelationshipType.MANDATORY;
                                    } else { // OPTIONAL
                                        leftSide = childFeature;
                                        rightSide = Collections.singletonList(fm.getFeature(parentID));
                                        type = RelationshipType.OPTIONAL;
                                    }

                                    fm.addRelationship(type, leftSide, rightSide);
                                }
                            }
                            break;
                        case "XOR":
                            checkState(childrenFeatures.size() > 0, "ALT relationship must have at least one child.");

                            leftSide = fm.getFeature(parentID);
                            rightSide = childrenFeatures;
                            type = RelationshipType.ALTERNATIVE;

                            fm.addRelationship(type, leftSide, rightSide);
                            break;
                        case "OR":
                            checkState(childrenFeatures.size() > 0, "OR relationship must have at least one child.");

                            leftSide = fm.getFeature(parentID);
                            rightSide = childrenFeatures;
                            type = RelationshipType.OR;

                            fm.addRelationship(type, leftSide, rightSide);
                            break;
                        default:
                            throw new FeatureModelParserException("Unexpected relationship type: " + relationshipType);
                    }
                }

                // examine sub-nodes
                for (int i = 0; i < children.length(); i++) {
                    JSONObject child = (JSONObject) children.get(i);
                    examineANode(child, features, fm);
                }
            }
        } catch (Exception e) {
            throw new FeatureModelParserException(e.getMessage());
        }
    }

    /**
     * Iterate objects in a {@link JSONObject} of the key "constraints" to
     * take constraints for a {@link FeatureModel}.
     *
     * @param constraints - a {@link JSONObject} of the key "constraints"
     * @param features - a {@link JSONObject} of the key "features"
     * @param fm - a {@link FeatureModel}
     */
    private void convertConstraints(JSONObject constraints, JSONObject features, FeatureModel fm) throws FeatureModelParserException {
        log.trace("{}Generating constraints >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        for (Iterator<String> it = constraints.keys(); it.hasNext(); ) {
            String key = it.next();

            examineAConstraintNode(constraints.getJSONObject(key), fm);
        }

        LoggerUtils.outdent();
    }

    /**
     * Examine a constraint that belongs to the value of the key "constraints"
     * to convert it into a constraint in the {@link FeatureModel}.
     *
     * @param constraint - a constraint of the key "constraints"
     * @param fm - a {@link FeatureModel}
     * @throws FeatureModelParserException - if there exists errors in the
     */
    private void examineAConstraintNode(JSONObject constraint, FeatureModel fm) throws FeatureModelParserException {
        try {
            if (constraint.has("type")) {
                JSONArray operands = constraint.getJSONArray("operands");

                String leftFeatureID = (((JSONObject) operands.get(0)).getJSONArray("operands")).get(0).toString();
                String rightFeatureID = (((JSONObject) operands.get(1)).getJSONArray("operands")).get(0).toString();

                Feature left = fm.getFeature(leftFeatureID);
                List<Feature> rightSideList = Collections.singletonList(fm.getFeature(rightFeatureID));

                RelationshipType type;
                String constraintType = constraint.getString("type");
                if (constraintType.equals("ExcludesTerm")) {
                    type = RelationshipType.EXCLUDES;
                } else if (constraintType.equals("ImpliesTerm")) {
                    type = RelationshipType.REQUIRES;
                } else {
                    throw new FeatureModelParserException("Unexpected constraint type: " + constraintType);
                }

                fm.addConstraint(type, left, rightSideList);
            }
        } catch (Exception e) {
            throw new FeatureModelParserException(e.getMessage());
        }
    }

    /**
     * Find a feature JSON Object based on its id.
     *
     * @param id - an id
     * @param features - features in the form of {@link JSONObject}
     * @return a {@link JSONObject} of the found feature
     * @throws FeatureModelParserException - when could not find the feature
     */
    private JSONObject getFeatureObject(String id, JSONObject features) throws FeatureModelParserException {
        for (Iterator<String> it = features.keys(); it.hasNext(); ) {
            String key = it.next();
            if (key.equals(id)) {
                return features.getJSONObject(key);
            }
        }
        throw new FeatureModelParserException("Couldn't find the JSONObject with [id=" + id + "]");
    }

    /**
     * Gets or creates a list of child {@link Feature}s of a {@link JSONObject} node on the
     * basic of {@link JSONObject} objects of the key "features".
     *
     * @param node - a {@link JSONObject}
     * @param featuresObject - a {@link JSONObject} of the key "features"
     * @return a list of {@link Feature}s
     * @throws FeatureModelParserException - when could not find a child feature
     */
    private List<Feature> createChildFeaturesIfAbsent(JSONObject node, JSONObject featuresObject, FeatureModel fm) throws FeatureModelParserException {
        List<Feature> features = new LinkedList<>();
        JSONArray children = node.getJSONArray("children");
        for (int i = 0; i < children.length(); i++) {
            JSONObject child = (JSONObject) children.get(i);
            String id = child.getString("id");

            Feature childFeature;
            try {
                // first, try to get the feature from the feature model
                childFeature = fm.getFeature(id);
            } catch (FeatureModelException e) {
                JSONObject childFeatureObject = getFeatureObject(id, featuresObject);
                String name = childFeatureObject.getString("name");

                // create a new feature
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
        return features;
    }
}
