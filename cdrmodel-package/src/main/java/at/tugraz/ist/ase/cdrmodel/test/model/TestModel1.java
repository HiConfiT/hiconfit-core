/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cdrmodel.test.model;

import at.tugraz.ist.ase.cdrmodel.CDRModel;
import at.tugraz.ist.ase.cdrmodel.IChocoModel;
import at.tugraz.ist.ase.cdrmodel.test.ITestModel;
import at.tugraz.ist.ase.cdrmodel.test.csp.CSPModels;
import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.kb.core.Constraint;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IteratorUtils;
import org.chocosolver.solver.Model;

import java.util.*;

@Slf4j
public class TestModel1 extends CDRModel implements IChocoModel, ITestModel {

    @Getter
    private Model model;

    private List<Set<Constraint>> allDiagnoses = null;
    private List<Set<Constraint>> allConflicts = null;

    public TestModel1() {
        super("Test 1");
    }

    @Override
    public void initialize() throws Exception {
        log.trace("{}Initializing CDRModel for {} >>>", LoggerUtils.tab, getName());
        LoggerUtils.indent();

        model = CSPModels.createModel1();

        // sets possibly faulty constraints to super class
        List<Constraint> C = new ArrayList<>();
        for (org.chocosolver.solver.constraints.Constraint c: model.getCstrs()) {
            Constraint constraint = new Constraint(c.toString());
            constraint.addChocoConstraint(c);

            C.add(constraint);
        }
        Collections.reverse(C);
        this.setPossiblyFaultyConstraints(C);
        log.trace("{}Added constraints to the possibly faulty constraints [C={}]", LoggerUtils.tab, C);

        identifyExpectedResults();

        model.unpost(model.getCstrs());

        LoggerUtils.outdent();
        log.debug("{}<<< Initialized CDRModel for {}", LoggerUtils.tab, getName());
    }

    private void identifyExpectedResults() {
        Set<Constraint> C = this.getPossiblyFaultyConstraints();

        // Expected diagnoses
        Set<Constraint> diag1 = new LinkedHashSet<>();
        diag1.add(IteratorUtils.get(C.iterator(), 2)); // ARITHM ([y >= x + 1])
        diag1.add(IteratorUtils.get(C.iterator(), 3)); // ARITHM ([x >= 2])

        Set<Constraint> diag2 = new LinkedHashSet<>();
        diag2.add(IteratorUtils.get(C.iterator(), 0)); // ARITHM ([y = -10])
        diag2.add(IteratorUtils.get(C.iterator(), 3)); // ARITHM ([x >= 2])

        Set<Constraint> diag3 = new LinkedHashSet<>();
        diag3.add(IteratorUtils.get(C.iterator(), 1)); // ARITHM ([x <= 0])
        diag3.add(IteratorUtils.get(C.iterator(), 2)); // ARITHM ([y >= x + 1])

        Set<Constraint> diag4 = new LinkedHashSet<>();
        diag4.add(IteratorUtils.get(C.iterator(), 0)); // ARITHM ([y = -10])
        diag4.add(IteratorUtils.get(C.iterator(), 1)); // ARITHM ([x <= 0])

        allDiagnoses = new ArrayList<>();
        allDiagnoses.add(diag1);
        allDiagnoses.add(diag2);
        allDiagnoses.add(diag3);
        allDiagnoses.add(diag4);

        // Expected conflicts
        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(IteratorUtils.get(C.iterator(), 1)); // ARITHM ([x <= 0])
        cs1.add(IteratorUtils.get(C.iterator(), 3)); // ARITHM ([x >= 2])

        Set<Constraint> cs2 = new LinkedHashSet<>();
        cs2.add(IteratorUtils.get(C.iterator(), 0)); // ARITHM ([y = -10])
        cs2.add(IteratorUtils.get(C.iterator(), 2)); // ARITHM ([y >= x + 1])

        allConflicts = new ArrayList<>();
        allConflicts.add(cs1);
        allConflicts.add(cs2);

        log.trace("{}Generated expected results", LoggerUtils.tab);
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
        TestModel1 clone = (TestModel1) super.clone();

        try {
            clone.initialize();
            return clone;
        } catch (Exception e) {
            throw new CloneNotSupportedException(e.getMessage());
        }
    }
}
