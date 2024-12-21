/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr_core.reader;

import at.tugraz.ist.ase.hiconfit.cacdr_core.Assignment;
import at.tugraz.ist.ase.hiconfit.cacdr_core.Requirement;
import at.tugraz.ist.ase.hiconfit.cacdr_core.factory.Assignments;
import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import at.tugraz.ist.ase.hiconfit.kb.core.KB;
import at.tugraz.ist.ase.hiconfit.kb.core.Variable;
import com.google.common.base.Preconditions;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class SolutionReader {

    private static KB kb;

    public SolutionReader(KB kb) {
        SolutionReader.kb = kb;
    }

    public Requirement read(@NonNull File file) throws IOException {
        log.trace("{}Reading the configuration file - {} >>>", LoggerUtils.tab(), file.getName());
        LoggerUtils.indent();

        @Cleanup InputStream is = new FileInputStream(file);
        @Cleanup BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        List<Assignment> assignments = br.lines().map(this::processAssignment).toList();
//        List<Assignment> assignments = new LinkedList<>();
//        br.lines().forEach(line -> {
//            Assignment assignment = Assignments.fromString(line, " ");
//            String variable = assignment.getVariable();
//            String value = assignment.getValue();
//
//            Variable var = kb.getVariable(variable);
//            Preconditions.checkArgument(var != null, variable + "is not a variable");
//            Preconditions.checkArgument(var.getDomain().contains(value), "Domain of " + variable + "doesn't contains " + value);
//
//            assignments.add(assignment);
//        });

        LoggerUtils.outdent();
        log.debug("{}<<< Read the configuration file - {}", LoggerUtils.tab(), file.getName());
        return Requirement.requirementBuilder().assignments(assignments).build();
    }

    private Assignment processAssignment(String line) {
        Assignment assignment = Assignments.fromString(line, " ");
        String variable = assignment.getVariable();
        String value = assignment.getValue();

        Variable var = kb.getVariable(variable);
        Preconditions.checkArgument(var != null, variable + "is not a variable");
        Preconditions.checkArgument(var.getDomain().contains(value), "Domain of " + variable + "doesn't contains " + value);

        return assignment;
    }

//    static class AssignmentBuilder {
//
//        /**
//         * Assignment format: variable value
//         */
//        public static Assignment build(@NonNull String anAssignment) {
//            log.trace("{}Building an assignment from [ass={}] >>>", LoggerUtils.tab(), anAssignment);
//            LoggerUtils.indent();
//
//            String[] tokens = anAssignment.split(" ");
//
//            String variable;
//            String value;
//            if (tokens.length > 2) {
//                value = tokens[tokens.length - 1];
//                String[] variable_array = Arrays.copyOfRange(tokens, 0, tokens.length - 1);
//                variable = String.join(" ", variable_array);
//            } else {
//                variable = tokens[0];
//                value = tokens[1];
//        }
//
//            Variable var = kb.getVariable(variable);
//            Preconditions.checkArgument(var != null, variable + "is not a variable");
//            Preconditions.checkArgument(var.getDomain().contains(value), "Domain of " + variable + "doesn't contains " + value);
//
//            Assignment assignment = Assignment.builder().variable(variable).value(value).build();
//
//            LoggerUtils.outdent();
//            log.trace("{}Built an assignment from [ass={}]", LoggerUtils.tab(), anAssignment);
//
//            return assignment;
//        }
//    }
}
