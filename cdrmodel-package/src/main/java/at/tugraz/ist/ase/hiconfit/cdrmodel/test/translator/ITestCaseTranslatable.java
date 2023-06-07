/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.cdrmodel.test.translator;

import at.tugraz.ist.ase.hiconfit.cdrmodel.test.ITestCase;
import at.tugraz.ist.ase.hiconfit.kb.core.KB;
import lombok.NonNull;

public interface ITestCaseTranslatable {
    /**
     * Translates a test case to Choco constraints.
     */
    void translate(@NonNull ITestCase testCase, @NonNull KB kb);
}
