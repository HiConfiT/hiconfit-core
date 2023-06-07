/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.heuristics.selector;

import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.search.strategy.selectors.variables.VariableSelector;
import org.chocosolver.solver.variables.IntVar;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class MFVVOVariableSelector implements VariableSelector<IntVar> {

    private final List<IntVar> varOrdering;

    public MFVVOVariableSelector(List<IntVar> vo) {
        this.varOrdering = vo;
    }

    @Override
    public IntVar getVariable(IntVar[] variables) {
        log.trace("{}getVariable - {}", LoggerUtils.tab(), variables);
        LoggerUtils.indent();

        List<IntVar> orderedChocoVars = new LinkedList<>();
        Collections.addAll(orderedChocoVars, variables);

        // reorder the variables
        for (int i = varOrdering.size() - 1; i >= 0; i--) {
            IntVar var = varOrdering.get(i);
            orderedChocoVars.remove(var);
            orderedChocoVars.add(0, var);
        }

        // find the first uninstantiated variable
        for (IntVar orderedChocoVar : orderedChocoVars) {
            if (!orderedChocoVar.isInstantiated()) {
                log.trace("{}selected variable - {}", LoggerUtils.tab(), orderedChocoVar);
                LoggerUtils.outdent();

                return orderedChocoVar;
            }
        }
        log.trace("{}NO selected variable", LoggerUtils.tab());
        LoggerUtils.outdent();
        return null;
    }
}
