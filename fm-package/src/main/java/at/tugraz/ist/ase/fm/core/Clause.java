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
 * Represents a clause of 3-CNF formulas.
 *
 * A Clause is written as follows:
 * F1 - i.e., F1 = true
 * ~F1 - i.e., F1 = false
 * where the symbol F1 is a literal, and ~ denotes to the negation.
 *
 * Refer to <a href="http://www.splot-research.org">http://www.splot-research.org</a> for more info regarding 3-CNF.
 */
@Getter
@EqualsAndHashCode
@Slf4j
public class Clause {
    private final String literal; // name of clause
    private final boolean positive; // value of clause (true or false)

    /**
     * Constructor
     * @param clause in one of two forms: 1) A (i.e., A = true) or 2) ~A (i.e., A = false)
     */
    @Builder
    public Clause(@NonNull String clause) {
        if (clause.startsWith("~")) {
            positive = false;
            literal = clause.substring(1);
        } else {
            positive = true;
            literal = clause;
        }

        log.trace("{}Added clause [clause={}]", LoggerUtils.tab(), this);
    }

    public String getClause() {
        if (positive)
            return literal;
        else
            return "~" + literal;
    }

    @Override
    public String toString() {
        return literal + " = " + (positive ? "true" : "false");
    }
}

