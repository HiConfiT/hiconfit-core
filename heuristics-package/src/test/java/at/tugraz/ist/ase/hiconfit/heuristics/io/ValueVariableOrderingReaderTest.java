/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.heuristics.io;

import at.tugraz.ist.ase.hiconfit.common.IOUtils;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.Feature;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import at.tugraz.ist.ase.hiconfit.fm.parser.FMParserFactory;
import at.tugraz.ist.ase.hiconfit.fm.parser.FeatureModelParser;
import at.tugraz.ist.ase.hiconfit.fm.parser.FeatureModelParserException;
import at.tugraz.ist.ase.hiconfit.heuristics.ValueVariableOrdering;
import at.tugraz.ist.ase.hiconfit.kb.camera.CameraKB;
import at.tugraz.ist.ase.hiconfit.kb.fm.FMKB;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Cleanup;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class ValueVariableOrderingReaderTest {

    private final static CameraKB cameraKB = new CameraKB(false);

    private static FMKB<Feature, AbstractRelationship<Feature>, CTConstraint> kb;
    private static FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel;

    @Test
    void testCameraKB() throws IOException, CsvValidationException {
        InputStream is = IOUtils.getInputStream(this.getClass().getClassLoader(), "vvo_camera.csv");

        ValueVariableOrderingReader reader = new ValueVariableOrderingReader();
        ValueVariableOrdering vvo = reader.read(is, cameraKB);

        System.out.println(vvo);

        assertNotNull(vvo);
        assertAll(
                () -> assertEquals(2, vvo.getVarOrdering().size()),
                () -> assertEquals(2, vvo.getIntVarOrdering().size()),
                () -> assertEquals(2, vvo.getValueOrdering().size()),
                () -> assertEquals("Display", vvo.getVarOrdering().get(0)),
                () -> assertEquals("Resolution", vvo.getVarOrdering().get(1)),
                () -> assertEquals("Display", vvo.getIntVarOrdering().get(0).getName()),
                () -> assertEquals("Resolution", vvo.getIntVarOrdering().get(1).getName()),
                () -> assertEquals("Display", vvo.getValueOrdering().get(0).getVarName()),
                () -> assertEquals("Resolution", vvo.getValueOrdering().get(1).getVarName()),
                () -> assertEquals(25, vvo.getValueOrdering().get(0).getOrdering().get(0)),
                () -> assertEquals(27, vvo.getValueOrdering().get(0).getOrdering().get(1)),
                () -> assertEquals(30, vvo.getValueOrdering().get(0).getOrdering().get(2)),
                () -> assertEquals(32, vvo.getValueOrdering().get(0).getOrdering().get(3)),
                () -> assertEquals(18, vvo.getValueOrdering().get(0).getOrdering().get(4)),
                () -> assertEquals(102, vvo.getValueOrdering().get(1).getOrdering().get(0)),
                () -> assertEquals(123, vvo.getValueOrdering().get(1).getOrdering().get(1)),
                () -> assertEquals(142, vvo.getValueOrdering().get(1).getOrdering().get(2)),
                () -> assertEquals(162, vvo.getValueOrdering().get(1).getOrdering().get(3)),
                () -> assertEquals(241, vvo.getValueOrdering().get(1).getOrdering().get(4)),
                () -> assertEquals(242, vvo.getValueOrdering().get(1).getOrdering().get(5)),
                () -> assertEquals(208, vvo.getValueOrdering().get(1).getOrdering().get(6)),
                () -> assertEquals(209, vvo.getValueOrdering().get(1).getOrdering().get(7)),
                () -> assertEquals(243, vvo.getValueOrdering().get(1).getOrdering().get(8)),
                () -> assertEquals(363, vvo.getValueOrdering().get(1).getOrdering().get(9)),
                () -> assertEquals(61, vvo.getValueOrdering().get(1).getOrdering().get(10))
        );
    }

    @Test
    void testFMKB() throws FeatureModelParserException, IOException, CsvValidationException {
        // read the feature model
        File fileFM = new File("src/test/resources/pizzas.xml");

        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
        featureModel = parser.parse(fileFM);

        // convert the feature model into FMKB
        kb = new FMKB<>(featureModel, true);

        // read the value variable ordering
        InputStream is = IOUtils.getInputStream(this.getClass().getClassLoader(), "vvo_pizzas.csv");

        ValueVariableOrderingReader reader = new ValueVariableOrderingReader();
        ValueVariableOrdering vvo = reader.read(is, kb);

        System.out.println(vvo);

        assertNotNull(vvo);
        assertAll(
                () -> assertEquals(3, vvo.getVarOrdering().size()),
                () -> assertEquals(3, vvo.getIntVarOrdering().size()),
                () -> assertEquals(3, vvo.getValueOrdering().size()),
                () -> assertEquals("CheesyCrust", vvo.getVarOrdering().get(0)),
                () -> assertEquals("Neapolitan", vvo.getVarOrdering().get(1)),
                () -> assertEquals("CheesyCrust", vvo.getIntVarOrdering().get(0).getName()),
                () -> assertEquals("Neapolitan", vvo.getIntVarOrdering().get(1).getName()),
                () -> assertEquals("CheesyCrust", vvo.getValueOrdering().get(0).getVarName()),
                () -> assertEquals("Neapolitan", vvo.getValueOrdering().get(1).getVarName()),
                () -> assertEquals(1, vvo.getValueOrdering().get(0).getOrdering().get(0)),
                () -> assertEquals(0, vvo.getValueOrdering().get(0).getOrdering().get(1)),
                () -> assertEquals(0, vvo.getValueOrdering().get(1).getOrdering().get(0)),
                () -> assertEquals(1, vvo.getValueOrdering().get(1).getOrdering().get(1))
        );
    }
}