package dev.masa.masuite.waterfall.listeners.home;

import dev.masa.masuite.common.models.Home;
import dev.masa.masuite.common.models.User;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.waterfall.MaSuiteWaterfall;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.TextReplacementConfig;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Optional;

public class TeleportHomeMessageListener implements Listener {

    private final MaSuiteWaterfall plugin;

    public TeleportHomeMessageListener(MaSuiteWaterfall plugin) {
        this.plugin = plugin;
    }

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

        Optional<Home> home = this.plugin.homeService().home(player.getUniqueId(), name);

        this.teleport(player, home);

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

        // Get targeted user
        String username = in.readUTF();
        Optional<User> user = this.plugin.userService().user(username);

        if (user.isEmpty()) {
            player.sendMessage(new TextComponent(this.plugin.messages().playerNotFound()));
            return;
        }

        // Search home & teleport
        String name = in.readUTF();
        Optional<Home> home = this.plugin.homeService().home(user.get().uniqueId(), name);

        this.teleport(player, home);


    }

    private void teleport(ProxiedPlayer player, Optional<Home> home) {
        Audience audience = this.plugin.adventure().player(player);
        if (home.isEmpty()) {
            audience.sendMessage(this.plugin.messages().homes().homeNotFound());
            return;
        }

        TextReplacementConfig replacement = TextReplacementConfig.builder()
                .match("%home%")
                .replacement(home.get().name())
                .build();

        this.plugin.teleportationService().teleportPlayerToLocation(player, home.get().location(), done -> {
            audience.sendMessage(this.plugin.messages().homes().homeTeleported().replaceText(replacement));
        });
    }
}
