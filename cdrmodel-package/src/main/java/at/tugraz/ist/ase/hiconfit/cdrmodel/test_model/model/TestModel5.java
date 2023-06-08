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
import com.google.common.collect.Iterators;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.Model;

import java.util.*;

/**
 * Test model for the diagnosis task
 */
@Slf4j
public class TestModel5 extends AbstractCDRModel implements IChocoModel, ITestModel {
    @Getter
    private Model model;

    private List<Set<Constraint>> allDiagnoses = null;
    private List<Set<Constraint>> allConflicts = null;

    public TestModel5() {
        super("Test 5");
    }

    @Override
    public void initialize() throws Exception {
        log.trace("{}Initializing CDRModel for {} >>>", LoggerUtils.tab(), getName());
        LoggerUtils.indent();

        model = CSPModels.createModel5();

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
        diag1.add(Iterators.get(C.iterator(), 2));

        Set<Constraint> diag2 = new LinkedHashSet<>();
        diag2.add(Iterators.get(C.iterator(), 0));

        allDiagnoses = new ArrayList<>();
        allDiagnoses.add(diag1);
        allDiagnoses.add(diag2);

        // Expected results
        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(Iterators.get(C.iterator(), 0));
        cs1.add(Iterators.get(C.iterator(), 2));

        allConflicts = new ArrayList<>();
        allConflicts.add(cs1);

        log.trace("{}Generated expected results", LoggerUtils.tab());
    }

    @Override
    public Set<Constraint> getExpectedFirstDiagnosis() {
        if (allDiagnoses != null)
            return allDiagnoses.get(0);
        return null;
    }

    @Override
    public List<Set<Constraint>> getExpectedAllDiagnoses() {
        return allDiagnoses;
    }

    @Override
    public Set<Constraint> getExpectedFirstConflict() {
        if (allConflicts != null)
            return allConflicts.get(0);
        return null;
    }

    @Override
    public List<Set<Constraint>> getExpectedAllConflicts() {
        return allConflicts;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        TestModel5 clone = (TestModel5) super.clone();

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
