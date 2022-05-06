/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.common;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.Variable;

import java.util.Arrays;
import java.util.List;

@UtilityClass
public class ChocoSolverUtils {

    /**
     * Get a variable with the given name.
     * @param model a Choco model
     * @param name a variable's name
     * @return a {@link Variable} with the given name, or
     * @throws IllegalArgumentException if no variable with the given name exists
     */
    public Variable getVariable(@NonNull Model model, @NonNull String name) {
        for (Variable v : model.getVars()) {
            if (v.getName().equals(name)) {
                return v;
            }
        }
        throw new IllegalArgumentException("No variable with name " + name + " exists");
    }

    /**
     * Print all constraints of a Choco model to the console.
     * @param model - a Choco model
     */
    public void printConstraintsWithoutFormat(@NonNull Model model) {
        List<Constraint> ac = Arrays.asList(model.getCstrs());
        ac.forEach(System.out::println);
    }
}
