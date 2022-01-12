package dev.masa.masuite.velocity.listeners.warp;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import dev.masa.masuite.common.models.Warp;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.velocity.MaSuiteVelocity;
import net.kyori.adventure.text.TextReplacementConfig;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Optional;

import static dev.masa.masuite.velocity.MaSuiteVelocity.MASUITE_MAIN_CHANNEL;

public class TeleportWarpMessageListener {

    private final MaSuiteVelocity plugin;

    public TeleportWarpMessageListener(MaSuiteVelocity plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void teleportWarp(PluginMessageEvent event) throws IOException {
        if (!event.getIdentifier().equals(MASUITE_MAIN_CHANNEL)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.WARPS_TELEPORT.channel)) {
            return;
        }

        Player player = (Player) event.getTarget();

        String name = in.readUTF();

        Optional<Warp> warp = this.plugin.warpService().warp(name);

        if (warp.isEmpty()) {
            player.sendMessage(this.plugin.messages().warps().warpNotFound());
            return;
        }

        TextReplacementConfig replacement = TextReplacementConfig.builder()
                .match("%warp%")
                .replacement(warp.get().name())
                .build();

        this.plugin.teleportationService().teleportPlayerToLocation(player, warp.get().location(), done ->
                player.sendMessage(this.plugin.messages().warps().warpTeleported().replaceText(replacement)));

    }
}
