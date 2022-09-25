/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.anomaly;

public interface IAnomalyType {
    String getDescription();
    String getNonViolatedDescription();
    String getViolatedDescription();
}
