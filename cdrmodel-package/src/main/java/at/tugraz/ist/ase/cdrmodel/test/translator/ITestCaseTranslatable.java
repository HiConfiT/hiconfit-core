/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cdrmodel.test.translator;

import at.tugraz.ist.ase.cdrmodel.test.ITestCase;
import at.tugraz.ist.ase.kb.core.KB;
import lombok.NonNull;

public interface ITestCaseTranslatable {
    /**
     * Translates a test case to Choco constraints.
     */
    void translate(@NonNull ITestCase testCase, @NonNull KB kb);
}
