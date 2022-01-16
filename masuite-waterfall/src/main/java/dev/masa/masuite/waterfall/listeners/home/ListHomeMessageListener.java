package dev.masa.masuite.waterfall.listeners.home;

import dev.masa.masuite.api.proxy.listeners.home.IListHomeMessageListener;
import dev.masa.masuite.common.models.home.Home;
import dev.masa.masuite.common.models.user.User;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.common.services.MessageService;
import dev.masa.masuite.waterfall.MaSuiteWaterfall;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public record ListHomeMessageListener(
        MaSuiteWaterfall plugin) implements Listener, IListHomeMessageListener<PluginMessageEvent> {

    @EventHandler
    public void listHomes(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.HOMES_LIST.channel)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();

        List<Home> homes = this.plugin.homeService().homes(player.getUniqueId());

        var title = MiniMessage.get().parse(this.plugin.messages().homes().homeListTitle());
        for (var home : homes) {
            title = title
                    .append(MiniMessage.get().parse(this.plugin.messages().homes().homeListName(), MessageService.Templates.homeTemplate(home)))
                    .append(MiniMessage.get().parse(this.plugin.messages().homes().homeListSplitter()));
        }

        Audience audience = this.plugin.adventure().player(player);
        audience.sendMessage(title);

    }

    @EventHandler
    public void listUserHomes(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.HOMES_LIST_OTHERS.channel)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();

        // Get target user
        String username = in.readUTF();
        Optional<User> user = this.plugin.userService().user(username);

        Audience audience = this.plugin.adventure().player(player);

        if (user.isEmpty()) {
            MessageService.sendMessage(audience, this.plugin.messages().playerNotFound());
            return;
        }

        // Query homes and send them to player
        List<Home> homes = this.plugin.homeService().homes(user.get().uniqueId());

        var title = MiniMessage.get().parse(this.plugin.messages().homes().homeListTitleOthers(), MessageService.Templates.userTemplate(user.get()));
        for (var home : homes) {
            title = title
                    .append(MiniMessage.get().parse(this.plugin.messages().homes().homeListName(), MessageService.Templates.homeTemplate(home)))
                    .append(MiniMessage.get().parse(this.plugin.messages().homes().homeListSplitter()));
        }

        audience.sendMessage(title);
    }
}
