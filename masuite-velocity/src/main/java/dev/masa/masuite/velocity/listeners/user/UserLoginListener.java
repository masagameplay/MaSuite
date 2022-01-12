package dev.masa.masuite.velocity.listeners.user;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import dev.masa.masuite.common.models.User;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.velocity.MaSuiteVelocity;
import dev.masa.masuite.velocity.utils.VelocityPluginMessage;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public record UserLoginListener(MaSuiteVelocity plugin) {

    @Subscribe
    public void onJoin(LoginEvent event) {
        if (!event.getResult().isAllowed()) return;
        Optional<User> optionalUser = this.plugin.userService().user(event.getPlayer().getUniqueId());
        if (optionalUser.isPresent()) {
            this.sendPlayerInfo(event.getPlayer());
            return;
        }
        User user = new User();
        user.uniqueId(event.getPlayer().getUniqueId());
        user.username(event.getPlayer().getUsername());
        user.firstLogin(new Date());
        user.lastLogin(new Date());
        this.plugin.userService().createOrUpdateUser(user);

        this.sendPlayerInfo(event.getPlayer());
    }

    /**
     * Send the player with a little delay to all servers. This will be used in tab completions
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

}
