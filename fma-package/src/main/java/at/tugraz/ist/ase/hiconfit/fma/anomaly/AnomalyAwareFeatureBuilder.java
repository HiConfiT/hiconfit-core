/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.anomaly;

import at.tugraz.ist.ase.hiconfit.fm.builder.IFeatureBuildable;
import at.tugraz.ist.ase.hiconfit.fm.core.Feature;
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
