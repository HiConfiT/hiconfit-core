/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.kb.fm;

import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.Feature;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import at.tugraz.ist.ase.hiconfit.fm.factory.FeatureModels;
import at.tugraz.ist.ase.hiconfit.fm.parser.FeatureModelParserException;
import at.tugraz.ist.ase.hiconfit.kb.core.BoolVariable;
import at.tugraz.ist.ase.hiconfit.kb.core.Variable;
import lombok.val;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.loop.monitors.IMonitorSolution;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Test not(KB)
 */
class FMKBTest5 {
    static FMKB<Feature, AbstractRelationship<Feature>, CTConstraint> kb;
    static FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel;

    @Test
    void test() throws FeatureModelParserException {
        val fileFM = new File("src/test/resources/pizzas.xml");
        featureModel = FeatureModels.fromFile(fileFM);

        kb = new FMKB<>(featureModel, true);

        Model model = kb.getModelKB();

        model.unpost(model.getCstrs());
//        model.post(kb.getNotKB().getChocoConstraints().toArray(new Constraint[0]));

        Solver solver = kb.getModelKB().getSolver();

        AtomicInteger numSolutions = new AtomicInteger();
        solver.plugMonitor((IMonitorSolution) () -> {
            numSolutions.getAndIncrement();

            for (Variable variable : kb.getVariableList()) {
                BoolVariable boolVariable = (BoolVariable) variable;
                System.out.print(boolVariable.getName() + " = " + boolVariable.getValue() + ", ");
            }
            System.out.println();
        });

        solver.findAllSolutions();
//        System.out.println("Number of solutions: " + numSolutions.get());

        assert numSolutions.get() == 40960;
    }
}