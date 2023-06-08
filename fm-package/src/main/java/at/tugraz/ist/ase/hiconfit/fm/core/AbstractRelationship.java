/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fm.core;

import at.tugraz.ist.ase.hiconfit.fm.translator.IConfRuleTranslatable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a relationship of a feature model.
 * <p>
 * This class should be immutable.
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class AbstractRelationship<F extends Feature> {

    @Getter
    protected F parent;
    @Getter
    protected List<F> children = new LinkedList<>();

    @EqualsAndHashCode.Include
    protected String confRule;

    protected IConfRuleTranslatable confRuleTranslator;

    public AbstractRelationship(@NonNull F from, @NonNull Collection<F> to, @NonNull IConfRuleTranslatable confRuleTranslator) {
        this.parent = from;
        this.children.addAll(to);
        this.confRuleTranslator = confRuleTranslator;

        this.confRule = confRuleTranslator.translate(this);
    }

    public void makeConnectionBetweenParentAndChildren() {
        parent.addRelationship(this);
        children.forEach(child -> child.setParent(parent));
    }

    //    /**
//     * Checks whether the relationship is optional.
//     * @return true if the relationship is OPTIONAL or OR, false otherwise.
//     */
//    public boolean isOptional() {
//        return type == RelationshipType.OPTIONAL || type == RelationshipType.OR;
//    }

    /**
     * Returns the first child.
     */
    public F getChild() {
        if (children.isEmpty()) {
            return null;
        }
        return children.get(0);
    }

    /**
     * Checks whether the given {@link Feature} belongs to the left part of the relationship/constraint.
     *
     * @param feature a {@link Feature}
     * @return true if yes, false otherwise.
     */
    public <F extends Feature> boolean isParent(@NonNull F feature) {
        return parent.equals(feature);
    }

    /**
     * Checks whether the given {@link Feature} belongs to the right part of the relationship/constraint.
     *
     * @param feature a {@link Feature}
     * @return true if yes, false otherwise.
     */
    public <F extends Feature> boolean isChild(@NonNull F feature) {
        return children.parallelStream().anyMatch(f -> f.equals(feature));
    }

    public <F extends Feature> boolean contains(@NonNull F feature) {
        return isParent(feature) || isChild(feature);
    }

    public abstract boolean isMandatory();
    public abstract boolean isOptional();
    public abstract boolean isAlternative();
    public abstract boolean isOr();
    public abstract boolean isGroup();

    @Override
    public String toString() {
        return confRule;
    }

    public void dispose() {
        parent = null;
        children.clear();
        children = null;
    }

    public abstract AbstractRelationship<F> clone();
}

