package dev.masa.masuite.velocity.listeners.user;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import dev.masa.masuite.common.models.User;
import dev.masa.masuite.velocity.MaSuiteVelocity;

import java.util.Date;
import java.util.Optional;

public class UserLoginListener {

    private final MaSuiteVelocity plugin;

    public UserLoginListener(MaSuiteVelocity plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onJoin(LoginEvent event) {
        if (!event.getResult().isAllowed()) return;
        Optional<User> optionalUser = this.plugin.userService().user(event.getPlayer().getUniqueId());
        if (optionalUser.isPresent()) return;
        User user = new User();
        user.uniqueId(event.getPlayer().getUniqueId());
        user.username(event.getPlayer().getUsername());
        user.firstLogin(new Date());
        user.lastLogin(new Date());
        this.plugin.userService().createOrUpdateUser(user);
    }

}
