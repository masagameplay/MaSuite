package dev.masa.masuite.waterfall.listeners.home;

import dev.masa.masuite.common.models.Home;
import dev.masa.masuite.common.objects.Location;
import dev.masa.masuite.waterfall.MaSuiteWaterfall;
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
    public void onTeleportHome(PluginMessageEvent event) throws IOException {
        if(!event.getTag().equals("BungeeCord")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals("masuite:homes:teleport")) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();

        String name = in.readUTF();

        Optional<Home> home = this.plugin.homeService().home(player.getUniqueId(), name);

        if(home.isEmpty()) {
            player.sendMessage(new TextComponent("§cCould not find home with that name"));
            return;
        }

        this.plugin.teleportationService().teleportPlayerToLocation(player, home.get().location(), done -> {
            player.sendMessage(new TextComponent("§aTeleported to " + home.get().name()));
        });

    }
}
