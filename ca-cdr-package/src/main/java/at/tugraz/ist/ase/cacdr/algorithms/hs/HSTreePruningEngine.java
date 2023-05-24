/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
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

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import static at.tugraz.ist.ase.cacdr.algorithms.hs.AbstractHSConstructor.*;
import static at.tugraz.ist.ase.common.ConstraintUtils.hasIntersection;
import static at.tugraz.ist.ase.eval.PerformanceEvaluator.incrementCounter;
import static at.tugraz.ist.ase.eval.PerformanceEvaluator.stop;

@Slf4j
public class HSTreePruningEngine {
    // Map of <label, list of nodes which have the label as its label>
    @Getter
    protected final ConcurrentHashMap<Set<Constraint>, ConcurrentLinkedQueue<Node>> label_nodesMap = new ConcurrentHashMap<>();
    private final Semaphore label_nodesMap_Semaphore = new Semaphore(1);

    protected AbstractHSConstructor hsConstructor;

    public HSTreePruningEngine(@NonNull AbstractHSConstructor hsConstructor) {
        this.hsConstructor = hsConstructor;
    }

    public void acquireLabelNodesMap() throws InterruptedException {
        label_nodesMap_Semaphore.acquire();
        log.debug("{}(HSTreePruningEngine) acquired for label_nodesMap", LoggerUtils.tab());
    }

    public void releaseLabelNodesMap() {
        label_nodesMap_Semaphore.release();
        log.debug("{}(HSTreePruningEngine) released for label_nodesMap", LoggerUtils.tab());
    }

    public void processLabels(@NonNull List<Set<Constraint>> labels) {
        if (!labels.isEmpty()) {
            stop(TIMER_NODE_LABEL);

            hsConstructor.addNodeLabels(labels);
        } else {
            // stop TIMER_NODE_LABEL without saving the time
            stop(TIMER_NODE_LABEL, false);
        }
    }

    public void addItemToLabelNodesMap(@NonNull Set<Constraint> label, @NonNull Node node) {
        label_nodesMap.putIfAbsent(label, new ConcurrentLinkedQueue<>());
        label_nodesMap.get(label).add(node);
        log.debug("{}(HSTreePruningEngine-addItemToLabelNodesMap) updated [label_nodesMap.size={}, label={}, node.id={}]", LoggerUtils.tab(), label_nodesMap.size(), label, node.getId());
    }

    public List<Set<Constraint>> getReusableLabels(@NonNull Node node) {
        List<Set<Constraint>> labels = new LinkedList<>();
        for (Set<Constraint> label : hsConstructor.getNodeLabels()) {
            // H(node) ∩ S = {}
            if (!hasIntersection(node.getPathLabel(), label)) {
                labels.add(label);
                incrementCounter(COUNTER_REUSE_LABELS);
                log.debug("{}(HSTreePruningEngine-getReusableLabels) Reuse [label={}, node={}]", LoggerUtils.tab(), label, node);
            }
        }
        return labels;
    }

    public boolean skipNode(@NonNull Node node) {
        boolean condition1 = hsConstructor.getMaxDepth() != 0 && hsConstructor.getMaxDepth() < node.getLevel();
        return node.getStatus() != NodeStatus.Open || condition1 || canPrune(node);
    }

    public boolean canPrune(@NonNull Node node) {
        // 3.i - if n is checked, and n' is such that H(n) ⊆ H(n'), then close the node n'
        // n is a diagnosis
        for (Set<Constraint> pathLabel : hsConstructor.getPathLabels()) {
            if (node.getPathLabel().containsAll(pathLabel)) {
                node.setStatus(NodeStatus.Closed);
                incrementCounter(COUNTER_CLOSE_1);

                log.debug("{}(HSTreePruningEngine-canPrune_3i) Closed [node={}]", LoggerUtils.tab(), node);

                return true;
            }
        }

        // 3.ii - if n has been generated and node n' is such that H(n') = H(n), then close node n'
        for (Node n : hsConstructor.getOpenNodes()) {
            if (n.getPathLabel().size() == node.getPathLabel().size()
                    && Sets.difference(n.getPathLabel(), node.getPathLabel()).isEmpty()) {
                node.setStatus(NodeStatus.Closed);
                incrementCounter(COUNTER_CLOSE_2);

                log.debug("{}(HSTreePruningEngine-canPrune_3i) Closed [node={}]", LoggerUtils.tab(), node);

                return true;
            }
        }

        return false;
    }

    public void reset() {
        this.label_nodesMap.clear();
    }

    public void dispose() {
        label_nodesMap.clear();
        hsConstructor = null;
    }
}

