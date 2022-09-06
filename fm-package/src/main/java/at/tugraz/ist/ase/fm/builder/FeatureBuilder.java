/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.builder;

import at.tugraz.ist.ase.fm.core.Feature;
import lombok.NonNull;

public class FeatureBuilder implements IFeatureBuildable {

    @SuppressWarnings("unchecked")
    public <F extends Feature> F buildRoot(@NonNull String name, @NonNull String id) {
        return (F) Feature.createRoot(name, id);
    }

    @SuppressWarnings("unchecked")
    public <F extends Feature> F buildFeature(@NonNull String name, @NonNull String id) {
        return (F) new Feature(name, id);
    }
}
