/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.anomaly;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AnomalyType implements IAnomalyType {
    VOID("void feature model",
        "\u2717 Void feature model",
        "\u2713 Consistency: ok"),
    DEAD("dead feature",
            "\u2717 Dead feature",
            "\u2713 No dead feature"),
    FALSEOPTIONAL("false optional feature",
            "\u2717 False optional feature",
            "\u2713 No false optional feature"),
    FULLMANDATORY("full mandatory feature",
            "\u2717 Full mandatory feature",
        "\u2713 No full mandatory feature"),
    CONDITIONALLYDEAD("conditionally dead feature",
            "\u2717 Conditionally dead feature",
        "\u2713 No conditionally dead feature"),
    REDUNDANT("redundant constraint",
            "\u2717 Redundant constraint",
        "\u2713 No redundant constraint");

    // these strings are used for the output of the results
    private final String description; // used for diagnostic messages
    private final String violatedDescription; // when the assumption is violated
    private final String nonViolatedDescription; // when the assumption is not violated
}
