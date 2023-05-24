/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hs.labeler;

import at.tugraz.ist.ase.cacdr.algorithms.DirectDebug;
import at.tugraz.ist.ase.cacdr.algorithms.hs.parameters.AbstractHSParameters;
import at.tugraz.ist.ase.cacdr.algorithms.hs.parameters.DirectDebugParameters;
import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.cdrmodel.test.ITestCase;
import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.kb.core.Constraint;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * HSLabeler for DirectDebug algorithm
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Getter
@Slf4j
public class DirectDebugLabeler extends DirectDebug implements IHSLabelable {

    private DirectDebugParameters initialParameters;

    /**
     * Constructor with parameters which contain C, and B
     * @param checker a {@link ChocoConsistencyChecker} object
     * @param parameters a {@link DirectDebugParameters} object
     */
    public DirectDebugLabeler(@NonNull ChocoConsistencyChecker checker, @NonNull DirectDebugParameters parameters) {
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
        checkArgument(parameters instanceof DirectDebugParameters, "parameter must be an instance of KBDiagParameters");
        DirectDebugParameters params = (DirectDebugParameters) parameters;

        if (params.getTC().isEmpty()) {
            log.debug("{}No test cases left", LoggerUtils.tab());
        }

        if (!params.getTC().isEmpty() && params.getC().size() > 1 && (params.getB().isEmpty() || checker.isConsistent(params.getB(), params.getTC(), true).isEmpty())) {
            Map.Entry<Set<ITestCase>, Set<Constraint>> result = findDiagnosis(params.getC(), params.getB(), params.getTC());

            Set<Constraint> diag = result.getValue();
            Set<ITestCase> TCp = result.getKey();

            // update the parameters, which will be used by the children's nodes
            // TCp
            params.setTCp(TCp);

            if (!diag.isEmpty()) {
                return Collections.singletonList(diag);
            }
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
        checkArgument(param_parentNode instanceof DirectDebugParameters, "parameter must be an instance of KBDiagParameters");
        DirectDebugParameters params = (DirectDebugParameters) param_parentNode;

        Set<Constraint> C = new LinkedHashSet<>(params.getC());
        C.remove(arcLabel);

        Set<Constraint> B = new LinkedHashSet<>(params.getB());
        B.add(arcLabel);

        Set<ITestCase> TC;
        if (params.getTCp() != null) {
            TC = new LinkedHashSet<>(params.getTCp());
        } else  {
            TC = Collections.emptySet();
        }

        return DirectDebugParameters.builder()
                .C(C)
                .B(B)
                .TV(params.getTV())
                .TC(TC).build();
    }

    public IHSLabelable getInstance(@NonNull ChocoConsistencyChecker checker) {
        return new DirectDebugLabeler(checker, this.initialParameters);
    }

    @Override
    public void dispose() {
        super.dispose();
        initialParameters = null;
    }
}
