package dev.masa.masuite.paper.listeners;

import dev.masa.masuite.paper.MaSuitePaper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public record PlayerJoinListener(MaSuitePaper plugin) implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
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
