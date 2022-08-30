/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cdrmodel.test_model.csp;

import at.tugraz.ist.ase.common.IOUtils;
import at.tugraz.ist.ase.common.LoggerUtils;
import lombok.Cleanup;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.Model;

import java.io.IOException;
import java.io.InputStream;

import static at.tugraz.ist.ase.csp2choco.CSP2ChocoTranslator.loadConstraints;

@UtilityClass
@Slf4j
public class CSPModels {
    public Model createModel1() throws IOException {
        log.trace("{}Creating model from csp1.mzn >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        // create a model
        Model model = new Model("Test 1");

        // Decision variables
        model.intVar("x", -10, 10);
        model.intVar("y", -10, 10);
        log.trace("{}Created variables", LoggerUtils.tab());

        ClassLoader classLoader = CSPModels.class.getClassLoader();
        @Cleanup InputStream inputStream = IOUtils.getInputStream(classLoader, "csp1.mzn");

        loadConstraints(inputStream, model);
        log.trace("{}Created constraints", LoggerUtils.tab());

        LoggerUtils.outdent();
        log.debug("{}<<< Created model from csp1.mzn", LoggerUtils.tab());

        return model;
    }

    public Model createModel2() throws IOException {
        log.trace("{}Creating model from csp2.mzn >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        // create a model
        Model model = new Model("Test 2");

        // Decision variables
        model.intVar("v1", 1, 3);
        model.intVar("v2", 1, 3);
        model.intVar("v3", 1, 3);
        log.trace("{}Created variables", LoggerUtils.tab());

        ClassLoader classLoader = CSPModels.class.getClassLoader();
        @Cleanup InputStream inputStream = IOUtils.getInputStream(classLoader, "csp2.mzn");

        loadConstraints(inputStream, model);
        log.trace("{}Created constraints", LoggerUtils.tab());

        LoggerUtils.outdent();
        log.debug("{}<<< Created model from csp2.mzn", LoggerUtils.tab());

        return model;
    }

    public Model createModel3() throws IOException {
        log.trace("{}Creating model from csp3.mzn >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        // create a model
        Model model = new Model("Test 3");

        // Decision variables
        model.intVar("v1", 1, 3);
        model.intVar("v2", 1, 3);
        model.intVar("v3", 1, 3);
        log.trace("{}Created variables", LoggerUtils.tab());

        ClassLoader classLoader = CSPModels.class.getClassLoader();
        @Cleanup InputStream inputStream = IOUtils.getInputStream(classLoader, "csp3.mzn");

        loadConstraints(inputStream, model);
        log.trace("{}Created constraints", LoggerUtils.tab());

        LoggerUtils.outdent();
        log.debug("{}<<< Created model from csp3.mzn", LoggerUtils.tab());

        return model;
    }

    public Model createModel4() throws IOException {
        log.trace("{}Creating model from csp4.mzn >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        // create a model
        Model model = new Model("Test 4");

        // Decision variables
        model.intVar("v1", 1, 3);
        model.intVar("v2", 1, 3);
        model.intVar("v3", 1, 3);
        log.trace("{}Created variables", LoggerUtils.tab());

        ClassLoader classLoader = CSPModels.class.getClassLoader();
        @Cleanup InputStream inputStream = IOUtils.getInputStream(classLoader, "csp4.mzn");

        loadConstraints(inputStream, model);
        log.trace("{}Created constraints", LoggerUtils.tab());

        LoggerUtils.outdent();
        log.debug("{}<<< Created model from csp4.mzn", LoggerUtils.tab());

        return model;
    }

    public Model createModel5() throws IOException {
        log.trace("{}Creating model from csp5.mzn >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        // create a model
        Model model = new Model("Test 5");

        // Decision variables
        model.intVar("v1", 1, 3);
        model.intVar("v2", 1, 3);
        model.intVar("v3", 1, 3);
        log.trace("{}Created variables", LoggerUtils.tab());

        ClassLoader classLoader = CSPModels.class.getClassLoader();
        @Cleanup InputStream inputStream = IOUtils.getInputStream(classLoader, "csp5.mzn");

        loadConstraints(inputStream, model);
        log.trace("{}Created constraints", LoggerUtils.tab());

        LoggerUtils.outdent();
        log.debug("{}<<< Created model from csp5.mzn", LoggerUtils.tab());

        return model;
    }

    public Model createModel6() throws IOException {
        log.trace("{}Creating model from csp6.mzn >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        // create a model
        Model model = new Model("Test 6");

        // Decision variables
        model.intVar("v1", 1, 3);
        model.intVar("v2", 1, 3);
        model.intVar("v3", 1, 3);
        log.trace("{}Created variables", LoggerUtils.tab());

        ClassLoader classLoader = CSPModels.class.getClassLoader();
        @Cleanup InputStream inputStream = IOUtils.getInputStream(classLoader, "csp6.mzn");

        loadConstraints(inputStream, model);
        log.trace("{}Created constraints", LoggerUtils.tab());

        LoggerUtils.outdent();
        log.debug("{}<<< Created model from csp6.mzn", LoggerUtils.tab());

        return model;
    }

    public Model createModel7() throws IOException {
        log.trace("{}Creating model from csp7.mzn >>>", LoggerUtils.tab());
        LoggerUtils.indent();

        // create a model
        Model model = new Model("WipeOutR_FM Test 1");

        // Decision variables
        model.intVar("x", -10, 10);
        model.intVar("y", -10, 10);
        log.trace("{}Created variables", LoggerUtils.tab());

        ClassLoader classLoader = CSPModels.class.getClassLoader();
        @Cleanup InputStream inputStream = IOUtils.getInputStream(classLoader, "csp7.mzn");

        loadConstraints(inputStream, model);
        log.trace("{}Created constraints", LoggerUtils.tab());

        LoggerUtils.outdent();
        log.debug("{}<<< Created model from csp7.mzn", LoggerUtils.tab());

        return model;
    }

    public Model createModel8() throws IOException {
        log.trace("{}Creating model from csp8.mzn >>>", LoggerUtils.tab());
        LoggerUtils.indent();


        // create a model
        Model model = new Model("WipeOutR_FM Test 2");

        // Decision variables
        model.intVar("x", -10, 10);
        model.intVar("y", -10, 10);
        log.trace("{}Created variables", LoggerUtils.tab());

        ClassLoader classLoader = CSPModels.class.getClassLoader();
        @Cleanup InputStream inputStream = IOUtils.getInputStream(classLoader, "csp8.mzn");

        loadConstraints(inputStream, model);
        log.trace("{}Created constraints", LoggerUtils.tab());

        LoggerUtils.outdent();
        log.debug("{}<<< Created model from csp8.mzn", LoggerUtils.tab());

        return model;
    }
}
