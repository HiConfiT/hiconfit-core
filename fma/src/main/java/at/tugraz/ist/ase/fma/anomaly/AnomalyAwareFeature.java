/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.anomaly;

import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.fm.core.Feature;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class AnomalyAwareFeature extends Feature {
    protected List<IAnomalyType> anomalies = new LinkedList<>();

    /**
     * Constructor for the root feature.
     */
    public static AnomalyAwareFeature createRoot(@NonNull String name, @NonNull String id) {
        checkArgument(!name.isEmpty(), "Feature name cannot be empty!");
        checkArgument(!id.isEmpty(), "Feature id cannot be empty!");

        AnomalyAwareFeature root = new AnomalyAwareFeature(name, id);
        root.isRoot = true;
        root.isAbstract = true;

        log.trace("{}Created root feature with [name={}, id={}]", LoggerUtils.tab(), name, id);
        return root;
    }

    /**
     * Constructor for the child feature.
     *
     * @param name name of the feature
     * @param id  id of the feature
     */
    public AnomalyAwareFeature(@NonNull String name, @NonNull String id) {
        super(name, id);
    }

    /**
     * Check whether the feature belongs to an anomaly type
     *
     * @param type - an {@link AnomalyType} type
     * @return true if it is right, false otherwise
     */
    public boolean isAnomalyType(IAnomalyType type) {
        return anomalies.contains(type);
    }

    /**
     * Set an anomaly type to the feature.
     *
     * @param type - an {@link AnomalyType} type
     */
    public void setAnomalyType(IAnomalyType type) {
        anomalies.add(type);
    }

    public void dispose() {
        super.dispose();
        this.anomalies.clear();
        this.anomalies = null;
    }

    public Object clone() throws CloneNotSupportedException {
        AnomalyAwareFeature clone = (AnomalyAwareFeature) super.clone();

        // copy anomalies
        clone.anomalies = new LinkedList<>(anomalies);

        return clone;
    }
}
