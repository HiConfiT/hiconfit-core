/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.translator;

import at.tugraz.ist.ase.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.fm.core.Feature;
import at.tugraz.ist.ase.fm.core.ast.ASTNode;
import lombok.NonNull;

/**
 * Defines how to translate relationships/constraints into a string.
 */
public interface IConfRuleTranslatable {
    <F extends Feature> String translate(@NonNull AbstractRelationship<F> r);
    String translate(@NonNull ASTNode r);
}
