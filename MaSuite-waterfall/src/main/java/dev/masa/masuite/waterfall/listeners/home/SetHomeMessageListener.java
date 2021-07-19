package dev.masa.masuite.waterfall.listeners.home;

import dev.masa.masuite.common.models.Home;
import dev.masa.masuite.common.models.User;
import dev.masa.masuite.common.objects.Location;
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

public class SetHomeMessageListener implements Listener {

    private final MaSuiteWaterfall plugin;

    public SetHomeMessageListener(MaSuiteWaterfall plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void createHomer(PluginMessageEvent event) throws IOException {
        if(!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.HOMES_SET.channel)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();

        String name = in.readUTF();

        // Deserialize location and assign correct server
        Location loc = new Location().deserialize(in.readUTF());
        loc.server(player.getServer().getInfo().getName());

        Home home = new Home(name, player.getUniqueId(), loc);

        this.plugin.homeService().createOrUpdateHome(home, (done, isCreated) -> {
            if(!done) {
                player.sendMessage(new TextComponent("§cHome with that name could not be found"));
                return;
            }
            if(isCreated) {
                player.sendMessage(new TextComponent("§aCreated home with name " + name));
            } else {
                player.sendMessage(new TextComponent("§aUpdated home with name " + name));
            }
        });
    }

    @EventHandler
    public void createHomeOthers(PluginMessageEvent event) throws IOException {
        if(!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.HOMES_SET_OTHERS.channel)) {
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

        // Build home
        String name = in.readUTF();
        // Deserialize location and assign correct server
        Location loc = new Location().deserialize(in.readUTF());
        loc.server(player.getServer().getInfo().getName());

        Home home = new Home(name, user.get().uniqueId(), loc);

        this.plugin.homeService().createOrUpdateHome(home, (done, isCreated) -> {
            if(!done) {
                player.sendMessage(new TextComponent("§cHome with that name could not be found"));
                return;
            }
            if(isCreated) {
                player.sendMessage(new TextComponent("§aCreated home with name " + name));
            } else {
                player.sendMessage(new TextComponent("§aUpdated home with name " + name));
            }
        });
    }
}
