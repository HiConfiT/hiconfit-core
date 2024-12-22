/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr_core.writer;

import at.tugraz.ist.ase.hiconfit.cacdr_core.Solution;
import lombok.NonNull;

import java.io.IOException;

public abstract class SolutionWriter {

    protected final String folder;

    public SolutionWriter(String folder) {
        this.folder = folder;
    }

    public abstract void write(@NonNull Solution solution, String filename) throws IOException;

}
