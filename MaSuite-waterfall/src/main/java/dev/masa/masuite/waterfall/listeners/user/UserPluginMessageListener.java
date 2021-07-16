package dev.masa.masuite.waterfall.listeners.user;

import dev.masa.masuite.common.models.User;
import dev.masa.masuite.waterfall.MaSuiteWaterfall;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Optional;

public class UserPluginMessageListener implements Listener {

    private final MaSuiteWaterfall plugin;

    public UserPluginMessageListener(MaSuiteWaterfall plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equals("BungeeCord")) {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals("masuite:user:info")) {
            return;
        }

        String username = in.readUTF();
        ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();

        Optional<User> user = this.plugin.userService().user(username);

        if (user.isEmpty()) {
            player.sendMessage(new TextComponent("&c Could not find user named " + username));
            return;
        }

        player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7&m[========&9 INFO &7&m========]")));
        player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7 * Username: &9" + user.get().username())));
        player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7 * First time login: &9" + user.get().firstLogin())));
        player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7 * Latest login: &9" + user.get().lastLogin())));
        player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7&m[=====================]")));

    }
}
