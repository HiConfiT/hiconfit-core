/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hs;

import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.kb.core.Constraint;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static at.tugraz.ist.ase.cacdr.algorithms.hs.AbstractHSConstructor.*;
import static at.tugraz.ist.ase.eval.PerformanceEvaluator.incrementCounter;
import static at.tugraz.ist.ase.eval.PerformanceEvaluator.stop;

@Slf4j
public class HSDAGPruningEngine extends HSTreePruningEngine {

    // Map of <pathLabel, Node>
    @Getter
    private final ConcurrentHashMap<Set<Constraint>, Node> nodesLookup = new ConcurrentHashMap<>();

    public HSDAGPruningEngine(@NonNull AbstractHSConstructor hsConstructor) {
        super(hsConstructor);
    }

    /**
     * This method is called after identifying new labels for a node.
     * In HS-DAG, this method checks if the returned labels are proper subsets of already known labels.
     * That is, it implements the pruning rule 3. according to the paper of Greiner et al.
     * Note also, that this pruning method is adapted to conflict searching algorithms that can return more than one conflict.
     * @param labels new identified labels
     */
    @Override
    public void processLabels(List<Set<Constraint>> labels) {
        if (!labels.isEmpty()) {
            stop(TIMER_NODE_LABEL);

            // check existing and obtained labels for subset-relations
            List<Set<Constraint>> nonMinLabels = new LinkedList<>();

            hsConstructor.getNodeLabels().stream().filter(fs -> !nonMinLabels.contains(fs)).forEachOrdered(fs -> {
            /*for (Iterator<Set<Constraint>> it = hsdag.getNodeLabels().iterator(); it.hasNext(); ) {
                Set<Constraint> fs = it.next();
                if (nonMinLabels.contains(fs)) {
                    continue;
                }*/

                labels.stream().filter(cs -> !nonMinLabels.contains(cs)).forEachOrdered(cs -> {
                /*for (Iterator<Set<Constraint>> iterator1 = labels.iterator(); iterator1.hasNext(); ) {
                    Set<Constraint> cs = iterator1.next();
                    if (nonMinLabels.contains(cs)) {
                        continue;
                    }*/
                    log.debug("{}(HSDAGPruningEngine-processLabels) Processing [fs={}, cs={}]", LoggerUtils.tab(), fs, cs);

                    Set<Constraint> greater = (fs.size() > cs.size()) ? fs : cs;
                    Set<Constraint> smaller = (fs.size() > cs.size()) ? cs : fs;

                    if (greater.containsAll(smaller)) {
                        nonMinLabels.add(greater);

                        if (greater.size() > smaller.size()) {
                            // update the DAG
                            ConcurrentLinkedQueue<Node> nodes = label_nodesMap.get(greater);
                            log.trace("{}(HSDAGPruningEngine-processLabels) updating [nodes={}]", LoggerUtils.tab(), nodes);

                            if (nodes != null) {
                                for (Node nd : nodes) {
                                    if (nd.getStatus() == NodeStatus.Open) {
                                        nd.setLabel(smaller); // relabel the node with smaller
                                        log.trace("{}(HSDAGPruningEngine-processLabels) reSetLabel [node={}]", LoggerUtils.tab(), nd);
                                        addItemToLabelNodesMap(smaller, nd); // add new label to the map

                                        Set<Constraint> delete = Sets.difference(greater, smaller);
                                        for (Constraint label : delete) {
                                            Node child = nd.getChildren().get(label);

                                            if (child != null) {
                                                incrementCounter(COUNTER_PRUNING);
                                                child.getParents().remove(nd);
                                                nd.getChildren().remove(label);
                                                cleanUpNodes(child);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        log.trace("{}(HSDAGPruningEngine-processLabels) no update DAG", LoggerUtils.tab());
                    }
                });
            });
            // remove the known non-minimal conflicts
            labels.removeAll(nonMinLabels);
            nonMinLabels.forEach(label_nodesMap::remove);

            // add new labels to the list of labels
            hsConstructor.addNodeLabels(labels);
        } else {
            // stop TIMER_NODE_LABEL without saving the time
            stop(TIMER_NODE_LABEL, false);
        }
    }

    /**
     * Removes the subtree from a lookup table starting from the given node.
     * @param node from which the conflictsearch should start
     */
    private void cleanUpNodes(Node node) {
        nodesLookup.remove(node.getPathLabel());
        log.debug("{}(HSDAGPruningEngine-cleanUpNodes) removed pathLabel from nodesLookup [pathLabel={}]", LoggerUtils.tab(), node.getPathLabel());

        if (node.getStatus() == NodeStatus.Open) {
            node.setStatus(NodeStatus.Pruned);
            incrementCounter(COUNTER_CLEANED_NODES);

            log.debug("{}(HSDAGPruningEngine-cleanUpNodes) pruned [node={}]", LoggerUtils.tab(), node);
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

    public Node getReusableNode(Set<Constraint> pathLabels, Constraint arcLabel) {
        Set<Constraint> h = new LinkedHashSet<>(pathLabels);
        h.add(arcLabel);
        return getNodesLookup().get(h);
    }

    public void reset() {
        nodesLookup.clear();
    }

    @Override
    public void dispose() {
        super.dispose();
        nodesLookup.clear();
    }
}
