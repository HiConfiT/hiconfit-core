/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.common;import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import static com.google.common.base.Preconditions.checkState;

@Slf4j
public class CmdLineOptionsBase {
    @Getter
    @Option(name = "-h",
            aliases="--help",
            usage = "Prints usage info.")
    private boolean help = false;

    protected CmdLineParser parser;
    private final String banner;
    private final String programTitle;
    private final String subtitle;
    private final String usage;

    public CmdLineOptionsBase(String banner, @NonNull String programTitle, String subtitle, @NonNull String usage) {
        this.banner = banner;
        this.programTitle = programTitle;
        this.subtitle = subtitle;
        this.usage = usage;
        this.parser = null;
    }

    public void parseArgument(@NonNull String[] args) {
        checkState(parser != null, "CmdLineParser not initialized");

        try {
            parser.parseArgument(args);
        } catch (CmdLineException clEx) {
            log.error("{}Unable to parse command-line options: {}", LoggerUtils.tab(), clEx.getMessage());
            printUsage();
            System.exit(-1);
        }
    }

    public void printUsage() {
        printWelcome();

        System.out.println(usage);
        System.out.println("Options:");
        parser.printUsage(System.out);
    }

    public void printWelcome() {
        checkState(parser != null, "CmdLineParser not initialized");

        if (banner != null) {
            System.out.println(banner);
        }
        System.out.println(programTitle);
        if (subtitle != null) {
            System.out.println(subtitle);
        }
    }
}
