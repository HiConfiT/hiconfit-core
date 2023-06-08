/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.heuristics.io;

import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import at.tugraz.ist.ase.hiconfit.heuristics.ValueOrdering;
import at.tugraz.ist.ase.hiconfit.kb.core.BoolVariable;
import at.tugraz.ist.ase.hiconfit.kb.core.Domain;
import at.tugraz.ist.ase.hiconfit.kb.core.IntVariable;
import at.tugraz.ist.ase.hiconfit.kb.core.Variable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class ValueOrderingReader implements IValueOrderingReadable {

    public ValueOrdering read(@NonNull String[] items, @NonNull Variable variable) {
        log.debug("{}Generating value ordering for {} with items {}", LoggerUtils.tab(), variable, Arrays.toString(items));

        ValueOrdering vo = new ValueOrdering(variable.getName());

        Domain domain = variable.getDomain();

        // a list presenting the initial order of values (descending order)
        List<Integer> list = IntStream.rangeClosed(0, domain.size() - 1)
                .boxed().collect(Collectors.toList());
        Collections.reverse(list);

        // apply the new ordering from the file
        int size = domain.size() + items.length - 2;
        for (int i = 1; i < items.length; i++) {
            String value = items[i];
            int index = domain.getValues().indexOf(value);

            if (index == -1) {
                log.error("{}Value {} not found in the domain {}", LoggerUtils.tab(), value, domain);
            } else {
                list.set(index, size);
                size--;
            }
        }

        Map<Integer, Integer> orderedValues = new TreeMap<>(Collections.reverseOrder());

        for (int i = 0; i < domain.size(); i++) {
            int chocoValue = domain.getChocoValues().get(i);
            int key = list.get(i);

            orderedValues.put(key, chocoValue);
        }

        for (Integer key : orderedValues.keySet()) {
            vo.setOrderedValue(orderedValues.get(key));
        }

        if (variable instanceof IntVariable) {
            vo.setIntVar(((IntVariable)variable).getChocoVar());
        } else if (variable instanceof BoolVariable) {
            vo.setIntVar(((BoolVariable)variable).getChocoVar());
        }

        return vo;
    }
}
