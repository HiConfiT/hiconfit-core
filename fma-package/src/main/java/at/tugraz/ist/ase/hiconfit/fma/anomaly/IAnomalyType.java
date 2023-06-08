/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.anomaly;

public interface IAnomalyType {
    String getDescription();
    String getNonViolatedDescription();
    String getViolatedDescription();
}
