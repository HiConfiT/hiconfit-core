/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.parser;

import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.fm.core.Feature;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import at.tugraz.ist.ase.fm.core.FeatureModelException;
import at.tugraz.ist.ase.fm.core.RelationshipType;
import at.tugraz.ist.ase.fm.parser.fm4conf.FM4ConfBaseListener;
import at.tugraz.ist.ase.fm.parser.fm4conf.FM4ConfLexer;
import at.tugraz.ist.ase.fm.parser.fm4conf.FM4ConfParser;
import com.google.common.annotations.Beta;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A parser for the descriptive format
 */
@Beta
@Slf4j
public class DescriptiveFormatParser extends FM4ConfBaseListener implements FeatureModelParser {

    private FeatureModel featureModel;

    /**
     * Check whether the format of the given file is Descriptive format
     *
     * @param filePath - a {@link File}
     * @return true - if the format of the given file is Descriptive format
     *          false - otherwise
     */
    @Override
    public boolean checkFormat(@NonNull File filePath) {
        // first, check the extension of file
        checkArgument(filePath.getName().endsWith(".fm4conf"), "The file is not in a Descriptive format!");

        // second, check the structure of file
        try {
            // use ANTLR4 to parse, if it raises an exception
            InputStream is = new FileInputStream(filePath);

            CharStream input = CharStreams.fromStream(is);
            FM4ConfLexer lexer = new FM4ConfLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            FM4ConfParser parser = new FM4ConfParser(tokens);
            parser.model();
        } catch (IOException e) {
            return false; // it's not Descriptive format
        }
        return true;
    }

    /**
     * This function parse the given {@link File} into a {@link FeatureModel}.
     *
     * @param filePath - a {@link File}
     * @return a {@link FeatureModel}
     * @throws FeatureModelParserException when error occurs in parsing
     */
    @Override
    public FeatureModel parse(@NonNull File filePath) throws FeatureModelParserException {
        checkArgument(checkFormat(filePath), "The format of file is not a Descriptive format or there are errors in the file!");

        log.trace("{}Parsing the feature model file [file={}] >>>", LoggerUtils.tab, filePath.getName());
        LoggerUtils.indent();

        try {
            InputStream is = new FileInputStream(filePath);

            CharStream input = CharStreams.fromStream(is);
            FM4ConfLexer lexer = new FM4ConfLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            FM4ConfParser parser = new FM4ConfParser(tokens);
            ParseTree tree = parser.model();

            // create a standard ANTLR parse tree walker
            ParseTreeWalker walker = new ParseTreeWalker();
            // feed to walker
            featureModel = new FeatureModel();
            featureModel.setName(filePath.getName());

            walker.walk(this, tree);        // walk parse tree
        } catch (IOException e) {
            throw new FeatureModelParserException(e.getMessage());
        }

        LoggerUtils.outdent();
        log.debug("{}<<< Parsed feature model [file={}, fm={}]", LoggerUtils.tab, filePath.getName(), featureModel);
        return featureModel;
    }

    @Override
    public void exitFeature(FM4ConfParser.FeatureContext ctx) {
        List<FM4ConfParser.IdentifierContext> ids = ctx.identifier();
        for (FM4ConfParser.IdentifierContext idCx : ids) {
            featureModel.addFeature(idCx.getText(), idCx.getText());
        }
    }

    @Override
    public void exitMandatory(FM4ConfParser.MandatoryContext ctx) {
        try {
            addRelationship(RelationshipType.MANDATORY, ctx.identifier());
        } catch (FeatureModelException e) {
            log.error("{}Error while adding mandatory relationship [relationship={}]", LoggerUtils.tab, ctx.getText());
        }
    }

    @Override
    public void exitOptional(FM4ConfParser.OptionalContext ctx) {
        try {
            addRelationship(RelationshipType.OPTIONAL, ctx.identifier());
        } catch (FeatureModelException e) {
            log.error("{}Error while adding optional relationship [relationship={}]", LoggerUtils.tab, ctx.getText());
        }
    }

    @Override
    public void exitAlternative(FM4ConfParser.AlternativeContext ctx) {
        try {
            addRelationship(RelationshipType.ALTERNATIVE, ctx.identifier());
        } catch (FeatureModelException e) {
            log.error("{}Error while adding alternative relationship [relationship={}]", LoggerUtils.tab, ctx.getText());
        }
    }

    @Override
    public void exitOr(FM4ConfParser.OrContext ctx) {
        try {
            addRelationship(RelationshipType.OR, ctx.identifier());
        } catch (FeatureModelException e) {
            log.error("{}Error while adding or relationship [relationship={}]", LoggerUtils.tab, ctx.getText());
        }
    }

    @Override
    public void exitRequires(FM4ConfParser.RequiresContext ctx) {
        try {
            addConstraint(RelationshipType.REQUIRES, ctx.identifier());
        } catch (FeatureModelException e) {
            log.error("{}Error while adding requires constraint [constraint={}]", LoggerUtils.tab, ctx.getText());
        }
    }

    @Override
    public void exitExcludes(FM4ConfParser.ExcludesContext ctx) {
        try {
            addConstraint(RelationshipType.EXCLUDES, ctx.identifier());
        } catch (FeatureModelException e) {
            log.error("{}Error while adding excludes constraint [constraint={}]", LoggerUtils.tab, ctx.getText());
        }
    }

    private void addRelationship(RelationshipType type, List<FM4ConfParser.IdentifierContext> ids) throws FeatureModelException {
        Feature leftSide = featureModel.getFeature(ids.get(0).getText());
        List<Feature> rightSide = new LinkedList<>();
        for (int i = 1; i < ids.size(); i++) {
            rightSide.add(featureModel.getFeature(ids.get(i).getText()));
        }

        featureModel.addRelationship(type, leftSide, rightSide);
    }

    private void addConstraint(RelationshipType type, List<FM4ConfParser.IdentifierContext> ids) throws FeatureModelException {
        Feature leftSide = featureModel.getFeature(ids.get(0).getText());
        List<Feature> rightSide = new LinkedList<>();
        for (int i = 1; i < ids.size(); i++) {
            rightSide.add(featureModel.getFeature(ids.get(i).getText()));
        }

        featureModel.addConstraint(type, leftSide, rightSide);
    }
}
