/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cdrmodel.test_model;

import at.tugraz.ist.ase.kb.core.Constraint;

import java.util.List;
import java.util.Set;

public interface ITestModel {
    Set<Constraint> getExpectedFirstDiagnosis();
    List<Set<Constraint>> getExpectedAllDiagnoses();

    Set<Constraint> getExpectedFirstConflict();
    List<Set<Constraint>> getExpectedAllConflicts();
}
