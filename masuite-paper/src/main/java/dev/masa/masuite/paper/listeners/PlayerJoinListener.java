package dev.masa.masuite.paper.listeners;

import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.paper.MaSuitePaper;
import dev.masa.masuite.paper.utils.BukkitPluginMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

public record PlayerJoinListener(MaSuitePaper plugin) implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (this.plugin.config().modules().homes()) {
            this.plugin.homes().put(event.getPlayer().getUniqueId(), new ArrayList<>());
            this.plugin.getServer().getScheduler().runTaskLaterAsynchronously(this.plugin, () -> {
                BukkitPluginMessage bpm = new BukkitPluginMessage(event.getPlayer(), MaSuiteMessage.HOMES_LIST_REQUEST);
                bpm.send();
            }, 60);
        }

        var targetLocation = this.plugin.locationTeleportationQueue().get(event.getPlayer().getUniqueId());
        if (targetLocation != null) {
            event.getPlayer().teleport(targetLocation);
            return;
        }

        var targetUUID = this.plugin.playerTeleportationQueue().get(event.getPlayer().getUniqueId());
        if (targetUUID != null) {
            var targetPlayer = this.plugin.getServer().getPlayer(targetUUID);
            if (targetPlayer == null) {
                this.plugin.getLogger().info("[Teleports] Player with unique id " + targetUUID + " not found. Cancelled player " + event.getPlayer().getUniqueId() + " teleportation to their location.");
                return;
            }
            event.getPlayer().teleport(targetPlayer);
        }
    }
}
