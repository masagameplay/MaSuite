package dev.masa.masuite.velocity.listeners.warp;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import dev.masa.masuite.api.proxy.listeners.warp.IDeleteWarpMessageListener;
import dev.masa.masuite.common.models.Warp;
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

public record DeleteWarpMessageListener(MaSuiteVelocity plugin) implements IDeleteWarpMessageListener<PluginMessageEvent> {

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
            MessageService.sendMessage(executor, this.plugin.messages().warps().warpNotFound());
            return;
        }

        this.plugin.warpService().deleteWarp(warp.get(), done -> {
            if (done) {
                MessageService.sendMessage(executor, this.plugin.messages().warps().warpDeleted(), MessageService.Templates.warpTemplate(warp.get()));
            } else {
                executor.sendMessage(Component.text("An error occurred while deleting home", NamedTextColor.RED));
            }
        });
    }


}
