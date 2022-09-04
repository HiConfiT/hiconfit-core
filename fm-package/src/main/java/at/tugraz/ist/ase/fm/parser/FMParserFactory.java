/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.parser;

import at.tugraz.ist.ase.fm.builder.IConstraintBuildable;
import at.tugraz.ist.ase.fm.builder.IFeatureBuildable;
import at.tugraz.ist.ase.fm.builder.IRelationshipBuildable;
import lombok.NonNull;

public class FMParserFactory {

    private IFeatureBuildable featureBuilder;
    private IRelationshipBuildable relationshipBuilder;
    private IConstraintBuildable constraintBuilder;

    private FMParserFactory(@NonNull IFeatureBuildable featureBuilder,
                            @NonNull IRelationshipBuildable relationshipBuilder,
                            @NonNull IConstraintBuildable constraintBuilder) {
        this.featureBuilder = featureBuilder;
        this.relationshipBuilder = relationshipBuilder;
        this.constraintBuilder = constraintBuilder;
    }

    public static FMParserFactory getInstance(@NonNull IFeatureBuildable featureBuilder,
                                              @NonNull IRelationshipBuildable relationshipBuilder,
                                              @NonNull IConstraintBuildable constraintBuilder) {
        return new FMParserFactory(featureBuilder, relationshipBuilder, constraintBuilder);
    }

    public FeatureModelParser getParser(@NonNull FMFormat fmFormat) {
        return switch (fmFormat) {
            case SXFM -> new SXFMParser<>(featureBuilder, relationshipBuilder, constraintBuilder);
            case FEATUREIDE -> new FeatureIDEParser<>(featureBuilder, relationshipBuilder, constraintBuilder);
            case GLENCOE -> new GLENCOEParser<>(featureBuilder, relationshipBuilder, constraintBuilder);
            case XMI -> new XMIParser<>(featureBuilder, relationshipBuilder, constraintBuilder);
            case DESCRIPTIVE -> new DescriptiveFormatParser<>(featureBuilder, relationshipBuilder, constraintBuilder);
            default -> throw new IllegalArgumentException("Unsupported feature model format: " + fmFormat);
        };
    }

    public FeatureModelParser getParser(@NonNull String filename) {
        return getParser(FMFormat.getFMFormat(filename));
    }
}
