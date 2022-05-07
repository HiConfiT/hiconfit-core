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
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static at.tugraz.ist.ase.cacdr.eval.CAEvaluator.*;
import static at.tugraz.ist.ase.common.ConstraintUtils.hasIntersection;

/**
 * Implementation of the HS-tree algorithm.
 * IHSLabeler algorithms have to return labels (conflict or diagnosis) which are guaranteed to be minimal.
 * For example: QuickXPlain, MXP, FastDiag
 *
 * source: <a href="https://github.com/jaccovs/Master-project">https://github.com/jaccovs/Master-project</a>
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Slf4j
public class HSTree extends AbstractHSConstructor {

    @Getter
    private Node root = null;
    protected final Queue<Node> openNodes = new LinkedList<>();
    // Map of <conflict, list of nodes which have the conflict as its label>
    protected Map<Set<Constraint>, List<Node>> cs_nodesMap = new LinkedHashMap<>();

    public HSTree(IHSLabelable labeler, ChocoConsistencyChecker checker) {
        super(labeler, checker);
    }

    /**
     * Builds the HS-tree.
     */
    public void construct() {
        AbstractHSParameters param = getLabeler().getInitialParameters();

        log.debug("{}Constructing the HS-tree for [C={}] >>>", LoggerUtils.tab, param.getC());
        LoggerUtils.indent();

        start(TIMER_HS_CONSTRUCTION_SESSION);
        start(TIMER_DIAGNOSIS);

        // generate root if there is none
        if (!hasRoot()) {
            start(TIMER_CONFLICT);
            List<Set<Constraint>> conflicts = getLabeler().getLabel(param);
            stop(TIMER_CONFLICT);

            if (conflicts.isEmpty()) {
                endConstruction();
                return;
            }

            // create root node
            Set<Constraint> label = selectConflict(conflicts);
            root = Node.createRoot(label, param);
            incrementCounter(COUNTER_CONSTRUCTED_NODES);

            addConflicts(conflicts); // to reuse conflicts
            addItemToCSNodesMap(label, root);

            if (stopConstruction()) {
                endConstruction();
                return;
            }

            expand(root);
        }

        while (hasNodesToExpand()) {
            Node node = getNextNode();
            if (skipNode(node)) continue;
            log.trace("{}Processing [node={}]", LoggerUtils.tab, node);
            LoggerUtils.indent();

            label(node);
            if (stopConstruction()) {
                LoggerUtils.outdent();
                endConstruction();
                return;
            }

            if (node.getStatus() == NodeStatus.Open) {
                expand(node);
            }

            System.gc();
            LoggerUtils.outdent();
        }

        endConstruction();
    }

    protected void endConstruction() {
        LoggerUtils.outdent();
        log.debug("{}<<< return [conflicts={}]", LoggerUtils.tab, getConflicts());
        log.debug("{}<<< return [diagnoses={}]", LoggerUtils.tab, getDiagnoses());

        stop(TIMER_HS_CONSTRUCTION_SESSION);
        stop(TIMER_DIAGNOSIS, false);

        if (log.isTraceEnabled()) {
            Utils.printInfo(root, getConflicts(), getDiagnoses());
        }
    }

    protected void label(Node node) {
        if (node.getLabel() == null) {
            // Reusing conflicts - H(node) ∩ S = {}, then label node by S
            List<Set<Constraint>> conflicts = getReusableConflicts(node);

            // compute conflicts if there are none to reuse
            if (conflicts.isEmpty()) {
                conflicts = computeLabel(node);
            }
            if (conflicts.isEmpty()) {
                node.setStatus(NodeStatus.Checked);
                Set<Constraint> diag = new LinkedHashSet<>(node.getPathLabels());
                getDiagnoses().add(diag);
                log.debug("{}Diagnosis #{} is found: {}", LoggerUtils.tab, getDiagnoses().size(), node.getPathLabels());

                stop(TIMER_DIAGNOSIS);
                start(TIMER_DIAGNOSIS);
                return;
            }
            Set<Constraint> label = selectConflict(conflicts);
            node.setLabel(label);
            addItemToCSNodesMap(label, node);
        }
    }

    protected List<Set<Constraint>> getReusableConflicts(Node node) {
        List<Set<Constraint>> conflicts = new LinkedList<>();
        for (Set<Constraint> conflict : getConflicts()) {
            // H(node) ∩ S = {}
            if (!hasIntersection(node.getPathLabels(), conflict)) {
                conflicts.add(conflict);
                incrementCounter(COUNTER_REUSE_CONFLICT);
                log.trace("{}Reuse [conflict={}, node={}]", LoggerUtils.tab, conflict, node);
                return conflicts;
            }
        }
        return conflicts;
    }

    protected List<Set<Constraint>> computeLabel(Node node) {
        AbstractHSParameters param = node.getParameters();

        start(TIMER_CONFLICT);
        List<Set<Constraint>> conflicts = getLabeler().getLabel(param);

        if (!conflicts.isEmpty()) {
            stop(TIMER_CONFLICT);

            addConflicts(conflicts);
        } else {
            // stop TIMER_CONFLICT without saving the time
            stop(TIMER_CONFLICT, false);
        }
        return conflicts;
    }

    protected void addConflicts(Collection<Set<Constraint>> conflicts) {
        for (Set<Constraint> conflict : conflicts) {
            getConflicts().add(conflict);
            log.debug("{}Conflict #{} is found: {}", LoggerUtils.tab, getConflicts().size(), conflict);
        }
    }

    protected void addItemToCSNodesMap(Set<Constraint> cs, Node node) {
        log.trace("{}addItemToCSNodesMap [cs_nodesMap.size={}, cs={}, node={}]", LoggerUtils.tab, cs_nodesMap.size(), cs, node);
        LoggerUtils.indent();
        if (!cs_nodesMap.containsKey(cs)) {
            cs_nodesMap.put(cs, new LinkedList<>());
            log.trace("{}Add new item", LoggerUtils.tab);
        }
        cs_nodesMap.get(cs).add(node);
        log.trace("{}Updated [cs_nodesMap.size={}]", LoggerUtils.tab, cs_nodesMap.size());
        LoggerUtils.outdent();
    }

    /**
     * Selects a conflict to label a node from a list of conflicts.
     * This implementation simply returns the first conflict from the given list.
     * @param conflicts list of conflicts
     * @return node label
     */
    protected Set<Constraint> selectConflict(List<Set<Constraint>> conflicts) {
        return conflicts.get(0);
    }

    protected boolean hasNodesToExpand() {
        return !openNodes.isEmpty();
    }

    protected Node getNextNode() {
        return openNodes.remove();
    }

    protected boolean skipNode(Node node) {
        boolean condition1 = getMaxDepth() != 0 && getMaxDepth() <= node.getLevel();
        return node.getStatus() != NodeStatus.Open || condition1 || canPrune(node);
    }

    protected void expand(Node nodeToExpand) {
        log.trace("{}Generating the children nodes of [node={}]", LoggerUtils.tab, nodeToExpand);
        LoggerUtils.indent();

        for (Constraint arcLabel : nodeToExpand.getLabel()) {
            AbstractHSParameters param_parentNode = nodeToExpand.getParameters();
            AbstractHSParameters new_param = getLabeler().createParameter(param_parentNode, arcLabel);

            Node node = Node.builder()
                    .parent(nodeToExpand)
                    .parameters(new_param)
                    .arcLabel(arcLabel)
                    .build();
            incrementCounter(COUNTER_CONSTRUCTED_NODES);

            if (!canPrune(node)) {
                openNodes.add(node);
            }
        }

        LoggerUtils.outdent();
    }

    protected boolean canPrune(Node node) {
        // 3.i - if n is checked, and n' is such that H(n) ⊆ H(n'), then close the node n'
        // n is a diagnosis
        for (Set<Constraint> diag : getDiagnoses()) {
            if (node.getPathLabels().containsAll(diag)) {
                node.setStatus(NodeStatus.Closed);
                incrementCounter(COUNTER_CLOSE_1);

                log.trace("{}Closed [node={}]", LoggerUtils.tab, node);

                return true;
            }
        }

        // 3.ii - if n has been generated and node n' is such that H(n') = H(n), then close node n'
        for (Node n : openNodes) {
            if (n.getPathLabels().size() == node.getPathLabels().size()
                    && Sets.difference(n.getPathLabels(), node.getPathLabels()).isEmpty()) {
                node.setStatus(NodeStatus.Closed);
                incrementCounter(COUNTER_CLOSE_2);

                log.trace("{}Closed [node={}]", LoggerUtils.tab, node);

                return true;
            }
        }

        return false;
    }

    protected boolean hasRoot() {
        return this.root != null;
    }

    @Override
    public void resetEngine() {
        super.resetEngine();
        this.root = null;
        this.cs_nodesMap.clear();
        this.openNodes.clear();
    }

    @Override
    public void dispose() {
        super.dispose();
        this.root = null;
        this.openNodes.clear();
        this.cs_nodesMap.clear();
    }
}
