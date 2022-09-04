/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core;

import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.fm.builder.IConstraintBuildable;
import at.tugraz.ist.ase.fm.builder.IFeatureBuildable;
import at.tugraz.ist.ase.fm.builder.IRelationshipBuildable;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.*;

/**
 * Represents a feature model
 * ver 2.0 - support a feature model tree, and generic type for features, relationships
 * ver 1.0 - support basic feature models
 * <p>
 * The order of features adding to feature model should follow the breadth-first order.
 */
@Slf4j
@Getter
public class FeatureModel<F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint> implements Cloneable {
    protected String name;

    protected List<F> bfFeatures = new LinkedList<>(); // breadth-first order
    protected List<R> relationships = new LinkedList<>();
    protected List<C> constraints = new LinkedList<>();

    protected F root = null;

    protected IFeatureBuildable featureBuilder;
    protected IRelationshipBuildable relationshipBuilder;
    protected IConstraintBuildable constraintBuilder;

    @Builder
    public FeatureModel(@NonNull String name,
                        @NonNull IFeatureBuildable featureBuilder,
                        @NonNull IRelationshipBuildable relationshipBuilder,
                        @NonNull IConstraintBuildable constraintBuilder) {
        this.name = name;
        this.featureBuilder = featureBuilder;
        this.relationshipBuilder = relationshipBuilder;
        this.constraintBuilder = constraintBuilder;
    }

    public boolean hasRoot() {
        return root != null;
    }

    /**
     * Adds a root feature
     * @param name name of root feature
     * @param id id of root feature
     */
    public F addRoot(@NonNull String name, @NonNull String id) {
        checkArgument(this.root == null, "Root feature already exists!");
        checkArgument(bfFeatures.isEmpty(), "Root feature already exists!");

        this.root = featureBuilder.buildRoot(name, id);
        this.bfFeatures.add(root);
        this.name = root.getName();

        log.trace("{}Added root [root={}]", LoggerUtils.tab(), this.root);
        return this.root;
    }

    public F addFeature(@NonNull String name, @NonNull String id) {
        checkState(this.root != null, "Root feature does not exist!");
        checkState(bfFeatures.size() >= 1, "Root feature does not exist!");
        checkArgument(isUniqueFeatureName(name), "Feature's name " + name + " already exists!");
        checkArgument(isUniqueFeatureId(id), "Feature's id " + id + " already exists!");

        F f = featureBuilder.buildFeature(name, id);
        this.bfFeatures.add(f);

        log.trace("{}Added feature [feature={}]", LoggerUtils.tab(), f);
        return f;
    }

    private boolean isUniqueFeatureName(String fname) {
        return bfFeatures.parallelStream().noneMatch(f -> f.isNameDuplicate(fname));
        /*for (Feature f: bfFeatures) {
            if (f.isNameDuplicate(fname)) {
                return false;
            }
        }
        return true;*/
    }

    private boolean isUniqueFeatureId(String id) {
        return bfFeatures.parallelStream().noneMatch(f -> f.isIdDuplicate(id));
        /*for (Feature f: bfFeatures) {
            if (f.isIdDuplicate(id)) {
                return false;
            }
        }
        return true;*/
    }

    /**
     * Gets the {@link Feature} at a given index.
     * @param index index of the feature
     * @return a {@link Feature}
     */
    public F getFeature(int index) {
        checkElementIndex(index, bfFeatures.size(), "Feature index out of bound!");

        return bfFeatures.get(index);
    }

    /**
     * Gets the {@link Feature} which has a given name.
     * @param id id of the feature
     * @return a {@link Feature}
     */
    public F getFeature(@NonNull String id) {
        checkArgument(!id.isEmpty(), "Feature id cannot be empty!");

        for (F f: bfFeatures) {
            if (f.isIdDuplicate(id)) {
                return f;
            }
        }
        throw new IllegalArgumentException("Feature '" + id + "' doesn't exist!");
    }

