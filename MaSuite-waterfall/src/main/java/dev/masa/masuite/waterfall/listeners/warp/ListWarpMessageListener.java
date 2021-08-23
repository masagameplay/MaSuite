package dev.masa.masuite.waterfall.listeners.warp;

import dev.masa.masuite.common.models.Warp;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.waterfall.MaSuiteWaterfall;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public class ListWarpMessageListener implements Listener {

    private final MaSuiteWaterfall plugin;

    public ListWarpMessageListener(MaSuiteWaterfall plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void listWarps(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.WARPS_LIST.channel)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();

        boolean publicPerm = in.readBoolean();
        boolean serverPerm = in.readBoolean();
        boolean hiddenPerm = in.readBoolean();

        List<Warp> warps = this.plugin.warpService().warps();

        Audience audience = this.plugin.adventure().player(player);

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

        audience.sendMessage(message);

    }
}
