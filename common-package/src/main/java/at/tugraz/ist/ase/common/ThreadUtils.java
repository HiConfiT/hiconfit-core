/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ThreadUtils {
    public String getThreadString() {
        return "[thread=" + Thread.currentThread().getId() + "]";
    }
}
