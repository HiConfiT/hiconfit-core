/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cdrmodel.fm;

import at.tugraz.ist.ase.cdrmodel.CDRModel;
import at.tugraz.ist.ase.cdrmodel.IChocoModel;
import at.tugraz.ist.ase.cdrmodel.IDebuggingModel;
import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.test.ITestCase;
import at.tugraz.ist.ase.test.TestSuite;
import at.tugraz.ist.ase.test.translator.ITestCaseTranslatable;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import at.tugraz.ist.ase.kb.core.Constraint;
import at.tugraz.ist.ase.kb.fm.FMKB;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;

import java.util.*;

import static at.tugraz.ist.ase.common.ChocoSolverUtils.getVariable;

/**
 * An extension class of {@link CDRModel} for a debugging task of feature models.
 */
@Slf4j
public class FMDebuggingModel extends CDRModel implements IChocoModel, IDebuggingModel {

    @Getter
    private final Model model;
    private final FMKB fmkb;
    private final TestSuite testSuite;
    private final ITestCaseTranslatable translator;

    @Getter
    private final boolean rootConstraints;

    @Getter
    private final boolean reversedConstraintsOrder;

    /**
     * The set of test cases.
     */
    @Getter
    private final Set<ITestCase> testcases = new LinkedHashSet<>();

    /**
     * A constructor
     * On the basic of a given {@link FeatureModel}, it creates
     * corresponding variables and constraints for the model.
     *
     * @param fm a {@link FeatureModel}
     * @param testSuite a {@link TestSuite}
     * @param translator an implementation of {@link ITestCaseTranslatable} which translates test cases to Choco constraints
     * @param rootConstraints true if the root constraint (f0 = true) should be added
     * @param reversedConstraintsOrder true if the order of constraints should be reversed before adding to the possibly faulty constraints
     */
    public FMDebuggingModel(@NonNull FeatureModel fm, @NonNull TestSuite testSuite,
                            @NonNull ITestCaseTranslatable translator,
                            boolean rootConstraints, boolean reversedConstraintsOrder) {
        super(fm.getName());

        this.testSuite = testSuite;
        this.fmkb = new FMKB(fm, false);
        this.model = fmkb.getModelKB();

        this.translator = translator;

        this.rootConstraints = rootConstraints;
        this.reversedConstraintsOrder = reversedConstraintsOrder;
    }

    /**
     * This function adds constraints to the possibly faulty constraints set, the correct constraints set,
     * and test cases are also translated to Choco constraints.
     */
    @Override
    public void initialize() {
        log.debug("{}Initializing FMDebuggingModel for {} >>>", LoggerUtils.tab, getName());
        LoggerUtils.indent();

        // sets possibly faulty constraints to super class
        log.trace("{}Adding possibly faulty constraints", LoggerUtils.tab);
        List<Constraint> C = new LinkedList<>(fmkb.getConstraintList());
        if (isReversedConstraintsOrder()) {
            Collections.reverse(C); // in default, this shouldn't happen
        }
        this.setPossiblyFaultyConstraints(C);

        // sets correct constraints to super class
        if (isRootConstraints()) {
            log.trace("{}Adding correct constraints", LoggerUtils.tab);
            // {f0 = true}
            int startIdx = model.getNbCstrs();
            String f0 = fmkb.getVariable(0).getName();
            BoolVar f0Var = (BoolVar) getVariable(model, f0);
            model.addClauseTrue(f0Var);

            Constraint constraint = new Constraint(f0 + " = true");
            constraint.addChocoConstraints(model, startIdx, model.getNbCstrs() - 1, false);

            this.setCorrectConstraints(Collections.singletonList(constraint));
        }

        // translates test cases to Choco constraints
        log.trace("{}Translating test cases to Choco constraints", LoggerUtils.tab);
        if (testSuite != null) {
            createTestCases();

            // sets the translated constraints
            testcases.addAll(testSuite.getTestCases());
        }

        // remove all Choco constraints, cause we just need variables and test cases
        model.unpost(model.getCstrs());

        LoggerUtils.outdent();
        log.debug("{}<<< Model {} initialized", LoggerUtils.tab, getName());
    }

    /**
     * Gets a corresponding {@link ITestCase} object of a textual testcase.
     * @param testcase a textual testcase.
     * @return a corresponding {@link ITestCase} object.
     */
    public ITestCase getTestCase(String testcase) {
        return testSuite.getTestCase(testcase);
    }

    /**
     * Translates test cases to Choco constraints.
     */
    private void createTestCases() {
        for (ITestCase testcase : testSuite.getTestCases()) {
            translator.translate(testcase, model);
        }
    }
}
