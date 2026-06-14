package dev.brominemc.bromine.api.command;

import static net.minestom.server.MinecraftServer.getCommandManager;

public class Command extends net.minestom.server.command.builder.Command {
    public Command(String name) {
        super(name);
    }

    public Command(String name, String... aliases) {
        super(name, aliases);
    }

    public void register() {
        getCommandManager().register(this);
    }

    public void unregister() {
        getCommandManager().unregister(this);
    }
}
