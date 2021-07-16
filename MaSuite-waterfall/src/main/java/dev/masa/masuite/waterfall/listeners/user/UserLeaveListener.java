package dev.masa.masuite.waterfall.listeners.user;

import dev.masa.masuite.common.models.User;
import dev.masa.masuite.waterfall.MaSuiteWaterfall;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Date;
import java.util.Optional;

public class UserLeaveListener implements Listener {

    private final MaSuiteWaterfall plugin;

    public UserLeaveListener(MaSuiteWaterfall plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLeave(PlayerDisconnectEvent event) {
        try {
            Optional<User> user = this.plugin.userService().user(event.getPlayer().getUniqueId());
            if (user.isPresent()) {
                user.get().lastLogin(new Date());
                this.plugin.userService().createOrUpdateUser(user.get());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
