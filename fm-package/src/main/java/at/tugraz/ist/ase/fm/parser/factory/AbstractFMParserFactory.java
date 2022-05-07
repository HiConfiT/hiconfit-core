/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.parser.factory;

import at.tugraz.ist.ase.fm.parser.FMFormat;
import at.tugraz.ist.ase.fm.parser.FeatureModelParser;

public abstract class AbstractFMParserFactory {
    abstract FeatureModelParser getParser(FMFormat fmFormat);
}
