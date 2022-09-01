/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.parser;

import lombok.NonNull;

public class FMParserFactory {

    private static final FMParserFactory instance = new FMParserFactory();

    private FMParserFactory() {}

    public static FMParserFactory getInstance(){
        return instance;
    }

    public FeatureModelParser getParser(@NonNull FMFormat fmFormat) {
        return switch (fmFormat) {
            case SXFM -> new SXFMParser();
            case FEATUREIDE -> new FeatureIDEParser();
            case GLENCOE -> new GLENCOEParser();
            case XMI -> new XMIParser();
            case DESCRIPTIVE -> new DescriptiveFormatParser();
            default -> throw new IllegalArgumentException("Unsupported feature model format: " + fmFormat);
        };
    }

    public FeatureModelParser getParser(@NonNull String filename) {
        return getParser(FMFormat.getFMFormat(filename));
    }
}
