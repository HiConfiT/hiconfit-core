///*
// * Consistency-based Algorithms for Conflict Detection and Resolution
// *
// * Copyright (c) 2022
// *
// * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
// */
//
//package at.tugraz.ist.ase.fma.assumption;
//
//import at.tugraz.ist.ase.cdrmodel.test.ITestCase;
//import at.tugraz.ist.ase.cdrmodel.test.TestCase;
//import at.tugraz.ist.ase.fm.core.AbstractRelationship;
//import at.tugraz.ist.ase.fm.core.CTConstraint;
//import at.tugraz.ist.ase.fm.core.Feature;
//import at.tugraz.ist.ase.fm.core.FeatureModel;
//import at.tugraz.ist.ase.fma.AnomalyType;
//import at.tugraz.ist.ase.fma.featuremodel.AnomalyAwareFeature;
//import at.tugraz.ist.ase.fma.featuremodel.AnomalyAwareFeatureModel;
//import at.tugraz.ist.ase.kb.core.Assignment;
//import lombok.NonNull;
//
//import java.util.LinkedList;
//import java.util.List;
//
///**
// * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
// * @author: Tamim Burgstaller (tamim.burgstaller@student.tugraz.at)
// */
//public class ConditionallyDeadAssumptions implements IFMAnalysisAssumptionCreatable{
//    @Override
//    public <F extends Feature, R extends AbstractRelationship<F>, C extends CTConstraint>
//    List<ITestCase> createAssumptions(@NonNull FeatureModel<F, R, C> fm) {
//        AnomalyAwareFeatureModel fm;
//        if (!(featureModel instanceof AnomalyAwareFeatureModel)) {
//            fm = new AnomalyAwareFeatureModel(featureModel);
//        }
//        else {
//            fm = (AnomalyAwareFeatureModel) featureModel;
//        }
//
//        List<ITestCase> testCases = new LinkedList<>();
//        for (int i = 1; i < fm.getNumOfFeatures(); i++) {
//            AnomalyAwareFeature feature = fm.getAnomalyAwareFeature(i);
//            if (!fm.isOptionalFeature(feature) || feature.isAnomalyType(AnomalyType.DEAD)) {
//                continue; // Only optional features can be conditionally dead - dead features are dead anyway
//            }
//
//            for (int j = 1; j < fm.getNumOfFeatures(); j++) {
//                Feature otherFeature = fm.getFeature(j);
//                if (i == j || !fm.isOptionalFeature(otherFeature) || fm.getAnomalyAwareFeature(j).isAnomalyType(AnomalyType.DEAD)) {
//                    continue;
//                }
//
//                String testcase = fm.getFeature(0).getName() + " = true & " + otherFeature.getName() + " = true & " + feature.getName() + " = true";
//                List<Assignment> assignments = new LinkedList<>();
//                assignments.add(Assignment.builder()
//                        .variable(fm.getFeature(0).getName())
//                        .value("true")
//                        .build());
//                assignments.add(Assignment.builder()
//                        .variable(otherFeature.getName())
//                        .value("true")
//                        .build());
//                assignments.add(Assignment.builder()
//                        .variable(feature.getName())
//                        .value("true")
//                        .build());
//
//                testCases.add(TestCase.builder()
//                        .testcase(testcase)
//                        .assignments(assignments).build());
//            }
//
//        }
//        return testCases;
//    }
//}
