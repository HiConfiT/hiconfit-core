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
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * A parser for the Glencoe format
 * <p>
 * Supports only requires and excludes constraints
 */
@Beta
@Slf4j
public class GLENCOEParser<F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint> implements FeatureModelParser<F, R, C> {

    public static final String FILE_EXTENSION = ".json";

    // Main tags
    public static final String TAG_FEATURES = "features";
    public static final String TAG_STRUCT = "tree";
    public static final String TAG_CONSTRAINT = "constraints";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_TYPE = "type";
    public static final String KEY_CHILDREN = "children";
    public static final String KEY_OPTIONAL = "optional";
    public static final String KEY_OPERANDS = "operands";

    public static final String TYPE_FEATURE = "FEATURE";
    public static final String TYPE_XOR = "XOR";
    public static final String TYPE_OR = "OR";

    public static final String TYPE_EXCLUDES = "ExcludesTerm";
    public static final String TYPE_IMPLY = "ImpliesTerm";

    private FeatureModel<F, R, C> fm;
    private JSONObject features;
    private JSONObject tree;
    private JSONObject constraints;

    private IFeatureBuildable featureBuilder;
    private IRelationshipBuildable relationshipBuilder;
    private IConstraintBuildable constraintBuilder;

    public GLENCOEParser(@NonNull IFeatureBuildable featureBuilder,
                         @NonNull IRelationshipBuildable relationshipBuilder,
                         @NonNull IConstraintBuildable constraintBuilder) {
        this.featureBuilder = featureBuilder;
        this.relationshipBuilder = relationshipBuilder;
        this.constraintBuilder = constraintBuilder;
    }

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
        checkArgument(filePath.getName().endsWith(FILE_EXTENSION), "The file is not in Glencoe format!");

