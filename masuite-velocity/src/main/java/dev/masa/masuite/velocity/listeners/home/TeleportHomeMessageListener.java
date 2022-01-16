package dev.masa.masuite.velocity.listeners.home;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import dev.masa.masuite.api.proxy.listeners.home.ITeleportHomeMessageListener;
import dev.masa.masuite.common.models.home.Home;
import dev.masa.masuite.common.models.user.User;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.common.services.MessageService;
import dev.masa.masuite.velocity.MaSuiteVelocity;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Optional;

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

        this.plugin.homeService().home(player.getUniqueId(), name).ifPresentOrElse(home -> this.teleport(player, home), () -> MessageService.sendMessage(player, this.plugin.messages().homes().homeNotFound()));
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
        Optional<User> user = this.plugin.userService().user(username);

        if (user.isEmpty()) {
            MessageService.sendMessage(player, this.plugin.messages().playerNotFound());
            return;
        }

        String name = in.readUTF();

        this.plugin.homeService().home(user.get().uniqueId(), name).ifPresentOrElse(home -> this.teleport(player, home), () -> MessageService.sendMessage(player, this.plugin.messages().homes().homeNotFound()));
    }

    private void teleport(Player player, Home home) {
        this.plugin.teleportationService().teleportPlayerToLocation(player, home.location(), done -> MessageService.sendMessage(player, this.plugin.messages().homes().homeTeleported(), MessageService.Templates.homeTemplate(home)));
    }
}
