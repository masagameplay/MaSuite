package dev.masa.masuite.velocity.listeners.user;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.velocity.MaSuiteVelocity;
import dev.masa.masuite.velocity.utils.VelocityPluginMessage;

import java.util.Date;

public record UserLeaveListener(MaSuiteVelocity plugin) {

    @Subscribe
    public void onLeave(DisconnectEvent event) {
        this.plugin.userService().user(event.getPlayer().getUniqueId()).ifPresent(user -> {
            user.lastLogin(new Date());
            this.plugin.userService().createOrUpdateUser(user);
        });

        for (var server : this.plugin().proxy().getAllServers()) {
            server.ping().whenComplete((serverPing, throwable) -> {
                if (throwable != null) return;
                var vpm = new VelocityPluginMessage(server, MaSuiteMessage.REMOVE_USER, event.getPlayer().getUsername());
                vpm.send();
            });
        }
    }
}
