package dev.masa.masuite.waterfall.listeners.user;

import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.waterfall.MaSuiteWaterfall;
import dev.masa.masuite.waterfall.utils.BungeePluginMessage;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Date;
import java.util.Map;

public record UserLeaveListener(MaSuiteWaterfall plugin) implements Listener {

    @EventHandler
    public void onLeave(PlayerDisconnectEvent event) {
        this.plugin.userService().user(event.getPlayer().getUniqueId()).thenAcceptAsync(user -> {
            if(user.isPresent()) {
                user.get().lastLogin(new Date());
                this.plugin.userService().createOrUpdateUser(user.get());
            }

        });

        for (Map.Entry<String, ServerInfo> entry : this.plugin().loader().getProxy().getServers().entrySet()) {
            ServerInfo serverInfo = entry.getValue();
            serverInfo.ping((result, error) -> {
                if (error == null) {
                    var bpm = new BungeePluginMessage(serverInfo, MaSuiteMessage.REMOVE_USER, event.getPlayer().getName());
                    bpm.send();
                }
            });
        }
    }

}
