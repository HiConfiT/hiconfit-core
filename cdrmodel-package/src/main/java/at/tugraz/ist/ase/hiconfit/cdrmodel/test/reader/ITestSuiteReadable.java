/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cdrmodel.test.reader;

import at.tugraz.ist.ase.hiconfit.cdrmodel.test.TestSuite;
import at.tugraz.ist.ase.hiconfit.cdrmodel.test.builder.ITestCaseBuildable;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;

public interface ITestSuiteReadable {
    TestSuite read(@NonNull InputStream is, @NonNull ITestCaseBuildable testCaseBuilder) throws IOException;
}
