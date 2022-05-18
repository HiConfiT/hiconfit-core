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
import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.kb.core.Constraint;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * An abstract class for HS algorithms
 *
 * source: <a href="https://github.com/jaccovs/Master-project">https://github.com/jaccovs/Master-project</a>
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Getter
public abstract class AbstractHSConstructor {
    // for evaluation
    public static final String TIMER_HS_CONSTRUCTION_SESSION = "Timer for HS construction session ";
    public static final String TIMER_NODE_LABEL = "Timer for node label ";
    public static final String TIMER_PATH_LABEL = "Timer for path label ";

    public static final String COUNTER_CONSTRUCTED_NODES = "The number of constructed nodes:";
    public static final String COUNTER_CLOSE_1 = "The number of 3.i closed nodes:";
    public static final String COUNTER_CLOSE_2 = "The number of 3.ii closed nodes:";
    public static final String COUNTER_REUSE_LABELS = "The number of reused labels:";
    public static final String COUNTER_REUSE_NODES = "The number of reused nodes:";
    public static final String COUNTER_PRUNING = "The number of pruning paths:";
    public static final String COUNTER_CLEANED_NODES = "The number of cleaned nodes:";

    @Setter
    private int maxNumberOfDiagnoses = -1; // -1 - all diagnoses
    @Setter
    private int maxNumberOfConflicts = -1; // -1 - all conflicts

    @Setter
    private int maxDepth = 0;

    /**
     * Use setter to preset known conflicts
     */
    @Setter
    private List<Set<Constraint>> nodeLabels = new LinkedList<>(); // conflict/diagnosis
    private final List<Set<Constraint>> pathLabels = new LinkedList<>(); // diagnosis/conflict

    private IHSLabelable labeler;

    public AbstractHSConstructor(IHSLabelable labeler) {
        this.labeler = labeler;
    }

    /**
     * Returns identified conflicts.
     * @return list of conflicts
     */
    public List<Set<Constraint>> getConflicts() {
        if (labeler.getType() == LabelerType.CONFLICT) {
            return nodeLabels;
        } else {
            return pathLabels;
        }
    }

    /**
     * Returns identified diagnoses.
     * @return list of diagnoses
     */
    public List<Set<Constraint>> getDiagnoses() {
        if (labeler.getType() == LabelerType.CONFLICT) {
            return pathLabels;
        } else {
            return nodeLabels;
        }
    }

    /**
     * Start the HS construction process
     */
    public abstract void construct();

    /**
     * Returns <code>true</code> if the goals of the diagnosis computations are achieved.
     * Override this method to add more stopping criteria.
     * @return <code>true</code> if the required number of diagnoses is found,
     * or the required number of conflicts is found.
     */
    public boolean stopConstruction() {
        // when the number of already identified diagnoses is greater than the limit, stop the computation
        boolean condition1 = (getMaxNumberOfDiagnoses() != -1 && getMaxNumberOfDiagnoses() <= getDiagnoses().size());
        // OR when the number of already identified conflicts is greater than the limit, stop the computation
        boolean condition2 = (getMaxNumberOfConflicts() != -1 && getMaxNumberOfConflicts() <= getConflicts().size());
        return condition1 || condition2;
    }

    protected abstract void addNodeLabels(Collection<Set<Constraint>> labels);
    protected abstract void addItemToLabelNodesMap(Set<Constraint> label, Node node);

    /**
     * Reverts the state of the engine to how it was when first instantiated
     */
    public void resetEngine() {
        nodeLabels.clear();
        pathLabels.clear();
    }

    public void dispose() {
        this.pathLabels.clear();
        this.nodeLabels.clear();
        this.labeler = null;
    }
}
