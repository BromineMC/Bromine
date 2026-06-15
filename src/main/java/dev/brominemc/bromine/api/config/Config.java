package dev.brominemc.bromine.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dev.brominemc.bromine.api.plugin.Plugin;

public class Config {
    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory()).findAndRegisterModules();

    public Config(Plugin plugin, String name) {

    }
}
