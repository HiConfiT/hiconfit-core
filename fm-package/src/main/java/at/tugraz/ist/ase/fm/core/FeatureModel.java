/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core;

import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.fm.builder.IConstraintBuildable;
import at.tugraz.ist.ase.fm.builder.IFeatureBuildable;
import at.tugraz.ist.ase.fm.builder.IRelationshipBuildable;
import at.tugraz.ist.ase.fm.core.ast.Operand;
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

    // the root of the feature model tree
    protected F root = null;

    protected IFeatureBuildable featureBuilder;
    protected IRelationshipBuildable relationshipBuilder;
    protected IConstraintBuildable constraintBuilder;

    /**
     * Constructor for a feature model
     * @param name name of the feature model
     * @param featureBuilder builder for features
     * @param relationshipBuilder builder for relationships
     * @param constraintBuilder builder for constraints
     */
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
     * Adds a root feature with the given name and id.
     * The root feature is abstract.
     * @param name name of root feature
     * @param id id of root feature
     * @return the root feature
     */
    public F addRoot(@NonNull String name, @NonNull String id) {
        checkArgument(this.root == null, "Root feature already exists!");
        checkArgument(bfFeatures.isEmpty(), "Root feature already exists!");

        this.root = featureBuilder.buildRoot(name, id);
        this.bfFeatures.add(root);

        log.trace("{}Added root [root={}]", LoggerUtils.tab(), this.root);
        return this.root;
    }

    /**
     * Adds a feature with the given name and id.
     * The order of adding features should follow the breadth-first order.
     * @param name name of feature
     * @param id id of feature
     * @return the added feature
     */
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

    /**
     * Checks if the given name is duplicated.
     * @param fname name of feature
     * @return true if the given name is unique, otherwise false
     */
    private boolean isUniqueFeatureName(String fname) {
        return bfFeatures.parallelStream().noneMatch(f -> f.isNameDuplicate(fname));
        /*for (Feature f: bfFeatures) {
            if (f.isNameDuplicate(fname)) {
                return false;
            }
        }
        return true;*/
    }

    /**
     * Checks if the given id is duplicated.
     * @param id id of feature
     * @return true if the given id is unique, otherwise false
     */
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

    /**
     * Gets the number of leaf features
     * @return the number of leaf features
     */
    public int getNumOfLeaf() {
        return (int) bfFeatures.parallelStream().filter(F::isLeaf).count();
    }

    /**
     * Gets all parent {@link Feature}s of the given {@link Feature}.
     * @param rightSide a {@link Feature}.
     * @return an array of {@link Feature}s.
     */
    public List<F> getMandatoryParents(@NonNull F rightSide) {
        List<F> parents = new LinkedList<>();
        List<F> parentsqueue = new LinkedList<>();

        exploreMandatoryParentFrom(rightSide, parents, parentsqueue);

        return parents;
    }

    @SuppressWarnings("unchecked")
    private void exploreMandatoryParentFrom(@NonNull F rightSide, List<F> parents, List<F> parentsqueue) {
        List<R> relationships = (List<R>) rightSide.getAllRelationships();
        List<C> cstrs = getRequiresConstraintsAndFeatureInRight(rightSide);

        for (R r : relationships) {
            if (r instanceof OrRelationship
                || r instanceof AlternativeRelationship) {
                parentsqueue.add(rightSide);
                getMandatoryParent(r, rightSide, parents, parentsqueue);
                parentsqueue.remove(parentsqueue.size() - 1);
            }
        }

        for (C c : cstrs) {
            parentsqueue.add(rightSide);
            getMandatoryParent(c, rightSide, parents, parentsqueue);
            parentsqueue.remove(parentsqueue.size() - 1);
        }
    }

    private void getMandatoryParent(R r, F feature, List<F> parents, List<F> parentsqueue) {
        // ignore the root feature
        if (feature.isRoot()) return;

        if (r.isChild(feature)) {
            F parent = r.getParent();

            checkAndExploreFurther(parent, parents, parentsqueue);
        } else if (r.isParent(feature)) {
            List<F> lefts = r.getChildren();
            for (F parent: lefts) {

                checkAndExploreFurther(parent, parents, parentsqueue);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void getMandatoryParent(C c, F feature, List<F> parents, List<F> parentsqueue) {
        // ignore the root feature
        if (feature.isRoot()) return;

        F parent = ((Operand<F>) c.getFormula().getLeft()).getFeature();

        // ignore the root feature
        checkAndExploreFurther(parent, parents, parentsqueue);
    }

    private void checkAndExploreFurther(F parent, List<F> parents, List<F> parentsqueue) {
        if (parent.isRoot()) return; // ignore the root feature
        if (parentsqueue.contains(parent)) return;
        if (parent.isMandatory() && !parents.contains(parent)) {
            parents.add(parent);
        }

        exploreMandatoryParentFrom(parent, parents, parentsqueue);
    }

    /**
     * Gets requires constraints which the given feature is on the right side.
     * @param feature a {@link F}.
     * @return a list of {@link C}.
     */
    private List<C> getRequiresConstraintsAndFeatureInRight(@NonNull F feature) {
        List<C> cstrs = new LinkedList<>();
        constraints.parallelStream().filter(CTConstraint::isRequires).forEachOrdered(cstr -> {
            List<F> right = cstr.getFormula().getRight().getFeatures();
            if (right.size() == 1 && right.contains(feature)) {

                List<F> left = cstr.getFormula().getLeft().getFeatures();
                List<Feature> subFeaturesOfRight = (right.get(0)).getChildren();

                if (left.size() == 1 && !subFeaturesOfRight.contains(left.get(0))) {
                    cstrs.add(cstr);
                }
            }
        });
        return cstrs;
    }

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
        return (int) relationships.parallelStream().filter(type::isInstance).count();
        /*for (Relationship relationship : relationships) {
            if (type.isInstance(relationship)) {
                count++;
            }
        }*/
    }

    public void addConstraint(@NonNull C cstr) {
        cstr.getFeatures().forEach(f -> checkArgument(bfFeatures.contains(f), "Feature of constraint must be already added to feature model"));

        this.constraints.add(cstr);

        log.trace("{}Added cross-tree constraint [constraint={}]", LoggerUtils.tab(), cstr);
    }

    public void addRequires(@NonNull F from, @NonNull F to) {
        addConstraint(constraintBuilder.buildConstraint(constraintBuilder.buildRequires(from, to)));
    }

    public void addExcludes(@NonNull F from, @NonNull F to) {
        addConstraint(constraintBuilder.buildConstraint(constraintBuilder.buildExcludes(from, to)));
    }

    /**
     * Gets the number of constraints.
     * @return number of constraints.
     */
    public int getNumOfConstraints() {
        return constraints.size();
    }

    /**
     * Gets the number of requires.
     * @return number of requires.
     */
    public int getNumOfRequires() {
        return (int) constraints.parallelStream().filter(c -> c.getFormula().isRequires()).count();
    }

    /**
     * Gets the number of excludes.
     * @return number of excludes.
     */
    public int getNumOfExcludes() {
        return (int) constraints.parallelStream().filter(c -> c.getFormula().isExcludes()).count();
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

