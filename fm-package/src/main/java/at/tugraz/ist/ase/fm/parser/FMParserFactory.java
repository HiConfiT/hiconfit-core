/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.parser;

import at.tugraz.ist.ase.fm.builder.*;
import at.tugraz.ist.ase.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.fm.core.CTConstraint;
import at.tugraz.ist.ase.fm.core.Feature;
import at.tugraz.ist.ase.fm.translator.ConfRuleTranslator;
import lombok.NonNull;

/**
 * Factory for creating a feature model parser.
 * @param <F> Type of feature
 * @param <R> Type of relationship
 * @param <C> Type of constraint
 */
public class FMParserFactory<F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint> {

    private final IFeatureBuildable featureBuilder; // Builder for creating features
    private final IRelationshipBuildable relationshipBuilder; // Builder for creating relationships
    private final IConstraintBuildable constraintBuilder; // Builder for creating constraints

    private FMParserFactory(@NonNull IFeatureBuildable featureBuilder,
                            @NonNull IRelationshipBuildable relationshipBuilder,
                            @NonNull IConstraintBuildable constraintBuilder) {
        this.featureBuilder = featureBuilder;
        this.relationshipBuilder = relationshipBuilder;
        this.constraintBuilder = constraintBuilder;
    }

    public static <F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint>
    FMParserFactory<F, R, C> getInstance(@NonNull IFeatureBuildable featureBuilder,
                                      @NonNull IRelationshipBuildable relationshipBuilder,
                                      @NonNull IConstraintBuildable constraintBuilder) {
        return new FMParserFactory<>(featureBuilder, relationshipBuilder, constraintBuilder);
    }

    public static <F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint>
    FMParserFactory<F, R, C> getInstance(@NonNull IFeatureBuildable featureBuilder) {
        ConfRuleTranslator translator = new ConfRuleTranslator();
        return getInstance(featureBuilder,
                           new RelationshipBuilder(translator),
                           new ConstraintBuilder(translator));
    }

    /**
     * Creates a feature model parser with built-in builders.
     * @return a feature model parser
     * @param <F> Type of feature
     * @param <R> Type of relationship
     * @param <C> Type of constraint
     */
    public static <F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint>
    FMParserFactory<F, R, C> getInstance() {
        return getInstance(new FeatureBuilder());
    }

    public FeatureModelParser<F, R, C> getParser(@NonNull FMFormat fmFormat) {
        return switch (fmFormat) {
            case SXFM -> new SXFMParser<>(featureBuilder, relationshipBuilder, constraintBuilder);
            case FEATUREIDE -> new FeatureIDEParser<>(featureBuilder, relationshipBuilder, constraintBuilder);
            case GLENCOE -> new GLENCOEParser<>(featureBuilder, relationshipBuilder, constraintBuilder);
            case XMI -> new XMIParser<>(featureBuilder, relationshipBuilder, constraintBuilder);
            case DESCRIPTIVE -> new DescriptiveFormatParser<>(featureBuilder, relationshipBuilder, constraintBuilder);
            default -> throw new IllegalArgumentException("Unsupported feature model format: " + fmFormat);
        };
    }

    public FeatureModelParser<F, R, C> getParser(@NonNull String filename) {
        return getParser(FMFormat.getFMFormat(filename));
    }
}
