/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.anomaly;

import at.tugraz.ist.ase.hiconfit.fma.builder.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AnomalyType implements IAnomalyType {
    VOID("void feature model",
        "✗ Void feature model",
        "✓ Consistency: ok",
            new VoidFMAnalysisBuilder()),
    DEAD("dead feature",
            "✗ Dead feature",
            "✓ No dead feature",
            new DeadFeatureAnalysisBuilder()),
    FALSEOPTIONAL("false optional feature",
            "✗ False optional feature",
            "✓ No false optional feature",
            new FalseOptionalAnalysisBuilder()),
    FULLMANDATORY("full mandatory feature",
            "✗ Full mandatory feature",
        "✓ No full mandatory feature",
            new FullMandatoryAnalysisBuilder()),
    CONDITIONALLYDEAD("conditionally dead feature",
            "✗ Conditionally dead feature",
        "✓ No conditionally dead feature",
            new ConditionallyDeadAnalysisBuilder()),
    REDUNDANT("redundant constraint",
            "✗ Redundant constraint",
        "✓ No redundant constraint",
            new RedundancyAnalysisBuilder());

    // these strings are used for the output of the results
    private final String description; // used for diagnostic messages
    private final String violatedDescription; // when the assumption is violated
    private final String nonViolatedDescription; // when the assumption is not violated
    private final IAnalysisBuildable builder;

    public String getRawViolatedDescription() {
        return getViolatedDescription().substring(2);
    }

    public String getRawNonViolatedDescription() {
        return getNonViolatedDescription().substring(2);
    }
}
