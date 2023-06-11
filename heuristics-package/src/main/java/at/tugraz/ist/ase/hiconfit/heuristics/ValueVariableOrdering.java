/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.heuristics;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.chocosolver.solver.variables.IntVar;

import java.util.LinkedList;
import java.util.List;

@ToString
public class ValueVariableOrdering {
    @Getter @Setter
    private List<String> varOrdering;
    @Getter @Setter
    private List<IntVar> intVarOrdering;
    @Getter
    private final List<ValueOrdering> valueOrdering;

    public ValueVariableOrdering() {
        varOrdering = new LinkedList<>();
        intVarOrdering = new LinkedList<>();
        valueOrdering = new LinkedList<>();
    }

    public void setValueOrdering(ValueOrdering valueOrdering) {
        this.valueOrdering.add(valueOrdering);
    }
}