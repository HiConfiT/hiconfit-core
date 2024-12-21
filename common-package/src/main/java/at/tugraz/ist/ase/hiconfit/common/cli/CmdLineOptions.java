/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.common.cli;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CmdLineOptions {

    public CmdLineOptionsWithCfg withCfg(@NonNull String programTitle, @NonNull String usage) {
        return new CmdLineOptionsWithCfg(null, programTitle, null, usage);
    }

    public CmdLineOptionsWithCfg withCfg(@NonNull String banner, @NonNull String programTitle, @NonNull String usage) {
        return new CmdLineOptionsWithCfg(banner, programTitle, null, usage);
    }

    public CmdLineOptionsWithCfg withCfg(@NonNull String banner, @NonNull String programTitle, @NonNull String subtitle, @NonNull String usage) {
        return new CmdLineOptionsWithCfg(banner, programTitle, subtitle, usage);
    }

}
