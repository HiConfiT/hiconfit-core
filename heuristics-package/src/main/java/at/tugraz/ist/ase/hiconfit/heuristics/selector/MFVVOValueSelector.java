/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.heuristics.selector;

import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import at.tugraz.ist.ase.hiconfit.heuristics.ValueOrdering;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.search.strategy.selectors.values.IntValueSelector;
import org.chocosolver.solver.variables.IntVar;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public class MFVVOValueSelector implements IntValueSelector {

    private final List<ValueOrdering> voList;
    private final List<Integer> lastIndexList = new LinkedList<>();

    public MFVVOValueSelector(List<ValueOrdering> voList){
        this.voList = voList;
        IntStream.range(0, voList.size()).forEach(i -> lastIndexList.add(-1));
    }

    @Override
    public int selectValue(IntVar var) {

        log.trace("{}selectValue: {}", LoggerUtils.tab(), var);
        LoggerUtils.indent();

        ValueOrdering vo = getValueOrdering(var);
        if (vo == null) {
            log.trace("{}NO valueOrdering found - get the lower bound value {}", LoggerUtils.tab(), var.getLB());
            LoggerUtils.outdent();
            return var.getLB();
        }

        int vo_index = voList.indexOf(vo);
        int lastIndex = lastIndexList.get(vo_index); // get the last index of the variable
        if (++lastIndex >= vo.getOrdering().size())
            lastIndex = 0;
        lastIndexList.set(vo_index, lastIndex); // update the last index of the variable

//        log.trace("{}valueOrdering found - {}, {}", LoggerUtils.tab(), vo.getVariable().getName(), vo.getOrdering());
//        int lastIndex = vo.getLastSelectedIndex();
//
//        log.trace("{}lastIndex: {}", LoggerUtils.tab(), lastIndex);
//
//        int value = vo.getOrdering().get(lastIndex);
//        log.trace("{}return value {}", LoggerUtils.tab(), vo.getOrdering().get(lastIndex));
//        log.trace("{}lastIndex: {}", LoggerUtils.tab(), vo.getVariable().getDomain().getValue(value));
//        LoggerUtils.outdent();

        LoggerUtils.outdent();
        return vo.getOrdering().get(lastIndex);
    }

    private ValueOrdering getValueOrdering(IntVar var) {
        return voList.stream().filter(vo -> vo.getIntVar() == var).findFirst().orElse(null);
        /*for (ValueOrdering vo : voList) {
            if (vo.getIntVar() == var) {
                return vo;
            }
        }
        return null;*/
    }
}
