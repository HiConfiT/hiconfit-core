/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core;

import at.tugraz.ist.ase.common.LoggerUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;

/**
 * Represents a feature model
 * ver 1.0 - support basic feature models
 */
@Slf4j
@Getter
public class FeatureModel {
    @Setter
    private String name;

    private final List<Feature> bfFeatures; // breadth-first order
    private final List<Relationship> relationships;
    private final List<Relationship> constraints;

    @Setter
    private boolean consistency;

    /**
     * A constructor
     */
    public FeatureModel() {
        bfFeatures = new LinkedList<>();
        relationships = new LinkedList<>();
        constraints = new LinkedList<>();
        consistency = false;
    }

    /**
     * Adds a new feature
     * @param fname name of the feature
     */
    public void addFeature(@NonNull String fname, @NonNull String id) {
        checkArgument(!fname.isEmpty(), "Feature name cannot be empty!");
        checkArgument(!id.isEmpty(), "Feature id cannot be empty!");
        checkArgument(isUniqueFeatureName(fname), "Feature's name " + fname + " already exists!");
        checkArgument(isUniqueFeatureId(id), "Feature's id " + id + " already exists!");

        Feature f = new Feature(fname, id);
        this.bfFeatures.add(f);

        log.trace("{}Added feature [feature={}]", LoggerUtils.tab, f);
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
    public Feature getFeature(int index) {
        checkElementIndex(index, bfFeatures.size(), "Index out of bound!");

        return bfFeatures.get(index);
    }

    /**
     * Gets the {@link Feature} which has a given name.
     * @param id id of the feature
     * @return a {@link Feature}
     */
    public Feature getFeature(@NonNull String id) throws FeatureModelException {
        checkArgument(!id.isEmpty(), "Feature name cannot be empty!");

        for (Feature f: bfFeatures) {
            if (f.isIdDuplicate(id)) {
                return f;
            }
        }
        throw new FeatureModelException("Feature '" + id + "' doesn't exist!");
    }

    /**
     * Gets the number of features
     * @return the number of features
     */
    public int getNumOfFeatures() {
        return bfFeatures.size();
    }

    /**
     * Checks whether the given {@link Feature} is mandatory.
     * @param feature a {@link Feature}
     * @return true if the given {@link Feature} is mandatory, false otherwise.
     */
    public boolean isMandatoryFeature(@NonNull Feature feature) {
        return relationships.parallelStream().filter(r -> r.getType() == RelationshipType.MANDATORY).anyMatch(r -> r.presentAtRightSide(feature));
        /*for (Relationship r : relationships) {
            if (r.getType() == RelationshipType.MANDATORY) {
                if (r.presentAtRightSide(feature)) {
                    return true;
                }
            }
        }
        return false;*/
    }

    /**
     * Checks whether the given {@link Feature} is optional.
     * @param feature a {@link Feature}
     * @return true if the given {@link Feature} is optional, false otherwise.
     */
    public boolean isOptionalFeature(@NonNull Feature feature) {
        for (Relationship r : relationships) {
            if (r.getType() == RelationshipType.OPTIONAL) {
                if (r.presentAtLeftSide(feature)) {
                    return true;
                }
            } else if (r.getType() == RelationshipType.OR || r.getType() == RelationshipType.ALTERNATIVE) {
                if (r.presentAtRightSide(feature)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets all {@link Feature}s participating on the right side of the constraint
     * in which the left side is the given {@link Feature}.
     * @param leftSide a {@link Feature}
     * @return an array of {@link Feature}s
     */
    public List<Feature> getRightSideOfRelationships(@NonNull Feature leftSide) throws FeatureModelException {
        List<Feature> children = new LinkedList<>();
        for (Relationship r : relationships) {
            if (r.getType() == RelationshipType.OPTIONAL) {
                if (r.presentAtRightSide(leftSide)) {
                    Feature left = ((BasicRelationship) r).getLeftSide();
                    Feature parent = getFeature(left.getId());
                    if (parent != null) {
                        children.add(parent);
                    }
                }
            } else {
                if (r.presentAtLeftSide(leftSide)) {
                    List<Feature> rightSide = ((BasicRelationship) r).getRightSide();
                    for (Feature right : rightSide) {
                        Feature child = getFeature(right.getId());
                        if (child != null) {
                            children.add(child);
                        }
                    }
                }
            }
        }
        return children;
    }

    /**
     * Gets all parent {@link Feature}s of the given {@link Feature}.
     * @param rightSide a {@link Feature}.
     * @return an array of {@link Feature}s.
     */
    public List<Feature> getMandatoryParents(@NonNull Feature rightSide) throws FeatureModelException {
        List<Feature> parents = new LinkedList<>();

        List<Relationship> relationships = getRelationshipsWith(rightSide);
        for (Relationship r : relationships) {
            List<Feature> parentsqueue = new LinkedList<>();
            if (r.isType(RelationshipType.REQUIRES)) {
                if (r.presentAtRightSide(rightSide)) {
                    parentsqueue.add(rightSide);
                    getMandatoryParent(r, rightSide, parents, parentsqueue);
                }
            } else if (r.isType(RelationshipType.ALTERNATIVE)
                    || r.isType(RelationshipType.OR)) {
                parentsqueue.add(rightSide);
                getMandatoryParent(r, rightSide, parents, parentsqueue);
            } // TODO - 3CNF
        }

        return parents;
    }

    private void getMandatoryParent(Relationship r, Feature feature, List<Feature> parents, List<Feature> parentsqueue) throws FeatureModelException {
        if (feature.toString().equals(this.getName())) return;

        if (r.isType(RelationshipType.REQUIRES)) {
            Feature parent = ((BasicRelationship) r).getLeftSide();

            if (parent.getName().equals(this.getName())) return;
            if (parentsqueue.contains(parent)) return;

            exploreMandatoryParent(parents, parentsqueue, parent);

        } else if (r.getType() == RelationshipType.ALTERNATIVE
                || r.getType() == RelationshipType.OR) {
            if (r.presentAtRightSide(feature)) {
                Feature parent = ((BasicRelationship) r).getLeftSide();

                if (parent.getName().equals(this.getName())) return;
                if (parentsqueue.contains(parent)) return;

                exploreMandatoryParent(parents, parentsqueue, parent);
            } else if (r.presentAtLeftSide(feature)) {
                List<Feature> lefts = ((BasicRelationship)r).getRightSide();
                for (Feature parent: lefts) {

                    if (parentsqueue.contains(parent)) return;
                    if (parent.getName().equals(this.getName())) return;

                    exploreMandatoryParent(parents, parentsqueue, parent);
                }
            }
        }
    }

    private void exploreMandatoryParent(List<Feature> parents, List<Feature> parentsqueue, Feature parent) throws FeatureModelException {
        if (this.isMandatoryFeature(parent)) {
            if (!parents.contains(parent)) {
                parents.add(parent);
            }
        } else {
            List<Relationship> relationships = getRelationshipsWith(parent);
            for (Relationship r1 : relationships) {
                if (r1.isType(RelationshipType.REQUIRES)) {
                    if (r1.presentAtRightSide(parent)) {
                        parentsqueue.add(parent);
                        getMandatoryParent(r1, parent, parents, parentsqueue);
                        parentsqueue.remove(parentsqueue.size() - 1);
                    }
                } else if (r1.isType(RelationshipType.ALTERNATIVE)
                        || r1.isType(RelationshipType.OR)) {
                    parentsqueue.add(parent);
                    getMandatoryParent(r1, parent, parents, parentsqueue);
                    parentsqueue.remove(parentsqueue.size() - 1);
                }
            }
        }
    }

    /**
     * Gets all {@link Relationship}s in which the given {@link Feature} participates.
     * @param feature a {@link Feature}
     * @return an array of {@link Relationship}s.
     */
    public List<Relationship> getRelationshipsWith(@NonNull Feature feature) {
        List<Relationship> rs = relationships.parallelStream().filter(r -> r.presentAtRightSide(feature) || r.presentAtLeftSide(feature))
                .collect(Collectors.toCollection(LinkedList::new));
        /*for (Relationship r : relationships) {
            if (r.presentAtRightSide(feature) || r.presentAtLeftSide(feature)) {
                rs.add(r);
            }
        }*/
        for (Relationship r : constraints) {
            if (!r.isType(RelationshipType.ThreeCNF)) {
                if (r.presentAtRightSide(feature) || r.presentAtLeftSide(feature)) {
                    rs.add(r);
                }
            } else { // 3CNF
                if (r.contains(feature)) {
                    rs.add(r);
                }
            }
        }
        return rs;
    }

    /**
     * Adds a new relationship to the feature model.
     * @param type type of the relationship
     * @param leftSide the left part of relationship
     * @param rightSide the right part of relationship
     */
    public void addRelationship(RelationshipType type, @NonNull Feature leftSide, @NonNull List<Feature> rightSide) {
        Relationship r = new BasicRelationship(type, leftSide, rightSide);
        this.relationships.add(r);

        log.trace("{}Added relationship [relationship={}]", LoggerUtils.tab, r);
    }

//    /**
//     * Gets all {@link Relationship}s
//     * @return an array of {@link Relationship}s
//     */
//    public List<Relationship> getRelationships() {
//        return relationships;
//    }

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
    public int getNumOfRelationships(RelationshipType type) {
        int count;
        if (type == RelationshipType.REQUIRES || type == RelationshipType.EXCLUDES) {
            count = (int) constraints.parallelStream().filter(relationship -> relationship.isType(type)).count();
            /*for (Relationship relationship : constraints) {
                if (relationship.isType(type)) {
                    count++;
                }
            }*/
        } else {
            count = (int) relationships.stream().filter(relationship -> relationship.isType(type)).count();
            /*for (Relationship relationship : relationships) {
                if (relationship.isType(type)) {
                    count++;
                }
            }*/
        }
        return count;
    }

    /**
     * Adds a new constraint
     * @param type type of relationship
     * @param leftSide left part of the constraint
     * @param rightSide right part of the constraint
     */
    public void addConstraint(RelationshipType type, @NonNull Feature leftSide, @NonNull List<Feature> rightSide) {
        Relationship r = new BasicRelationship(type, leftSide, rightSide);
        this.constraints.add(r);

        log.trace("{}Added constraint [constraint={}]", LoggerUtils.tab, r);
    }

    /**
     * Adds a new 3CNF constraint
     * @param type type of relationship
     * @param constraint3CNF 3CNF constraint
     */
    public void addConstraint(RelationshipType type, String constraint3CNF) {
        Relationship r = new ThreeCNFConstraint(type, constraint3CNF);
        this.constraints.add(r);

        log.trace("{}Added constraint [constraint={}]", LoggerUtils.tab, r);
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
}

