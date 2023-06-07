/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

// Generated from /Users/manleviet/Development/GitHub/CSP2ChocoTranslator/src/main/java/at/tugraz/ist/ase/csp2choco/antlr/CSP2Choco.g4 by ANTLR 4.10.1
package at.tugraz.ist.ase.hiconfit.csp2choco.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CSP2ChocoParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CSP2ChocoVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CSP2ChocoParser#model}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel(CSP2ChocoParser.ModelContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSP2ChocoParser#statement_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement_list(CSP2ChocoParser.Statement_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSP2ChocoParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(CSP2ChocoParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSP2ChocoParser#constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraint(CSP2ChocoParser.ConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSP2ChocoParser#requirement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRequirement(CSP2ChocoParser.RequirementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Not}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNot(CSP2ChocoParser.NotContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parens}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParens(CSP2ChocoParser.ParensContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Or}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr(CSP2ChocoParser.OrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDiv(CSP2ChocoParser.MulDivContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSub(CSP2ChocoParser.AddSubContext ctx);
	/**
	 * Visit a parse tree produced by the {@code And}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd(CSP2ChocoParser.AndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code id}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(CSP2ChocoParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Implication}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplication(CSP2ChocoParser.ImplicationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Minus}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinus(CSP2ChocoParser.MinusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code int}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInt(CSP2ChocoParser.IntContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Comparation}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparation(CSP2ChocoParser.ComparationContext ctx);
}