/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.test.builder;

import at.tugraz.ist.ase.test.ITestCase;
import lombok.NonNull;

public interface ITestCaseBuildable {
    ITestCase buildTestCase(@NonNull String testcase);
}
