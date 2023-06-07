/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hs;

import at.tugraz.ist.ase.cacdr.algorithms.hs.labeler.IHSLabelable;
import at.tugraz.ist.ase.cacdr.algorithms.hs.parameters.AbstractHSParameters;
import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.kb.core.Constraint;
import lombok.extern.slf4j.Slf4j;

import static at.tugraz.ist.ase.eval.PerformanceEvaluator.incrementCounter;

/**
 * Implementation of the HS-dag algorithm.
 * IHSLabeler algorithms could return labels (conflict or diagnosis) which are not minimal.
 * <p>
 * source: <a href="https://github.com/jaccovs/Master-project">https://github.com/jaccovs/Master-project</a>
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Slf4j
public class HSDAG extends HSTree {

//    @Setter
//    protected HSDAGPruningEngine pruningEngine = null;

    public HSDAG(IHSLabelable labeler) {
        super(labeler);
        setPruningEngineName("HS-DAG");
    }

    @Override
    protected void expand(Node nodeToExpand) {
        log.debug("{}(HSDAG-expand) Generating the children nodes of [node={}]", LoggerUtils.tab(), nodeToExpand);
        LoggerUtils.indent();

        for (Constraint arcLabel : nodeToExpand.getLabel()) {
            AbstractHSParameters param_parentNode = nodeToExpand.getParameters();
            AbstractHSParameters new_param = getLabeler().createParameter(param_parentNode, arcLabel);

            // rule 1.a - reuse node
            Node node = ((HSDAGPruningEngine)pruningEngine).getReusableNode(nodeToExpand.getPathLabel(), arcLabel);
            if (node != null) {
                node.addParent(nodeToExpand);

                incrementCounter(COUNTER_REUSE_NODES);
                log.debug("{}(HSDAG-expand) Reusing [node={}]", LoggerUtils.tab(), node);
            } else { // rule 1.b - generate a new node
                node = Node.builder()
                        .parent(nodeToExpand)
                        .parameters(new_param)
                        .arcLabel(arcLabel)
                        .build();
                ((HSDAGPruningEngine)pruningEngine).getNodesLookup().put(node.getPathLabel(), node);
                incrementCounter(COUNTER_CONSTRUCTED_NODES);

                if (!pruningEngine.canPrune(node)) {
                    openNodes.add(node);
                    log.debug("{}(HSDAG-expand) Created [node={}]", LoggerUtils.tab(), node);
                }
            }
        }

        LoggerUtils.outdent();
    }

    @Override
    public void resetEngine() {
        super.resetEngine();
        pruningEngine.reset();
    }

    @Override
    public void dispose() {
        super.dispose();
        pruningEngine.dispose();
        pruningEngine = null;
    }
}