    /**
     * Gets the number of features
     * @return the number of features
     */
    public int getNumOfFeatures() {
        return bfFeatures.size();
    }

//    /**
//     * Checks whether the given {@link Feature} is mandatory.
//     * @param feature a {@link Feature}
//     * @return true if the given {@link Feature} is mandatory, false otherwise.
//     */
//    public boolean isMandatoryFeature(@NonNull Feature feature) {
//        return relationships.parallelStream().filter(r -> r.getType() == RelationshipType.MANDATORY).anyMatch(r -> r.presentAtRightSide(feature));
//        /*for (Relationship r : relationships) {
//            if (r.getType() == RelationshipType.MANDATORY) {
//                if (r.presentAtRightSide(feature)) {
//                    return true;
//                }
//            }
//        }
//        return false;*/
//    }
//
//    /**
//     * Checks whether the given {@link Feature} is optional.
//     * @param feature a {@link Feature}
//     * @return true if the given {@link Feature} is optional, false otherwise.
//     */
//    public boolean isOptionalFeature(@NonNull Feature feature) {
//        for (Relationship r : relationships) {
//            if (r.getType() == RelationshipType.OPTIONAL) {
//                if (r.presentAtLeftSide(feature)) {
//                    return true;
//                }
//            } else if (r.getType() == RelationshipType.OR || r.getType() == RelationshipType.ALTERNATIVE) {
//                if (r.presentAtRightSide(feature)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

//    /**
//     * Gets all {@link Feature}s participating on the right side of the constraint
//     * in which the left side is the given {@link Feature}.
//     * @param leftSide a {@link Feature}
//     * @return an array of {@link Feature}s
//     */
//    public List<Feature> getRightSideOfRelationships(@NonNull Feature leftSide) throws FeatureModelException {
//        List<Feature> children = new LinkedList<>();
//        for (Relationship r : relationships) {
//            if (r.getType() == RelationshipType.OPTIONAL) {
//                if (r.presentAtRightSide(leftSide)) {
//                    Feature left = ((BasicRelationship) r).getLeftSide();
//                    Feature parent = getFeature(left.getId());
//                    if (parent != null) {
//                        children.add(parent);
//                    }
//                }
//            } else {
//                if (r.presentAtLeftSide(leftSide)) {
//                    List<Feature> rightSide = ((BasicRelationship) r).getRightSide();
//                    for (Feature right : rightSide) {
//                        Feature child = getFeature(right.getId());
//                        if (child != null) {
//                            children.add(child);
//                        }
//                    }
//                }
//            }
//        }
//        return children;
//    }
//
//    /**
//     * Gets all parent {@link Feature}s of the given {@link Feature}.
//     * @param rightSide a {@link Feature}.
//     * @return an array of {@link Feature}s.
//     */
//    public List<Feature> getMandatoryParents(@NonNull Feature rightSide) throws FeatureModelException {
//        List<Feature> parents = new LinkedList<>();
//
//        List<Relationship> relationships = getRelationshipsWith(rightSide);
//        for (Relationship r : relationships) {
//            List<Feature> parentsqueue = new LinkedList<>();
//            if (r.isType(RelationshipType.REQUIRES)) {
//                if (r.presentAtRightSide(rightSide)) {
//                    parentsqueue.add(rightSide);
//                    getMandatoryParent(r, rightSide, parents, parentsqueue);
//                }
//            } else if (r.isType(RelationshipType.ALTERNATIVE)
//                    || r.isType(RelationshipType.OR)) {
//                parentsqueue.add(rightSide);
//                getMandatoryParent(r, rightSide, parents, parentsqueue);
//            } // TODO - 3CNF
//        }
//
//        return parents;
//    }
//
//    private void getMandatoryParent(Relationship r, Feature feature, List<Feature> parents, List<Feature> parentsqueue) throws FeatureModelException {
//        if (feature.toString().equals(this.getName())) return;
//
//        if (r.isType(RelationshipType.REQUIRES)) {
//            Feature parent = ((BasicRelationship) r).getLeftSide();
//
//            if (parent.getName().equals(this.getName())) return;
//            if (parentsqueue.contains(parent)) return;
//
//            exploreMandatoryParent(parents, parentsqueue, parent);
//
//        } else if (r.getType() == RelationshipType.ALTERNATIVE
//                || r.getType() == RelationshipType.OR) {
//            if (r.presentAtRightSide(feature)) {
//                Feature parent = ((BasicRelationship) r).getLeftSide();
//
//                if (parent.getName().equals(this.getName())) return;
//                if (parentsqueue.contains(parent)) return;
//
//                exploreMandatoryParent(parents, parentsqueue, parent);
//            } else if (r.presentAtLeftSide(feature)) {
//                List<Feature> lefts = ((BasicRelationship)r).getRightSide();
//                for (Feature parent: lefts) {
//
//                    if (parentsqueue.contains(parent)) return;
//                    if (parent.getName().equals(this.getName())) return;
//
//                    exploreMandatoryParent(parents, parentsqueue, parent);
//                }
//            }
//        }
//    }
//
//    private void exploreMandatoryParent(List<Feature> parents, List<Feature> parentsqueue, Feature parent) throws FeatureModelException {
//        if (this.isMandatoryFeature(parent)) {
//            if (!parents.contains(parent)) {
//                parents.add(parent);
//            }
//        } else {
//            List<Relationship> relationships = getRelationshipsWith(parent);
//            for (Relationship r1 : relationships) {
//                if (r1.isType(RelationshipType.REQUIRES)) {
//                    if (r1.presentAtRightSide(parent)) {
//                        parentsqueue.add(parent);
//                        getMandatoryParent(r1, parent, parents, parentsqueue);
//                        parentsqueue.remove(parentsqueue.size() - 1);
//                    }
//                } else if (r1.isType(RelationshipType.ALTERNATIVE)
//                        || r1.isType(RelationshipType.OR)) {
//                    parentsqueue.add(parent);
//                    getMandatoryParent(r1, parent, parents, parentsqueue);
//                    parentsqueue.remove(parentsqueue.size() - 1);
//                }
//            }
//        }
//    }

//    /**
//     * Gets all {@link Relationship}s in which the given {@link Feature} participates.
//     * @param feature a {@link Feature}
//     * @return an array of {@link Relationship}s.
//     */
//    public List<Relationship> getRelationshipsWith(@NonNull Feature feature) {
//        List<Relationship> rs = relationships.parallelStream().filter(r -> r.presentAtRightSide(feature) || r.presentAtLeftSide(feature))
//                .collect(Collectors.toCollection(LinkedList::new));
//        /*for (Relationship r : relationships) {
//            if (r.presentAtRightSide(feature) || r.presentAtLeftSide(feature)) {
//                rs.add(r);
//            }
//        }*/
//        for (Relationship r : constraints) {
//            if (!r.isType(RelationshipType.ThreeCNF)) {
//                if (r.presentAtRightSide(feature) || r.presentAtLeftSide(feature)) {
//                    rs.add(r);
//                }
//            } else { // 3CNF
//                if (r.contains(feature)) {
//                    rs.add(r);
//                }
//            }
//        }
//        return rs;
//    }

