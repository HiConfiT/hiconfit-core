/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hs;

/**
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public enum NodeStatus {
    Open,
    Closed,
    Pruned,
    Checked// Checked - the label of this node is a Conflict or a Diagnosis
}
