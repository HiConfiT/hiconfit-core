/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.kb.core;

import at.tugraz.ist.ase.common.LoggerUtils;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.variables.IntVar;

@EqualsAndHashCode(callSuper = true)
@Getter
@Slf4j
public class IntVariable extends Variable implements Cloneable {
    private IntVar chocoVar;

    @Builder
    public IntVariable(@NonNull String name, @NonNull Domain domain, @NonNull IntVar chocoVar) {
        super(name, domain);
        this.chocoVar = chocoVar;

        log.trace("{}Created IntVariable [var={}]", LoggerUtils.tab(), this);
    }

    public String getValue() {
        return domain.getValue(getChocoValue());
    }

    public int getChocoValue() {
        return chocoVar.getValue();
    }

    public IntVariable clone() throws CloneNotSupportedException {
        IntVariable clone = (IntVariable) super.clone();

        clone.chocoVar = chocoVar;

        log.trace("{}Cloned IntVariable [var={}]", LoggerUtils.tab(), clone);
        return clone;
    }

    @Override
    public void dispose() {
        chocoVar = null;
        super.dispose();
    }
}