        // second, check the structure of file
        try {
            // read the file
            InputStream is = new FileInputStream(filePath);

            JSONTokener tokener = new JSONTokener(is);
            JSONObject object = new JSONObject(tokener);

            // if it has three object "features", "tree" and "constraints"
            features = object.getJSONObject(TAG_FEATURES);
            tree = object.getJSONObject(TAG_STRUCT);
            constraints = object.getJSONObject(TAG_CONSTRAINT);

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
    public FeatureModel<F, R, C> parse(@NonNull File filePath) throws FeatureModelParserException {
        checkArgument(checkFormat(filePath), "The format of file is not Glencoe format or there are errors in the file!");

        log.trace("{}Parsing the feature model file [file={}] >>>", LoggerUtils.tab(), filePath.getName());
        LoggerUtils.indent();

        // create the feature model
        fm = new FeatureModel<>(filePath.getName(), featureBuilder, relationshipBuilder, constraintBuilder);

        convertTree();

        if (fm.getNumOfFeatures() == 0) {
            throw new FeatureModelParserException("Couldn't parse any features in the feature model file!");
        }

        convertConstraints(constraints);

        LoggerUtils.outdent();
        log.debug("{}<<< Parsed feature model [file={}, fm={}]", LoggerUtils.tab(), filePath.getName(), fm);
        return fm;
    }

    /**
     * Iterate objects in the {@link JSONObject} of the key "tree" to
     * take the feature names and relationships between features.
     *
     * @throws FeatureModelParserException when error occurs in parsing
     */
    private void convertTree() throws FeatureModelParserException {
        log.trace("{}Generating features and relationships >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        String rootId = tree.getString(KEY_ID);
        JSONObject rootFeature = getFeatureObject(rootId);

        checkState(rootFeature != null, "Couldn't find the root feature!");

        String rootName = rootFeature.getString(KEY_NAME);
        fm.addRoot(rootName, rootId);

        examineANode(tree);

        LoggerUtils.outdent();
    }

    /**
     * Examine a node to convert child nodes into features,
     * and relationships of a {@link FeatureModel}.
     *
     * @param node - a {@link JSONObject}
     * @throws FeatureModelParserException when error occurs in parsing
     */
    private void examineANode(JSONObject node) throws FeatureModelParserException {
        try {
            String parentID = node.getString(KEY_ID);
            JSONObject parentFeature = getFeatureObject(parentID);

            if (node.has(KEY_CHILDREN)) {
                // takes children nodes
                JSONArray childrenNodes = node.getJSONArray(KEY_CHILDREN);
                // creates child features
                List<F> childrenFeatures = createChildFeaturesIfAbsent(node);

                // convert relationships
                if (parentFeature.has(KEY_TYPE)) {
                    String relationshipType = parentFeature.getString(KEY_TYPE);
                    switch (relationshipType) {
                        case TYPE_FEATURE:
                            for (F child : childrenFeatures) {
                                JSONObject childFeatureObject = getFeatureObject(child.getId());

                                // takes optional
                                if (childFeatureObject.has(KEY_OPTIONAL)) {
                                    if (!childFeatureObject.getBoolean(KEY_OPTIONAL)) { // MANDATORY
                                        F parent = fm.getFeature(parentID);

                                        fm.addMandatoryRelationship(parent, child);
                                    } else { // OPTIONAL
                                        F parent = fm.getFeature(parentID);

                                        fm.addOptionalRelationship(parent, child);
                                    }
                                }
                            }
                            break;
                        case TYPE_XOR:
                            checkState(childrenFeatures.size() > 0, "ALT relationship must have at least one child.");

                            F parent = fm.getFeature(parentID);
                            List<F> children = childrenFeatures;

                            fm.addAlternativeRelationship(parent, children);
                            break;
                        case TYPE_OR:
                            checkState(childrenFeatures.size() > 0, "OR relationship must have at least one child.");

                            parent = fm.getFeature(parentID);
                            children = childrenFeatures;

                            fm.addOrRelationship(parent, children);
                            break;
                        default:
                            throw new FeatureModelParserException("Unexpected relationship type: " + relationshipType);
                    }
                }

                // examine sub-nodes
                for (int i = 0; i < childrenNodes.length(); i++) {
                    JSONObject child = (JSONObject) childrenNodes.get(i);
                    examineANode(child);
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
     */
    private void convertConstraints(JSONObject constraints) throws FeatureModelParserException {
        log.trace("{}Generating constraints >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        for (Iterator<String> it = constraints.keys(); it.hasNext(); ) {
            String key = it.next();

            examineAConstraintNode(constraints.getJSONObject(key));
        }

        LoggerUtils.outdent();
    }

    /**
     * Examine a constraint that belongs to the value of the key "constraints"
     * to convert it into a constraint in the {@link FeatureModel}.
     *
     * @param constraint - a constraint of the key "constraints"
     * @throws FeatureModelParserException - if there exists errors in the
     */
    private void examineAConstraintNode(JSONObject constraint) throws FeatureModelParserException {
        try {
            if (constraint.has(KEY_TYPE)) {
                JSONArray operands = constraint.getJSONArray(KEY_OPERANDS);

                String leftFeatureID = (((JSONObject) operands.get(0)).getJSONArray(KEY_OPERANDS)).get(0).toString();
                String rightFeatureID = (((JSONObject) operands.get(1)).getJSONArray(KEY_OPERANDS)).get(0).toString();

                F left = fm.getFeature(leftFeatureID);
                F right = fm.getFeature(rightFeatureID);

                String constraintType = constraint.getString(KEY_TYPE);
                if (constraintType.equals(TYPE_EXCLUDES)) {
                    fm.addConstraint(constraintBuilder.buildConstraint(constraintBuilder.buildExcludes(left, right)));
                } else if (constraintType.equals(TYPE_IMPLY)) {
                    fm.addConstraint(constraintBuilder.buildConstraint(constraintBuilder.buildRequires(left, right)));
                } else {
                    throw new FeatureModelParserException("Unexpected constraint type: " + constraintType);
                }
            }
        } catch (Exception e) {
            throw new FeatureModelParserException(e.getMessage());
        }
    }

    /**
     * Find a feature JSON Object based on its id.
     *
     * @param id - an id
     * @return a {@link JSONObject} of the found feature
     * @throws FeatureModelParserException - when could not find the feature
     */
    private JSONObject getFeatureObject(String id) throws FeatureModelParserException {
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
     * @return a list of {@link Feature}s
     * @throws FeatureModelParserException - when could not find a child feature
     */
    private List<F> createChildFeaturesIfAbsent(JSONObject node) throws FeatureModelParserException {
        List<F> features = new LinkedList<>();
        JSONArray children = node.getJSONArray(KEY_CHILDREN);
        for (int i = 0; i < children.length(); i++) {
            JSONObject child = (JSONObject) children.get(i);
            String id = child.getString(KEY_ID);

            JSONObject childFeatureObject = getFeatureObject(id);
            String name = childFeatureObject.getString(KEY_NAME);

            F childFeature = fm.addFeature(name, id);
            features.add(childFeature);
        }
        return features;
    }

    public void dispose() {
        fm = null;
        features = null;
        constraints = null;
        tree = null;
        featureBuilder = null;
        relationshipBuilder = null;
        constraintBuilder = null;
    }
}
