/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.builder;

import at.tugraz.ist.ase.cdrmodel.fm.FMCdrModel;
import at.tugraz.ist.ase.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.fm.core.CTConstraint;
import at.tugraz.ist.ase.fm.core.FeatureModel;
import at.tugraz.ist.ase.fma.FMAnalyzer;
import at.tugraz.ist.ase.fma.analysis.RedundancyAnalysis;
import at.tugraz.ist.ase.fma.anomaly.AnomalyAwareFeature;
import lombok.NonNull;

public class RedundancyAnalysisBuilder implements IAnalysisBuildable {
    /**
     * Build RedundancyAnalysis on the basis of a given feature model
     * and add the generated analyses to the FMAnalyzer
     * @param featureModel the given feature model
     * @param analyzer the FMAnalyzer
     */
    @Override
    public void build(@NonNull FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> featureModel,
                      @NonNull FMAnalyzer analyzer) throws CloneNotSupportedException {
        // REDUNDANCIES
        FMCdrModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
                model = new FMCdrModel<>(featureModel, true, false, true);
        model.initialize();

        // create the redundancy analysis
        RedundancyAnalysis redundancyAnalysis = new RedundancyAnalysis(model);

        analyzer.addAnalysis(redundancyAnalysis);
    }
}
