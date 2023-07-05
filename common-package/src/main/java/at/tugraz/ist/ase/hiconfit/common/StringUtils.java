/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.common;

import com.google.common.base.CharMatcher;
import com.google.common.base.Functions;
import com.google.common.base.Predicates;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {
    /**
     * Replace special characters (e.g. not in [a-zA-Z0-9_]) by underscore characters.
     *
     * @param st - a string needed to replace
     * @return a new string in which the special characters are replaced by underscore characters
     */
    public String replaceSpecialCharactersByUnderscore(@NonNull String st) {
        String pattern = "[^\\w]"; // [^a-zA-Z0-9_]
        return CharMatcher.forPredicate(Predicates.compose(Predicates.containsPattern(pattern), Functions.toStringFunction()))
                        .replaceFrom(st, "_");
    }
}
