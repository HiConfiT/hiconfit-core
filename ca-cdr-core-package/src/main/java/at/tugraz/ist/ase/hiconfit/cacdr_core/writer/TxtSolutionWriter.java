/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr_core.writer;

import at.tugraz.ist.ase.hiconfit.cacdr_core.Solution;
import com.google.common.base.Joiner;
import lombok.Cleanup;
import lombok.NonNull;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TxtSolutionWriter extends SolutionWriter {

    public TxtSolutionWriter(String folder) {
        super(folder);
    }

    @Override
    public void write(@NonNull Solution solution) throws IOException {
        createFileWriter();

        @Cleanup BufferedWriter writer = new BufferedWriter(fileWriter);

        List<String> assignments = solution.getAssignments().stream()
                .map(assignment -> String.format("%s=%s", assignment.getVariable(), assignment.getValue()))
                .collect(Collectors.toList());

        Joiner.on(",").appendTo(writer, assignments);
    }

    @Override
    protected void createFileWriter() throws IOException {
        ++SolutionWriter.counter;
        this.fileWriter = new FileWriter(String.format(this.folder + "conf_%s.txt", SolutionWriter.counter));
    }
}
