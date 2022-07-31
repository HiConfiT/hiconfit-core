/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.test.reader;

import at.tugraz.ist.ase.test.TestSuite;
import at.tugraz.ist.ase.test.builder.ITestCaseBuildable;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;

public interface ITestSuiteReadable {
    TestSuite read(@NonNull InputStream is, @NonNull ITestCaseBuildable testCaseBuilder) throws IOException;
}
