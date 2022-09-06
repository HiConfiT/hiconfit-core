/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.anomaly;

import at.tugraz.ist.ase.fm.builder.IFeatureBuildable;
import at.tugraz.ist.ase.fm.core.Feature;
import lombok.NonNull;

public class AnomalyAwareFeatureBuilder implements IFeatureBuildable {
    @SuppressWarnings("unchecked")
    public <F extends Feature> F buildRoot(@NonNull String name, @NonNull String id) {
        return (F) AnomalyAwareFeature.createRoot(name, id);
    }

    @SuppressWarnings("unchecked")
    public <F extends Feature> F buildFeature(@NonNull String name, @NonNull String id) {
        return (F) new AnomalyAwareFeature(name, id);
    }
}
