package dev.masa.masuite.velocity.listeners.teleport;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import dev.masa.masuite.api.objects.TeleportRequestType;
import dev.masa.masuite.api.proxy.listeners.teleport.ITeleportRequestListener;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.common.services.MessageService;
import dev.masa.masuite.velocity.MaSuiteVelocity;
import net.kyori.adventure.text.Component;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Optional;

import static dev.masa.masuite.velocity.MaSuiteVelocity.MASUITE_MAIN_CHANNEL;

public record TeleportRequestListener(MaSuiteVelocity plugin) implements ITeleportRequestListener<PluginMessageEvent> {

    @Subscribe
    public void createRequest(PluginMessageEvent event) throws IOException {
        if (!event.getIdentifier().equals(MASUITE_MAIN_CHANNEL)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.TELEPORT_REQUEST_CREATE.channel)) {
            return;
        }

        Player player = (Player) event.getTarget();
        String receiverName = in.readUTF();
        TeleportRequestType type = TeleportRequestType.valueOf(in.readUTF().toUpperCase());


        Optional<Player> receiver = this.plugin.proxy().getPlayer(receiverName);

        if (receiver.isEmpty()) {
            MessageService.sendMessage(player, this.plugin.messages().playerNotOnline());
            return;
        }

        var request = this.plugin.teleportRequestService().request(receiver.get().getUniqueId());
        if (request.isPresent()) {
            MessageService.sendMessage(player, this.plugin.messages().teleports().sender().teleportRequestPendingReceiver(), MessageService.Templates.teleportRequestTemplate(player.getUsername(), receiver.get().getUsername()));
            return;
        }

        this.plugin.teleportRequestService().createRequest(player.getUniqueId(), receiver.get().getUniqueId(), type);

    }

    @Subscribe
    public void acceptRequest(PluginMessageEvent event) throws IOException {
        if (!event.getIdentifier().equals(MASUITE_MAIN_CHANNEL)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.TELEPORT_REQUEST_ACCEPT.channel)) {
            return;
        }

        Player player = (Player) event.getTarget();
        var request = this.plugin.teleportRequestService().request(player.getUniqueId());

        if (request.isEmpty()) {
            MessageService.sendMessage(player, this.plugin.messages().teleports().receiver().noPendingRequests());
            return;
        }

        this.plugin.teleportRequestService().acceptRequest(request.get());

    }

    @Subscribe
    public void denyRequest(PluginMessageEvent event) throws IOException {
        if (!event.getIdentifier().equals(MASUITE_MAIN_CHANNEL)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.TELEPORT_REQUEST_DENY.channel)) {
            return;
        }

        Player player = (Player) event.getTarget();
        var request = this.plugin.teleportRequestService().request(player.getUniqueId());

        if (request.isEmpty()) {
            MessageService.sendMessage(player, this.plugin.messages().teleports().receiver().noPendingRequests());
            return;
        }

        this.plugin.teleportRequestService().denyRequest(request.get());
    }

    @Subscribe
    public void teleportLock(PluginMessageEvent event) throws IOException {
        if (!event.getIdentifier().equals(MASUITE_MAIN_CHANNEL)) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();
        if (!channel.equals(MaSuiteMessage.TELEPORT_REQUEST_LOCK.channel)) {
            return;
        }

        Player player = (Player) event.getTarget();
        String value = in.readUTF().toLowerCase();

        if (value.equals("accept")) {
            this.plugin.teleportRequestService().toggleTeleportationLock(player.getUniqueId(), true);
            MessageService.sendMessage(player, this.plugin.messages().teleports().teleportationRequestLock().accept());
        } else if (value.equals("deny")) {
            this.plugin.teleportRequestService().toggleTeleportationLock(player.getUniqueId(), false);
            MessageService.sendMessage(player, this.plugin.messages().teleports().teleportationRequestLock().deny());
        } else {
            this.plugin.teleportRequestService().removeTeleportationLock(player.getUniqueId());
            MessageService.sendMessage(player, this.plugin.messages().teleports().teleportationRequestLock().off());
        }

    }
}
