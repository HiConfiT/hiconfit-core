/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fm.parser;

import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import at.tugraz.ist.ase.hiconfit.fm.builder.IConstraintBuildable;
import at.tugraz.ist.ase.hiconfit.fm.builder.IFeatureBuildable;
import at.tugraz.ist.ase.hiconfit.fm.builder.IRelationshipBuildable;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.Feature;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import at.tugraz.ist.ase.hiconfit.fm.core.ast.ASTNode;
import at.tugraz.ist.ase.hiconfit.fm.parser.fm4conf.FM4ConfBaseListener;
import at.tugraz.ist.ase.hiconfit.fm.parser.fm4conf.FM4ConfLexer;
import at.tugraz.ist.ase.hiconfit.fm.parser.fm4conf.FM4ConfParser;
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
 * <p>
 * Supports only requires and excludes constraints
 */
@Beta
@Slf4j
public class DescriptiveFormatParser<F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint> extends FM4ConfBaseListener implements FeatureModelParser<F, R, C> {

    public static final String FILE_EXTENSION = ".fm4conf";

    private FeatureModel<F, R, C> fm;
    private ParseTree tree;

    private IFeatureBuildable featureBuilder;
    private IRelationshipBuildable relationshipBuilder;
    private IConstraintBuildable constraintBuilder;

    public DescriptiveFormatParser(@NonNull IFeatureBuildable featureBuilder,
                                   @NonNull IRelationshipBuildable relationshipBuilder,
                                   @NonNull IConstraintBuildable constraintBuilder) {
        this.featureBuilder = featureBuilder;
        this.relationshipBuilder = relationshipBuilder;
        this.constraintBuilder = constraintBuilder;
    }

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
        checkArgument(filePath.getName().endsWith(FILE_EXTENSION), "The file is not in a Descriptive format!");

        // second, check the structure of file
        try {
            // use ANTLR4 to parse, if it raises an exception
            InputStream is = new FileInputStream(filePath);

            CharStream input = CharStreams.fromStream(is);
            FM4ConfLexer lexer = new FM4ConfLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            FM4ConfParser parser = new FM4ConfParser(tokens);
            tree = parser.model();
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
    public FeatureModel<F, R, C> parse(@NonNull File filePath) throws FeatureModelParserException {
        checkArgument(checkFormat(filePath), "The format of file is not a Descriptive format or there are errors in the file!");

        log.trace("{}Parsing the feature model file [file={}] >>>", LoggerUtils.tab(), filePath.getName());
        LoggerUtils.indent();

        // create a standard ANTLR parse tree walker
        ParseTreeWalker walker = new ParseTreeWalker();
        // feed to walker
        fm = new FeatureModel<>(filePath.getName(), featureBuilder, relationshipBuilder, constraintBuilder);

        walker.walk(this, tree);        // walk parse tree

        LoggerUtils.outdent();
        log.debug("{}<<< Parsed feature model [file={}, fm={}]", LoggerUtils.tab(), filePath.getName(), fm);
        return fm;
    }

    @Override
    public void exitFeature(FM4ConfParser.FeatureContext ctx) {
        List<FM4ConfParser.IdentifierContext> ids = ctx.identifier();
        ids.forEach(idCx -> {
            if (!fm.hasRoot()) {
                fm.addRoot(idCx.getText(), idCx.getText());
            } else {
                fm.addFeature(idCx.getText(), idCx.getText());
            }
        });
    }

    @Override
    public void exitMandatory(FM4ConfParser.MandatoryContext ctx) {
        try {
            F parent = getParent(ctx.identifier());
            List<F> children = getChildren(ctx.identifier());

            fm.addMandatoryRelationship(parent, children.get(0));
        } catch (Exception e) {
            log.error("{}Error while adding mandatory relationship [relationship={}]", LoggerUtils.tab(), ctx.getText());
        }
    }

    @Override
    public void exitOptional(FM4ConfParser.OptionalContext ctx) {
        try {
            F parent = getParent(ctx.identifier());
            List<F> children = getChildren(ctx.identifier());

            fm.addOptionalRelationship(parent, children.get(0));
        } catch (Exception e) {
            log.error("{}Error while adding optional relationship [relationship={}]", LoggerUtils.tab(), ctx.getText());
        }
    }

    @Override
    public void exitAlternative(FM4ConfParser.AlternativeContext ctx) {
        try {
            F parent = getParent(ctx.identifier());
            List<F> children = getChildren(ctx.identifier());

            fm.addAlternativeRelationship(parent, children);
        } catch (Exception e) {
            log.error("{}Error while adding alternative relationship [relationship={}]", LoggerUtils.tab(), ctx.getText());
        }
    }

    @Override
    public void exitOr(FM4ConfParser.OrContext ctx) {
        try {
            F parent = getParent(ctx.identifier());
            List<F> children = getChildren(ctx.identifier());

            fm.addOrRelationship(parent, children);
        } catch (Exception e) {
            log.error("{}Error while adding or relationship [relationship={}]", LoggerUtils.tab(), ctx.getText());
        }
    }

    @Override
    public void exitRequires(FM4ConfParser.RequiresContext ctx) {
        try {
            F parent = getParent(ctx.identifier());
            List<F> children = getChildren(ctx.identifier());

            ASTNode left = constraintBuilder.buildOperand(parent);
            ASTNode right = constraintBuilder.buildOperand(children.get(0));

            ASTNode formula = constraintBuilder.buildRequires(left, right);

            fm.addConstraint(constraintBuilder.buildConstraint(formula));
        } catch (Exception e) {
            log.error("{}Error while adding requires constraint [constraint={}]", LoggerUtils.tab(), ctx.getText());
        }
    }

    @Override
    public void exitExcludes(FM4ConfParser.ExcludesContext ctx) {
        try {
            F parent = getParent(ctx.identifier());
            List<F> children = getChildren(ctx.identifier());

            ASTNode left = constraintBuilder.buildOperand(parent);
            ASTNode right = constraintBuilder.buildOperand(children.get(0));

            ASTNode formula = constraintBuilder.buildExcludes(left, right);

            fm.addConstraint(constraintBuilder.buildConstraint(formula));
        } catch (Exception e) {
            log.error("{}Error while adding excludes constraint [constraint={}]", LoggerUtils.tab(), ctx.getText());
        }
    }

    private F getParent(List<FM4ConfParser.IdentifierContext> ids) {
        F parent = null;
        if (ids.size() > 1) {
            parent = fm.getFeature(ids.get(0).getText());
        }
        return parent;
    }

    private List<F> getChildren(List<FM4ConfParser.IdentifierContext> ids) {
        List<F> children = new LinkedList<>();
        for (int i = 1; i < ids.size(); i++) {
            children.add(fm.getFeature(ids.get(i).getText()));
        }
        return children;
    }

    public void dispose() {
        fm = null;
        tree = null;
        featureBuilder = null;
        relationshipBuilder = null;
        constraintBuilder = null;
    }
}