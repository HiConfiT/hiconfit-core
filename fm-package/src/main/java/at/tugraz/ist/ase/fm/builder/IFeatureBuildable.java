/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.builder;

import at.tugraz.ist.ase.fm.core.Feature;
import lombok.NonNull;

public interface IFeatureBuildable {

    <F extends Feature> F buildRoot(@NonNull String name, @NonNull String id);

    <F extends Feature> F buildFeature(@NonNull String name, @NonNull String id);
}
