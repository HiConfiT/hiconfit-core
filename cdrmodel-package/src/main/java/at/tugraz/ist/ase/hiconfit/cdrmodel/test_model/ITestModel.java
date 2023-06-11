/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cdrmodel.test_model;

import at.tugraz.ist.ase.hiconfit.kb.core.Constraint;

import java.util.List;
import java.util.Set;

public interface ITestModel {
    Set<Constraint> getExpectedFirstDiagnosis();
    List<Set<Constraint>> getExpectedAllDiagnoses();

    Set<Constraint> getExpectedFirstConflict();
    List<Set<Constraint>> getExpectedAllConflicts();
}
