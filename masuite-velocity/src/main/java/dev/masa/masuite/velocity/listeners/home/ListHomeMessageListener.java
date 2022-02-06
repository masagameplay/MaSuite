package dev.masa.masuite.velocity.listeners.home;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import dev.masa.masuite.api.proxy.listeners.home.IListHomeMessageListener;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.common.services.MessageService;
import dev.masa.masuite.velocity.MaSuiteVelocity;
import dev.masa.masuite.velocity.utils.VelocityPluginMessage;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import static dev.masa.masuite.velocity.MaSuiteVelocity.MASUITE_MAIN_CHANNEL;

public record ListHomeMessageListener(MaSuiteVelocity plugin) implements IListHomeMessageListener<PluginMessageEvent> {

    @Subscribe
    public void listHomes(PluginMessageEvent event) throws IOException {
        if (!event.getIdentifier().equals(MASUITE_MAIN_CHANNEL)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.HOMES_LIST.channel)) {
            return;
        }

        Player player = (Player) event.getTarget();

        this.plugin.homeService().homes(player.getUniqueId()).thenAcceptAsync((homes) -> {
            var title = MiniMessage.get().parse(this.plugin.messages().homes().homeListTitle());
            for (var home : homes) {
                title = title
                        .append(MiniMessage.get().parse(this.plugin.messages().homes().homeListName(), MessageService.Templates.homeTemplate(home)))
                        .append(MiniMessage.get().parse(this.plugin.messages().homes().homeListSplitter()));
            }

            player.sendMessage(title);
        });
    }

    @Subscribe
    public void listUserHomes(PluginMessageEvent event) throws IOException {
        if (!event.getIdentifier().equals(MASUITE_MAIN_CHANNEL)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.HOMES_LIST_OTHERS.channel)) {
            return;
        }

        Player player = (Player) event.getTarget();

        String username = in.readUTF();
        this.plugin.userService().user(username).thenAcceptAsync((user) -> {
            if (user.isEmpty()) {
                MessageService.sendMessage(player, this.plugin.messages().playerNotFound());
                return;
            }

            // Query homes and send them to player
            this.plugin.homeService().homes(user.get().uniqueId()).thenAcceptAsync(homes -> {
                var title = MiniMessage.get().parse(this.plugin.messages().homes().homeListTitleOthers(), MessageService.Templates.userTemplate(user.get()));
                for (var home : homes) {
                    title = title
                            .append(MiniMessage.get().parse(this.plugin.messages().homes().homeListName(), MessageService.Templates.homeTemplate(home)))
                            .append(MiniMessage.get().parse(this.plugin.messages().homes().homeListSplitter()));
                }

                player.sendMessage(title);
            });
        });


    }

    @Subscribe
    public void serverRequestHomes(PluginMessageEvent event) throws IOException {
        if (!event.getIdentifier().equals(MASUITE_MAIN_CHANNEL)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.HOMES_LIST_REQUEST.channel)) {
            return;
        }

        Player player = (Player) event.getTarget();

        this.plugin.homeService().homes(player.getUniqueId()).thenAccept(homes -> {
            for(var home : homes) {
                VelocityPluginMessage vpm = new VelocityPluginMessage(player.getCurrentServer().get().getServer(), MaSuiteMessage.HOME_LIST_ADD, player.getUniqueId().toString(), home.name());
                vpm.send();
            }
        });
    }
}
