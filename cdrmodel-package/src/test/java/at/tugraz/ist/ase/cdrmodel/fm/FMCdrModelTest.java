/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cdrmodel.fm;

import at.tugraz.ist.ase.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.fm.core.CTConstraint;
import at.tugraz.ist.ase.fm.core.Feature;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import at.tugraz.ist.ase.fm.parser.FMParserFactory;
import at.tugraz.ist.ase.fm.parser.FeatureModelParser;
import at.tugraz.ist.ase.fm.parser.FeatureModelParserException;
import at.tugraz.ist.ase.kb.core.Constraint;
import lombok.Cleanup;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FMCdrModelTest {

    @Test
    void testRootTrue_CFInConflictsTrue() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/FM_10_0.splx");
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(fileFM);

        FMCdrModel<Feature, AbstractRelationship<Feature>, CTConstraint> model = new FMCdrModel<>(featureModel, true, true, true, true);
        model.initialize();

        assertAll(() -> assertEquals(1, model.getCorrectConstraints().size()),
                () -> {
                    List<Constraint> constraints = model.getCorrectConstraints().stream().toList();
                    assertEquals(1, constraints.get(0).getChocoConstraints().size());
                    assertEquals("ARITHM ([FM_10_0 = 1])", constraints.get(0).getChocoConstraints().get(0).toString());
                },
                () -> assertEquals(8, model.getPossiblyFaultyConstraints().size()),
                ()-> {
                    List<Constraint> constraints = model.getPossiblyFaultyConstraints().stream().toList();
                    assertEquals(1, constraints.get(7).getChocoConstraints().size());
                    assertEquals("ARITHM ([FM_10_0 + not(F1) >= 1])", constraints.get(7).getChocoConstraints().get(0).toString());
                    assertEquals(2, constraints.get(6).getChocoConstraints().size());
                    assertEquals("ARITHM ([FM_10_0 + not(F2) >= 1])", constraints.get(6).getChocoConstraints().get(0).toString());
                    assertEquals("ARITHM ([F2 + not(FM_10_0) >= 1])", constraints.get(6).getChocoConstraints().get(1).toString());
                    assertEquals(4, constraints.get(5).getChocoConstraints().size());
                    assertEquals("ARITHM ([FM_10_0 + not(F3) >= 1])", constraints.get(5).getChocoConstraints().get(0).toString());
                    assertEquals("ARITHM ([FM_10_0 + not(F4) >= 1])", constraints.get(5).getChocoConstraints().get(1).toString());
                    assertEquals("ARITHM ([FM_10_0 + not(F5) >= 1])", constraints.get(5).getChocoConstraints().get(2).toString());
                    assertEquals("SUM ([not(FM_10_0) + F5 + F4 + F3 >= 1])", constraints.get(5).getChocoConstraints().get(3).toString());
                    assertEquals(7, constraints.get(4).getChocoConstraints().size());
                    assertEquals("BV_1 = [0,1]=>ARITHM ([F6 = [0,1] + F7 = [0,1] = 1]), !BV_1 = [0,1]=>ARITHM ([PropNotEqualXY_C(F6, F7)])", constraints.get(4).getChocoConstraints().get(0).toString());
                    assertEquals("ARITHM ([FM_10_0 + not(F6) >= 1])", constraints.get(4).getChocoConstraints().get(2).toString());
                    assertEquals("ARITHM ([FM_10_0 + not(F7) >= 1])", constraints.get(4).getChocoConstraints().get(3).toString());
                    assertEquals("ARITHM ([not(FM_10_0) + BV_1 >= 1])", constraints.get(4).getChocoConstraints().get(4).toString());
                    assertEquals("ARITHM ([BV_1 + not(F6) >= 1])", constraints.get(4).getChocoConstraints().get(5).toString());
                    assertEquals("ARITHM ([BV_1 + not(F7) >= 1])", constraints.get(4).getChocoConstraints().get(6).toString());
                    assertEquals(1, constraints.get(3).getChocoConstraints().size());
                    assertEquals("ARITHM ([F2 + not(F8) >= 1])", constraints.get(3).getChocoConstraints().get(0).toString());
                    assertEquals(1, constraints.get(2).getChocoConstraints().size());
                    assertEquals("ARITHM ([F6 + not(F8) >= 1])", constraints.get(2).getChocoConstraints().get(0).toString());
                    assertEquals(1, constraints.get(1).getChocoConstraints().size());
                    assertEquals("ARITHM ([not(F1) + not(F4) >= 1])", constraints.get(1).getChocoConstraints().get(0).toString());
                    assertEquals(1, constraints.get(0).getChocoConstraints().size());
                    assertEquals("SUM ([not(F1) + F8 + F7 >= 1])", constraints.get(0).getChocoConstraints().get(0).toString());
                });
    }

    @Test
    void testRootTrue_CFInConflictsFalse() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/FM_10_0.splx");
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(fileFM);

        FMCdrModel<Feature, AbstractRelationship<Feature>, CTConstraint> model = new FMCdrModel<>(featureModel, true, true, false, true);
        model.initialize();

        assertAll(() -> assertEquals(9, model.getCorrectConstraints().size()),
//                () -> {
//                    List<Constraint> constraints = model.getCorrectConstraints().stream().toList();
//                    assertEquals(1, constraints.get(0).getChocoConstraints().size());
//                    assertEquals("ARITHM ([FM_10_0 = 1])", constraints.get(0).getChocoConstraints().get(0).toString());
//                },
                () -> assertEquals(0, model.getPossiblyFaultyConstraints().size()),
                ()-> {
                    List<Constraint> constraints = model.getCorrectConstraints().stream().toList();
                    assertEquals(1, constraints.get(0).getChocoConstraints().size());
                    assertEquals("ARITHM ([FM_10_0 = 1])", constraints.get(0).getChocoConstraints().get(0).toString());
                    assertEquals(1, constraints.get(1).getChocoConstraints().size());
                    assertEquals("ARITHM ([FM_10_0 + not(F1) >= 1])", constraints.get(1).getChocoConstraints().get(0).toString());
                    assertEquals(2, constraints.get(2).getChocoConstraints().size());
                    assertEquals("ARITHM ([FM_10_0 + not(F2) >= 1])", constraints.get(2).getChocoConstraints().get(0).toString());
                    assertEquals("ARITHM ([F2 + not(FM_10_0) >= 1])", constraints.get(2).getChocoConstraints().get(1).toString());
                    assertEquals(4, constraints.get(3).getChocoConstraints().size());
                    assertEquals("ARITHM ([FM_10_0 + not(F3) >= 1])", constraints.get(3).getChocoConstraints().get(0).toString());
                    assertEquals("ARITHM ([FM_10_0 + not(F4) >= 1])", constraints.get(3).getChocoConstraints().get(1).toString());
                    assertEquals("ARITHM ([FM_10_0 + not(F5) >= 1])", constraints.get(3).getChocoConstraints().get(2).toString());
                    assertEquals("SUM ([not(FM_10_0) + F5 + F4 + F3 >= 1])", constraints.get(3).getChocoConstraints().get(3).toString());
                    assertEquals(7, constraints.get(4).getChocoConstraints().size());
                    assertEquals("BV_1 = [0,1]=>ARITHM ([F6 = [0,1] + F7 = [0,1] = 1]), !BV_1 = [0,1]=>ARITHM ([PropNotEqualXY_C(F6, F7)])", constraints.get(4).getChocoConstraints().get(0).toString());
                    assertEquals("ARITHM ([FM_10_0 + not(F6) >= 1])", constraints.get(4).getChocoConstraints().get(2).toString());
                    assertEquals("ARITHM ([FM_10_0 + not(F7) >= 1])", constraints.get(4).getChocoConstraints().get(3).toString());
                    assertEquals("ARITHM ([not(FM_10_0) + BV_1 >= 1])", constraints.get(4).getChocoConstraints().get(4).toString());
                    assertEquals("ARITHM ([BV_1 + not(F6) >= 1])", constraints.get(4).getChocoConstraints().get(5).toString());
                    assertEquals("ARITHM ([BV_1 + not(F7) >= 1])", constraints.get(4).getChocoConstraints().get(6).toString());
                    assertEquals(1, constraints.get(5).getChocoConstraints().size());
                    assertEquals("ARITHM ([F2 + not(F8) >= 1])", constraints.get(5).getChocoConstraints().get(0).toString());
                    assertEquals(1, constraints.get(6).getChocoConstraints().size());
                    assertEquals("ARITHM ([F6 + not(F8) >= 1])", constraints.get(6).getChocoConstraints().get(0).toString());
                    assertEquals(1, constraints.get(7).getChocoConstraints().size());
                    assertEquals("ARITHM ([not(F1) + not(F4) >= 1])", constraints.get(7).getChocoConstraints().get(0).toString());
                    assertEquals(1, constraints.get(8).getChocoConstraints().size());
                    assertEquals("SUM ([not(F1) + F8 + F7 >= 1])", constraints.get(8).getChocoConstraints().get(0).toString());
                });
    }

    @Test
    void testRootFalse_CFInConflictsTrue() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/FM_10_0.splx");
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(fileFM);

        FMCdrModel<Feature, AbstractRelationship<Feature>, CTConstraint> model = new FMCdrModel<>(featureModel, true, false, true, true);
        model.initialize();

        assertAll(() -> assertEquals(0, model.getCorrectConstraints().size()),
//                () -> {
//                    List<Constraint> constraints = model.getCorrectConstraints().stream().toList();
//                    assertEquals(1, constraints.get(0).getChocoConstraints().size());
//                    assertEquals("ARITHM ([FM_10_0 = 1])", constraints.get(0).getChocoConstraints().get(0).toString());
//                },
                () -> assertEquals(8, model.getPossiblyFaultyConstraints().size()),
                ()-> {
                    List<Constraint> constraints = model.getPossiblyFaultyConstraints().stream().toList();
                    assertEquals(1, constraints.get(7).getChocoConstraints().size());
                    assertEquals("ARITHM ([FM_10_0 + not(F1) >= 1])", constraints.get(7).getChocoConstraints().get(0).toString());
                    assertEquals(2, constraints.get(6).getChocoConstraints().size());
                    assertEquals("ARITHM ([FM_10_0 + not(F2) >= 1])", constraints.get(6).getChocoConstraints().get(0).toString());
                    assertEquals("ARITHM ([F2 + not(FM_10_0) >= 1])", constraints.get(6).getChocoConstraints().get(1).toString());
                    assertEquals(4, constraints.get(5).getChocoConstraints().size());
                    assertEquals("ARITHM ([FM_10_0 + not(F3) >= 1])", constraints.get(5).getChocoConstraints().get(0).toString());
                    assertEquals("ARITHM ([FM_10_0 + not(F4) >= 1])", constraints.get(5).getChocoConstraints().get(1).toString());
                    assertEquals("ARITHM ([FM_10_0 + not(F5) >= 1])", constraints.get(5).getChocoConstraints().get(2).toString());
                    assertEquals("SUM ([not(FM_10_0) + F5 + F4 + F3 >= 1])", constraints.get(5).getChocoConstraints().get(3).toString());
                    assertEquals(7, constraints.get(4).getChocoConstraints().size());
                    assertEquals("BV_1 = [0,1]=>ARITHM ([F6 = [0,1] + F7 = [0,1] = 1]), !BV_1 = [0,1]=>ARITHM ([PropNotEqualXY_C(F6, F7)])", constraints.get(4).getChocoConstraints().get(0).toString());
                    assertEquals("ARITHM ([FM_10_0 + not(F6) >= 1])", constraints.get(4).getChocoConstraints().get(2).toString());
                    assertEquals("ARITHM ([FM_10_0 + not(F7) >= 1])", constraints.get(4).getChocoConstraints().get(3).toString());
                    assertEquals("ARITHM ([not(FM_10_0) + BV_1 >= 1])", constraints.get(4).getChocoConstraints().get(4).toString());
                    assertEquals("ARITHM ([BV_1 + not(F6) >= 1])", constraints.get(4).getChocoConstraints().get(5).toString());
                    assertEquals("ARITHM ([BV_1 + not(F7) >= 1])", constraints.get(4).getChocoConstraints().get(6).toString());
                    assertEquals(1, constraints.get(3).getChocoConstraints().size());
                    assertEquals("ARITHM ([F2 + not(F8) >= 1])", constraints.get(3).getChocoConstraints().get(0).toString());
                    assertEquals(1, constraints.get(2).getChocoConstraints().size());
                    assertEquals("ARITHM ([F6 + not(F8) >= 1])", constraints.get(2).getChocoConstraints().get(0).toString());
                    assertEquals(1, constraints.get(1).getChocoConstraints().size());
                    assertEquals("ARITHM ([not(F1) + not(F4) >= 1])", constraints.get(1).getChocoConstraints().get(0).toString());
                    assertEquals(1, constraints.get(0).getChocoConstraints().size());
                    assertEquals("SUM ([not(F1) + F8 + F7 >= 1])", constraints.get(0).getChocoConstraints().get(0).toString());
                });
    }

    @Test
    void testRootFalse_CFInConflictsFalse() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/FM_10_0.splx");
        @Cleanup("dispose")
        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
        FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel = parser.parse(fileFM);

        FMCdrModel<Feature, AbstractRelationship<Feature>, CTConstraint> model = new FMCdrModel<>(featureModel, true, false, false, true);
        model.initialize();

        assertAll(() -> assertEquals(0, model.getPossiblyFaultyConstraints().size()),
//                () -> {
//                    List<Constraint> constraints = model.getCorrectConstraints().stream().toList();
//                    assertEquals(1, constraints.get(0).getChocoConstraints().size());
//                    assertEquals("ARITHM ([FM_10_0 = 1])", constraints.get(0).getChocoConstraints().get(0).toString());
//                },
                () -> assertEquals(8, model.getCorrectConstraints().size()),
                ()-> {
                    List<Constraint> constraints = model.getCorrectConstraints().stream().toList();
                    assertEquals(1, constraints.get(0).getChocoConstraints().size());
                    assertEquals("ARITHM ([FM_10_0 + not(F1) >= 1])", constraints.get(0).getChocoConstraints().get(0).toString());
                    assertEquals(2, constraints.get(1).getChocoConstraints().size());
                    assertEquals("ARITHM ([FM_10_0 + not(F2) >= 1])", constraints.get(1).getChocoConstraints().get(0).toString());
                    assertEquals("ARITHM ([F2 + not(FM_10_0) >= 1])", constraints.get(1).getChocoConstraints().get(1).toString());
                    assertEquals(4, constraints.get(2).getChocoConstraints().size());
                    assertEquals("ARITHM ([FM_10_0 + not(F3) >= 1])", constraints.get(2).getChocoConstraints().get(0).toString());
                    assertEquals("ARITHM ([FM_10_0 + not(F4) >= 1])", constraints.get(2).getChocoConstraints().get(1).toString());
                    assertEquals("ARITHM ([FM_10_0 + not(F5) >= 1])", constraints.get(2).getChocoConstraints().get(2).toString());
                    assertEquals("SUM ([not(FM_10_0) + F5 + F4 + F3 >= 1])", constraints.get(2).getChocoConstraints().get(3).toString());
                    assertEquals(7, constraints.get(3).getChocoConstraints().size());
                    assertEquals("BV_1 = [0,1]=>ARITHM ([F6 = [0,1] + F7 = [0,1] = 1]), !BV_1 = [0,1]=>ARITHM ([PropNotEqualXY_C(F6, F7)])", constraints.get(3).getChocoConstraints().get(0).toString());
                    assertEquals("ARITHM ([FM_10_0 + not(F6) >= 1])", constraints.get(3).getChocoConstraints().get(2).toString());
                    assertEquals("ARITHM ([FM_10_0 + not(F7) >= 1])", constraints.get(3).getChocoConstraints().get(3).toString());
                    assertEquals("ARITHM ([not(FM_10_0) + BV_1 >= 1])", constraints.get(3).getChocoConstraints().get(4).toString());
                    assertEquals("ARITHM ([BV_1 + not(F6) >= 1])", constraints.get(3).getChocoConstraints().get(5).toString());
                    assertEquals("ARITHM ([BV_1 + not(F7) >= 1])", constraints.get(3).getChocoConstraints().get(6).toString());
                    assertEquals(1, constraints.get(4).getChocoConstraints().size());
                    assertEquals("ARITHM ([F2 + not(F8) >= 1])", constraints.get(4).getChocoConstraints().get(0).toString());
                    assertEquals(1, constraints.get(5).getChocoConstraints().size());
                    assertEquals("ARITHM ([F6 + not(F8) >= 1])", constraints.get(5).getChocoConstraints().get(0).toString());
                    assertEquals(1, constraints.get(6).getChocoConstraints().size());
                    assertEquals("ARITHM ([not(F1) + not(F4) >= 1])", constraints.get(6).getChocoConstraints().get(0).toString());
                    assertEquals(1, constraints.get(7).getChocoConstraints().size());
                    assertEquals("SUM ([not(F1) + F8 + F7 >= 1])", constraints.get(7).getChocoConstraints().get(0).toString());
                });
    }
}