    /**
     * Adds a new MANDATORY relationship to the feature model.
     * The features involved in the relationship must already exist in the feature model.
     * @param from the parent feature.
     * @param to the child feature.
     */
    public void addMandatoryRelationship(@NonNull F from, @NonNull F to) {
        checkArgument(bfFeatures.contains(from), "Parent feature of relationship must be already added to feature model");
        checkArgument(bfFeatures.contains(to), "Child feature of relationship must be already added to feature model");

        R r = relationshipBuilder.buildMandatoryRelationship(from, to);

        this.relationships.add(r);

        // make the connection between the parent and the children
        r.makeConnectionBetweenParentAndChildren();

        log.trace("{}Added MANDATORY relationship [relationship={}]", LoggerUtils.tab(), r);
    }

    /**
     * Adds a new OPTIONAL relationship to the feature model.
     * The features involved in the relationship must already exist in the feature model.
     * @param from the parent feature.
     * @param to the child feature.
     */
    public void addOptionalRelationship(@NonNull F from, @NonNull F to) {
        checkArgument(bfFeatures.contains(from), "Parent feature of relationship must be already added to feature model");
        checkArgument(bfFeatures.contains(to), "Child feature of relationship must be already added to feature model");

        R r = relationshipBuilder.buildOptionalRelationship(from, to);

        this.relationships.add(r);

        // make the connection between the parent and the children
        r.makeConnectionBetweenParentAndChildren();

        log.trace("{}Added OPTIONAL relationship [relationship={}]", LoggerUtils.tab(), r);
    }

    /**
     * Adds a new OR relationship to the feature model.
     * The features involved in the relationship must already exist in the feature model.
     * @param from the parent feature.
     * @param to the child features.
     */
    public void addOrRelationship(@NonNull F from, @NonNull List<F> to) {
        checkArgument(bfFeatures.contains(from), "Parent feature of relationship must be already added to feature model");
        to.forEach(child -> checkArgument(bfFeatures.contains(child), "Child feature of relationship must be already added to feature model"));

        R r = relationshipBuilder.buildOrRelationship(from, to);

        this.relationships.add(r);

        // make the connection between the parent and the children
        r.makeConnectionBetweenParentAndChildren();

        log.trace("{}Added OR relationship [relationship={}]", LoggerUtils.tab(), r);
    }

