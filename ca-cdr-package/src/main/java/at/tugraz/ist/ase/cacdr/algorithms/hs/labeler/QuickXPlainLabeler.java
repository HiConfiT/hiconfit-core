/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hs.labeler;

import at.tugraz.ist.ase.cacdr.algorithms.QuickXPlain;
import at.tugraz.ist.ase.cacdr.algorithms.hs.parameters.AbstractHSParameters;
import at.tugraz.ist.ase.cacdr.algorithms.hs.parameters.FastDiagV2Parameters;
import at.tugraz.ist.ase.cacdr.algorithms.hs.parameters.QuickXPlainParameters;
import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.kb.core.Constraint;
import lombok.Getter;
import lombok.NonNull;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * HSLabeler for QuickXPlain algorithm
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Getter
public class QuickXPlainLabeler extends QuickXPlain implements IHSLabelable {

    private QuickXPlainParameters initialParameters;

    /**
     * Constructor with parameters which contain C, and B
     * @param checker a {@link ChocoConsistencyChecker} object
     * @param parameters a {@link FastDiagV2Parameters} object
     */
    public QuickXPlainLabeler(@NonNull ChocoConsistencyChecker checker, @NonNull QuickXPlainParameters parameters) { // @NonNull Set<Constraint> C, @NonNull Set<Constraint> B
        super(checker);
        this.initialParameters = parameters;
    }

    /**
     * Returns the reasoning type of the algorithm
     * @return {@link LabelerType}
     */
    public LabelerType getType() {
        return LabelerType.CONFLICT;
    }

    /**
     * Identifies a conflict.
     * @param parameters the current parameters
     * @return a conflict
     */
    public List<Set<Constraint>> getLabel(@NonNull AbstractHSParameters parameters) {// Set<Constraint> C) {
        checkArgument(parameters instanceof QuickXPlainParameters, "parameter must be an instance of QuickXPlainParameter");
        QuickXPlainParameters params = (QuickXPlainParameters) parameters;

        Set<Constraint> cs = findConflictSet(params.getC(), params.getB());

        if (!cs.isEmpty()) {
            // reverse the order of the constraints
            List<Constraint> csList = new LinkedList<>(cs);
            Collections.reverse(csList);

            return Collections.singletonList(new LinkedHashSet<>(csList));
        }
        return Collections.emptyList();
    }

    /**
     * Identifies the new node's parameters on the basis of the parent node's parameters.
     * @param param_parentNode the parameters of the parent node
     * @param arcLabel the arcLabel leading to the new node
     * @return new parameters for the new node
     */
    public AbstractHSParameters createParameter(@NonNull AbstractHSParameters param_parentNode, @NonNull Constraint arcLabel) {
        checkArgument(param_parentNode instanceof QuickXPlainParameters, "parameter must be an instance of QuickXPlainParameter");
        QuickXPlainParameters params = (QuickXPlainParameters) param_parentNode;

        Set<Constraint> C = new LinkedHashSet<>(params.getC());
        C.remove(arcLabel);

        Set<Constraint> B = new LinkedHashSet<>(params.getB());

        return QuickXPlainParameters.builder()
                .C(C)
                .B(B).build();
    }

    public IHSLabelable getInstance(@NonNull ChocoConsistencyChecker checker) {
        return new QuickXPlainLabeler(checker, this.initialParameters);
    }

    @Override
    public void dispose() {
        super.dispose();
        initialParameters = null;
    }
}
