package dev.masa.masuite.velocity.services;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.masa.masuite.api.services.ITeleportationService;
import dev.masa.masuite.common.objects.Location;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.velocity.MaSuiteVelocity;
import dev.masa.masuite.velocity.utils.VelocityPluginMessage;

import java.util.function.Consumer;

public class TeleportationService implements ITeleportationService<Player, Location> {

    private final MaSuiteVelocity plugin;

    public TeleportationService(MaSuiteVelocity plugin) {
        this.plugin = plugin;
    }

    @Override
    public void teleportPlayerToLocation(Player player, Location location, Consumer<Boolean> done) {
        // If location does not have server specified, the server will be the same as players current server
        if(location.server() == null) {
            location.server(player.getCurrentServer().get().getServerInfo().getName());
        }

        this.plugin.proxy().getServer(location.server()).ifPresent(registeredServer -> {
            VelocityPluginMessage message = new VelocityPluginMessage(registeredServer, MaSuiteMessage.TELEPORT_TO_LOCATION, player.getUniqueId().toString(), location.serialize());
            this.connectAndSendPlayer(player, registeredServer, message, done);
        });
    }

    @Override
    public void teleportPlayerToPlayer(Player player, Player target, Consumer<Boolean> done) {
        player.getCurrentServer().ifPresent(server -> {
            VelocityPluginMessage message = new VelocityPluginMessage(server.getServer(), MaSuiteMessage.TELEPORT_TO_PLAYER, player.getUniqueId().toString(), target.getUniqueId().toString());
            this.connectAndSendPlayer(player, server.getServer(), message, done);
        });
    }

    private void connectAndSendPlayer(Player player, RegisteredServer registeredServer, VelocityPluginMessage message, Consumer<Boolean> done) {
        if(player.getCurrentServer().isPresent() && player.getCurrentServer().get().getServerInfo().equals(registeredServer.getServerInfo())) {
            message.send();
            done.accept(true);
            return;
        }

        player.createConnectionRequest(registeredServer).connectWithIndication().thenRun(() -> {
            message.send();
            done.accept(true);
        });

    }
}
