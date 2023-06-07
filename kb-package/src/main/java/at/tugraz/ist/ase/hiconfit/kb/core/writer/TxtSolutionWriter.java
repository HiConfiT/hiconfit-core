/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.kb.core.writer;

import at.tugraz.ist.ase.hiconfit.kb.core.Assignment;
import at.tugraz.ist.ase.hiconfit.kb.core.Solution;
import com.google.common.base.Joiner;
import lombok.Cleanup;
import lombok.NonNull;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TxtSolutionWriter extends SolutionWriter {

    public TxtSolutionWriter(String folder) {
        super(folder);
    }

    @Override
    public void write(@NonNull Solution solution) throws IOException {
        createFileWriter();

        @Cleanup BufferedWriter writer = new BufferedWriter(fileWriter);

        List<String> assignments = new ArrayList<>();
        for (int i = 0; i < solution.size(); i++) {
            Assignment assignment = solution.getAssignment(i);
            String var = assignment.getVariable();
            String value = assignment.getValue();

            assignments.add(String.format("%s=%s", var, value));
        }

        Joiner.on(",").appendTo(writer, assignments);
    }

    @Override
    protected void createFileWriter() throws IOException {
        ++SolutionWriter.counter;
        this.fileWriter = new FileWriter(String.format(this.folder + "conf_%s.txt", SolutionWriter.counter));
    }
}
