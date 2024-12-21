/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.common.cfg;

import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;

@UtilityClass
public class TomlConfigLoader {

    public <B extends BaseAppConfig> B loadConfig(String configPath, Class<B> configClass) throws IOException {
        TomlMapper mapper = new TomlMapper();
        return mapper.readValue(new File(configPath), configClass);
    }

}
