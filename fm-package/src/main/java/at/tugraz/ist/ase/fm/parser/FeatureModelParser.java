/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.parser;

import at.tugraz.ist.ase.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.fm.core.Feature;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import lombok.NonNull;

import java.io.File;

/**
 * An interface for all feature model parsers
 */
public interface FeatureModelParser<F extends Feature, R extends AbstractRelationship<F>> {
    /**
     * Checks the format of a feature model file.
     *
     * @param filePath - a {@link File}
     * @return true - if the feature model file has the same format with the parser
     *         false - otherwise
     */
    boolean checkFormat(@NonNull File filePath);

    /**
     * Parses the feature model file into a {@link FeatureModel}.
     *
     * @param filePath - a {@link File}
     * @return a {@link FeatureModel}
     * @throws FeatureModelParserException - a PaserException
     */
    FeatureModel<F, R> parse(@NonNull File filePath) throws FeatureModelParserException;

    void dispose();
}

