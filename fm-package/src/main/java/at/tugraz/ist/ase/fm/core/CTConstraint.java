/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core;

import at.tugraz.ist.ase.fm.core.ast.ASTNode;
import lombok.Getter;
import lombok.NonNull;

import java.util.LinkedList;
import java.util.List;

/**
 * A Cross Tree Constraint
 * holds an Abstract Syntax Tree (AST) of the constraint
 */
@Getter
public class CTConstraint implements Cloneable {
    private final String constraint;
    private ASTNode formula;

    public CTConstraint(@NonNull ASTNode formula) {
        this.formula = formula;
        this.constraint = formula.toString();
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
