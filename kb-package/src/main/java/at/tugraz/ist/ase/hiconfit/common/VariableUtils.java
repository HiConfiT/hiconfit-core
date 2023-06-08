/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.common;

import at.tugraz.ist.ase.hiconfit.kb.core.IntVariable;
import at.tugraz.ist.ase.hiconfit.kb.core.Variable;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.chocosolver.solver.variables.IntVar;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@UtilityClass
public class VariableUtils {
    public List<IntVar> getIntVarOrdering(@NonNull List<String> varOrdering, @NonNull List<Variable> variables) {
        return varOrdering.stream().map(varName -> getIntVarByName(varName, variables)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public IntVar getIntVarByName(@NonNull String varName, @NonNull List<Variable> variables) {
        return variables.parallelStream().filter(variable -> variable.getName().equals(varName)).findFirst().map(variable -> ((IntVariable) variable).getChocoVar()).orElse(null);
    }
}
