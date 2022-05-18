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
import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.kb.core.Constraint;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static at.tugraz.ist.ase.eval.PerformanceEvaluator.*;
import static at.tugraz.ist.ase.common.IOUtils.*;

/**
 * Implementation of the HS-dag algorithm.
 * IHSLabeler algorithms could return labels (conflict or diagnosis) which are not minimal.
 *
 * source: <a href="https://github.com/jaccovs/Master-project">https://github.com/jaccovs/Master-project</a>
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Slf4j
public class HSDAG extends HSTree {

    // Map of <pathLabel, Node>
    private final Map<Set<Constraint>, Node> nodesLookup = new HashMap<>();

    public HSDAG(IHSLabelable labeler) {
        super(labeler);
    }

    @Override
    protected List<Set<Constraint>> computeLabel(Node node) {
        AbstractHSParameters param = node.getParameters();

        start(TIMER_NODE_LABEL + getThreadString() + ": ");
        List<Set<Constraint>> conflicts = getLabeler().getLabel(param);

        if (!conflicts.isEmpty()) {
            stop(TIMER_NODE_LABEL + getThreadString() + ": ");

            // check existing and obtained labels for subset-relations
            List<Set<Constraint>> nonMinLabels = new LinkedList<>();

            getConflicts().parallelStream().filter(fs -> !nonMinConflicts.contains(fs)).forEachOrdered(fs -> {
                /*for (Set<Constraint> fs : getConflicts()) {
                    if (nonMinConflicts.contains(fs)) {
                        continue;
                    }*/

                conflicts.parallelStream().filter(cs -> !nonMinConflicts.contains(cs)).forEachOrdered(cs -> {
                    /*for (Set<Constraint> cs : conflicts) {
                        if (nonMinConflicts.contains(cs)) {
                            continue;
                        }*/

                    Set<Constraint> greater = (fs.size() > cs.size()) ? fs : cs;
                    Set<Constraint> smaller = (fs.size() > cs.size()) ? cs : fs;

                    if (greater.containsAll(smaller)) {
                        nonMinConflicts.add(greater);

                        // update the DAG
                        List<Node> nodes = this.label_nodesMap.get(greater);

                        if (nodes != null) {
                            nodes.forEach(nd -> {
                                incrementCounter(COUNTER_PRUNING);

                                nd.setLabel(smaller); // relabel the node with smaller
                                addItemToLabelNodesMap(smaller, nd); // add new label to the map

                                Set<Constraint> delete = Sets.difference(greater, smaller);
                                delete.forEach(label -> {
                                    Node child = nd.getChildren().get(label);

                                    if (child != null) {
                                        child.getParents().remove(nd);
                                    }

                                    nd.getChildren().remove(label);

                                    cleanUpNodes(nd);
                                });
                            });
                        }
                    }
                });
            });
            // remove the known non-minimal conflicts
            conflicts.removeAll(nonMinConflicts);
            nonMinConflicts.forEach(cs -> this.cs_nodesMap.remove(cs));

            // add new labels to the list of labels
            addNodeLabels(labels);
        } else {
            // stop TIMER_CONFLICT without saving the time
            stop(TIMER_NODE_LABEL  + getThreadString() + ": ", false);
        }

        return labels;
    }

    private void cleanUpNodes(Node node) {
        if (!node.getParents().isEmpty()) {
            return;
        }

        nodesLookup.remove(node.getPathLabel());
        if (node.getStatus() == NodeStatus.Open) {
            node.setStatus(NodeStatus.Pruned);
            incrementCounter(COUNTER_CLEANED_NODES);
        }

        // downward clean up
        node.getChildren().keySet().parallelStream()
                .map(arcLabel -> node.getChildren().get(arcLabel))
                .forEachOrdered(this::cleanUpNodes);
        /*for (Constraint arcLabel : node.getChildren().keySet()) {
            Node child = node.getChildren().get(arcLabel);
            cleanUpNodes(child);
        }*/
    }

    @Override
    protected void expand(Node nodeToExpand) {
        log.trace("{}Generating the children nodes of [node={}]", LoggerUtils.tab(), nodeToExpand);
        LoggerUtils.indent();

        for (Constraint arcLabel : nodeToExpand.getLabel()) {
            AbstractHSParameters param_parentNode = nodeToExpand.getParameters();
            AbstractHSParameters new_param = getLabeler().createParameter(param_parentNode, arcLabel);

            // rule 1.a - reuse node
            Node node = getReusableNode(nodeToExpand.getPathLabel(), arcLabel);
            if (node != null) {
                node.addParent(nodeToExpand);

                incrementCounter(COUNTER_REUSE_NODES);
                log.trace("{}Reusing [node={}]", LoggerUtils.tab(), node);
            } else { // rule 1.b - generate a new node
                node = Node.builder()
                        .parent(nodeToExpand)
                        .parameters(new_param)
                        .arcLabel(arcLabel)
                        .build();
                this.nodesLookup.put(node.getPathLabel(), node);
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
