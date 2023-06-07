/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.eval;

import at.tugraz.ist.ase.common.LoggerUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Counter extends AbstractEvaluator {
    @Getter
    private long value = 0;

    public Counter(String name) {
        super(name);

        log.trace("{}Created a counter for [counter={}]", LoggerUtils.tab(), name);
    }

    public long increment(int step) {
        this.value = this.value + step;

        log.trace("{}Incremented the counter [counter={}, step(s)={}]", LoggerUtils.tab(), name, step);

        return getValue();
    }

    @Override
    public String toString() {
        return Long.toString(getValue());
    }
}
