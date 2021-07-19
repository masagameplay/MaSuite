package dev.masa.masuite.waterfall.listeners.home;

import dev.masa.masuite.common.models.Home;
import dev.masa.masuite.common.models.User;
import dev.masa.masuite.common.objects.MaSuiteMessage;
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

public class DeleteHomeMessageListener implements Listener {

    private final MaSuiteWaterfall plugin;

    public DeleteHomeMessageListener(MaSuiteWaterfall plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void deleteHome(PluginMessageEvent event) throws IOException {
        if(!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.HOMES_DELETE.channel)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();

        String name = in.readUTF();

        Optional<Home> home = this.plugin.homeService().home(player.getUniqueId(), name);

        this.delete(player, home);
    }

    @EventHandler
    public void deleteHomeOthers(PluginMessageEvent event) throws IOException {
        if(!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.HOMES_DELETE_OTHERS.channel)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();

        // Get targeted user
        String username = in.readUTF();
        Optional<User> user = this.plugin.userService().user(username);

        if (user.isEmpty()) {
            player.sendMessage(new TextComponent("§cCould not find user named " + username));
            return;
        }

        // Find & delete
        String name = in.readUTF();

        Optional<Home> home = this.plugin.homeService().home(user.get().uniqueId(), name);

        this.delete(player, home);

    }

    private void delete(ProxiedPlayer player, Optional<Home> home) {
        if(home.isEmpty()) {
            player.sendMessage(new TextComponent("§cHome with that name could not be found."));
            return;
        }

        this.plugin.homeService().deleteHome(home.get(), done -> {
            if(done) {
                player.sendMessage(new TextComponent("§aDeleted home with name " + home.get().name()));
            } else {
                player.sendMessage(new TextComponent("§cAn error occurred while deleting home"));
            }
        });
    }
}
