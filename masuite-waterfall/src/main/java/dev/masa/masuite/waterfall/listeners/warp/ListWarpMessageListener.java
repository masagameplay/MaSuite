package dev.masa.masuite.waterfall.listeners.warp;

import dev.masa.masuite.api.proxy.listeners.warp.IListWarpMessageListener;
import dev.masa.masuite.common.models.Warp;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.common.services.MessageService;
import dev.masa.masuite.waterfall.MaSuiteWaterfall;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public record ListWarpMessageListener(MaSuiteWaterfall plugin) implements Listener, IListWarpMessageListener<PluginMessageEvent> {

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

        Component message = MiniMessage.get().parse(this.plugin.messages().warps().globalListTitle());

        // TODO: Fix listing
        for (Warp warp : warps) {
            message = message.append(MiniMessage.get().parse(this.plugin.messages().warps().warpListName(), MessageService.Templates.warpTemplate(warp))).append(MiniMessage.get().parse(this.plugin.messages().warps().warpListSplitter()));
        }

        audience.sendMessage(message);

    }
}
