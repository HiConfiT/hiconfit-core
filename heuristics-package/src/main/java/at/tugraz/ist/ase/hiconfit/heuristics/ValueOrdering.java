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
public class ValueOrdering {
    @Getter
    private final String varName;
    @Getter @Setter
    private IntVar intVar;
    @Getter
    private final List<Integer> ordering;

    public ValueOrdering(String varName) {
        this.varName = varName;
        this.ordering = new LinkedList<>();
    }

    public void setOrderedValue(int value) {
        ordering.add(value);
    }
}
