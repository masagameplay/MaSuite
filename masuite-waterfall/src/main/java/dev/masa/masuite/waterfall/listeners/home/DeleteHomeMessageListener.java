package dev.masa.masuite.waterfall.listeners.home;

import dev.masa.masuite.api.proxy.listeners.home.IDeleteHomeMessageListener;
import dev.masa.masuite.common.models.home.Home;
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
import java.util.Optional;

public record DeleteHomeMessageListener(MaSuiteWaterfall plugin) implements Listener, IDeleteHomeMessageListener<PluginMessageEvent> {

    @EventHandler
    public void deleteHome(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.HOMES_DELETE.channel)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();

        String name = in.readUTF();

        this.plugin.homeService().home(player.getUniqueId(), name).thenAcceptAsync((home) -> this.delete(player, home));
    }

    @EventHandler
    public void deleteHomeOthers(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.HOMES_DELETE_OTHERS.channel)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();

        Audience audience = this.plugin.adventure().player(player);

        // Get targeted user
        String username = in.readUTF();
        String name = in.readUTF();

        this.plugin.userService().user(username).thenAcceptAsync((user) -> {
            if (user.isEmpty()) {
                MessageService.sendMessage(audience, this.plugin.messages().playerNotFound());
                return;
            }
            // Find & delete (seek and destroy)
            this.plugin.homeService().home(user.get().uniqueId(), name).thenAcceptAsync((home -> this.delete(player, home)));
        });

    }


    private void delete(ProxiedPlayer player, Optional<Home> home) {
        Audience audience = this.plugin.adventure().player(player);
        if (home.isEmpty()) {
            MessageService.sendMessage(audience, this.plugin.messages().homes().homeNotFound());
            return;
        }

        this.plugin.homeService().deleteHome(home.get()).thenAccept(done -> {
            if (done) {
                MessageService.sendMessage(audience, this.plugin.messages().homes().homeDeleted(), MessageService.Templates.homeTemplate(home.get()));
            } else {
                audience.sendMessage(Component.text("An error occurred while deleting home", NamedTextColor.RED));
            }
        });
    }
}
