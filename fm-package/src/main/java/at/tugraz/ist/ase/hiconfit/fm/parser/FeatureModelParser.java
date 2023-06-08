/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fm.parser;

import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.Feature;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import lombok.NonNull;

import java.io.File;

/**
 * An interface for all feature model parsers
 */
public interface FeatureModelParser<F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint> {
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
     * @throws FeatureModelParserException - a ParserException
     */
    FeatureModel<F, R, C> parse(@NonNull File filePath) throws FeatureModelParserException;

    void dispose();
}
