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
public class TestModel3 extends CDRModel implements IChocoModel, ITestModel {
    @Getter
    private Model model;

    private List<Set<Constraint>> allDiagnoses = null;
    private List<Set<Constraint>> allConflicts = null;

    public TestModel3() {
        super("Test 3");
    }

    @Override
    public void initialize() throws Exception {
        log.trace("{}Initializing CDRModel for {} >>>", LoggerUtils.tab, getName());
        LoggerUtils.indent();

        model = CSPModels.createModel3();

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

        // Expected results
        Set<Constraint> diag1 = new LinkedHashSet<>();
        diag1.add(IteratorUtils.get(C.iterator(), 2));
        diag1.add(IteratorUtils.get(C.iterator(), 5));

        Set<Constraint> diag2 = new LinkedHashSet<>();
        diag2.add(IteratorUtils.get(C.iterator(), 0));
        diag2.add(IteratorUtils.get(C.iterator(), 5));

        Set<Constraint> diag3 = new LinkedHashSet<>();
        diag3.add(IteratorUtils.get(C.iterator(), 2));
        diag3.add(IteratorUtils.get(C.iterator(), 3));
        diag3.add(IteratorUtils.get(C.iterator(), 6));

        Set<Constraint> diag4 = new LinkedHashSet<>();
        diag4.add(IteratorUtils.get(C.iterator(), 0));
        diag4.add(IteratorUtils.get(C.iterator(), 3));
        diag4.add(IteratorUtils.get(C.iterator(), 6));

        allDiagnoses = new ArrayList<>();
        allDiagnoses.add(diag1);
        allDiagnoses.add(diag2);
        allDiagnoses.add(diag3);
        allDiagnoses.add(diag4);

        // Expected results
        Set<Constraint> cs1 = new LinkedHashSet<>();
        cs1.add(IteratorUtils.get(C.iterator(), 5));
        cs1.add(IteratorUtils.get(C.iterator(), 6));

        Set<Constraint> cs2 = new LinkedHashSet<>();
        cs2.add(IteratorUtils.get(C.iterator(), 3));
        cs2.add(IteratorUtils.get(C.iterator(), 5));

        Set<Constraint> cs3 = new LinkedHashSet<>();
        cs3.add(IteratorUtils.get(C.iterator(), 0));
        cs3.add(IteratorUtils.get(C.iterator(), 2));

        allConflicts = new ArrayList<>();
        allConflicts.add(cs1);
        allConflicts.add(cs2);
        allConflicts.add(cs3);

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
        TestModel3 clone = (TestModel3) super.clone();

        try {
            clone.initialize();
            return clone;
        } catch (Exception e) {
            throw new CloneNotSupportedException(e.getMessage());
        }
    }
}
