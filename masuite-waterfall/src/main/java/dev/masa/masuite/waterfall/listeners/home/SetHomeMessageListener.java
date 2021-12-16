package dev.masa.masuite.waterfall.listeners.home;

import dev.masa.masuite.common.models.Home;
import dev.masa.masuite.common.models.User;
import dev.masa.masuite.common.objects.Location;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.waterfall.MaSuiteWaterfall;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
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
    public void createHome(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
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

        this.createHome(player, home);
    }

    @EventHandler
    public void createHomeOthers(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
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

        Audience audience = this.plugin.adventure().player(player);

        if (user.isEmpty()) {
            audience.sendMessage(this.plugin.messages().playerNotFound());
            return;
        }

        // Build home
        String name = in.readUTF();
        // Deserialize location and assign correct server
        Location loc = new Location().deserialize(in.readUTF());
        loc.server(player.getServer().getInfo().getName());

        Home home = new Home(name, user.get().uniqueId(), loc);

        this.createHome(player, home);

    }

    private void createHome(ProxiedPlayer player, Home home) {
        Audience audience = this.plugin.adventure().player(player);

        TextReplacementConfig replacement = TextReplacementConfig.builder()
                .match("%home%")
                .replacement(home.name())
                .build();

        this.plugin.homeService().createOrUpdateHome(home, (done, isCreated) -> {
            if (!done) {
                audience.sendMessage(Component.text("An error occurred while creating / updating home.", NamedTextColor.RED));
                return;
            }
            if (isCreated) {
                audience.sendMessage(this.plugin.messages().homes().homeSet().replaceText(replacement));
            } else {
                audience.sendMessage(this.plugin.messages().homes().homeUpdated().replaceText(replacement));
            }
        });
    }

}
