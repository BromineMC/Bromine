package dev.brominemc.bromine.api.listener;

import dev.brominemc.bromine.api.plugin.Plugin;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.trait.CancellableEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class Listener {
    private static final GlobalEventHandler geh = MinecraftServer.getGlobalEventHandler();
    private static final Map<Class<? extends net.minestom.server.event.Event>, List<E>> registered = new ConcurrentHashMap<>();

    private final Plugin plugin;
    private E listener;

    public Listener(Plugin plugin) {
        this.plugin = plugin;
        for (Method method : getClass().getMethods()) {
            if (method.isAnnotationPresent(Event.class) && method.getParameterCount() == 1) {
                Class<? extends net.minestom.server.event.Event> type = (Class<? extends net.minestom.server.event.Event>) method.getParameterTypes()[0];
                Event annotation = method.getAnnotation(Event.class);
                listener = new E(annotation.priority(), annotation.ignoreCancelled(), method, this);

                List<E> listeners = registered.computeIfAbsent(type, t -> {
                    EventListener<? extends net.minestom.server.event.Event> listen = EventListener.builder(t)
                            .handler(event -> {
                                for (E e : registered.getOrDefault(t, new ArrayList<>())) {
                                    try {
                                        if (event instanceof CancellableEvent ce && ce.isCancelled() && !e.ignoreCancelled) continue;
                                        e.method().invoke(e.l(), event);
                                    } catch (IllegalAccessException | InvocationTargetException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                }
                            })
                            .build();
                    geh.addListener(listen);
                    return new ArrayList<>();
                });
                listeners.add(listener);
                listeners.sort(Comparator.comparingInt(E::priority));
            }
        }
    }

    public void register() {
        plugin.addListener(this);
        listener.active(true);
    }

    public void unregister() {
        plugin.removeListener(this);
        listener.active(false);
    }

    private static class E {
        private final int priority;
        private final boolean ignoreCancelled;
        private final Method method;
        private final Listener l;
        private boolean active = false;

        private E(int priority, boolean ignoreCancelled, Method method, Listener l) {
            this.priority = priority;
            this.ignoreCancelled = ignoreCancelled;
            this.method = method;
            this.l = l;
        }

        public int priority() {
            return priority;
        }

        public boolean ignoreCancelled() {
            return ignoreCancelled;
        }

        public Method method() {
            return method;
        }

        public Listener l() {
            return l;
        }

        public boolean active() {
            return active;
        }

        public void active(boolean active) {
            this.active = active;
        }
    }
}