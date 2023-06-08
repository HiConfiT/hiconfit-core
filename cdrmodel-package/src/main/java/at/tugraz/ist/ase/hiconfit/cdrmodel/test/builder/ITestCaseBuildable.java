/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cdrmodel.test.builder;

import at.tugraz.ist.ase.hiconfit.cdrmodel.test.ITestCase;
import lombok.NonNull;

public interface ITestCaseBuildable {
    ITestCase buildTestCase(@NonNull Object testcase);
}
