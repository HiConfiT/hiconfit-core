/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.kb.app.cli;

import at.tugraz.ist.ase.common.CmdLineOptionsBase;
import lombok.Getter;
import lombok.NonNull;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

import java.util.List;

public class KBStatistics_CmdLineOptions extends CmdLineOptionsBase {
    @Getter
    @Option(name = "-kb",
            aliases="--knowledge-base",
            usage = "Specify the name of a knowledge base, e.g. PC or Renault.",
            handler = StringArrayOptionHandler.class)
    private List<String> kb;

    @Getter
    @Option(name = "-fm",
            aliases="--feature-model",
            usage = "Specify the name of a feature model.")
    private String fm = null;

    @Getter
    @Option(name = "-fm-dir",
            aliases="--feature-model-dir",
            usage = "Specify the directory of feature models.")
    private String fmDir = null;

    @Getter
    @Option(name = "-out",
            aliases="--output-file",
            usage = "Specify the output file.")
    private String outFile = "./statistics.txt";

    public KBStatistics_CmdLineOptions(String banner, @NonNull String programTitle, String subtitle, @NonNull String usage) {
        super(banner, programTitle, subtitle, usage);

        parser = new CmdLineParser(this);
    }
}
