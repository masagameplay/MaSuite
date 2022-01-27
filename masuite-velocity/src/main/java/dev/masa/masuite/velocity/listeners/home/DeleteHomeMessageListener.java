package dev.masa.masuite.velocity.listeners.home;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import dev.masa.masuite.api.proxy.listeners.home.IDeleteHomeMessageListener;
import dev.masa.masuite.common.models.home.Home;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.common.services.MessageService;
import dev.masa.masuite.velocity.MaSuiteVelocity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Optional;

import static dev.masa.masuite.velocity.MaSuiteVelocity.MASUITE_MAIN_CHANNEL;

public record DeleteHomeMessageListener(
        MaSuiteVelocity plugin) implements IDeleteHomeMessageListener<PluginMessageEvent> {

    @Subscribe
    public void deleteHome(PluginMessageEvent event) throws IOException {
        if (!event.getIdentifier().equals(MASUITE_MAIN_CHANNEL)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.HOMES_DELETE.channel)) {
            return;
        }

        Player player = (Player) event.getTarget();
        String name = in.readUTF();

        this.plugin.homeService().home(player.getUniqueId(), name).thenAcceptAsync((home) -> this.delete(player, home));
    }

    @Subscribe
    public void deleteHomeOthers(PluginMessageEvent event) throws IOException {
        if (!event.getIdentifier().equals(MASUITE_MAIN_CHANNEL)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.HOMES_DELETE_OTHERS.channel)) {
            return;
        }

        Player player = (Player) event.getTarget();
        String username = in.readUTF();
        String name = in.readUTF();

        this.plugin.userService().user(username).thenAcceptAsync((user) -> {
            if (user.isEmpty()) {
                MessageService.sendMessage(player, this.plugin.messages().playerNotFound());
                return;
            }
            // Find & delete (seek and destroy)
            this.plugin.homeService().home(user.get().uniqueId(), name).thenAcceptAsync((home -> this.delete(player, home)));
        });


    }

    private void delete(Player player, Optional<Home> home) {
        if (home.isEmpty()) {
            MessageService.sendMessage(player, this.plugin.messages().homes().homeNotFound());
            return;
        }

        this.plugin.homeService().deleteHome(home.get()).thenAccept(done -> {
            if (done) {
                MessageService.sendMessage(player, this.plugin.messages().homes().homeDeleted(), MessageService.Templates.homeTemplate(home.get()));
            } else {
                player.sendMessage(Component.text("An error occurred while deleting home", NamedTextColor.RED));
            }
        });
    }
}
