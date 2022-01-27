package dev.masa.masuite.velocity.listeners.user;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.masa.masuite.common.models.teleport.Spawn;
import dev.masa.masuite.common.models.user.User;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.common.services.MessageService;
import dev.masa.masuite.velocity.MaSuiteVelocity;
import dev.masa.masuite.velocity.utils.VelocityPluginMessage;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public record UserLoginListener(MaSuiteVelocity plugin) {

    // Budget solution to get spawn listeners to work, but it works so ¯\_(ツ)_/¯
    private static final ConcurrentHashMap<UUID, Boolean> hasPlayedBefore = new ConcurrentHashMap<>();

    /**
     * Create a new {@link User} if needed. This will also set value to {@link #hasPlayedBefore} map,
     * and it will be removed when {@link #onNetworkConnect} has been called.
     * <p>
     * Everything this does:
     * - Creates a new user if needed
     * - Checks if user has been played before or not and adds the information to {@link #hasPlayedBefore}
     * - Sends user info to all online servers with {@link #sendPlayerInfo} for tab complete.
     *
     * @param event LoginEvent
     */
    @Subscribe
    public void onJoin(LoginEvent event) {
        if (!event.getResult().isAllowed()) return;
        this.plugin.userService().user(event.getPlayer().getUniqueId()).thenAcceptAsync(optionalUser -> {
            if (optionalUser.isPresent()) {
                hasPlayedBefore.put(event.getPlayer().getUniqueId(), true);
            } else {
                User user = new User();
                user.uniqueId(event.getPlayer().getUniqueId());
                user.username(event.getPlayer().getUsername());
                user.firstLogin(new Date());
                user.lastLogin(new Date());
                this.plugin.userService().createOrUpdateUser(user);
                hasPlayedBefore.put(event.getPlayer().getUniqueId(), false);
            }

            this.sendPlayerInfo(event.getPlayer());
        });
    }

    /**
     * Do stuff here that requires player's current server (for example spawns) to work
     * <p>
     * Everything this does:
     * - Check if player has previous server (not a network join), if so instantly returns
     * - Checks if {@link #hasPlayedBefore} contains required info, if not instantly returns
     * - Checks if player has played before and {@link dev.masa.masuite.common.configuration.teleport.TeleportSettingsConfig#spawnOnJoin()} is enabled,
     * if so teleports to default spawn
     * - Checks if player has not played before and {@link dev.masa.masuite.common.configuration.teleport.TeleportSettingsConfig#spawnOnFirstJoin()} is enabled,
     * if so teleports to first time spawn
     *
     * @param event {@link ServerPostConnectEvent}
     */
    @Subscribe
    public void onNetworkConnect(ServerPostConnectEvent event) {
        // If the player has previous server, skip it
        if (event.getPreviousServer() != null) {
            return;
        }

        var playedBefore = hasPlayedBefore.get(event.getPlayer().getUniqueId());
        hasPlayedBefore.remove(event.getPlayer().getUniqueId());

        if (playedBefore == null) {
            this.plugin.logger.warn("For some reason player " + event.getPlayer().getUsername() + " does not have set played before field. Skipping spawn logic...");
            return;
        }

        // If config has enabled spawn on join and player has played before, teleport to default spawn
        if (this.plugin.config().teleports().spawnOnJoin() && playedBefore) {
            teleportToSpawn(event.getPlayer(), true, event.getPlayer().getCurrentServer().get().getServer());
        }

        // If config has enabled first spawn teleporting and player has not played before, teleport to first spawn
        if (this.plugin.config().teleports().spawnOnFirstJoin() && !playedBefore) {
            teleportToSpawn(event.getPlayer(), false, event.getPlayer().getCurrentServer().get().getServer());
        }
    }

    /**
     * Send the player with a little delay to all servers. This will be used in tab completions
     *
     * @param player - player to send
     */
    private void sendPlayerInfo(Player player) {
        this.plugin().proxy().getScheduler().buildTask(this.plugin, () -> {
            for (var server : this.plugin().proxy().getAllServers()) {
                server.ping().whenComplete((serverPing, throwable) -> {
                    if (throwable != null) return;
                    var vpm = new VelocityPluginMessage(server, MaSuiteMessage.ADD_USER, player.getUsername());
                    vpm.send();
                });
            }
        }).delay(1, TimeUnit.SECONDS).schedule();
    }

    /**
     * Teleports {@link Player} to spawn by given spawn type.
     * <p>
     * If the player has permission "masuite.teleport.bypass.spawn-on-join", the force teleportation will be skipped.
     *
     * @param player       player to teleport
     * @param defaultSpawn should the player be teleported to default spawn or first time spawn
     */
    private void teleportToSpawn(Player player, boolean defaultSpawn, RegisteredServer server) {
        if (player.hasPermission("masuite.teleport.bypass.spawn-on-join")) {
            return;
        }

        CompletableFuture<Optional<Spawn>> spawnFuture;
        if (this.plugin.config().teleports().spawnType().equals("global")) {
            spawnFuture = this.plugin.spawnService().spawn(defaultSpawn);
        } else {
            spawnFuture = this.plugin.spawnService().spawn(server.getServerInfo().getName(), true);
        }

        spawnFuture.thenAccept(spawn -> {
            if (spawn.isEmpty()) {
                this.plugin.logger.warn("Spawn not found but it spawn on (first) join has been enabled.");
                return;
            }
            this.plugin.teleportationService().teleportPlayerToLocation(player, spawn.get().location(), done ->
                    MessageService.sendMessage(player,
                            this.plugin.messages().teleports().spawn().teleported(),
                            MessageService.Templates.spawnTemplate(spawn.get())
                    )
            );
        });
    }
}
