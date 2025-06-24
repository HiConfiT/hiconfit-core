/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2023-2025
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.common.cli;

import lombok.Getter;
import lombok.NonNull;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * Command line options supporting a configuration file.
 */
@Getter
public class CmdLineOptionsWithCfg extends CmdLineOptionsBase {

    @Option(name = "-cfg",
            aliases="--configuration-file",
            usage = "Specify the configuration file.")
    private final String confFile = null;

    public CmdLineOptionsWithCfg(String banner, @NonNull String programTitle, String subtitle, @NonNull String usage) {
        super(banner, programTitle, subtitle, usage);

        parser = new CmdLineParser(this);
    }
}
