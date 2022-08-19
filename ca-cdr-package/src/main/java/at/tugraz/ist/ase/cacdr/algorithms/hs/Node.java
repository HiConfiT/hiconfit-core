/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hs;

import at.tugraz.ist.ase.cacdr.algorithms.hs.parameters.AbstractHSParameters;
import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.kb.core.Constraint;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * A data structure representing a node of an HS-dag.
 *
 * <ul>
 *     <li>R. Reiter, A theory of diagnosis from first principles, Artificial Intelligence,
 *     Volume 32, Issue 1, 1987, pp. 57-95.</li>
 *     <li>R. Greiner, B. A. Smith, and R. W. Wilkerson,
 *     A correction to the algorithm in reiter’s theory of diagnosis, Artif Intell,
 *     vol. 41, no. 1, pp. 79–88, 1989</li>
 * </ul>
 *
 * @author z003pczy (Rosu Mara)
 * @author rtaupe (Richard Taupe) - source: <a href="https://github.com/siemens/JMiniZinc">https://github.com/siemens/JMiniZinc</a>
 * @author Dietmar - source: <a href="https://github.com/jaccovs/Master-project">https://github.com/jaccovs/Master-project</a>
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Getter
@NoArgsConstructor
@Slf4j
public class Node {

    private static long generatingNodeId = -1;
    private final long id = ++generatingNodeId;

    /**
     * The tree level
     */
    private int level = 0;

    /**
     * The node status
     */
    @Setter
    private NodeStatus status = NodeStatus.Open;

    /**
     * A label of this node.
     * It can be a minimal conflict set, or a diagnosis. Can be null.
     */
    @Setter
    private Set<Constraint> label;

    /**
     * This is the constraint associated to the arch which comes to this node.
     * Can be null for the root node.
     */
    private Constraint arcLabel = null;

    /**
     * Labels of the path to here
     */
    private final Set<Constraint> pathLabel = new LinkedHashSet<>();

    /**
     * The node's children
     */
    private final Map<Constraint, Node> children = new LinkedHashMap<>();

    /**
     * The node's parent. Can be null for the root node.
     */
    private List<Node> parents = null;

    /**
     * The labelers' parameters
     */
    @Setter
    private AbstractHSParameters parameters;

    /**
     * Constructor for the root node.
     */
    public static Node createRoot(@NonNull Set<Constraint> label,
                                  @NonNull AbstractHSParameters parameters) {
        generatingNodeId = -1;

        Node root = new Node();
        root.label = label;
        root.parameters = parameters;

        log.trace("{}Created root node with [label={}]", LoggerUtils.tab(), label);
        return root;
    }

    /**
     * Constructor for child nodes.
     */
    @Builder
    public Node(@NonNull Node parent,
                @NonNull Constraint arcLabel,
                @NonNull AbstractHSParameters parameters) {
        this.parents = new LinkedList<>();
        this.parents.add(parent);
        this.level = parent.level + 1;
        this.arcLabel = arcLabel;
        this.parameters = parameters;

        this.pathLabel.addAll(parent.pathLabel);
        this.pathLabel.add(arcLabel);

        parent.children.put(arcLabel, this);

        log.trace("{}Created child node with [parent={}, arcLabel={}]", LoggerUtils.tab(), parent, arcLabel);
    }

    /**
     * Adds a parent to this node.
     */
    public void addParent(Node parent) {
        if (isRoot()) {
            throw new IllegalArgumentException("The root node cannot have parents.");
        } else {
            parents.add(parent);

            log.trace("{}Added parent node with [parent={}, child={}]", LoggerUtils.tab(), parent, this);
        }
    }

    /**
     * Adds a child node to this node.
     */
    public void addChild(@NonNull Constraint arcLabel, @NonNull Node child) {
        this.children.put(arcLabel, child);
        child.addParent(this);

        log.trace("{}Added child node with [parent={}, arcLabel={}, child={}]", LoggerUtils.tab(), this, arcLabel, child);
    }

    /**
     * Returns isRoot value
     *
     * @return true if this node is the root node, otherwise false.
     */
    public boolean isRoot() {
        return this.parents == null;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", level=" + level +
                ", status=" + status +
                ", label=" + label +
                ", parameter=" + parameters +
                ", arcLabel=" + arcLabel +
                ", pathLabels=" + pathLabel +
                '}';
    }

    public void dispose() {
        label = null;
        arcLabel = null;
        pathLabel.clear();
        children.clear();
        parents.clear();
        parents = null;
        parameters = null;
    }
}
