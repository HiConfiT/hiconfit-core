/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hs;

import at.tugraz.ist.ase.cacdr.algorithms.hs.labeler.IHSLabelable;
import at.tugraz.ist.ase.cacdr.algorithms.hs.labeler.LabelerType;
import at.tugraz.ist.ase.cacdr.algorithms.hs.parameters.AbstractHSParameters;
import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.kb.core.Constraint;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import static at.tugraz.ist.ase.cacdr.eval.CAEvaluator.*;

/**
 * Implementation of the HS-tree algorithm.
 * IHSLabeler algorithms have to return labels (conflict or diagnosis) which are guaranteed to be minimal.
 * For example: QuickXPlain, MXP, FastDiag
 * <p>
 * source: <a href="https://github.com/jaccovs/Master-project">https://github.com/jaccovs/Master-project</a>
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Slf4j
public class HSTree extends AbstractHSConstructor {

    @Getter
    protected Node root = null;
    protected final ConcurrentLinkedQueue<Node> openNodes = new ConcurrentLinkedQueue<>();

    @Setter
    protected HSTreePruningEngine pruningEngine = null;

    public HSTree(@NonNull IHSLabelable labeler) {
        super(labeler);
        setPruningEngineName("HS-Tree"); // for logging
    }

    /**
     * Builds the HS-tree.
     */
    public void construct() {
        AbstractHSParameters param = getLabeler().getInitialParameters();

        log.debug("{}(HSTree-construct) Constructing the {} for [C={}] >>>", LoggerUtils.tab(), getPruningEngineName(), param.getC());
        LoggerUtils.indent();

        start(TIMER_HS_CONSTRUCTION_SESSION);
        start(TIMER_PATH_LABEL);

        // generate root if there is none
        boolean hasRootLabel = createRoot(param);

        if (!shouldStopConstruction() && hasRootLabel) {
            createNodes();
        }

        stopConstruction();
    }

    protected boolean createRoot(AbstractHSParameters param) {
        boolean hasRootLabel = true;

        if (!hasRoot()) {
            List<Set<Constraint>> labels = computeLabel(getLabeler(), param);
            stop(TIMER_NODE_LABEL);

            if (labels.isEmpty()) {
                hasRootLabel = false;
            } else { // create root node
                Set<Constraint> label = selectLabel(labels);
                root = Node.createRoot(label, param);
                incrementCounter(COUNTER_CONSTRUCTED_NODES);

                openNodes.add(root);

                addNodeLabels(labels); // to reuse labels
                pruningEngine.addItemToLabelNodesMap(label, root);

                log.debug("{}(HSTree-construct) Created root node [root={}]", LoggerUtils.tab(), root);
            }
        }
        return hasRootLabel;
    }

    protected void stopConstruction() {
        LoggerUtils.outdent();
        log.debug("{}<<< return [conflicts={}]", LoggerUtils.tab(), getConflicts());
        log.debug("{}<<< return [diagnoses={}]", LoggerUtils.tab(), getDiagnoses());

        stop(TIMER_HS_CONSTRUCTION_SESSION);
        stop(TIMER_PATH_LABEL, false);

        if (log.isTraceEnabled()) {
            Utils.printInfo(root, getConflicts(), getDiagnoses());
        }
    }

    protected void createNodes() {
        while (hasNodesToExpand()) {
            Node node = getNextNode();

            if (!node.isRoot()) {
                if (pruningEngine.skipNode(node)) continue;

                log.debug("{}(HSTree-createNodes) Processing [node={}]", LoggerUtils.tab(), node);
                LoggerUtils.indent();

                label(node);

                if (shouldStopConstruction()) {
                    LoggerUtils.outdent();
                    break;
                }
            }

            if (node.getStatus() == NodeStatus.Open) {
                expand(node);
            }

            System.gc();
            if (!node.isRoot()) {
                LoggerUtils.outdent();
            }
        }
    }

    protected void label(Node node) {
        // Reusing labels - H(node) ∩ S = {}, then label node by S
        List<Set<Constraint>> labels = pruningEngine.getReusableLabels(node);

        // compute labels if there are none to reuse
        if (labels.isEmpty()) {
            labels = computeLabel(getLabeler(), node);

            pruningEngine.processLabels(labels);
        }

        if (!labels.isEmpty()) {
            Set<Constraint> label = selectLabel(labels);

            node.setLabel(label);
            pruningEngine.addItemToLabelNodesMap(label, node);

            log.debug("{}(HSTree-label) Node [node={}] has label [label={}]", LoggerUtils.tab(), node, label);
        } else { // found a path label
            foundAPathLabelAtNode(node);

            stop(TIMER_PATH_LABEL);
            start(TIMER_PATH_LABEL);
        }
    }

    protected void expand(Node nodeToExpand) {
        log.debug("{}(HSTree-expand) Generating the children nodes of [node={}]", LoggerUtils.tab(), nodeToExpand);
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

            if (!pruningEngine.canPrune(node)) {
                openNodes.add(node);
                log.debug("{}(HSTree-expand) Created [node={}]", LoggerUtils.tab(), node);
            }
        });

        LoggerUtils.outdent();
    }

    protected List<Set<Constraint>> computeLabel(IHSLabelable labeler, AbstractHSParameters param) {
        start(TIMER_NODE_LABEL);
        return labeler.getLabel(param);
    }

    public List<Set<Constraint>> computeLabel(IHSLabelable labeler, Node node) {
        AbstractHSParameters param = node.getParameters();

        start(TIMER_NODE_LABEL);
        return labeler.getLabel(param);
    }

    protected void addNodeLabels(Collection<Set<Constraint>> labels) {
        labels.forEach(label -> {
            getNodeLabels().add(label);
            log.debug("{}{} #{} is found: {}", LoggerUtils.tab(),
                    getLabeler().getType() == LabelerType.CONFLICT ? "Conflict" : "Diagnosis",
                    getNodeLabels().size(), label);
        });
    }

    public void foundAPathLabelAtNode(Node node) {
        node.setStatus(NodeStatus.Checked);
        Set<Constraint> pathLabel = new LinkedHashSet<>(node.getPathLabel());

        addPathLabel(pathLabel);

        log.debug("{}{} #{} is found: {}", LoggerUtils.tab(),
                getLabeler().getType() == LabelerType.CONFLICT ? "Diagnosis" : "Conflict",
                getDiagnoses().size(), node.getPathLabel());
    }

    protected void addPathLabel(Set<Constraint> pathLabel) {
        getPathLabels().add(pathLabel);
    }

    @Override
    protected ConcurrentLinkedQueue<Node> getOpenNodes() {
        return openNodes;
    }

    /**
     * Selects a label (conflict/diagnosis) to label a node from a list of conflicts/diagnoses.
     * This implementation simply returns the first conflict/diagnosis from the given list.
     * @param labels list of labels (conflicts/diagnoses)
     * @return node label
     */
    public Set<Constraint> selectLabel(List<Set<Constraint>> labels) {
        return labels.get(0);
    }

    protected boolean hasNodesToExpand() {
        return !openNodes.isEmpty();
    }

    protected Node getNextNode() {
        return openNodes.remove();
    }

    protected boolean hasRoot() {
        return this.root != null;
    }

    @Override
    public void resetEngine() {
        super.resetEngine();
        this.root = null;
        this.openNodes.clear();
        this.pruningEngine.reset();
    }

    @Override
    public void dispose() {
        super.dispose();
        this.root = null;
        this.openNodes.clear();
        this.pruningEngine.dispose();
        this.pruningEngine = null;
    }
}

