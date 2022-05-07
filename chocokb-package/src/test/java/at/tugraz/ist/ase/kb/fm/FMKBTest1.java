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

public class FMKBTest1 {
    @Test
    void testLinux() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/linux-2.6.33.3_simple.xml");
        FeatureIDEParser parser = new FeatureIDEParser();
        FeatureModel featureModel = parser.parse(fileFM);

        FMKB kb = new FMKB(featureModel, true);

        kb.getConstraintList().forEach(System.out::println);
    }

    @Test
    void testLinux1() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/linux-2.6.33.3.xml");
        FeatureIDEParser parser = new FeatureIDEParser();
        FeatureModel featureModel = parser.parse(fileFM);

        FMKB kb = new FMKB(featureModel, true);

        kb.getConstraintList().forEach(System.out::println);
    }
}
