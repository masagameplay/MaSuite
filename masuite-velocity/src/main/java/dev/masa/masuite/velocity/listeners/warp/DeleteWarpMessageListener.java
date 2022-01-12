package dev.masa.masuite.velocity.listeners.warp;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import dev.masa.masuite.common.models.Warp;
import dev.masa.masuite.common.objects.MaSuiteMessage;

import dev.masa.masuite.velocity.MaSuiteVelocity;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Optional;

import static dev.masa.masuite.velocity.MaSuiteVelocity.MASUITE_MAIN_CHANNEL;

public class DeleteWarpMessageListener {

    private final MaSuiteVelocity plugin;

    public DeleteWarpMessageListener(MaSuiteVelocity plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void deleteWarp(PluginMessageEvent event) throws IOException {
        if (!event.getIdentifier().equals(MASUITE_MAIN_CHANNEL)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.WARPS_DELETE.channel)) {
            return;
        }

        CommandSource executor = (CommandSource) event.getTarget();

        String name = in.readUTF();

        Optional<Warp> warp = this.plugin.warpService().warp(name);

        if (warp.isEmpty()) {
            executor.sendMessage(this.plugin.messages().warps().warpNotFound());
            return;
        }

        TextReplacementConfig replacement = TextReplacementConfig.builder()
                .match("%warp%")
                .replacement(warp.get().name())
                .build();

        this.plugin.warpService().deleteWarp(warp.get(), done -> {
            if (done) {
                executor.sendMessage(this.plugin.messages().warps().warpDeleted().replaceText(replacement));
            } else {
                executor.sendMessage(Component.text("An error occurred while deleting home", NamedTextColor.RED));
            }
        });
    }


}
