/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.core;

/**
 * The types of relationships/constraints
 */
public enum RelationshipType {
    MANDATORY,
    OPTIONAL,
    ALTERNATIVE,
    OR,
    REQUIRES,
    EXCLUDES,
    ThreeCNF // 3CNF
}
