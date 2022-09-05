/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.translator;

import at.tugraz.ist.ase.fm.core.*;
import at.tugraz.ist.ase.fm.core.ast.*;
import lombok.NonNull;

import java.util.stream.Collectors;

/**
 * Defines how to translate relationships/constraints into a string.
 */
public class ConfRuleTranslator implements IConfRuleTranslatable {
    public <F extends Feature> String translate(@NonNull AbstractRelationship<F> r) {
        if (r instanceof MandatoryRelationship<F>) {
            return String.format("mandatory(%s, %s)", r.getParent(), r.getChild());
        } else if (r instanceof OptionalRelationship<F>) {
            return String.format("optional(%s, %s)", r.getParent(), r.getChild());
        } else if (r instanceof AlternativeRelationship<F>) {
            return String.format("alternative(%s, %s)", r.getParent(), r.getChildren().stream().map(Feature::getName).collect(Collectors.joining(", ")));
        } else if (r instanceof OrRelationship<F>) {
            return String.format("or(%s, %s)", r.getParent(), r.getChildren().stream().map(Feature::getName).collect(Collectors.joining(", ")));
        } else {
            throw new IllegalArgumentException("Unknown relationship type");
        }
    }

    public String translate(@NonNull ASTNode r) {
        if (r instanceof AndOperator) {
            return String.format("%s /\\ %s", translate(r.getLeft()), translate(r.getRight()));
        } else if (r instanceof OrOperator) {
            return String.format("%s \\/ %s", translate(r.getLeft()), translate(r.getRight()));
        } else if (r instanceof RequiresOperator) {
            return String.format("requires(%s, %s)", translate(r.getLeft()), translate(r.getRight()));
        } else if (r instanceof ExcludesOperator) {
            return String.format("excludes(%s, %s)", translate(r.getLeft()), translate(r.getRight()));
        } else if (r instanceof ImpliesOperator) {
            return String.format("%s -> %s", translate(r.getLeft()), translate(r.getRight()));
        } else if (r instanceof EquivalenceOperator) {
            return String.format("%s <-> %s", translate(r.getLeft()), translate(r.getRight()));
        } else if (r instanceof NotOperator) {
            if (r.getRight().isOperand()) {
                return String.format("~%s", translate(r.getRight()));
            } else {
                return String.format("~(%s)", translate(r.getRight()));
            }
        } else if (r instanceof Operand<?>) {
            return ((Operand<?>) r).getFeature().toString();
        } else {
            throw new IllegalArgumentException("Unknown constraint type");
        }
    }
}
