/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core;

import at.tugraz.ist.ase.common.LoggerUtils;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a literal of 3-CNF formulas.
 * <p>
 * A Literal is written as follows:
 * F1 - i.e., F1 = true
 * ~F1 - i.e., F1 = false
 * where F1 is a variable, and ~ denotes to the negative literal.
 * <p>
 * Refer to <a href="http://www.splot-research.org">http://www.splot-research.org</a> for more info regarding 3-CNF.
 */
@Getter
@EqualsAndHashCode
@Slf4j
public class Literal {
    private final String variable; // name of literal
    private final boolean positive; // value of literal (true or false)

    /**
     * Constructor
     * @param literal in one of two forms: 1) A (i.e., A = true) or 2) ~A (i.e., A = false)
     */
    @Builder
    public Literal(@NonNull String literal) {
        if (literal.startsWith("~")) {
            positive = false;
            variable = literal.substring(1);
        } else {
            positive = true;
            variable = literal;
        }

        log.trace("{}Added literal [literal={}]", LoggerUtils.tab(), this);
    }

    public String getLiteral() {
        if (positive)
            return variable;
        else
            return "~" + variable;
    }

    @Override
    public String toString() {
        return variable + " = " + (positive ? "true" : "false");
    }
}

