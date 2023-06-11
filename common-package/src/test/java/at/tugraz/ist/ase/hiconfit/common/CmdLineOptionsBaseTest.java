/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.common;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CmdLineOptionsBaseTest {

    static class CmdLineOptions extends CmdLineOptionsBase {
        @Getter
        @Option(name = "-i",
                aliases="--input",
                usage = "The dataset.")
        private String dataModelFile = null;

        @Getter
        @Option(name = "-p",
                aliases="--parameters",
                usage = "Parameters for the matrix factorization.")
        private String confFile = null;

        @Getter
        @Option(name = "-u",
                aliases="--user",
                usage = "User id.")
        private int userId = -1;

        public CmdLineOptions(String banner, @NonNull String programTitle, String subtitle, @NonNull String usage) {
            super(banner, programTitle, subtitle, usage);

            parser = new CmdLineParser(this);
        }
    }

    @Test
    void shouldPrintUsage() {
        String programTitle = "CRM Recommendation";
        String usage = "Usage: java -jar CRMRec.jar [options]";
        String[] args = new String[]{"-h"};

        CmdLineOptions cmdLineOptions = new CmdLineOptions(null, programTitle, null, usage);
        cmdLineOptions.parseArgument(args);

        assertTrue(cmdLineOptions.isHelp());

        if (cmdLineOptions.isHelp()) {
            cmdLineOptions.printUsage();
        }
    }

    @Test
    void shouldHaveOtherArguments() {
        String programTitle = "CRM Recommendation";
        String usage = "Usage: java -jar CRMRec.jar [options]";
        String[] args = new String[]{"-i", "./data/interactions.csv", "-p", "conf.txt", "-u", "2"};

        CmdLineOptions cmdLineOptions = new CmdLineOptions(null, programTitle, null, usage);
        cmdLineOptions.parseArgument(args);

        assertFalse(cmdLineOptions.isHelp());
        assertEquals("./data/interactions.csv", cmdLineOptions.getDataModelFile());
        assertEquals("conf.txt", cmdLineOptions.getConfFile());
        assertEquals(2, cmdLineOptions.getUserId());
    }
}