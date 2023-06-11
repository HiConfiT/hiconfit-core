/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.heuristics.mf;

public class MFException extends RuntimeException {
    public MFException(String message) {
        super(message);
    }

    public MFException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
