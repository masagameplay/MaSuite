package dev.masa.masuite.velocity.listeners.teleport;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import dev.masa.masuite.api.proxy.listeners.teleport.ITeleportMessageListener;
import dev.masa.masuite.common.objects.Location;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.common.services.MessageService;
import dev.masa.masuite.velocity.MaSuiteVelocity;
import net.kyori.adventure.text.minimessage.Template;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dev.masa.masuite.velocity.MaSuiteVelocity.MASUITE_MAIN_CHANNEL;

public record TeleportMessageListener(MaSuiteVelocity plugin) implements ITeleportMessageListener<PluginMessageEvent> {

    @Subscribe
    public void teleportToPlayer(PluginMessageEvent event) throws IOException {
        if (!event.getIdentifier().equals(MASUITE_MAIN_CHANNEL)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.TELEPORT_TO_PLAYER.channel)) {
            return;
        }
        Player player = (Player) event.getTarget();

        String name = in.readUTF();
        Optional<Player> target = this.plugin.proxy().getPlayer(name);

        if (target.isEmpty()) {
            MessageService.sendMessage(player, this.plugin.messages().playerNotOnline());
            return;
        }

        this.plugin.teleportationService().teleportPlayerToPlayer(player, target.get(), (done) -> {
            if (done) {
                MessageService.sendMessage(player, this.plugin.messages().teleports().teleportedToPlayer(), List.of(Template.of("username", target.get().getUsername())));
            }
        });
    }

    @Subscribe
    public void teleportPlayerToPlayer(PluginMessageEvent event) throws IOException {
        if (!event.getIdentifier().equals(MASUITE_MAIN_CHANNEL)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.TELEPORT_PLAYER_TO_PLAYER.channel)) {
            return;
        }
        Player player = (Player) event.getTarget();

        String name = in.readUTF();
        String secondName = in.readUTF();
        Optional<Player> target = this.plugin.proxy().getPlayer(name);
        Optional<Player> secondTarget = this.plugin.proxy().getPlayer(secondName);

        if (target.isEmpty()) {
            MessageService.sendMessage(player, this.plugin.messages().playerNotOnline());
            return;
        }

        if (secondTarget.isEmpty()) {
            MessageService.sendMessage(player, this.plugin.messages().playerNotOnline());
            return;
        }

        this.plugin.teleportationService().teleportPlayerToPlayer(target.get(), secondTarget.get(), (done) -> {
            if (done) {
                MessageService.sendMessage(player, this.plugin.messages().teleports().teleportedPlayerToPlayer(), List.of(
                        Template.of("first-player", target.get().getUsername()),
                        Template.of("second-player", secondTarget.get().getUsername())
                ));
            }
        });
    }

    @Subscribe
    public void teleportToLocation(PluginMessageEvent event) throws IOException {
        if (!event.getIdentifier().equals(MASUITE_MAIN_CHANNEL)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.TELEPORT_TO_LOCATION.channel)) {
            return;
        }
        Player player = (Player) event.getTarget();

        Location location = new Location().deserialize(in.readUTF());

        this.plugin.teleportationService().teleportPlayerToLocation(player, location, (done) -> {
            if (done) {
                MessageService.sendMessage(player, this.plugin.messages().teleports().teleportedToLocation(), MessageService.Templates.locationTemplate(location));
            }
        });
    }

    @Subscribe
    public void teleportPlayerToLocation(PluginMessageEvent event) throws IOException {
        if (!event.getIdentifier().equals(MASUITE_MAIN_CHANNEL)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.TELEPORT_PLAYER_TO_LOCATION.channel)) {
            return;
        }
        Player player = (Player) event.getTarget();

        String name = in.readUTF();
        Optional<Player> target = this.plugin.proxy().getPlayer(name);

        if (target.isEmpty()) {
            MessageService.sendMessage(player, this.plugin.messages().playerNotOnline());
            return;
        }

        Location location = new Location().deserialize(in.readUTF());
        location.server(player.getCurrentServer().get().getServerInfo().getName());

        this.plugin.teleportationService().teleportPlayerToLocation(target.get(), location, (done) -> {
            if (done) {
                var template = new ArrayList<>(MessageService.Templates.locationTemplate(location));
                template.add(Template.of("username", target.get().getUsername()));
                MessageService.sendMessage(player, this.plugin.messages().teleports().teleportedPlayerToLocation(), template);
            }
        });
    }


}
