/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hs.labeler;

import at.tugraz.ist.ase.cacdr.algorithms.hs.parameters.AbstractHSParameters;
import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.kb.core.Constraint;
import lombok.NonNull;

import java.util.List;
import java.util.Set;

/**
 * Defines an interface for HS algorithms' theorem prover.
 * In the "label" step, the HS-tree and HSDAG algorithms use a theorem prover to determine
 * a conflict or a diagnosis.
 *
 * @author David - source: <a href="https://github.com/jaccovs/Master-project">https://github.com/jaccovs/Master-project</a>
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public interface IHSLabelable extends Cloneable {

    /**
     * Returns the reasoning type of the labeler.
     * @return {@link LabelerType}
     */
    LabelerType getType();

    /**
     * Returns the initial parameters
     * @return the initial parameters {@link AbstractHSParameters}
     */
    AbstractHSParameters getInitialParameters();

    /**
     * Identifies a conflict or diagnosis.
     * @param parameters the current parameters
     * @return a list of conflicts or diagnoses
     */
    List<Set<Constraint>> getLabel(@NonNull AbstractHSParameters parameters);

    /**
     * Identifies the new node's parameters on the basis of the parent node's parameters.
     * @param param_parentNode the parameters of the parent node
     * @param arcLabel the arcLabel leading to the new node
     * @return new parameters for the new node
     */
    AbstractHSParameters createParameter(@NonNull AbstractHSParameters param_parentNode, @NonNull Constraint arcLabel);

    IHSLabelable getInstance(@NonNull ChocoConsistencyChecker checker);

    void dispose();
}
