/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hs.labeler;

import at.tugraz.ist.ase.cacdr.algorithms.FlexDiag;
import at.tugraz.ist.ase.cacdr.algorithms.hs.parameters.AbstractHSParameters;
import at.tugraz.ist.ase.cacdr.algorithms.hs.parameters.FlexDiagParameters;
import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.kb.core.Constraint;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * HSLabeler for FlexDiag algorithm
 */
@Getter
public class FlexDiagLabeler extends FlexDiag implements IHSLabelable {

    private final FlexDiagParameters initialParameters;

    /**
     * Constructor with parameters which contain C, and AC
     * @param checker a {@link ChocoConsistencyChecker} object
     * @param parameters a {@link FlexDiagParameters} object
     */
    public FlexDiagLabeler(@NonNull ChocoConsistencyChecker checker, @NonNull FlexDiagParameters parameters) {
        super(checker);
        this.initialParameters = parameters;
    }

    /**
     * Returns the reasoning type of the algorithm
     * @return {@link LabelerType}
     */
    public LabelerType getType() {
        return LabelerType.DIAGNOSIS;
    }

    /**
     * Identifies a diagnosis.
     * @param parameters the current parameters
     * @return a diagnosis
     */
    public List<Set<Constraint>> getLabel(@NonNull AbstractHSParameters parameters) {
        checkArgument(parameters instanceof FlexDiagParameters, "parameter must be an instance of FlexDiagParameters");
        FlexDiagParameters params = (FlexDiagParameters) parameters;

        Set<Constraint> diag = findDiagnosis(params.getC(), params.getAC(), params.getM());

        if (!diag.isEmpty()) {
            return Collections.singletonList(diag);
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
        checkArgument(param_parentNode instanceof FlexDiagParameters, "parameter must be an instance of FlexDiagParameters");
        FlexDiagParameters params = (FlexDiagParameters) param_parentNode;

        Set<Constraint> S = new LinkedHashSet<>(params.getC());
        S.remove(arcLabel);

        Set<Constraint> AC = new LinkedHashSet<>(params.getAC());

        return FlexDiagParameters.builder()
                .S(S)
                .AC(AC)
                .m(params.getM())
                .build();
    }
}
