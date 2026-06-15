package dev.brominemc.bromine.api.plugin;

import java.util.List;

public record PluginInfo(String main, String name, List<String> authors, String version) {}