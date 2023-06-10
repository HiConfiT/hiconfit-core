/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cacdr_core.reader;

import at.tugraz.ist.ase.hiconfit.cacdr_core.TestSuite;
import at.tugraz.ist.ase.hiconfit.cacdr_core.builder.ITestCaseBuildable;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;

public interface ITestSuiteReadable {
    TestSuite read(@NonNull InputStream is, @NonNull ITestCaseBuildable testCaseBuilder) throws IOException;
}
