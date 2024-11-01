/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cdrmodel.fm;

import at.tugraz.ist.ase.hiconfit.cacdr_core.ITestCase;
import at.tugraz.ist.ase.hiconfit.cacdr_core.TestSuite;
import at.tugraz.ist.ase.hiconfit.cacdr_core.translator.ITestCaseTranslatable;
import at.tugraz.ist.ase.hiconfit.cacdr_core.translator.fm.FMTestCaseTranslator;
import at.tugraz.ist.ase.hiconfit.cdrmodel.IDebuggingModel;
import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.Feature;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * An extension class of {@link FMCdrModel} for a debugging task of feature models, in which:
 * + C = CF
 * + B = { f0 = true }
 * + Test cases
 * + hasNegativeConstraints = false
 * + rootConstraints = true
 * + cfInConflicts = true
 * + reversedConstraintsOrder = false
 */
@Slf4j
public class FMDebuggingModel<F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint>
        extends FMCdrModel<F, R, C> implements IDebuggingModel {

    protected TestSuite testSuite;
    @Setter
    protected ITestCaseTranslatable translator = new FMTestCaseTranslator();

    /**
     * The set of test cases.
     */
    @Getter
    protected Set<ITestCase> testcases = new LinkedHashSet<>();

    /**
     * A constructor
     * On the basic of a given {@link FeatureModel}, it creates
     * corresponding variables and constraints for the model.
     *
     * @param fm a {@link FeatureModel}
     * @param testSuite a {@link TestSuite}
     */
    public FMDebuggingModel(@NonNull FeatureModel<F, R, C> fm,
                            @NonNull TestSuite testSuite) {
        super(fm, false, true, true, false);

        this.testSuite = testSuite;
    }

    /**
     * This function adds constraints to the possibly faulty constraints set, the correct constraints set,
     * and test cases are also translated to Choco constraints.
     */
    @Override
    public void initialize() {
        log.debug("{}Initializing FMDebuggingModel for {} >>>", LoggerUtils.tab(), getName());
        LoggerUtils.indent();

        // sets possibly faulty constraints to super class
        // sets correct constraints to super class
        initializeConstraintSets();

        // translates test cases to Choco constraints
        log.trace("{}Translating test cases to Choco constraints", LoggerUtils.tab());
        createTestCases();
        // sets the translated constraints
        testcases.addAll(testSuite.getTestCases());

        // remove all Choco constraints, because we just need variables and test cases
        model.unpost(model.getCstrs());

        LoggerUtils.outdent();
        log.debug("{}<<< Model {} initialized", LoggerUtils.tab(), getName());
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
            translator.translate(testcase, fmkb);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object clone() throws CloneNotSupportedException {
        FMDebuggingModel<F, R, C> clone = (FMDebuggingModel<F, R, C>) super.clone();

        clone.testSuite = (TestSuite) testSuite.clone();
        clone.testcases = new LinkedHashSet<>();

        return clone;
    }

    @Override
    public void dispose() {
        super.dispose();
        testcases.clear();
        testSuite = null;
        translator = null;
    }
}
