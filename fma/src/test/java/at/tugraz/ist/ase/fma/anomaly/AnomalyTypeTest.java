/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.anomaly;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnomalyTypeTest {
    @Test
    void test() {
        AnomalyType anomalyType = AnomalyType.VOID;

        assertEquals(AnomalyType.VOID, anomalyType);
    }

    @Test
    void testEnumSet() {
        EnumSet<AnomalyType> anomalyTypes = EnumSet.of(AnomalyType.VOID, AnomalyType.FULLMANDATORY);

        anomalyTypes.add(AnomalyType.CONDITIONALLYDEAD);

        assertEquals(3, anomalyTypes.size());
    }

    @Test
    void testLoopEnumSet() {
        EnumSet<AnomalyType> anomalyTypes = EnumSet.of(AnomalyType.VOID, AnomalyType.FULLMANDATORY);

        anomalyTypes.add(AnomalyType.CONDITIONALLYDEAD);

        for (AnomalyType anomalyType : anomalyTypes) {
            System.out.println(anomalyType);
        }
    }

    @Test
    void testContains() {
        EnumSet<AnomalyType> anomalyTypes = EnumSet.of(AnomalyType.VOID, AnomalyType.FULLMANDATORY);

        anomalyTypes.add(AnomalyType.CONDITIONALLYDEAD);

        assertTrue(anomalyTypes.contains(AnomalyType.CONDITIONALLYDEAD));
    }
}