/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cdrmodel.fm;

import at.tugraz.ist.ase.cdrmodel.AbstractCDRModel;
import at.tugraz.ist.ase.cdrmodel.IDebuggingModel;
import at.tugraz.ist.ase.cdrmodel.test.ITestCase;
import at.tugraz.ist.ase.cdrmodel.test.TestSuite;
import at.tugraz.ist.ase.cdrmodel.test.translator.ITestCaseTranslatable;
import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.fm.core.CTConstraint;
import at.tugraz.ist.ase.fm.core.Feature;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * An extension class of {@link AbstractCDRModel} for a debugging task of feature models, in which:
 * + C = CF
 * + B = { f0 = true }
 * + Test cases
 */
@Slf4j
public class FMDebuggingModel<F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint> extends FMCdrModel<F, R, C> implements IDebuggingModel {

    private TestSuite testSuite;
    private ITestCaseTranslatable translator;

    /**
     * The set of test cases.
     */
    @Getter
    private Set<ITestCase> testcases = new LinkedHashSet<>();

    /**
     * A constructor
     * On the basic of a given {@link FeatureModel}, it creates
     * corresponding variables and constraints for the model.
     *
     * @param fm a {@link FeatureModel}
     * @param testSuite a {@link TestSuite}
     * @param translator an implementation of {@link ITestCaseTranslatable} which translates test cases to Choco constraints
     * @param hasNegativeConstraints generate negative constraints if true
     * @param rootConstraints true if the root constraint (f0 = true) should be added
     * @param reversedConstraintsOrder true if the order of constraints should be reversed before adding to the possibly faulty constraints
     */
    public FMDebuggingModel(@NonNull FeatureModel<F, R, C> fm, @NonNull TestSuite testSuite, @NonNull ITestCaseTranslatable translator,
                            boolean hasNegativeConstraints, boolean rootConstraints, boolean reversedConstraintsOrder) {
        super(fm, hasNegativeConstraints, rootConstraints, reversedConstraintsOrder);

        this.testSuite = testSuite;
        this.translator = translator;
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
        if (testSuite != null) {
            createTestCases();

            // sets the translated constraints
            testcases.addAll(testSuite.getTestCases());
        }

        // remove all Choco constraints, cause we just need variables and test cases
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
