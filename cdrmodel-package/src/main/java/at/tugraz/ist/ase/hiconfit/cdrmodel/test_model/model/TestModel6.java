/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cdrmodel.test_model.model;

import at.tugraz.ist.ase.hiconfit.cdrmodel.AbstractCDRModel;
import at.tugraz.ist.ase.hiconfit.cdrmodel.IChocoModel;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test_model.ITestModel;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test_model.csp.CSPModels;
import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import at.tugraz.ist.ase.hiconfit.kb.core.Constraint;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IteratorUtils;
import org.chocosolver.solver.Model;

import java.util.*;

/**
 * Test model for the diagnosis task
 */
@Slf4j
public class TestModel6 extends AbstractCDRModel implements IChocoModel, ITestModel {
    @Getter
    private Model model;

    private List<Set<Constraint>> allDiagnoses = null;
    private List<Set<Constraint>> allConflicts = null;

    public TestModel6() {
        super("Test 6");
    }

    @Override
    public void initialize() throws Exception {
        log.trace("{}Initializing CDRModel for {} >>>", LoggerUtils.tab(), getName());
        LoggerUtils.indent();

        model = CSPModels.createModel6();

        // sets possibly faulty constraints to super class
        List<Constraint> C = new ArrayList<>();
        for (org.chocosolver.solver.constraints.Constraint c: model.getCstrs()) {
            Constraint constraint = new Constraint(c.toString());
            constraint.addChocoConstraint(c);

            C.add(constraint);
        }
        Collections.reverse(C);
        this.setPossiblyFaultyConstraints(C);
        log.trace("{}Added constraints to the possibly faulty constraints [C={}]", LoggerUtils.tab(), C);

        identifyExpectedResults();

        model.unpost(model.getCstrs());

        LoggerUtils.outdent();
        log.debug("{}<<< Initialized CDRModel for {}", LoggerUtils.tab(), getName());
    }

    private void identifyExpectedResults() {
        Set<Constraint> C = this.getPossiblyFaultyConstraints();

        // Expected results
        Set<Constraint> diag1 = new LinkedHashSet<>();
        diag1.add(IteratorUtils.get(C.iterator(), 4));
        diag1.add(IteratorUtils.get(C.iterator(), 5));

        Set<Constraint> diag2 = new LinkedHashSet<>();
        diag2.add(IteratorUtils.get(C.iterator(), 1));
        diag2.add(IteratorUtils.get(C.iterator(), 5));

        Set<Constraint> diag3 = new LinkedHashSet<>();
        diag3.add(IteratorUtils.get(C.iterator(), 3));
        diag3.add(IteratorUtils.get(C.iterator(), 4));
        diag3.add(IteratorUtils.get(C.iterator(), 6));

        Set<Constraint> diag4 = new LinkedHashSet<>();
        diag4.add(IteratorUtils.get(C.iterator(), 1));
        diag4.add(IteratorUtils.get(C.iterator(), 3));
        diag4.add(IteratorUtils.get(C.iterator(), 6));

        allDiagnoses = new ArrayList<>();
        allDiagnoses.add(diag1);
        allDiagnoses.add(diag2);
        allDiagnoses.add(diag3);
        allDiagnoses.add(diag4);

        log.trace("{}Generated expected results", LoggerUtils.tab());
    }

    @Override
    public Set<Constraint> getExpectedFirstDiagnosis() {
        return allDiagnoses.get(0);
    }

    @Override
    public List<Set<Constraint>> getExpectedAllDiagnoses() {
        return allDiagnoses;
    }

    @Override
    public Set<Constraint> getExpectedFirstConflict() {
        return null;
    }

    @Override
    public List<Set<Constraint>> getExpectedAllConflicts() {
        return null;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        TestModel6 clone = (TestModel6) super.clone();

        try {
            clone.initialize();
            return clone;
        } catch (Exception e) {
            throw new CloneNotSupportedException(e.getMessage());
        }
    }

    @Override
    public void dispose() {
        model = null;
        allDiagnoses.clear();
        allConflicts.clear();
    }
}
