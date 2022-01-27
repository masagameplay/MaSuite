package dev.masa.masuite.velocity.listeners.home;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import dev.masa.masuite.api.proxy.listeners.home.ITeleportHomeMessageListener;
import dev.masa.masuite.common.models.home.Home;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.common.services.MessageService;
import dev.masa.masuite.velocity.MaSuiteVelocity;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import static dev.masa.masuite.velocity.MaSuiteVelocity.MASUITE_MAIN_CHANNEL;

public record TeleportHomeMessageListener(
        MaSuiteVelocity plugin) implements ITeleportHomeMessageListener<PluginMessageEvent> {

    @Subscribe
    public void teleportHome(PluginMessageEvent event) throws IOException {
        if (!event.getIdentifier().equals(MASUITE_MAIN_CHANNEL)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.HOMES_TELEPORT.channel)) {
            return;
        }
        Player player = (Player) event.getTarget();

        String name = in.readUTF();

        this.plugin.homeService().home(player.getUniqueId(), name).thenAcceptAsync(home -> {
            if (home.isEmpty()) {
                MessageService.sendMessage(player, this.plugin.messages().homes().homeNotFound());
                return;
            }
            this.teleport(player, home.get());
        });
    }

    @Subscribe
    public void teleportHomeOthers(PluginMessageEvent event) throws IOException {
        if (!event.getIdentifier().equals(MASUITE_MAIN_CHANNEL)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.HOMES_TELEPORT_OTHERS.channel)) {
            return;
        }
        Player player = (Player) event.getTarget();

        String username = in.readUTF();
        String name = in.readUTF();
        this.plugin.userService().user(username).thenAcceptAsync(user -> {
            if (user.isEmpty()) {
                MessageService.sendMessage(player, this.plugin.messages().playerNotFound());
                return;
            }

            this.plugin.homeService().home(user.get().uniqueId(), name).thenAcceptAsync(home -> {
                if (home.isEmpty()) {
                    MessageService.sendMessage(player, this.plugin.messages().homes().homeNotFound());
                    return;
                }
                this.teleport(player, home.get());
            });
        });
    }

    private void teleport(Player player, Home home) {
        this.plugin.teleportationService().teleportPlayerToLocation(player, home.location(), done -> MessageService.sendMessage(player, this.plugin.messages().homes().homeTeleported(), MessageService.Templates.homeTemplate(home)));
    }
}
