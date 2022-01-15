package dev.masa.masuite.waterfall.listeners.teleport;

import dev.masa.masuite.api.proxy.listeners.teleport.ITeleportRequestListener;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.waterfall.MaSuiteWaterfall;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public record TeleportRequestListener(
        MaSuiteWaterfall plugin) implements Listener, ITeleportRequestListener<PluginMessageEvent> {

    @EventHandler
    public void createRequest(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.TELEPORT_REQUEST_CREATE.channel)) {
            return;
        }
        throw new UnsupportedOperationException("Creating teleportation request is not yet supported on Waterfall.");
    }

    @EventHandler
    public void acceptRequest(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.TELEPORT_REQUEST_ACCEPT.channel)) {
            return;
        }
        throw new UnsupportedOperationException("Accepting teleportation request is not yet supported on Waterfall.");
    }

    @EventHandler
    public void denyRequest(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.TELEPORT_REQUEST_DENY.channel)) {
            return;
        }
        throw new UnsupportedOperationException("Denying teleportation request is not yet supported on Waterfall.");
    }

    @EventHandler
    public void teleportLock(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.TELEPORT_REQUEST_LOCK.channel)) {
            return;
        }

        throw new UnsupportedOperationException("Teleportation request lock is not yet supported on Waterfall.");
    }
}
