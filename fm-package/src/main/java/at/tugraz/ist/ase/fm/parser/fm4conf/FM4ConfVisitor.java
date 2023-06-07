/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

// Generated from /Users/manleviet/Development/GitHub/CA-CDR-V2/fm-package/src/main/java/at/tugraz/ist/ase/fm/parser/fm4conf/FM4Conf.g4 by ANTLR 4.10.1
package at.tugraz.ist.ase.fm.parser.fm4conf;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link FM4ConfParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface FM4ConfVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link FM4ConfParser#model}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel(FM4ConfParser.ModelContext ctx);
	/**
	 * Visit a parse tree produced by {@link FM4ConfParser#fm4confver}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFm4confver(FM4ConfParser.Fm4confverContext ctx);
	/**
	 * Visit a parse tree produced by {@link FM4ConfParser#modelname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelname(FM4ConfParser.ModelnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link FM4ConfParser#feature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeature(FM4ConfParser.FeatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link FM4ConfParser#relationship}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationship(FM4ConfParser.RelationshipContext ctx);
	/**
	 * Visit a parse tree produced by {@link FM4ConfParser#constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraint(FM4ConfParser.ConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link FM4ConfParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(FM4ConfParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mandatory}
	 * labeled alternative in {@link FM4ConfParser#relationshiprule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMandatory(FM4ConfParser.MandatoryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code optional}
	 * labeled alternative in {@link FM4ConfParser#relationshiprule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptional(FM4ConfParser.OptionalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alternative}
	 * labeled alternative in {@link FM4ConfParser#relationshiprule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlternative(FM4ConfParser.AlternativeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code or}
	 * labeled alternative in {@link FM4ConfParser#relationshiprule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr(FM4ConfParser.OrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code requires}
	 * labeled alternative in {@link FM4ConfParser#constraintrule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRequires(FM4ConfParser.RequiresContext ctx);
	/**
	 * Visit a parse tree produced by the {@code excludes}
	 * labeled alternative in {@link FM4ConfParser#constraintrule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExcludes(FM4ConfParser.ExcludesContext ctx);
}