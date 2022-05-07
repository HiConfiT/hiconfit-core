/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.parser.factory;

import at.tugraz.ist.ase.fm.parser.*;

public class FMParserFactory extends AbstractFMParserFactory {

    private static final FMParserFactory instance = new FMParserFactory();

    private FMParserFactory() {}

    public static FMParserFactory getInstance(){
        return instance;
    }

    @Override
    public FeatureModelParser getParser(FMFormat fmFormat) {
        return switch (fmFormat) {
            case SXFM -> new SXFMParser();
            case FEATUREIDE -> new FeatureIDEParser();
            case GLENCOE -> new GLENCOEParser();
            case XMI -> new XMIParser();
            case DESCRIPTIVE -> new DescriptiveFormatParser();
            default -> throw new IllegalArgumentException("Unsupported feature model format: " + fmFormat);
        };
    }
}
