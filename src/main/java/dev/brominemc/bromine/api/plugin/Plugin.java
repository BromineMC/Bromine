package dev.brominemc.bromine.api.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dev.brominemc.bromine.api.command.Command;
import dev.brominemc.bromine.api.listener.Listener;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class Plugin {
    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory()).findAndRegisterModules();

    public abstract void onEnable();
    public abstract void onDisable();

    private final List<Command> commands = new ArrayList<>();
    private final List<Listener> listeners = new ArrayList<>();
    private boolean enabled = false;
    private PluginInfo info;

    public Plugin jar(JarFile jar) {
        JarEntry yml = jar.getJarEntry("plugin.yml");
        if (yml == null) throw new IllegalStateException("plugin.yml not in jar " + jar.getName());

        try (InputStream is = jar.getInputStream(yml)) {
            info = mapper.readValue(is, PluginInfo.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @ApiStatus.Internal
    public void addCommand(Command command) {
        commands.add(command);
    }

    @ApiStatus.Internal
    public void removeCommand(Command command) {
        commands.remove(command);
    }

    public List<Command> getCommands() {
        return new ArrayList<>(commands);
    }

    @ApiStatus.Internal
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    @ApiStatus.Internal
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public List<Listener> getListeners() {
        return new ArrayList<>(listeners);
    }

    public void enable() {
        if (enabled()) return;
        enabled = true;
        onEnable();
    }

    public void disable() {
        if (disabled()) return;
        enabled = false;
        onDisable();
    }

    public boolean enabled() {
        return enabled;
    }

    public boolean disabled() {
        return !enabled;
    }

    public PluginInfo info() {
        return info;
    }
}
