/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.csp2choco;

import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.csp2choco.antlr.CSP2ChocoBaseListener;
import at.tugraz.ist.ase.csp2choco.antlr.CSP2ChocoLexer;
import at.tugraz.ist.ase.csp2choco.antlr.CSP2ChocoParser;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;
import org.javatuples.Pair;

import java.io.IOException;
import java.io.InputStream;

import static at.tugraz.ist.ase.common.ChocoSolverUtils.getVariable;
import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class CSP2ChocoTranslator extends CSP2ChocoBaseListener {

    @Getter
    private final Model model;

    public CSP2ChocoTranslator(@NonNull Model model) {
        this.model = model;
    }

    // Utility functions
    public static void loadConstraints(@NonNull InputStream inputFile, @NonNull Model model) throws IOException {
        CSP2ChocoTranslator translator = new CSP2ChocoTranslator(model);

        translator.translate(inputFile); // translate the input file into a Model with constraints
    }

    public void translate(@NonNull InputStream inputFile) throws IOException {
        log.trace("{}Translating CSP to Choco model >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        CharStream input = CharStreams.fromStream(inputFile);
        CSP2ChocoLexer lexer = new CSP2ChocoLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CSP2ChocoParser parser = new CSP2ChocoParser(tokens);
        ParseTree tree = parser.model();

        // create a standard ANTLR parse tree walker
        ParseTreeWalker walker = new ParseTreeWalker();
        // feed to walker
        walker.walk(this, tree);        // walk parse tree

        LoggerUtils.outdent();
        log.debug("{}<<< Translated CSP to Choco model", LoggerUtils.tab());
    }

    public void exitConstraint(CSP2ChocoParser.ConstraintContext ctx) {
        String key = ctx.expr().getText();
        log.trace("{}Parsing the constraint [cstr={}] >>>", LoggerUtils.tab(), key);
        LoggerUtils.indent();

        ParserRuleContext leftContext = (ParserRuleContext) ctx.expr().getChild(0);
        TerminalNode opNode = (TerminalNode) ctx.expr().getChild(1);
        ParserRuleContext rightContext = (ParserRuleContext) ctx.expr().getChild(2);

        IntVar leftVar = leftContextTranslate(model, leftContext);
        String op = opNodeTranslate(opNode);
        Pair<IntVar, Integer> rightVar = rightContextTranslate(model, rightContext);

        Constraint constraint;
        if (rightVar.getValue0() != null) {
            constraint = model.arithm(leftVar, op, rightVar.getValue0());
        } else {
            constraint = model.arithm(leftVar, op, rightVar.getValue1());
        }
        constraint.post();

        LoggerUtils.outdent();
        log.debug("{}<<< Parsed the constraint [cstr={}, choco_cstr={}]", LoggerUtils.tab(), key, constraint);
    }

    private String opNodeTranslate(TerminalNode node) {
        String op = CSP2ChocoParser._LITERAL_NAMES[node.getSymbol().getType()];
        op = op.substring(1, op.length() - 1);

        if (op.equals("==")) op = "=";

        return op;
    }

    private IntVar leftContextTranslate(Model model, ParserRuleContext leftContext) {
        checkArgument(leftContext instanceof CSP2ChocoParser.IdContext, "{}Unsupported context [context={}]", LoggerUtils.tab(), leftContext);

        CSP2ChocoParser.IdContext id = (CSP2ChocoParser.IdContext) leftContext;
        return (IntVar) getVariable(model, id.getText());
    }

    private Pair<IntVar, Integer> rightContextTranslate(Model model, ParserRuleContext rightContext) {
        checkArgument(rightContext instanceof CSP2ChocoParser.IntContext || rightContext instanceof CSP2ChocoParser.IdContext,
                "{}Unsupported context [context={}]", LoggerUtils.tab(), rightContext);

        if (rightContext instanceof CSP2ChocoParser.IdContext id) {
            return Pair.with((IntVar) getVariable(model, id.getText()), Integer.MIN_VALUE);
        } else {
            CSP2ChocoParser.IntContext valueContext = (CSP2ChocoParser.IntContext) rightContext;
            return Pair.with(null, Integer.parseInt(valueContext.getText()));
        }
    }
}