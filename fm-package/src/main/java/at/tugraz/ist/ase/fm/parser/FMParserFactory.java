/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.parser;

import at.tugraz.ist.ase.fm.builder.IFeatureBuildable;
import at.tugraz.ist.ase.fm.builder.IRelationshipBuildable;
import lombok.NonNull;

public class FMParserFactory {

    private IFeatureBuildable featureBuilder;
    private IRelationshipBuildable relationshipBuilder;

    private FMParserFactory(@NonNull IFeatureBuildable featureBuilder, @NonNull IRelationshipBuildable relationshipBuilder) {
        this.featureBuilder = featureBuilder;
        this.relationshipBuilder = relationshipBuilder;
    }

    public static FMParserFactory getInstance(@NonNull IFeatureBuildable featureBuilder, @NonNull IRelationshipBuildable relationshipBuilder){
        return new FMParserFactory(featureBuilder, relationshipBuilder);
    }

    public FeatureModelParser getParser(@NonNull FMFormat fmFormat) {
        return switch (fmFormat) {
            case SXFM -> new SXFMParser<>(featureBuilder, relationshipBuilder);
            case FEATUREIDE -> new FeatureIDEParser<>(featureBuilder, relationshipBuilder);
            case GLENCOE -> new GLENCOEParser<>(featureBuilder, relationshipBuilder);
            case XMI -> new XMIParser<>(featureBuilder, relationshipBuilder);
            case DESCRIPTIVE -> new DescriptiveFormatParser<>(featureBuilder, relationshipBuilder);
            default -> throw new IllegalArgumentException("Unsupported feature model format: " + fmFormat);
        };
    }

    public FeatureModelParser getParser(@NonNull String filename) {
        return getParser(FMFormat.getFMFormat(filename));
    }
}
