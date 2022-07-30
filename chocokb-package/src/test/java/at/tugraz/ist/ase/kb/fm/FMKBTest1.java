/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.kb.fm;

import at.tugraz.ist.ase.fm.core.FeatureModel;
import at.tugraz.ist.ase.fm.parser.FeatureIDEParser;
import at.tugraz.ist.ase.fm.parser.FeatureModelParserException;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FMKBTest1 {
    @Test
    void testLinux() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/linux-2.6.33.3_simple.xml");
        FeatureIDEParser parser = new FeatureIDEParser();
        FeatureModel featureModel = parser.parse(fileFM);

        FMKB kb = new FMKB(featureModel, true);

        assertEquals(kb.getNumVariables(), 6467);
        assertEquals(featureModel.getNumOfRelationships(), 6322);
        assertEquals(featureModel.getNumOfConstraints(), 9);
        assertEquals(kb.getNumConstraints(), 6331);

//        kb.getConstraintList().forEach(System.out::println);
    }

    @Test
    void testLinux1() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/linux-2.6.33.3.xml");
        FeatureIDEParser parser = new FeatureIDEParser();
        FeatureModel featureModel = parser.parse(fileFM);

        FMKB kb = new FMKB(featureModel, true);

        assertEquals(kb.getNumVariables(), 6467);
        assertEquals(kb.getNumConstraints(), 13972);

//        kb.getConstraintList().forEach(System.out::println);
    }
}
