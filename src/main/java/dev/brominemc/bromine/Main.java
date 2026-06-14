package dev.brominemc.bromine;

import net.minestom.server.MinecraftServer;

public class Main {
    static void main() {
        MinecraftServer server = MinecraftServer.init();

        server.start("0.0.0.0", 25566);
    }
}
