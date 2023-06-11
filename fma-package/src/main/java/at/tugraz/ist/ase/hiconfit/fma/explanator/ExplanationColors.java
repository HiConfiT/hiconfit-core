/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.explanator;

import at.tugraz.ist.ase.hiconfit.common.ConsoleColors;
import lombok.experimental.UtilityClass;

/**
 * Special colors for feature model analysis
 */
@UtilityClass
public class ExplanationColors {
    public String OK = ConsoleColors.BLUE;
    public String ANOMALY = ConsoleColors.RED;
    public String EXPLANATION = ConsoleColors.BLACK;
    public String ASSUMPTION = ConsoleColors.GREEN;
}
