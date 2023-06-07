/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

// Generated from /Users/manleviet/Development/GitHub/CA-CDR-V2/fm-package/src/main/java/at/tugraz/ist/ase/fm/parser/fm4conf/FM4Conf.g4 by ANTLR 4.10.1
package at.tugraz.ist.ase.fm.parser.fm4conf;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link FM4ConfParser}.
 */
public interface FM4ConfListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link FM4ConfParser#model}.
	 * @param ctx the parse tree
	 */
	void enterModel(FM4ConfParser.ModelContext ctx);
	/**
	 * Exit a parse tree produced by {@link FM4ConfParser#model}.
	 * @param ctx the parse tree
	 */
	void exitModel(FM4ConfParser.ModelContext ctx);
	/**
	 * Enter a parse tree produced by {@link FM4ConfParser#fm4confver}.
	 * @param ctx the parse tree
	 */
	void enterFm4confver(FM4ConfParser.Fm4confverContext ctx);
	/**
	 * Exit a parse tree produced by {@link FM4ConfParser#fm4confver}.
	 * @param ctx the parse tree
	 */
	void exitFm4confver(FM4ConfParser.Fm4confverContext ctx);
	/**
	 * Enter a parse tree produced by {@link FM4ConfParser#modelname}.
	 * @param ctx the parse tree
	 */
	void enterModelname(FM4ConfParser.ModelnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link FM4ConfParser#modelname}.
	 * @param ctx the parse tree
	 */
	void exitModelname(FM4ConfParser.ModelnameContext ctx);
	/**
	 * Enter a parse tree produced by {@link FM4ConfParser#feature}.
	 * @param ctx the parse tree
	 */
	void enterFeature(FM4ConfParser.FeatureContext ctx);
	/**
	 * Exit a parse tree produced by {@link FM4ConfParser#feature}.
	 * @param ctx the parse tree
	 */
	void exitFeature(FM4ConfParser.FeatureContext ctx);
	/**
	 * Enter a parse tree produced by {@link FM4ConfParser#relationship}.
	 * @param ctx the parse tree
	 */
	void enterRelationship(FM4ConfParser.RelationshipContext ctx);
	/**
	 * Exit a parse tree produced by {@link FM4ConfParser#relationship}.
	 * @param ctx the parse tree
	 */
	void exitRelationship(FM4ConfParser.RelationshipContext ctx);
	/**
	 * Enter a parse tree produced by {@link FM4ConfParser#constraint}.
	 * @param ctx the parse tree
	 */
	void enterConstraint(FM4ConfParser.ConstraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link FM4ConfParser#constraint}.
	 * @param ctx the parse tree
	 */
	void exitConstraint(FM4ConfParser.ConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link FM4ConfParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(FM4ConfParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link FM4ConfParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(FM4ConfParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mandatory}
	 * labeled alternative in {@link FM4ConfParser#relationshiprule}.
	 * @param ctx the parse tree
	 */
	void enterMandatory(FM4ConfParser.MandatoryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mandatory}
	 * labeled alternative in {@link FM4ConfParser#relationshiprule}.
	 * @param ctx the parse tree
	 */
	void exitMandatory(FM4ConfParser.MandatoryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code optional}
	 * labeled alternative in {@link FM4ConfParser#relationshiprule}.
	 * @param ctx the parse tree
	 */
	void enterOptional(FM4ConfParser.OptionalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code optional}
	 * labeled alternative in {@link FM4ConfParser#relationshiprule}.
	 * @param ctx the parse tree
	 */
	void exitOptional(FM4ConfParser.OptionalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alternative}
	 * labeled alternative in {@link FM4ConfParser#relationshiprule}.
	 * @param ctx the parse tree
	 */
	void enterAlternative(FM4ConfParser.AlternativeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alternative}
	 * labeled alternative in {@link FM4ConfParser#relationshiprule}.
	 * @param ctx the parse tree
	 */
	void exitAlternative(FM4ConfParser.AlternativeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code or}
	 * labeled alternative in {@link FM4ConfParser#relationshiprule}.
	 * @param ctx the parse tree
	 */
	void enterOr(FM4ConfParser.OrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code or}
	 * labeled alternative in {@link FM4ConfParser#relationshiprule}.
	 * @param ctx the parse tree
	 */
	void exitOr(FM4ConfParser.OrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code requires}
	 * labeled alternative in {@link FM4ConfParser#constraintrule}.
	 * @param ctx the parse tree
	 */
	void enterRequires(FM4ConfParser.RequiresContext ctx);
	/**
	 * Exit a parse tree produced by the {@code requires}
	 * labeled alternative in {@link FM4ConfParser#constraintrule}.
	 * @param ctx the parse tree
	 */
	void exitRequires(FM4ConfParser.RequiresContext ctx);
	/**
	 * Enter a parse tree produced by the {@code excludes}
	 * labeled alternative in {@link FM4ConfParser#constraintrule}.
	 * @param ctx the parse tree
	 */
	void enterExcludes(FM4ConfParser.ExcludesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code excludes}
	 * labeled alternative in {@link FM4ConfParser#constraintrule}.
	 * @param ctx the parse tree
	 */
	void exitExcludes(FM4ConfParser.ExcludesContext ctx);
}