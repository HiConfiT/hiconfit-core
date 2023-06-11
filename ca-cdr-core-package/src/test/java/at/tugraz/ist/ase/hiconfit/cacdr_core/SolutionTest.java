/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr_core;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SolutionTest {
    @Test
    void testToString() {
        Assignment a1 = Assignment.builder().variable("a1").value("a1").build();
        Assignment a2 = Assignment.builder().variable("a2").value("a2").build();

        Solution s = Solution.builder().assignments(List.of(a1, a2)).build();

        assertEquals("a1=a1, a2=a2", s.toString());
    }

    @Test
    void testEquals() {
        Assignment a1 = Assignment.builder().variable("a1").value("a1").build();
        Assignment a2 = Assignment.builder().variable("a2").value("a2").build();

        Solution s1 = Solution.builder().assignments(List.of(a1, a2)).build();
        Solution s2 = Solution.builder().assignments(List.of(a1, a2)).build();
        Solution s3 = Solution.builder().assignments(List.of(a2, a1)).build();

        assertEquals(s1, s2);
        assertEquals(s2, s1);
        assertEquals(s1, s1);
        assertNotEquals(s1, s3);
        assertNotEquals(s3, s1);
        assertNotEquals(s2, s3);
    }
}