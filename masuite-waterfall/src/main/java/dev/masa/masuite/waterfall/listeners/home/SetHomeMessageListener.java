package dev.masa.masuite.waterfall.listeners.home;

import dev.masa.masuite.api.proxy.listeners.home.ISetHomeMessageListener;
import dev.masa.masuite.common.models.home.Home;
import dev.masa.masuite.common.objects.Location;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.common.services.MessageService;
import dev.masa.masuite.waterfall.MaSuiteWaterfall;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public record SetHomeMessageListener(MaSuiteWaterfall plugin) implements Listener, ISetHomeMessageListener<PluginMessageEvent> {

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
        String name = in.readUTF();

        Audience audience = this.plugin.adventure().player(player);

        // Build home

        // Deserialize location and assign correct server
        Location loc = new Location().deserialize(in.readUTF());
        loc.server(player.getServer().getInfo().getName());

        this.plugin.userService().user(username).thenAcceptAsync((user) -> {
            if (user.isEmpty()) {
                MessageService.sendMessage(audience, this.plugin.messages().playerNotFound());
                return;
            }

            Home home = new Home(name, user.get().uniqueId(), loc);
            this.createHome(player, home);
        });

    }

    private void createHome(ProxiedPlayer player, Home home) {
        Audience audience = this.plugin.adventure().player(player);

        this.plugin.homeService().createOrUpdateHome(home, (done, isCreated) -> {
            if (!done) {
                audience.sendMessage(Component.text("An error occurred while creating / updating home.", NamedTextColor.RED));
                return;
            }
            if (isCreated) {
                MessageService.sendMessage(audience, this.plugin.messages().homes().homeSet(), MessageService.Templates.homeTemplate(home));
            } else {
                MessageService.sendMessage(audience, this.plugin.messages().homes().homeUpdated(), MessageService.Templates.homeTemplate(home));
            }
        });
    }

}
