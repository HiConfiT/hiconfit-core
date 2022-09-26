/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cdrmodel.test.writer;

import at.tugraz.ist.ase.cdrmodel.test.ITestCase;
import lombok.NonNull;

import java.util.List;

public interface ITestSuiteWritable {
    void write(@NonNull List<ITestCase> testCases, @NonNull String path);
}
