/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fm.factory;

import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.Feature;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import at.tugraz.ist.ase.hiconfit.fm.parser.FMParserFactory;
import at.tugraz.ist.ase.hiconfit.fm.parser.FeatureModelParserException;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.io.File;

@UtilityClass
public class FeatureModels {

    public <F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint> FeatureModel<F, R, C>
        fromFile(@NonNull File fileFM) throws FeatureModelParserException {
        // create the factory for feature models
        val factory = FMParserFactory.getInstance();

        // create the parser
        @Cleanup("dispose") val parser = factory.getParser(fileFM.getName());
        return (FeatureModel<F, R, C>) parser.parse(fileFM); // parse the feature model
    }

}
