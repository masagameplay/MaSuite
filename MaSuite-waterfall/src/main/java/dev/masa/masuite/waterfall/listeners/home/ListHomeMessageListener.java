package dev.masa.masuite.waterfall.listeners.home;

import dev.masa.masuite.common.models.Home;
import dev.masa.masuite.common.models.User;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.waterfall.MaSuiteWaterfall;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ListHomeMessageListener implements Listener {

    private final MaSuiteWaterfall plugin;

    public ListHomeMessageListener(MaSuiteWaterfall plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void listHomes(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.HOMES_LIST.channel)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();

        List<Home> homes = this.plugin.homeService().homes(player.getUniqueId());

        BaseComponent baseComponent = new TextComponent("§9Homes: §7");

        for (Home home : homes) {
            baseComponent.addExtra(home.name());
            baseComponent.addExtra(", ");
        }

        player.sendMessage(baseComponent);

    }

    @EventHandler
    public void listUserHomes(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.HOMES_LIST_OTHERS.channel)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();

        // Get target user
        String username = in.readUTF();
        Optional<User> user = this.plugin.userService().user(username);

        if (user.isEmpty()) {
            player.sendMessage(new TextComponent(this.plugin.messages().playerNotFound()));
            return;
        }

        // Query homes and send them to player
        List<Home> homes = this.plugin.homeService().homes(user.get().uniqueId());

        BaseComponent baseComponent = new TextComponent("§9" + user.get().username() + "'s homes: §7");

        for (Home home : homes) {
            baseComponent.addExtra(home.name());
            baseComponent.addExtra(", ");
        }

        player.sendMessage(baseComponent);

    }
}
