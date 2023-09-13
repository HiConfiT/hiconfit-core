/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cdrmodel.test_model.model;

import at.tugraz.ist.ase.hiconfit.cdrmodel.AbstractCDRModel;
import at.tugraz.ist.ase.hiconfit.cdrmodel.IChocoModel;
import at.tugraz.ist.ase.hiconfit.cdrmodel.IKBRedundancyDetectable;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test_model.csp.CSPModels;
import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import at.tugraz.ist.ase.hiconfit.kb.core.Constraint;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Test model for the WipeOutR_FM algorithm
 */
@Slf4j
public class TestModel7 extends AbstractCDRModel implements IChocoModel, IKBRedundancyDetectable {
    @Getter
    private Model model;

    @Getter
    private List<Constraint> nonRedundantConstraints = new LinkedList<>();

    public TestModel7() {
        super("WipeOutR_FM Test 1");
    }

    @Override
    public void initialize() throws IOException {
        log.trace("{}Initializing CDRModel for {} >>>", LoggerUtils.tab(), getName());
        LoggerUtils.indent();

        // create the model
        model = CSPModels.createModel7();

        // sets possibly faulty constraints to super class
        List<Constraint> C = new ArrayList<>();
        for (org.chocosolver.solver.constraints.Constraint c: model.getCstrs()) {
            Constraint constraint = new Constraint(c.toString(), Collections.emptyList());
            constraint.addChocoConstraint(c);

            org.chocosolver.solver.constraints.Constraint opC = c.getOpposite(); // get negation of constraint
            model.post(opC);
            constraint.addNegChocoConstraint(opC);

            C.add(constraint);
        }
        this.setPossiblyFaultyConstraints(C);
        log.trace("{}Added constraints to the possibly faulty constraints [C={}]", LoggerUtils.tab(), C);

        // expected results
        nonRedundantConstraints.add(C.get(1));
        nonRedundantConstraints.add(C.get(2));

        model.unpost(model.getCstrs());

        LoggerUtils.outdent();
        log.debug("{}<<< Initialized CDRModel for {}", LoggerUtils.tab(), getName());
    }

    public Object clone() throws CloneNotSupportedException {
        TestModel7 clone = (TestModel7) super.clone();

        clone.nonRedundantConstraints = new LinkedList<>();

        try {
            clone.initialize();
        } catch (IOException e) {
            throw new CloneNotSupportedException(e.getMessage());
        }

        return clone;
    }

    @Override
    public void dispose() {
        model = null;
        nonRedundantConstraints.clear();
    }
}
