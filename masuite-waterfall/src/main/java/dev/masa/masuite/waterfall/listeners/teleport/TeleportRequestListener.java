package dev.masa.masuite.waterfall.listeners.teleport;

import dev.masa.masuite.api.proxy.listeners.teleport.ITeleportRequestListener;
import dev.masa.masuite.waterfall.MaSuiteWaterfall;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;

public record TeleportRequestListener(
        MaSuiteWaterfall plugin) implements Listener, ITeleportRequestListener<PluginMessageEvent> {

    @EventHandler
    public void createRequest(PluginMessageEvent event) throws IOException {
        throw new UnsupportedOperationException("Creating teleportation request is not yet supported on Waterfall.");
    }

    @EventHandler
    public void acceptRequest(PluginMessageEvent event) throws IOException {
        throw new UnsupportedOperationException("Accepting teleportation request is not yet supported on Waterfall.");
    }

    @EventHandler
    public void denyRequest(PluginMessageEvent event) throws IOException {
        throw new UnsupportedOperationException("Denying teleportation request is not yet supported on Waterfall.");
    }

    @EventHandler
    public void teleportLock(PluginMessageEvent event) throws IOException {
        throw new UnsupportedOperationException("Teleportation request lock is not yet supported on Waterfall.");
    }
}
