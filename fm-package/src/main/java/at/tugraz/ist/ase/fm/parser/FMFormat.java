/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.parser;

/**
 * An enum of types of feature model formats.
 */
public enum FMFormat {
    NONE,
    SXFM, // SPLOT format
    FEATUREIDE, // FeatureIDE format
    XMI, // v.control format
    GLENCOE, // Glencoe format
    DESCRIPTIVE; // my format

    public static FMFormat getFMFormat(String format) {
        return switch (format) {
            case "sxfm", "splx" -> FMFormat.SXFM;
            case "xml" -> FMFormat.FEATUREIDE;
            case "xmi" -> FMFormat.XMI;
            case "json" -> FMFormat.GLENCOE;
            case "fm4conf" -> FMFormat.DESCRIPTIVE;
            default -> FMFormat.NONE;
        };
    }
}
