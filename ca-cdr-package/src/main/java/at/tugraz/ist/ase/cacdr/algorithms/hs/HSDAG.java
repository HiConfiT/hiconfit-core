/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hs;

import at.tugraz.ist.ase.cacdr.algorithms.hs.labeler.IHSLabelable;
import at.tugraz.ist.ase.cacdr.algorithms.hs.parameters.AbstractHSParameters;
import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.kb.core.Constraint;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static at.tugraz.ist.ase.eval.PerformanceEvaluator.*;

/**
 * Implementation of the HS-dag algorithm.
 * IHSLabeler algorithms could return labels (conflict or diagnosis) which are not minimal.
 *
 * source: <a href="https://github.com/jaccovs/Master-project">https://github.com/jaccovs/Master-project</a>
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Slf4j
public class HSDAG extends HSTree {

    // Map of <pathLabels, Node>
    private final Map<Set<Constraint>, Node> nodesLookup = new HashMap<>();

    public HSDAG(IHSLabelable labeler, ChocoConsistencyChecker checker) {
        super(labeler, checker);
    }

    @Override
    protected List<Set<Constraint>> computeLabel(Node node) {
        AbstractHSParameters param = node.getParameters();

        start(TIMER_CONFLICT);
        List<Set<Constraint>> conflicts = getLabeler().getLabel(param);

        if (!conflicts.isEmpty()) {
            stop(TIMER_CONFLICT);

            // check existing and obtained conflicts for subset-relations
            List<Set<Constraint>> nonMinConflicts = new LinkedList<>();

            for (Set<Constraint> fs : getConflicts()) {
                if (nonMinConflicts.contains(fs)) {
                    continue;
                }
                for (Set<Constraint> cs : conflicts) {
                    if (nonMinConflicts.contains(cs)) {
                        continue;
                    }
                    Set<Constraint> greater = (fs.size() > cs.size()) ? fs : cs;
                    Set<Constraint> smaller = (fs.size() > cs.size()) ? cs : fs;

                    if (greater.containsAll(smaller)) {
                        nonMinConflicts.add(greater);
                        // update the DAG
                        List<Node> nodes = this.cs_nodesMap.get(greater);

                        if (nodes != null) {
                            for (Node nd : nodes) {
                                incrementCounter(COUNTER_PRUNING);

                                nd.setLabel(smaller); // relabel the node with smaller
                                addItemToCSNodesMap(smaller, nd); // add new label to the map

                                Set<Constraint> delete = Sets.difference(greater, smaller);
                                for (Constraint label : delete) {
                                    Node child = nd.getChildren().get(label);

                                    if (child != null) {
                                        child.getParents().remove(nd);
                                    }
                                    nd.getChildren().remove(label);

                                    cleanUpNodes(nd);
                                }
                            }
                        }
                    }
                }
            }
            // remove the known non-minimal conflicts
            conflicts.removeAll(nonMinConflicts);
            for (Set<Constraint> cs : nonMinConflicts) {
                this.cs_nodesMap.remove(cs);
            }

            // add new conflicts to the list of conflicts
            addConflicts(conflicts);
        } else {
            // stop TIMER_CONFLICT without saving the time
            stop(TIMER_CONFLICT, false);
        }

        return conflicts;
    }

    private void cleanUpNodes(Node node) {
        if (!node.getParents().isEmpty()) {
            return;
        }

        nodesLookup.remove(node.getPathLabels());
        if (node.getStatus() == NodeStatus.Open) {
            node.setStatus(NodeStatus.Pruned);
            incrementCounter(COUNTER_CLEANED_NODES);
        }

        // downward clean up
        for (Constraint arcLabel : node.getChildren().keySet()) {
            Node child = node.getChildren().get(arcLabel);
            cleanUpNodes(child);
        }
    }

    @Override
    protected void expand(Node nodeToExpand) {
        log.trace("{}Generating the children nodes of [node={}]", LoggerUtils.tab, nodeToExpand);
        LoggerUtils.indent();

        for (Constraint arcLabel : nodeToExpand.getLabel()) {
            AbstractHSParameters param_parentNode = nodeToExpand.getParameters();
            AbstractHSParameters new_param = getLabeler().createParameter(param_parentNode, arcLabel);

            // rule 1.a - reuse node
            Node node = getReusableNode(nodeToExpand.getPathLabels(), arcLabel);
            if (node != null) {
                node.addParent(nodeToExpand);

                incrementCounter(COUNTER_REUSE_NODES);
                log.trace("{}Reusing [node={}]", LoggerUtils.tab, node);
            } else { // rule 1.b - generate a new node
                node = Node.builder()
                        .parent(nodeToExpand)
                        .parameters(new_param)
                        .arcLabel(arcLabel)
                        .build();
                this.nodesLookup.put(node.getPathLabels(), node);
                incrementCounter(COUNTER_CONSTRUCTED_NODES);

                if (!canPrune(node)) {
                    openNodes.add(node);
                }
            }
        }

        LoggerUtils.outdent();
    }

    private Node getReusableNode(Set<Constraint> pathLabels, Constraint arcLabel) {
        Set<Constraint> h = new LinkedHashSet<>(pathLabels);
        h.add(arcLabel);
        return this.nodesLookup.get(h);
    }

    @Override
    public void resetEngine() {
        super.resetEngine();
        this.nodesLookup.clear();
    }
}
