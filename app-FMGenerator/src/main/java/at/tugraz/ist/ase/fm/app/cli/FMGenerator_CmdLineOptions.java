/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fm.app.cli;

import at.tugraz.ist.ase.common.CmdLineOptionsBase;
import lombok.Getter;
import lombok.NonNull;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class FMGenerator_CmdLineOptions extends CmdLineOptionsBase {

    @Getter
    @Option(name = "-c",
            aliases="--#constraints",
            usage = "Number of constraints",
            required = true)
    private int numConstraints;

    @Getter
    @Option(name ="-fm",
            aliases="--#feature-models",
            usage = "Number of feature models",
            required = true)
    private int numGenFMs;

    @Getter
    @Option(name = "-ctc",
        aliases="--cross-tree-constraints",
        usage = "The ratio of cross tree constraints")
    private double CTC = 0.8;

    @Getter
    @Option(name = "-g",
            aliases="--max-generations",
            usage = "Max generations")
    private int maxGenerations = 5;

    @Getter
    @Option(name = "-out",
            aliases="--output-folder",
            usage = "Specify the output folder.")
    private String outFolder = "./";

    public FMGenerator_CmdLineOptions(String banner, @NonNull String programTitle, String subtitle, @NonNull String usage) {
        super(banner, programTitle, subtitle, usage);

        parser = new CmdLineParser(this);
    }
}
