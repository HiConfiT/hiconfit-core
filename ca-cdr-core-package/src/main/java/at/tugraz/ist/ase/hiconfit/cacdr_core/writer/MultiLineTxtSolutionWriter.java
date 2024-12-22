/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2024
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

public class MultiLineTxtSolutionWriter extends SolutionWriter {

    public MultiLineTxtSolutionWriter(String folder) {
        super(folder);
    }

    @Override
    public void write(@NonNull Solution solution, String filename) throws IOException {
        String outputFile = folder + filename;

        @Cleanup BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

//        for (Assignment assignment : s.getAssignments()) {
//            combWriter.write(assignment.getVariable() + " " + assignment.getValue());
//            combWriter.newLine();
//        }
//
//        // print
//        boolean first = true;
//        for (Assignment assignment : s.getAssignments()) {
//            if (first) {
//                first = false;
//            } else {
//                combWriter.newLine();
//            }
//            combWriter.write(assignment.getVariable() + " " + assignment.getValue());
//        }

        List<String> assignments = solution.getAssignments().stream()
                .map(assignment -> String.format("%s %s", assignment.getVariable(), assignment.getValue()))
                .collect(Collectors.toList());

        Joiner.on("\n").appendTo(writer, assignments);
    }
}
