package dev.brominemc.bromine.api.command;

import dev.brominemc.bromine.api.plugin.Plugin;

import static net.minestom.server.MinecraftServer.getCommandManager;

public class Command extends net.minestom.server.command.builder.Command {
    private final Plugin plugin;

    public Command(Plugin plugin, String name) {
        super(name);
        this.plugin = plugin;
    }

    public Command(Plugin plugin, String name, String... aliases) {
        super(name, aliases);
        this.plugin = plugin;
    }

    public void register() {
        plugin.addCommand(this);
        getCommandManager().register(this);
    }

    public void unregister() {
        plugin.removeCommand(this);
        getCommandManager().unregister(this);
    }
}
