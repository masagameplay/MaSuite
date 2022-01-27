package dev.masa.masuite.waterfall.listeners.home;

import dev.masa.masuite.api.proxy.listeners.home.ITeleportHomeMessageListener;
import dev.masa.masuite.common.models.home.Home;
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

public record TeleportHomeMessageListener(
        MaSuiteWaterfall plugin) implements Listener, ITeleportHomeMessageListener<PluginMessageEvent> {

    @EventHandler
    public void teleportHome(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.HOMES_TELEPORT.channel)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();

        String name = in.readUTF();

        Audience audience = this.plugin.adventure().player(player);
        this.plugin.homeService().home(player.getUniqueId(), name).thenAcceptAsync(home -> {
            if (home.isEmpty()) {
                MessageService.sendMessage(audience, this.plugin.messages().homes().homeNotFound());
                return;
            }
            this.teleport(player, home.get());
        });

    }

    @EventHandler
    public void teleportHomeOthers(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.HOMES_TELEPORT_OTHERS.channel)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();

        Audience audience = this.plugin.adventure().player(player);

        // Get targeted user
        String username = in.readUTF();
        String name = in.readUTF();
        this.plugin.userService().user(username).thenAcceptAsync(user -> {
            if (user.isEmpty()) {
                MessageService.sendMessage(audience, this.plugin.messages().playerNotFound());
                return;
            }

            this.plugin.homeService().home(user.get().uniqueId(), name).thenAcceptAsync(home -> {
                if (home.isEmpty()) {
                    MessageService.sendMessage(audience, this.plugin.messages().homes().homeNotFound());
                    return;
                }
                this.teleport(player, home.get());
            });
        });


    }

    private void teleport(ProxiedPlayer player, Home home) {
        Audience audience = this.plugin.adventure().player(player);
        this.plugin.teleportationService().teleportPlayerToLocation(player, home.location(), done -> {
            MessageService.sendMessage(audience, this.plugin.messages().homes().homeTeleported(), MessageService.Templates.homeTemplate(home));
        });
    }
}
