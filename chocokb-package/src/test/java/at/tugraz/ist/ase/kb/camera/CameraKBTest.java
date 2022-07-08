/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.kb.camera;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CameraKBTest {
    static CameraKB kb;

    @BeforeAll
    static void setUp() {
        kb = new CameraKB(false);
    }

    @Test
    void testDomains() {
        System.out.println(kb.getConfigurationSize());
    }
}