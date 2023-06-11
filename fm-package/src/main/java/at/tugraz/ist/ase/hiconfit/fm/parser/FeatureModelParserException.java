/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fm.parser;

/**
 * An exception for errors which occur in parsing feature model files
 */
public class FeatureModelParserException extends Exception {

    public FeatureModelParserException(String message) {
        super(message);
    }

    public FeatureModelParserException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

