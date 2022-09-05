/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core;

import at.tugraz.ist.ase.fm.core.ast.ASTNode;
import at.tugraz.ist.ase.fm.translator.IConfRuleTranslatable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.LinkedList;
import java.util.List;

/**
 * A Cross Tree Constraint
 * holds an Abstract Syntax Tree (AST) of the constraint
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CTConstraint implements Cloneable {
    @EqualsAndHashCode.Include
    protected String constraint;
    protected ASTNode formula;

    protected IConfRuleTranslatable translator;

    public CTConstraint(@NonNull ASTNode formula, @NonNull IConfRuleTranslatable translator) {
        this.formula = formula;
        this.translator = translator;
        this.constraint = translator.translate(formula);
    }

    /**
     * Gets the list of features that are involved in the constraint.
     * @return the list of features that are involved in the constraint.
     */
    public <F extends Feature> List<F> getFeatures() {
        return new LinkedList<>(formula.getFeatures());
    }

    @Override
    public String toString() {
        return constraint;
    }

    public void dispose() {
        formula.dispose();
        formula = null;
    }

    public CTConstraint clone() throws CloneNotSupportedException {
        CTConstraint clone = (CTConstraint) super.clone();
        clone.formula = formula.clone();
        return clone;
    }
}
