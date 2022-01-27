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
        // Save last login
        this.plugin.userService().user(event.getPlayer().getUniqueId()).thenAcceptAsync(user -> {
            if(user.isPresent()) {
                user.get().lastLogin(new Date());
                this.plugin.userService().createOrUpdateUser(user.get());
            }

        });

        // Cancel request if player leaves
        var request = this.plugin().teleportRequestService().request(event.getPlayer().getUniqueId());
        request.ifPresent(velocityTeleportRequest -> this.plugin().teleportRequestService().denyRequest(velocityTeleportRequest));


        // Remove player name from tab completions from all servers that are online
        for (var server : this.plugin().proxy().getAllServers()) {
            server.ping().whenComplete((serverPing, throwable) -> {
                if (throwable != null) return;
                var vpm = new VelocityPluginMessage(server, MaSuiteMessage.REMOVE_USER, event.getPlayer().getUsername());
                vpm.send();
            });
        }
    }
}
