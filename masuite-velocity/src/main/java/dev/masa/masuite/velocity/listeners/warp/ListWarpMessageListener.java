package dev.masa.masuite.velocity.listeners.warp;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import dev.masa.masuite.api.proxy.listeners.warp.IListWarpMessageListener;
import dev.masa.masuite.common.models.Warp;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.velocity.MaSuiteVelocity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

import static dev.masa.masuite.velocity.MaSuiteVelocity.MASUITE_MAIN_CHANNEL;

public record ListWarpMessageListener(MaSuiteVelocity plugin) implements IListWarpMessageListener<PluginMessageEvent> {

    @Subscribe
    public void listWarps(PluginMessageEvent event) throws IOException {
        if (!event.getIdentifier().equals(MASUITE_MAIN_CHANNEL)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.WARPS_LIST.channel)) {
            return;
        }

        CommandSource executor = (CommandSource) event.getTarget();

        boolean publicPerm = in.readBoolean();
        boolean serverPerm = in.readBoolean();
        boolean hiddenPerm = in.readBoolean();

        List<Warp> warps = this.plugin.warpService().warps();

        Component message = this.plugin.messages().warps().globalListTitle();

        // TODO: Fix listing
        for (Warp warp : warps) {
            TextReplacementConfig replacement = TextReplacementConfig.builder()
                    .match("%warp%")
                    .replacement(warp.name())
                    .match("%server%")
                    .replacement(warp.location().server())
                    .build();


            // message = message.append(message.replaceText(replacement)).append(this.plugin.homeMessages().homeListSplitter());
        }

        executor.sendMessage(message);

    }
}
