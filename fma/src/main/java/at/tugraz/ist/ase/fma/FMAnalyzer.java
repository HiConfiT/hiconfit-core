/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma;

import at.tugraz.ist.ase.cdrmodel.fm.FMDebuggingModel;
import at.tugraz.ist.ase.cdrmodel.test.ITestCase;
import at.tugraz.ist.ase.cdrmodel.test.TestSuite;
import at.tugraz.ist.ase.cdrmodel.test.translator.fm.FMTestCaseTranslator;
import at.tugraz.ist.ase.common.ConsoleColors;
import at.tugraz.ist.ase.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.fm.core.CTConstraint;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import at.tugraz.ist.ase.fma.analysis.*;
import at.tugraz.ist.ase.fma.anomaly.AnomalyAwareFeature;
import at.tugraz.ist.ase.fma.anomaly.AnomalyType;
import at.tugraz.ist.ase.fma.assumption.*;
import at.tugraz.ist.ase.fma.explanator.*;
import at.tugraz.ist.ase.fma.test.AssumptionAwareTestCase;
import at.tugraz.ist.ase.kb.core.Constraint;
import lombok.NonNull;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 * @author: Tamim Burgstaller (tamim.burgstaller@student.tugraz.at)
 */
public class FMAnalyzer {
    private final Map<AbstractFMAnalysis<?>, AbstractAnomalyExplanator<?>> analyses = new LinkedHashMap<>();

    public FMAnalyzer() {
    }

    public void addAnalysis(AbstractFMAnalysis<?> analysis, AbstractAnomalyExplanator<?> explanator) {
        analyses.put(analysis, explanator);
    }

    public void run() throws ExecutionException, InterruptedException {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        for (AbstractFMAnalysis<?> analysis : analyses.keySet()) {
            pool.execute(analysis);
        }

        List<AbstractAnomalyExplanator<?>> runningTasks = new LinkedList<>();
        for (AbstractFMAnalysis<?> analysis : analyses.keySet()) {
            if (!analysis.get()) {
                AbstractAnomalyExplanator<?> explanator = analyses.get(analysis);

                if (explanator != null) {
                    pool.execute(explanator);

                    runningTasks.add(explanator);
                }
            }
        }

        for (AbstractAnomalyExplanator<?> tasks : runningTasks) {
            tasks.join();
        }

        pool.shutdown();
    }

    /**
     * TODO - migrate this function to a new class called ...Builder (e.g. AnalysesBuilder)
     * FMAnalyzer should be simple - focus on the execution of the analyses and explanators
     * The generation of the analyses and explanators should be done in a separate class
     */
    public void performFullAnalysis(@NonNull FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> featureModel) throws ExecutionException, InterruptedException, CloneNotSupportedException {
        /// VOID FEATURE MODEL
        // create a test case/assumption
        // check void feature model - inconsistent( CF ∪ { c0 })
        VoidFMAssumption voidFMAssumption = new VoidFMAssumption();
        List<ITestCase> testCases = voidFMAssumption.createAssumptions(featureModel);
        TestSuite testSuite = TestSuite.builder().testCases(testCases).build();

        FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> debuggingModel = new FMDebuggingModel<>(featureModel, testSuite, new FMTestCaseTranslator(), false, false, false);
        debuggingModel.initialize();

        // create the specified analysis and the corresponding explanator
        VoidFMAnalysis analysis = new VoidFMAnalysis(debuggingModel, testCases.get(0));
        VoidFMExplanator explanator = new VoidFMExplanator(debuggingModel, testCases.get(0));

        FMAnalyzer analyzer = new FMAnalyzer();
        analyzer.addAnalysis(analysis, explanator); // add the analysis to the analyzer
        analyzer.run(); // run the analyzer

        // print the result
        ExplanationColors.EXPLANATION = ConsoleColors.WHITE;
        if (analysis.get()) {
            System.out.println(ExplanationColors.OK + "\u2713 Consistency: ok");
        } else {
            System.out.println(ExplanationColors.ANOMALY + "X Void feature model");
            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(explanator.get(), "void feature model"));
        }

        assertTrue(analysis.get());

        List<List<AbstractFMAnalysis<?>>> allAnalyses = new ArrayList<>(Collections.emptyList());
        List<List<AbstractAnomalyExplanator<List<Set<Constraint>>>>> allExplanators = new ArrayList<>(Collections.emptyList());

        // prepare for collecting analyses and explanators
        for (AnomalyType a : AnomalyType.values()) {
            allAnalyses.add(new ArrayList<>(Collections.emptyList()));
            allExplanators.add(new ArrayList<>(Collections.emptyList()));
        }

        analyzer = new FMAnalyzer(); // TODO necessary?
        FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> debuggingModelClone = null;

        boolean anomalyFound = false;

        /// DEAD FEATURES
        // create a test case/assumption
        // check dead features - inconsistent( CF ∪ { c0 } U { fi = true })
        DeadFeatureAssumptions deadFeatureAssumptions = new DeadFeatureAssumptions();
        List<ITestCase> deadFeatureTestCases = deadFeatureAssumptions.createAssumptions(featureModel);
        TestSuite deadFeatureTestSuite = TestSuite.builder().testCases(deadFeatureTestCases).build();

        FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> deadFeatureDebuggingModel = new FMDebuggingModel<>(featureModel, deadFeatureTestSuite, new FMTestCaseTranslator(), false, false, false);
        deadFeatureDebuggingModel.initialize();

        for (ITestCase deadFeatureTestCase : deadFeatureTestCases) {
            // create the specified analyses and the corresponding explanators
            debuggingModelClone = (FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>) deadFeatureDebuggingModel.clone();
            debuggingModelClone.initialize();
            DeadFeatureAnalysis deadFeatureAnalysis = new DeadFeatureAnalysis(debuggingModelClone, deadFeatureTestCase);
            DeadFeatureExplanator deadFeatureExplanator = new DeadFeatureExplanator(debuggingModelClone, deadFeatureTestCase);
            analyzer.addAnalysis(deadFeatureAnalysis, deadFeatureExplanator); // add the analysis to the analyzer

            allAnalyses.get(AnomalyType.DEAD.ordinal()).add(deadFeatureAnalysis);
            allExplanators.get(AnomalyType.DEAD.ordinal()).add(deadFeatureExplanator);
        }

        analyzer.run(); // run the analyzer

        // Check the results and set dead features
        for (int runningAnalysis = 0; runningAnalysis < allAnalyses.get(AnomalyType.DEAD.ordinal()).size(); runningAnalysis++) {
            if (!allAnalyses.get(AnomalyType.DEAD.ordinal()).get(runningAnalysis).get()) {
                System.out.println(ExplanationColors.ANOMALY + "X Dead feature: " + ((AssumptionAwareTestCase) allAnalyses.get(AnomalyType.DEAD.ordinal()).get(runningAnalysis).getAssumption()).getAssumptions());
                System.out.println(ExplanationUtils.convertToDescriptiveExplanation(allExplanators.get(AnomalyType.DEAD.ordinal()).get(runningAnalysis).get(), "dead feature"));

                for (AnomalyAwareFeature deadFeature : ((AssumptionAwareTestCase) allAnalyses.get(AnomalyType.DEAD.ordinal()).get(runningAnalysis).getAssumption()).getAssumptions()) {
                    featureModel.getFeature(deadFeature.getId()).setAnomalyType(AnomalyType.DEAD);
                }

                anomalyFound = true;
            }
        }

        analyzer = new FMAnalyzer(); // TODO necessary?

        /// FULL MANDATORY
        // create a test case/assumption
        // check full mandatory features - inconsistent( CF ∪ { c0 } U { fi = false })
        FullMandatoryAssumptions fullMandatoryAssumptions = new FullMandatoryAssumptions();
        List<ITestCase> fullMandatoryTestCases = fullMandatoryAssumptions.createAssumptions(featureModel);
        TestSuite fullMandatoryTestSuite = TestSuite.builder().testCases(fullMandatoryTestCases).build();

        FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> fullMandatoryDebuggingModel = new FMDebuggingModel<>(featureModel, fullMandatoryTestSuite, new FMTestCaseTranslator(), false, false, false);
        fullMandatoryDebuggingModel.initialize();

        for (ITestCase fullMandatoryTestCase : fullMandatoryTestCases) {
            // create the specified analyses and the corresponding explanators
            debuggingModelClone = (FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>) fullMandatoryDebuggingModel.clone();
            debuggingModelClone.initialize();
            FullMandatoryAnalysis fullMandatoryAnalysis = new FullMandatoryAnalysis(debuggingModelClone, fullMandatoryTestCase);
            FullMandatoryExplanator fullMandatoryExplanator = new FullMandatoryExplanator(debuggingModelClone, fullMandatoryTestCase);
            analyzer.addAnalysis(fullMandatoryAnalysis, fullMandatoryExplanator); // add the analysis to the analyzer

            allAnalyses.get(AnomalyType.FULLMANDATORY.ordinal()).add(fullMandatoryAnalysis);
            allExplanators.get(AnomalyType.FULLMANDATORY.ordinal()).add(fullMandatoryExplanator);
        }

        // CONDITIONALLY DEAD
        // create a test case/assumption
        // check conditionally dead features - inconsistent( CF ∪ { c0 } U { fj = true } U { fi = true } ) for any fj
        ConditionallyDeadAssumptions conditionallyDeadAssumptions = new ConditionallyDeadAssumptions();
        List<ITestCase> conditionallyDeadTestCases = conditionallyDeadAssumptions.createAssumptions(featureModel);
        TestSuite conditionallyDeadTestSuite = TestSuite.builder().testCases(conditionallyDeadTestCases).build();

        FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> conditionallyDeadDebuggingModel = new FMDebuggingModel<>(featureModel, conditionallyDeadTestSuite, new FMTestCaseTranslator(), false, false, false);
        conditionallyDeadDebuggingModel.initialize();

