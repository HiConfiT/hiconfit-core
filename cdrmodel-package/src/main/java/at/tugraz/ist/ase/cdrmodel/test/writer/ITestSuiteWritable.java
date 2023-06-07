/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cdrmodel.test.writer;

import at.tugraz.ist.ase.cdrmodel.test.ITestCase;
import lombok.NonNull;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.util.List;

public interface ITestSuiteWritable {
    void write(@NonNull List<ITestCase> testCases, @NonNull String path) throws ParserConfigurationException, TransformerException;
}
