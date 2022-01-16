package dev.masa.masuite.waterfall.listeners.warp;

import dev.masa.masuite.api.proxy.listeners.warp.ITeleportWarpMessageListener;
import dev.masa.masuite.common.models.warp.Warp;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.common.services.MessageService;
import dev.masa.masuite.waterfall.MaSuiteWaterfall;
import net.kyori.adventure.audience.Audience;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Optional;

public record TeleportWarpMessageListener(
        MaSuiteWaterfall plugin) implements Listener, ITeleportWarpMessageListener<PluginMessageEvent> {

    @EventHandler
    public void teleportWarp(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.WARPS_TELEPORT.channel)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();

        String name = in.readUTF();
        boolean permissionToWarpName = in.readBoolean();
        boolean permissionToGlobal = in.readBoolean();
        boolean permissionToServer = in.readBoolean();
        boolean permissionToHidden = in.readBoolean();

        Optional<Warp> warp = this.plugin.warpService().warp(name);

        Audience audience = this.plugin.adventure().player(player);
        if (warp.isEmpty()) {
            MessageService.sendMessage(audience, this.plugin.messages().warps().warpNotFound());
            return;
        }

        if (this.plugin.config().warps().enablePerWarpPermission() && !permissionToWarpName) {
            MessageService.sendMessage(audience, this.plugin.messages().warps().warpNotFound());
            return;
        }

        if (warp.get().isGlobal() && !permissionToGlobal) {
            MessageService.sendMessage(audience, this.plugin.messages().warps().warpNotFound());
            return;
        }

        if (!warp.get().isGlobal() && !permissionToServer) {
            MessageService.sendMessage(audience, this.plugin.messages().warps().warpNotFound());
            return;
        }

        if (!warp.get().isPublic() && !permissionToHidden) {
            MessageService.sendMessage(audience, this.plugin.messages().warps().warpNotFound());
            return;
        }

        this.plugin.teleportationService().teleportPlayerToLocation(player, warp.get().location(), done ->
                MessageService.sendMessage(audience, this.plugin.messages().warps().warpTeleported(), MessageService.Templates.warpTemplate(warp.get()))
        );

    }
}
