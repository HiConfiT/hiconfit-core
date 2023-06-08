/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.kb.fm;

import at.tugraz.ist.ase.hiconfit.fm.builder.ConstraintBuilder;
import at.tugraz.ist.ase.hiconfit.fm.builder.FeatureBuilder;
import at.tugraz.ist.ase.hiconfit.fm.builder.RelationshipBuilder;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.Feature;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import at.tugraz.ist.ase.hiconfit.fm.parser.FeatureIDEParser;
import at.tugraz.ist.ase.hiconfit.fm.parser.FeatureModelParserException;
import at.tugraz.ist.ase.hiconfit.fm.translator.ConfRuleTranslator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FMKBTest1 {

    static FeatureBuilder featureBuilder = new FeatureBuilder();
    static ConfRuleTranslator confRuleTranslator = new ConfRuleTranslator();
    static RelationshipBuilder relationshipBuilder = new RelationshipBuilder(confRuleTranslator);
    static ConstraintBuilder constraintBuilder = new ConstraintBuilder(confRuleTranslator);

    @Test
    void testLinux() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/linux-2.6.33.3_simple.xml");

        FeatureIDEParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = new FeatureIDEParser<>(featureBuilder, relationshipBuilder, constraintBuilder);
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(fileFM);

        FMKB<Feature, AbstractRelationship<Feature>, CTConstraint> kb = new FMKB<>(featureModel, true);

        Assertions.assertEquals(kb.getNumVariables(), 6467);
        assertEquals(featureModel.getNumOfRelationships(), 6322);
        assertEquals(featureModel.getNumOfConstraints(), 9);
        Assertions.assertEquals(kb.getNumConstraints(), 6331);

//        kb.getConstraintList().forEach(System.out::println);
    }

    @Test
    void testLinux1() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/linux-2.6.33.3.xml");
        FeatureIDEParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = new FeatureIDEParser<>(featureBuilder, relationshipBuilder, constraintBuilder);
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(fileFM);

        FMKB<Feature, AbstractRelationship<Feature>, CTConstraint> kb = new FMKB<>(featureModel, true);

        Assertions.assertEquals(kb.getNumVariables(), 6467);
        Assertions.assertEquals(kb.getNumConstraints(), 13972);

//        kb.getConstraintList().forEach(System.out::println);
    }
}