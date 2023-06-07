/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
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
import org.chocosolver.solver.variables.BoolVar;

@EqualsAndHashCode(callSuper = true)
@Getter
@Slf4j
public class BoolVariable extends Variable implements Cloneable {
    private BoolVar chocoVar;

    @Builder
    public BoolVariable(@NonNull String name, @NonNull Domain domain, @NonNull BoolVar chocoVar) {
        super(name, domain);
        this.chocoVar = chocoVar;

        log.trace("{}Created BoolVariable [var={}]", LoggerUtils.tab(), this);
    }

    public String getValue() {
        return domain.getValue(getChocoValue());
    }

    public int getChocoValue() {
        return chocoVar.getValue();
    }

    public BoolVariable clone() throws CloneNotSupportedException {
        BoolVariable clone = (BoolVariable) super.clone();

        clone.chocoVar = chocoVar;

        log.trace("{}Cloned BoolVariable [var={}]", LoggerUtils.tab(), clone);
        return clone;
    }

    @Override
    public void dispose() {
        chocoVar = null;
        super.dispose();
    }
}
