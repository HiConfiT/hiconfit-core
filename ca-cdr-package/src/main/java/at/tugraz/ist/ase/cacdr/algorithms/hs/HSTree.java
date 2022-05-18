/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hs;

import at.tugraz.ist.ase.cacdr.algorithms.hs.labeler.IHSLabelable;
import at.tugraz.ist.ase.cacdr.algorithms.hs.labeler.LabelerType;
import at.tugraz.ist.ase.cacdr.algorithms.hs.parameters.AbstractHSParameters;
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
    // Map of <label, list of nodes which have the label as its label>
    protected Map<Set<Constraint>, List<Node>> label_nodesMap = new LinkedHashMap<>();

    public HSTree(IHSLabelable labeler) {
        super(labeler);
    }

    /**
     * Builds the HS-tree.
     */
    public void construct() {
        AbstractHSParameters param = getLabeler().getInitialParameters();

        log.debug("{}Constructing the HS-tree for [C={}] >>>", LoggerUtils.tab(), param.getC());
        LoggerUtils.indent();

        start(TIMER_HS_CONSTRUCTION_SESSION);
        start(TIMER_PATH_LABEL);

        // generate root if there is none
        if (!hasRoot()) {
            start(TIMER_NODE_LABEL);
            List<Set<Constraint>> labels = getLabeler().getLabel(param);
            stop(TIMER_NODE_LABEL);

            if (labels.isEmpty()) {
                endConstruction();
                return;
            }

            // create root node
            Set<Constraint> label = selectLabel(labels);
            root = Node.createRoot(label, param);
            incrementCounter(COUNTER_CONSTRUCTED_NODES);

            addNodeLabels(labels); // to reuse labels
            addItemToLabelNodesMap(label, root);

            if (stopConstruction()) {
                endConstruction();
                return;
            }

            expand(root);
        }

        while (hasNodesToExpand()) {
            Node node = getNextNode();
            if (skipNode(node)) continue;
            log.trace("{}Processing [node={}]", LoggerUtils.tab(), node);
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
        log.debug("{}<<< return [conflicts={}]", LoggerUtils.tab(), getConflicts());
        log.debug("{}<<< return [diagnoses={}]", LoggerUtils.tab(), getDiagnoses());

        stop(TIMER_HS_CONSTRUCTION_SESSION);
        stop(TIMER_PATH_LABEL, false);

        if (log.isTraceEnabled()) {
            Utils.printInfo(root, getConflicts(), getDiagnoses());
        }
    }

    protected void label(Node node) {
        if (node.getLabel() == null) {
            // Reusing labels - H(node) ∩ S = {}, then label node by S
            List<Set<Constraint>> labels = getReusableLabels(node);

            // compute labels if there are none to reuse
            if (labels.isEmpty()) {
                labels = computeLabel(node);
            }
            if (labels.isEmpty()) {
                node.setStatus(NodeStatus.Checked);
                Set<Constraint> pathLabel = new LinkedHashSet<>(node.getPathLabel());
                getPathLabels().add(pathLabel);
                log.debug("{}{} #{} is found: {}", LoggerUtils.tab(),
                        getLabeler().getType() == LabelerType.CONFLICT ? "Diagnosis" : "Conflict",
                        getDiagnoses().size(), node.getPathLabel());

                stop(TIMER_PATH_LABEL);
                start(TIMER_PATH_LABEL);
                return;
            }
            Set<Constraint> label = selectLabel(labels);
            node.setLabel(label);
            addItemToLabelNodesMap(label, node);
        }
    }

    protected List<Set<Constraint>> getReusableLabels(Node node) {
        List<Set<Constraint>> labels = new LinkedList<>();
        for (Set<Constraint> label : getNodeLabels()) {
            // H(node) ∩ S = {}
            if (!hasIntersection(node.getPathLabel(), label)) {
                labels.add(label);
                incrementCounter(COUNTER_REUSE_LABELS);
                log.trace("{}Reuse [label={}, node={}]", LoggerUtils.tab(), label, node);
                return labels;
            }
        }
        return labels;
    }

    protected List<Set<Constraint>> computeLabel(Node node) {
        AbstractHSParameters param = node.getParameters();

        start(TIMER_NODE_LABEL);
        List<Set<Constraint>> labels = getLabeler().getLabel(param);

        if (!labels.isEmpty()) {
            stop(TIMER_NODE_LABEL);

            addNodeLabels(labels);
        } else {
            // stop TIMER_NODE_LABEL without saving the time
            stop(TIMER_NODE_LABEL, false);
        }
        return labels;
    }

    protected void addNodeLabels(Collection<Set<Constraint>> labels) {
        labels.forEach(label -> {
            getNodeLabels().add(label);
            log.debug("{}{} #{} is found: {}", LoggerUtils.tab(),
                    getLabeler().getType() == LabelerType.CONFLICT ? "Conflict" : "Diagnosis",
                    getNodeLabels().size(), label);
        });
    }

    protected void addItemToLabelNodesMap(Set<Constraint> label, Node node) {
        log.trace("{}addItemToLabelNodesMap [label_nodesMap.size={}, label={}, node={}]", LoggerUtils.tab(), label_nodesMap.size(), label, node);
        LoggerUtils.indent();
        if (!label_nodesMap.containsKey(label)) {
            label_nodesMap.put(label, new LinkedList<>());
            log.trace("{}Add new item", LoggerUtils.tab());
        }
        label_nodesMap.get(label).add(node);
        log.trace("{}Updated [label_nodesMap.size={}]", LoggerUtils.tab(), label_nodesMap.size()); 
        LoggerUtils.outdent();
    }

    /**
     * Selects a conflict/diagnosis to label a node from a list of conflicts/diagnoses.
     * This implementation simply returns the first conflict/diagnosis from the given list.
     * @param labels list of labels (conflicts/diagnoses)
     * @return node label
     */
    protected Set<Constraint> selectLabel(List<Set<Constraint>> labels) {
        return labels.get(0);
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
        log.trace("{}Generating the children nodes of [node={}]", LoggerUtils.tab(), nodeToExpand);
        LoggerUtils.indent();

        nodeToExpand.getLabel().forEach(arcLabel -> {
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
        });

        LoggerUtils.outdent();
    }

    protected boolean canPrune(Node node) {
        // 3.i - if n is checked, and n' is such that H(n) ⊆ H(n'), then close the node n'
        // n is a diagnosis
        for (Set<Constraint> pathLabel : getPathLabels()) {
            if (node.getPathLabel().containsAll(pathLabel)) {
                node.setStatus(NodeStatus.Closed);
                incrementCounter(COUNTER_CLOSE_1);

                log.trace("{}Closed [node={}]", LoggerUtils.tab(), node);

                return true;
            }
        }

        // 3.ii - if n has been generated and node n' is such that H(n') = H(n), then close node n'
        for (Node n : openNodes) {
            if (n.getPathLabel().size() == node.getPathLabel().size()
                    && Sets.difference(n.getPathLabel(), node.getPathLabel()).isEmpty()) {
                node.setStatus(NodeStatus.Closed);
                incrementCounter(COUNTER_CLOSE_2);

                log.trace("{}Closed [node={}]", LoggerUtils.tab(), node);

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
        this.label_nodesMap.clear();
        this.openNodes.clear();
    }

    @Override
    public void dispose() {
        super.dispose();
        this.root = null;
        this.openNodes.clear();
        this.label_nodesMap.clear();
    }
}