        for (ITestCase conditionallyDeadTestCase : conditionallyDeadTestCases) {
            // create the specified analyses and the corresponding explanators
            debuggingModelClone = (FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>) conditionallyDeadDebuggingModel.clone();
            debuggingModelClone.initialize();
            ConditionallyDeadAnalysis conditionallyDeadAnalysis = new ConditionallyDeadAnalysis(debuggingModelClone, conditionallyDeadTestCase);
            ConditionallyDeadExplanator conditionallyDeadExplanator = new ConditionallyDeadExplanator(debuggingModelClone, conditionallyDeadTestCase);
            analyzer.addAnalysis(conditionallyDeadAnalysis, conditionallyDeadExplanator); // add the analysis to the analyzer

            allAnalyses.get(AnomalyType.CONDITIONALLYDEAD.ordinal()).add(conditionallyDeadAnalysis);
            allExplanators.get(AnomalyType.CONDITIONALLYDEAD.ordinal()).add(conditionallyDeadExplanator);
        }

        /// FALSE OPTIONAL
        // create a test case/assumption
        // check false optional features  - inconsistent( CF ∪ { c0 } U { fpar = true ^ fopt = false } )
        FalseOptionalAssumptions falseOptionalAssumptions = new FalseOptionalAssumptions();
        List<ITestCase> falseOptionalTestCases = falseOptionalAssumptions.createAssumptions(featureModel);
        TestSuite falseOptionalTestSuite = TestSuite.builder().testCases(falseOptionalTestCases).build();

        FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> falseOptionalDebuggingModel = new FMDebuggingModel<>(featureModel, falseOptionalTestSuite, new FMTestCaseTranslator(), false, false, false);
        falseOptionalDebuggingModel.initialize();

        for (ITestCase falseOptionalTestCase : falseOptionalTestCases) {
            // create the specified analyses and the corresponding explanators
            debuggingModelClone = (FMDebuggingModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>) falseOptionalDebuggingModel.clone();
            debuggingModelClone.initialize();
            FalseOptionalAnalysis falseOptionalAnalysis = new FalseOptionalAnalysis(debuggingModelClone, falseOptionalTestCase);
            FalseOptionalExplanator falseOptionalExplanator = new FalseOptionalExplanator(debuggingModelClone, falseOptionalTestCase);
            analyzer.addAnalysis(falseOptionalAnalysis, falseOptionalExplanator); // add the analysis to the analyzer

            allAnalyses.get(AnomalyType.FALSEOPTIONAL.ordinal()).add(falseOptionalAnalysis);
            allExplanators.get(AnomalyType.FALSEOPTIONAL.ordinal()).add(falseOptionalExplanator);
        }

        analyzer.run(); // run the analyzer

        for (AnomalyType anomaly : AnomalyType.values()) {
            if (anomaly.ordinal() < 2) {
                continue;
            }
            for (int runningAnalysis = 0; runningAnalysis < allAnalyses.get(anomaly.ordinal()).size(); runningAnalysis++) {
                if (!allAnalyses.get(anomaly.ordinal()).get(runningAnalysis).get()) {
                    switch (anomaly) {
                        case FULLMANDATORY -> {
                            System.out.println(ExplanationColors.ANOMALY + "X Full mandatory feature: " + ((AssumptionAwareTestCase) allAnalyses.get(anomaly.ordinal()).get(runningAnalysis).getAssumption()).getAssumptions());
                            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(allExplanators.get(anomaly.ordinal()).get(runningAnalysis).get(), "full mandatory feature"));
                        }
                        case CONDITIONALLYDEAD -> {
                            System.out.println(ExplanationColors.ANOMALY + "X Conditionally dead feature: " + ((AssumptionAwareTestCase) allAnalyses.get(anomaly.ordinal()).get(runningAnalysis).getAssumption()).getAssumptions());
                            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(allExplanators.get(anomaly.ordinal()).get(runningAnalysis).get(), "conditionally dead feature"));
                        }
                        case FALSEOPTIONAL -> {
                            System.out.println(ExplanationColors.ANOMALY + "X False optional feature: " + ((AssumptionAwareTestCase) allAnalyses.get(anomaly.ordinal()).get(runningAnalysis).getAssumption()).getAssumptions());
                            System.out.println(ExplanationUtils.convertToDescriptiveExplanation(allExplanators.get(anomaly.ordinal()).get(runningAnalysis).get(), "false optional feature"));
                        }
                    }

                    for (AnomalyAwareFeature featureWithAnomaly : ((AssumptionAwareTestCase) allAnalyses.get(anomaly.ordinal()).get(runningAnalysis).getAssumption()).getAssumptions()) {
                        featureModel.getFeature(featureWithAnomaly.getId()).setAnomalyType(anomaly);
                    }

                    anomalyFound = true;
                }
            }
        }

        if (!anomalyFound) {
            System.out.println(ConsoleColors.GREEN + "\u2713 No anomaly found" + ConsoleColors.RESET);
        }
    }
}
