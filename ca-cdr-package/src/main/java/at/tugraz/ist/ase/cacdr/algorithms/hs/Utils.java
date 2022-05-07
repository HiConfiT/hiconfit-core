/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hs;

import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.kb.core.Constraint;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@UtilityClass
@Slf4j
public class Utils {
    // Only for testing
    public void printInfo(Node root, List<Set<Constraint>> conflicts, List<Set<Constraint>> diagnoses) {
        printNode(root);
        log.trace("{}conflicts: {}", LoggerUtils.tab, conflicts);
        log.trace("{}diagnoses: {}", LoggerUtils.tab, diagnoses);
    }

    public void printNode(Node node) {
        if (node != null) {
            log.trace("{}[node={}]", LoggerUtils.tab, node);
            LoggerUtils.indent();

            for (Node child : node.getChildren().values()) {
                printNode(child);
            }

            LoggerUtils.outdent();
        }
    }
}
