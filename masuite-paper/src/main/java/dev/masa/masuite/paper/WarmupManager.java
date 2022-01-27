package dev.masa.masuite.paper;

import dev.masa.masuite.common.services.MessageService;
import dev.masa.masuite.paper.utils.BukkitWarmup;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;
import java.util.function.Consumer;

public class WarmupManager implements Listener {

    private final HashMap<UUID, String> warmups = new HashMap<>();
    private final HashMap<String, Integer> warmupTimes = new HashMap<>();

    private final MaSuitePaper plugin;

    public WarmupManager(MaSuitePaper plugin) {
        this.plugin = plugin;
    }

    /**
     * Apply warmup for the player
     *
     * @param player           player to add
     * @param bypassPermission permission to bypass warmup
     * @param type             type of the warmup
     * @param callback         callback
     */
    public void applyWarmup(Player player, String bypassPermission, String type, Consumer<Boolean> callback) {
        int warmupTime = 0;

        if (this.warmupTimes.get(type) != null) {
            warmupTime = this.warmupTimes.get(type);
        }

        // If warmup time is 0 or lower
        if (warmupTime <= 0) {
            callback.accept(true);
            return;
        }

        // If player has bypass permission
        if (player.hasPermission(bypassPermission)) {
            callback.accept(true);
            return;
        }

        // Add player to warmups list
        warmups.put(player.getUniqueId(), type);
        // Send teleportation message
        Audience audience = this.plugin.adventure().player(player);
        MessageService.sendMessage(audience, this.plugin.messages().teleportationStarted(), MessageService.Templates.timeTemplate(warmupTime));

        new BukkitWarmup(warmupTime, plugin) {
            @Override
            public void count(int current) {
                if (current == 0) {
                    if (warmups.containsKey(player.getUniqueId())) {
                        callback.accept(true);
                        warmups.remove(player.getUniqueId());
                        return;
                    }
                    callback.accept(false);
                    warmups.remove(player.getUniqueId());
                }
            }
        }.start();
    }

    /**
     * Add warmup time to cache
     *
     * @param type type of the warmup
     * @param time time of the warmup
     */
    public void addWarmupTime(String type, int time) {
        warmupTimes.put(type, time);
    }

    /**
     * Listen player moving and if player has ongoing warmup then cancel the action
     *
     * @param event PlayerMoveEvent
     */
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        String warmupType = warmups.get(event.getPlayer().getUniqueId());
        if (warmupType == null) {
            return;
        }
        if (warmupTimes.get(warmupType) <= 0) {
            return;
        }

        Location movedFrom = event.getFrom();
        Location movedTo = event.getTo();

        // Moving
        if (movedFrom.getBlockX() != movedTo.getBlockX() || movedFrom.getBlockY() != movedTo.getBlockY() || movedFrom.getBlockZ() != movedTo.getBlockZ()) {
            Audience audience = this.plugin.adventure().player(event.getPlayer());
            MessageService.sendMessage(audience, this.plugin.messages().teleportationCancelled());
            warmups.remove(event.getPlayer().getUniqueId());
        }
    }

}