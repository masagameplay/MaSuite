package dev.masa.masuite.velocity.listeners.teleport;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import dev.masa.masuite.api.proxy.listeners.teleport.ISpawnMessageListener;
import dev.masa.masuite.common.models.teleport.Spawn;
import dev.masa.masuite.common.objects.Location;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.common.services.MessageService;
import dev.masa.masuite.velocity.MaSuiteVelocity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Optional;

import static dev.masa.masuite.velocity.MaSuiteVelocity.MASUITE_MAIN_CHANNEL;

public record SpawnMessageListener(MaSuiteVelocity plugin) implements ISpawnMessageListener<PluginMessageEvent> {

    @Subscribe
    public void createSpawn(PluginMessageEvent event) throws IOException {
        if (!event.getIdentifier().equals(MASUITE_MAIN_CHANNEL)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.SPAWN_CREATE.channel)) {
            return;
        }
        Player player = (Player) event.getTarget();

        Location location = new Location().deserialize(in.readUTF());
        location.server(player.getCurrentServer().get().getServerInfo().getName());

        boolean isDefaultSpawn = in.readBoolean();

        Spawn spawn = new Spawn(location, isDefaultSpawn);
        this.plugin.spawnService().createOrUpdateSpawn(spawn, (done, isCreated) -> {
            if (!done) {
                player.sendMessage(Component.text("An error occurred while creating / updating spawn.", NamedTextColor.RED));
                return;
            }
            if (isCreated) {
                MessageService.sendMessage(player, this.plugin.messages().teleports().spawn().created(), MessageService.Templates.spawnTemplate(spawn));
            } else {
                MessageService.sendMessage(player, this.plugin.messages().teleports().spawn().updated(), MessageService.Templates.spawnTemplate(spawn));
            }
        });

    }

    @Subscribe
    public void deleteSpawn(PluginMessageEvent event) throws IOException {
        if (!event.getIdentifier().equals(MASUITE_MAIN_CHANNEL)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.SPAWN_DELETE.channel)) {
            return;
        }
        Player player = (Player) event.getTarget();
        boolean isDefault = in.readBoolean();

        this.plugin.spawnService().spawn(player.getCurrentServer().get().getServerInfo().getName(), isDefault).thenAcceptAsync(spawn -> {
            if (spawn.isEmpty()) {
                MessageService.sendMessage(player, this.plugin.messages().teleports().spawn().spawnNotFound());
                return;
            }

            this.plugin.spawnService().deleteSpawn(spawn.get()).thenAccept(done -> {
                if (done) {
                    MessageService.sendMessage(player, this.plugin.messages().teleports().spawn().deleted(), MessageService.Templates.spawnTemplate(spawn.get()));
                } else {
                    player.sendMessage(Component.text("An error occurred while deleting spawn", NamedTextColor.RED));
                }
            });
        });
    }

    @Subscribe
    public void teleportToSpawn(PluginMessageEvent event) throws IOException {
        if (!event.getIdentifier().equals(MASUITE_MAIN_CHANNEL)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.SPAWN_TELEPORT.channel)) {
            return;
        }
        Player player = (Player) event.getTarget();
        boolean isDefault = in.readBoolean();

        this.plugin.spawnService().spawn(player.getCurrentServer().get().getServerInfo().getName(), isDefault).thenAccept(spawn -> {
            if (spawn.isEmpty()) {
                MessageService.sendMessage(player, this.plugin.messages().teleports().spawn().spawnNotFound());
                return;
            }

            this.plugin.teleportationService().teleportPlayerToLocation(player, spawn.get().location(), done ->
                    MessageService.sendMessage(player, this.plugin.messages().teleports().spawn().teleported(), MessageService.Templates.spawnTemplate(spawn.get()))
            );
        });
    }
}
