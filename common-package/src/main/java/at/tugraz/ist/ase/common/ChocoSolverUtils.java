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
import org.chocosolver.solver.constraints.nary.cnf.LogOp;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.Variable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;

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

    /**
     * Unpost Choco constraints from the constraint with the start index to the end of the list of constraints.
     * @param startIdx - the start index of the constraints to be unposted
     */
    public void unpostConstraintsFrom(int startIdx, @NonNull Model model) {
        int index = model.getNbCstrs() - 1;
        while (index >= startIdx) {
            model.unpost(model.getCstrs()[index]);
            index--;
        }
    }

    public List<Constraint> getConstraints(@NonNull Model model, int startIdx, int endIdx) {
        Constraint[] cstrs = model.getCstrs();

        checkElementIndex(startIdx, cstrs.length, "startIdx must be within the range of constraints");
        checkElementIndex(endIdx, cstrs.length, "endIdx must be within the range of constraints");
        checkArgument(startIdx <= endIdx, "startIdx must be <= endIdx");

        List<Constraint> constraints = new LinkedList<>();
        int index = startIdx;
        while (index <= endIdx) {
            constraints.add(cstrs[index]);

            index++;
        }
        return constraints;
    }

    public void addAssignmentToLogOp(@NonNull LogOp logOp, @NonNull Model model,
                                     @NonNull String var, @NonNull String value) {
        BoolVar v = (BoolVar) getVariable(model, var); // get the corresponding variable
        if (value.equals("true")) { // true
            logOp.addChild(v);
        } else { // false
            logOp.addChild(v.not());
        }
    }
}
