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

        start(TIMER_NODE_LABEL);
        List<Set<Constraint>> labels = getLabeler().getLabel(param);

        if (!labels.isEmpty()) {
            stop(TIMER_NODE_LABEL);

            // check existing and obtained labels for subset-relations
            List<Set<Constraint>> nonMinLabels = new LinkedList<>();

            for (Set<Constraint> fs : getNodeLabels()) {
                if (nonMinLabels.contains(fs)) {
                    continue;
                }
                for (Set<Constraint> l : labels) {
                    if (nonMinLabels.contains(l)) {
                        continue;
                    }
                    Set<Constraint> greater = (fs.size() > l.size()) ? fs : l;
                    Set<Constraint> smaller = (fs.size() > l.size()) ? l : fs;

                    if (greater.containsAll(smaller)) {
                        nonMinLabels.add(greater);
                        // update the DAG
                        List<Node> nodes = this.label_nodesMap.get(greater);

                        if (nodes != null) {
                            for (Node nd : nodes) {
                                incrementCounter(COUNTER_PRUNING);

                                nd.setLabel(smaller); // relabel the node with smaller
                                addItemToLabelNodesMap(smaller, nd); // add new label to the map

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
            // remove the known non-minimal labels
            labels.removeAll(nonMinLabels);
            for (Set<Constraint> label : nonMinLabels) {
                this.label_nodesMap.remove(label);
            }

            // add new labels to the list of labels
            addNodeLabels(labels);
        } else {
            // stop TIMER_CONFLICT without saving the time
            stop(TIMER_NODE_LABEL, false);
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
            Node node = getReusableNode(nodeToExpand.getPathLabel(), arcLabel);
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
