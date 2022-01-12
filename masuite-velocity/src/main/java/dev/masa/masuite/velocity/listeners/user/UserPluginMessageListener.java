package dev.masa.masuite.velocity.listeners.user;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import dev.masa.masuite.common.models.User;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.velocity.MaSuiteVelocity;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Optional;

import static dev.masa.masuite.velocity.MaSuiteVelocity.MASUITE_MAIN_CHANNEL;

public class UserPluginMessageListener {

    private final MaSuiteVelocity plugin;

    public UserPluginMessageListener(MaSuiteVelocity plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) throws IOException {
        if (!event.getIdentifier().equals(MASUITE_MAIN_CHANNEL)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.USER_INFO.channel)) {
            return;
        }

        String username = in.readUTF();
        Player player = (Player) event.getTarget();

        Optional<User> user = this.plugin.userService().user(username);

        if (user.isEmpty()) {
            player.sendMessage(this.plugin.messages().playerNotFound());
            return;
        }

        player.sendMessage(LegacyComponentSerializer.builder().build().deserialize("&7&m[========&9 INFO &7&m========]"));
        player.sendMessage(LegacyComponentSerializer.builder().build().deserialize( "&7 * Username: &9" + user.get().username()));
        player.sendMessage(LegacyComponentSerializer.builder().build().deserialize(  "&7 * First time login: &9" + user.get().firstLogin()));
        player.sendMessage(LegacyComponentSerializer.builder().build().deserialize(  "&7 * Latest login: &9" + user.get().lastLogin()));
        player.sendMessage(LegacyComponentSerializer.builder().build().deserialize(  "&7&m[=====================]"));

    }
}
