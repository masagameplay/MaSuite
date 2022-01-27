package dev.masa.masuite.waterfall.listeners.user;

import dev.masa.masuite.common.models.user.User;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.waterfall.MaSuiteWaterfall;
import dev.masa.masuite.waterfall.utils.BungeePluginMessage;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public record UserLoginListener(MaSuiteWaterfall plugin) implements Listener {

    @EventHandler
    public void onLogin(LoginEvent event) {
        event.registerIntent(this.plugin.loader());

        this.plugin.userService().user(event.getConnection().getUniqueId()).thenAcceptAsync(optionalUser -> {
            try {
                if (optionalUser.isEmpty()) {
                    User user = new User();
                    user.uniqueId(event.getConnection().getUniqueId());
                    user.username(event.getConnection().getName());
                    user.firstLogin(new Date());
                    user.lastLogin(new Date());
                    this.plugin.userService().createOrUpdateUser(user);
                }

                this.plugin.loader().getProxy().getScheduler().schedule(this.plugin.loader(), () -> {
                    for (Map.Entry<String, ServerInfo> entry : this.plugin.loader().getProxy().getServers().entrySet()) {
                        ServerInfo serverInfo = entry.getValue();
                        serverInfo.ping((result, error) -> {
                            if (error == null) {
                                var bpm = new BungeePluginMessage(serverInfo,
                                        MaSuiteMessage.ADD_USER,
                                        event.getConnection().getName()
                                );
                                bpm.send();
                            }
                        });
                    }
                }, 1, TimeUnit.SECONDS);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                event.completeIntent(this.plugin.loader());
            }
        });
    }

}
