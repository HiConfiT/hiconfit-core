/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

// Generated from /Users/manleviet/Development/GitHub/CSP2ChocoTranslator/src/main/java/at/tugraz/ist/ase/csp2choco/antlr/CSP2Choco.g4 by ANTLR 4.10.1
package at.tugraz.ist.ase.hiconfit.csp2choco.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CSP2ChocoParser}.
 */
public interface CSP2ChocoListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CSP2ChocoParser#model}.
	 * @param ctx the parse tree
	 */
	void enterModel(CSP2ChocoParser.ModelContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSP2ChocoParser#model}.
	 * @param ctx the parse tree
	 */
	void exitModel(CSP2ChocoParser.ModelContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSP2ChocoParser#statement_list}.
	 * @param ctx the parse tree
	 */
	void enterStatement_list(CSP2ChocoParser.Statement_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSP2ChocoParser#statement_list}.
	 * @param ctx the parse tree
	 */
	void exitStatement_list(CSP2ChocoParser.Statement_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSP2ChocoParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(CSP2ChocoParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSP2ChocoParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(CSP2ChocoParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSP2ChocoParser#constraint}.
	 * @param ctx the parse tree
	 */
	void enterConstraint(CSP2ChocoParser.ConstraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSP2ChocoParser#constraint}.
	 * @param ctx the parse tree
	 */
	void exitConstraint(CSP2ChocoParser.ConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSP2ChocoParser#requirement}.
	 * @param ctx the parse tree
	 */
	void enterRequirement(CSP2ChocoParser.RequirementContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSP2ChocoParser#requirement}.
	 * @param ctx the parse tree
	 */
	void exitRequirement(CSP2ChocoParser.RequirementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Not}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 */
	void enterNot(CSP2ChocoParser.NotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Not}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 */
	void exitNot(CSP2ChocoParser.NotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parens}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 */
	void enterParens(CSP2ChocoParser.ParensContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parens}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 */
	void exitParens(CSP2ChocoParser.ParensContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Or}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 */
	void enterOr(CSP2ChocoParser.OrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Or}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 */
	void exitOr(CSP2ChocoParser.OrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 */
	void enterMulDiv(CSP2ChocoParser.MulDivContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 */
	void exitMulDiv(CSP2ChocoParser.MulDivContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 */
	void enterAddSub(CSP2ChocoParser.AddSubContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 */
	void exitAddSub(CSP2ChocoParser.AddSubContext ctx);
	/**
	 * Enter a parse tree produced by the {@code And}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 */
	void enterAnd(CSP2ChocoParser.AndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code And}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 */
	void exitAnd(CSP2ChocoParser.AndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code id}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 */
	void enterId(CSP2ChocoParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by the {@code id}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 */
	void exitId(CSP2ChocoParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Implication}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 */
	void enterImplication(CSP2ChocoParser.ImplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Implication}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 */
	void exitImplication(CSP2ChocoParser.ImplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Minus}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 */
	void enterMinus(CSP2ChocoParser.MinusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Minus}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 */
	void exitMinus(CSP2ChocoParser.MinusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code int}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 */
	void enterInt(CSP2ChocoParser.IntContext ctx);
	/**
	 * Exit a parse tree produced by the {@code int}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 */
	void exitInt(CSP2ChocoParser.IntContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Comparation}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 */
	void enterComparation(CSP2ChocoParser.ComparationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Comparation}
	 * labeled alternative in {@link CSP2ChocoParser}.
	 * @param ctx the parse tree
	 */
	void exitComparation(CSP2ChocoParser.ComparationContext ctx);
}