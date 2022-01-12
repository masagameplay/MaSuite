package dev.masa.masuite.velocity.listeners.warp;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import dev.masa.masuite.common.models.Warp;
import dev.masa.masuite.common.objects.Location;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.velocity.MaSuiteVelocity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import static dev.masa.masuite.velocity.MaSuiteVelocity.MASUITE_MAIN_CHANNEL;

public class SetWarpMessageListener {

    private final MaSuiteVelocity plugin;

    public SetWarpMessageListener(MaSuiteVelocity plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void createWarp(PluginMessageEvent event) throws IOException {
        if (!event.getIdentifier().equals(MASUITE_MAIN_CHANNEL)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.WARPS_SET.channel)) {
            return;
        }

        Player player = (Player) event.getTarget();

        String name = in.readUTF();
        String location = in.readUTF();
        boolean isPublic = in.readBoolean();
        boolean global = in.readBoolean();

        // Deserialize location and assign correct server
        Location loc = new Location().deserialize(location);
        player.getCurrentServer().ifPresentOrElse(serverConnection -> loc.server(serverConnection.getServerInfo().getName()), () -> {
            throw new IllegalStateException("Server of player "+player.getUsername()+" not found");
        });

        Warp warp = new Warp(name, loc, isPublic, global);

        TextReplacementConfig replacement = TextReplacementConfig.builder()
                .match("%warp%")
                .replacement(warp.name())
                .build();

        this.plugin.warpService().createOrUpdateWarp(warp, (done, isCreated) -> {
            if (!done) {
                player.sendMessage(Component.text("An error occurred while creating / updating warp.", NamedTextColor.RED));
                return;
            }
            if (isCreated) {
                player.sendMessage(this.plugin.messages().warps().warpCreated().replaceText(replacement));
            } else {
                player.sendMessage(this.plugin.messages().warps().warpUpdated().replaceText(replacement));
            }
        });
    }
}
