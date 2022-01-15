package dev.masa.masuite.velocity.listeners.warp;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import dev.masa.masuite.api.proxy.listeners.warp.ITeleportWarpMessageListener;
import dev.masa.masuite.common.models.Warp;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.common.services.MessageService;
import dev.masa.masuite.velocity.MaSuiteVelocity;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Optional;

import static dev.masa.masuite.velocity.MaSuiteVelocity.MASUITE_MAIN_CHANNEL;

public record TeleportWarpMessageListener(
        MaSuiteVelocity plugin) implements ITeleportWarpMessageListener<PluginMessageEvent> {

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
        boolean permissionToWarpName = in.readBoolean();
        boolean permissionToGlobal = in.readBoolean();
        boolean permissionToServer = in.readBoolean();
        boolean permissionToHidden = in.readBoolean();

        Optional<Warp> warp = this.plugin.warpService().warp(name);

        if (warp.isEmpty()) {
            MessageService.sendMessage(player, this.plugin.messages().warps().warpNotFound());
            return;
        }

        if (this.plugin.config().warps().enablePerWarpPermission() && !permissionToWarpName) {
            MessageService.sendMessage(player, this.plugin.messages().warps().warpNotFound());
            return;
        }

        if (warp.get().isGlobal() && !permissionToGlobal) {
            MessageService.sendMessage(player, this.plugin.messages().warps().warpNotFound());
            return;
        }

        if (!warp.get().isGlobal() && !permissionToServer) {
            MessageService.sendMessage(player, this.plugin.messages().warps().warpNotFound());
            return;
        }

        if (!warp.get().isPublic() && !permissionToHidden) {
            MessageService.sendMessage(player, this.plugin.messages().warps().warpNotFound());
            return;
        }

        this.plugin.teleportationService().teleportPlayerToLocation(player, warp.get().location(), done ->
                MessageService.sendMessage(player, this.plugin.messages().warps().warpTeleported(), MessageService.Templates.warpTemplate(warp.get())));

    }
}
