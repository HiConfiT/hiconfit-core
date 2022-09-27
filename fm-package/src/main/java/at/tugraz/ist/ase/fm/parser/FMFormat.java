/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.parser;

import lombok.NonNull;

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

    public boolean isValid() {
        return this != NONE;
    }

    public static FMFormat getFMFormat(@NonNull String filename) {
        String format = getFMFormatString(filename);
        return switch (format) {
            case "sxfm", "splx" -> FMFormat.SXFM;
            case "xml" -> FMFormat.FEATUREIDE;
            case "xmi" -> FMFormat.XMI;
            case "json" -> FMFormat.GLENCOE;
            case "fm4conf" -> FMFormat.DESCRIPTIVE;
            default -> FMFormat.NONE;
        };
    }

    private static String getFMFormatString(@NonNull String filename) {
        if (filename.endsWith(".sxfm") || filename.endsWith(".splx")) {
            return "sxfm";
        } else if (filename.endsWith(".xml")) {
            return "xml";
        } else if (filename.endsWith(".xmi")) {
            return "xmi";
        } else if (filename.endsWith(".json")) {
            return "json";
        } else if (filename.endsWith(".fm4conf")) {
            return "fm4conf";
        }
        return "";
    }
}
