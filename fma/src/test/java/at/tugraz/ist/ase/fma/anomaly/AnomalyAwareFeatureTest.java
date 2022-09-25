/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.anomaly;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnomalyAwareFeatureTest {
    @Test
    void test() {
        AnomalyAwareFeature anomalyAwareFeature = new AnomalyAwareFeature("feature1", "feature1");
        anomalyAwareFeature.setAnomalyType(AnomalyType.DEAD);
        anomalyAwareFeature.setAnomalyType(AnomalyType.FULLMANDATORY);

        assertEquals("feature1", anomalyAwareFeature.getName());
        assertTrue(anomalyAwareFeature.isAnomalyType(AnomalyType.DEAD));
        assertTrue(anomalyAwareFeature.isAnomalyType(AnomalyType.FULLMANDATORY));
        assertFalse(anomalyAwareFeature.isAnomalyType(AnomalyType.FALSEOPTIONAL));
    }
}