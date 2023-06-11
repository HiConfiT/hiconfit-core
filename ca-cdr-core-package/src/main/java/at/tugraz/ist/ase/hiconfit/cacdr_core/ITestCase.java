/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr_core;

import org.chocosolver.solver.constraints.Constraint;

import java.util.List;

public interface ITestCase extends Cloneable {
    List<Assignment> getAssignments();

    List<Constraint> getChocoConstraints();
    List<Constraint> getNegChocoConstraints();

    boolean isViolated();

    void dispose();
}