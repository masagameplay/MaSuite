package dev.masa.masuite.waterfall.listeners;

import dev.masa.masuite.common.models.User;
import dev.masa.masuite.waterfall.MaSuiteWaterfall;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Date;
import java.util.Optional;

public final class UserLoginListener implements Listener {

    private final MaSuiteWaterfall plugin;

    public UserLoginListener(MaSuiteWaterfall plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        event.registerIntent(this.plugin.loader());
        Optional<User> optionalUser = this.plugin.userService().user(event.getConnection().getUniqueId());

        if (optionalUser.isPresent()) {
            ProxyServer.getInstance().broadcast("User found");
        } else {
            User user = new User();
            user.uniqueId(event.getConnection().getUniqueId());
            user.username(event.getConnection().getName());
            user.firstLogin(new Date());
            user.lastLogin(new Date());
        }

        event.completeIntent(this.plugin.loader());

    }

}
