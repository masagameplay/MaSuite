package dev.masa.masuite.velocity.listeners.user;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import dev.masa.masuite.velocity.MaSuiteVelocity;

import java.util.Date;

public class UserLeaveListener {

    private final MaSuiteVelocity plugin;

    public UserLeaveListener(MaSuiteVelocity plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onLeave(DisconnectEvent event) {
        this.plugin.userService().user(event.getPlayer().getUniqueId()).ifPresent(user -> {
            user.lastLogin(new Date());
            this.plugin.userService().createOrUpdateUser(user);
        });
    }
}
