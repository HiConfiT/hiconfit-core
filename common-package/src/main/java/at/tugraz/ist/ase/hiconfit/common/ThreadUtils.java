/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ThreadUtils {
    public String getThreadString() {
        return "[thread=" + Thread.currentThread().getId() + "]";
    }
}
