/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.kb.core.writer;

import at.tugraz.ist.ase.hiconfit.kb.core.Solution;
import lombok.NonNull;

import java.io.FileWriter;
import java.io.IOException;

public abstract class SolutionWriter {

    protected final String folder;
    protected static int counter = 0;

    protected FileWriter fileWriter;

    public SolutionWriter(String folder) {
        this.folder = folder;
    }

    public abstract void write(@NonNull Solution solution) throws IOException;

    protected void createFileWriter() throws IOException {
        counter++;
        fileWriter = new FileWriter(String.format(folder + "conf_%s.xml", counter));
    }
}
