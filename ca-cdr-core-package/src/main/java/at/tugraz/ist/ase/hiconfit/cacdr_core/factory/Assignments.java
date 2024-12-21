/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr_core.factory;

import at.tugraz.ist.ase.hiconfit.cacdr_core.Assignment;
import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class Assignments {

    public Assignment fromVariableValue(@NonNull String variable, @NonNull String value) {
        return Assignment.builder().variable(variable).value(value).build();
    }

    /**
     * Create an assignment from a string with the format: variable<delimiter>value
     * For example, "variable=value"
     * @param assignment the assignment string
     * @param delimiter the delimiter
     * @return an {@link Assignment} object
     */
    public Assignment fromString(@NonNull String assignment, @NonNull String delimiter) {
        String[] items = assignment.split(delimiter);

        String variable;
        String value;
        switch (delimiter) {
            case "=":
                Preconditions.checkArgument(items.length == 2,
                        "The assignment must have the format: variable" + delimiter + "value. Provided: " + assignment);

                variable = items[0];
                value = items[1];
                break;
            case " ":
                if (items.length > 2) {
                    value = items[items.length - 1];
                    String[] variable_array = Arrays.copyOfRange(items, 0, items.length - 1);
                    variable = String.join(" ", variable_array);
                } else {
                    variable = items[0];
                    value = items[1];
                }
                break;
            default:
                throw new IllegalArgumentException("The delimiter is not supported: " + delimiter);
        }
        return fromVariableValue(variable, value);
    }

    /**
     * Create an assignment from a string with the format: variable or ~variable
     * @param clause the clause
     * @return an {@link Assignment} object
     */
    public Assignment fromClause(@NonNull String clause) {
        String variable;
        String value;
        if (clause.startsWith("~")) {
            value = "false";
            variable = clause.substring(1);
        } else {
            value = "true";
            variable = clause;
        }
        return fromVariableValue(variable, value);
    }

}