    /**
     * Adds a new ALTERNATIVE relationship to the feature model.
     * The features involved in the relationship must already exist in the feature model.
     * @param from the parent feature.
     * @param to the child features.
     */
    public void addAlternativeRelationship(@NonNull F from, @NonNull List<F> to) {
        checkArgument(bfFeatures.contains(from), "Parent feature of relationship must be already added to feature model");
        to.forEach(child -> checkArgument(bfFeatures.contains(child), "Child feature of relationship must be already added to feature model"));

        R r = relationshipBuilder.buildAlternativeRelationship(from, to);

        this.relationships.add(r);

        // make the connection between the parent and the children
        r.makeConnectionBetweenParentAndChildren();

        log.trace("{}Added ALTERNATIVE relationship [relationship={}]", LoggerUtils.tab(), r);
    }

    /**
     * Gets the number of relationships.
     * @return number of relationships
     */
    public int getNumOfRelationships() {
        return relationships.size();
    }

    /**
     * Gets the number of relationships with the specific type.
     * @param type type of relationships
     * @return number of relationships with the specific type.
     */
    public <cls extends AbstractRelationship<F>> int getNumOfRelationships(Class<cls> type) {
        int count;
//        if (type == RelationshipType.REQUIRES || type == RelationshipType.EXCLUDES) {
//            count = (int) constraints.parallelStream().filter(relationship -> relationship.isType(type)).count();
//            /*for (Relationship relationship : constraints) {
//                if (relationship.isType(type)) {
//                    count++;
//                }
//            }*/
//        } else {
            count = (int) relationships.parallelStream().filter(type::isInstance).count();
            /*for (Relationship relationship : relationships) {
                if (type.isInstance(relationship)) {
                    count++;
                }
            }*/
//        }
        return count;
    }

    public void addConstraint(@NonNull C cstr) {
        cstr.getFeatures().forEach(f -> checkArgument(bfFeatures.contains(f), "Feature of constraint must be already added to feature model"));

        this.constraints.add(cstr);

        log.trace("{}Added cross-tree constraint [constraint={}]", LoggerUtils.tab(), cstr);
    }

    public void addRequires(@NonNull F from, @NonNull F to) {
        addConstraint(constraintBuilder.buildRequires(from, to));
    }

    public void addExcludes(@NonNull F from, @NonNull F to) {
        addConstraint(constraintBuilder.buildExcludes(from, to));
    }

    /**
     * Gets the number of constraints.
     * @return number of constraints.
     */
    public int getNumOfConstraints() {
        return constraints.size();
    }

    @Override
    public String toString() {
        if (bfFeatures.isEmpty()) return "";

        StringBuilder st = new StringBuilder();

        st.append("FEATURES:\n");
        bfFeatures.parallelStream().map(feature -> String.format("\t%s\n", feature)).forEachOrdered(st::append);

        st.append("RELATIONSHIPS:\n");
        relationships.parallelStream().map(relationship -> String.format("\t%s\n", relationship)).forEachOrdered(st::append);

        st.append("CONSTRAINTS:\n");
        constraints.parallelStream().map(constraint -> String.format("\t%s\n", constraint)).forEachOrdered(st::append);

        return st.toString();
    }

    public void dispose() {
        bfFeatures.clear();
        bfFeatures = null;
        relationships.clear();
        relationships = null;
        constraints.clear();
        constraints = null;
        featureBuilder = null;
        relationshipBuilder = null;
    }

    @SuppressWarnings("unchecked")
    public Object clone() throws CloneNotSupportedException {
        FeatureModel<F, R, C> clone = (FeatureModel<F, R, C>) super.clone();

        // copy bfFeatures
        clone.bfFeatures = new LinkedList<>();
        for (F f : bfFeatures) {
            clone.bfFeatures.add((F) f.clone());
        }

        // copy relationships
        clone.relationships = new LinkedList<>();
        relationships.forEach(r -> clone.relationships.add((R) r.clone()));

        // copy constraints
        clone.constraints = new LinkedList<>();
        for (C cstr : constraints) {
            clone.constraints.add((C) cstr.clone());
        }

        return clone;
    }
}

