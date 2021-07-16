package dev.masa.masuite.paper.listeners;

import dev.masa.masuite.paper.MaSuitePaper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final MaSuitePaper plugin;

    public PlayerJoinListener(MaSuitePaper plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (this.plugin.locationTeleportationQueue().containsKey(event.getPlayer().getUniqueId())) {
            event.getPlayer().teleport(this.plugin.locationTeleportationQueue().get(event.getPlayer().getUniqueId()));
        }
    }
}
