package dev.masa.masuite.waterfall.listeners.teleport;

import dev.masa.masuite.api.proxy.listeners.teleport.ITeleportMessageListener;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.common.services.MessageService;
import dev.masa.masuite.waterfall.MaSuiteWaterfall;
import net.kyori.adventure.audience.Audience;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public record TeleportMessageListener(
        MaSuiteWaterfall plugin) implements Listener, ITeleportMessageListener<PluginMessageEvent> {

    @EventHandler
    public void teleportToPlayer(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.TELEPORT_TO_PLAYER.channel)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();

        String name = in.readUTF();
        ProxiedPlayer target = this.plugin().loader().getProxy().getPlayer(name);

        Audience audience = this.plugin.adventure().player(player);
        if (target == null) {
            MessageService.sendMessage(audience, this.plugin().messages().playerNotOnline());
            return;
        }

        this.plugin.teleportationService().teleportPlayerToPlayer(player, target, (done) -> {
            // TODO: Send message to player
            this.plugin.loader().getLogger().info(player.getName() + " teleport to " + target.getName());
        });
    }

    @EventHandler
    public void teleportPlayerToPlayer(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.TELEPORT_PLAYER_TO_PLAYER.channel)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();

        String name = in.readUTF();
        String secondTargetUUID = in.readUTF();
        ProxiedPlayer target = this.plugin().loader().getProxy().getPlayer(name);
        ProxiedPlayer secondTarget = this.plugin().loader().getProxy().getPlayer(secondTargetUUID);

        Audience audience = this.plugin.adventure().player(player);
        if (target == null) {
            MessageService.sendMessage(audience, this.plugin().messages().playerNotOnline());
            return;
        }

        if (secondTarget == null) {
            MessageService.sendMessage(audience, this.plugin().messages().playerNotOnline());
            return;
        }

        this.plugin.teleportationService().teleportPlayerToPlayer(target, secondTarget, (done) -> {
            // TODO: Send message to executor
            this.plugin.loader().getLogger().info(target.getName() + " teleport to " + secondTarget.getName());
        });


    }

    @EventHandler
    public void teleportToLocation(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.TELEPORT_TO_LOCATION.channel)) {
            return;
        }
        throw new UnsupportedOperationException("Not implemented");
    }

    @EventHandler
    public void teleportPlayerToLocation(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.TELEPORT_PLAYER_TO_LOCATION.channel)) {
            return;
        }

        throw new UnsupportedOperationException("Not implemented");
    }

    @EventHandler
    public void teleportAll(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.TELEPORT_ALL_TO_PLAYER.channel)) {
            return;
        }
        throw new UnsupportedOperationException("Not implemented");
    }
}
