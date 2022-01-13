package dev.masa.masuite.waterfall.services;

import dev.masa.masuite.api.services.ITeleportationService;
import dev.masa.masuite.common.objects.Location;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.waterfall.MaSuiteWaterfall;
import dev.masa.masuite.waterfall.utils.BungeePluginMessage;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.function.Consumer;

public record TeleportationService(MaSuiteWaterfall plugin) implements ITeleportationService<ProxiedPlayer, Location> {

    @Override
    public void teleportPlayerToLocation(ProxiedPlayer player, Location location, Consumer<Boolean> done) {
        ServerInfo destinationServer = this.plugin.loader().getProxy().getServerInfo(location.server());
        BungeePluginMessage message = new BungeePluginMessage(destinationServer, MaSuiteMessage.TELEPORT_TO_LOCATION, player.getUniqueId().toString(), location.serialize());
        this.connectAndSendPlayer(player, destinationServer, message, done);
    }

    @Override
    public void teleportPlayerToPlayer(ProxiedPlayer player, ProxiedPlayer target, Consumer<Boolean> done) {
        ServerInfo destinationServer = target.getServer().getInfo();
        BungeePluginMessage message = new BungeePluginMessage(destinationServer, MaSuiteMessage.TELEPORT_TO_PLAYER, player.getUniqueId().toString(), target.getUniqueId().toString());
        this.connectAndSendPlayer(player, destinationServer, message, done);
    }

    /**
     * A helper function to send player to targeted server
     *
     * @param player            player to send
     * @param destinationServer destination server
     * @param message           plugin message to send
     * @param done              when teleportation is done
     */
    private void connectAndSendPlayer(ProxiedPlayer player, ServerInfo destinationServer, BungeePluginMessage message, Consumer<Boolean> done) {
        if (player.getServer().getInfo().equals(destinationServer)) {
            message.send();
            done.accept(true);
            return;
        }

        this.plugin.loader().getProxy().getScheduler().runAsync(this.plugin.loader(), () -> {
            player.connect(destinationServer, (connected, throwable) -> {
                if (connected) {
                    message.send();
                    done.accept(true);
                } else {
                    done.accept(false);
                }
            });

        });
    }
}
