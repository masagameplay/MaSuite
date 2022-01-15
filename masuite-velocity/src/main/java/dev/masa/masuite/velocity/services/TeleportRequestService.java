package dev.masa.masuite.velocity.services;

import com.velocitypowered.api.proxy.Player;
import dev.masa.masuite.api.objects.TeleportRequestType;
import dev.masa.masuite.common.services.AbstractTeleportRequestService;
import dev.masa.masuite.velocity.MaSuiteVelocity;
import dev.masa.masuite.velocity.objects.VelocityTeleportRequest;
import net.kyori.adventure.text.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TeleportRequestService extends AbstractTeleportRequestService<VelocityTeleportRequest> {

    private final MaSuiteVelocity plugin;

    public TeleportRequestService(MaSuiteVelocity plugin) {
        this.plugin = plugin;
    }

    @Override
    public void expireRequest(VelocityTeleportRequest request) {
        super.expireRequest(request);

        request.senderAsPlayer().ifPresent(player -> player.sendMessage(Component.text("Request expired")));
        request.receiverAsPlayer().ifPresent(player -> player.sendMessage(Component.text("Request expired")));
    }

    @Override
    public void denyRequest(VelocityTeleportRequest request) {
        super.denyRequest(request);

        var senderPlayer = request.senderAsPlayer();
        var receiverPlayer = request.receiverAsPlayer();

        senderPlayer.ifPresent(player -> player.sendMessage(Component.text("Player ").append(Component.text(receiverPlayer.get().getUsername())).append(Component.text(" has denied your teleport request"))));
        receiverPlayer.ifPresent(player -> player.sendMessage(Component.text("You denied ").append(Component.text(senderPlayer.get().getUsername())).append(Component.text("'s teleport request"))));
    }

    @Override
    public void createRequest(UUID senderId, UUID receiverId, TeleportRequestType requestType) {
        var request = new VelocityTeleportRequest(senderId, receiverId, requestType);

        var sender = request.senderAsPlayer();
        var receiver = request.receiverAsPlayer();

        if(sender.isEmpty()) {
            this.plugin.logger.info("Player " + senderId + " created teleportation request but they are not online?");
            return;
        }

        if(receiver.isEmpty()) {
            sender.get().sendMessage(this.plugin.messages().playerNotOnline());
            return;
        }

        var task = this.plugin.proxy().getScheduler()
                .buildTask(this.plugin, () -> this.expireRequest(request))
                .delay(30, TimeUnit.SECONDS)
                .schedule();

        request.task(task);

        this.requests().put(receiverId, request);

        if(this.teleportationLocks().containsKey(receiverId)) {
            var teleportLock = this.teleportationLocks().get(receiverId);
            if(teleportLock) {
                this.acceptRequest(request);
                return;
            }
            this.denyRequest(request);
            return;
        }

        if(requestType.equals(TeleportRequestType.TO)) {
            sender.get().sendMessage(Component.text("Teleportation request to player ").append(Component.text(receiver.get().getUsername())).append(Component.text("'s location sent.")));
            receiver.get().sendMessage(Component.text("Player ").append(Component.text(receiver.get().getUsername())).append(Component.text(" wants to teleport to your location.")));
        }

        if(requestType.equals(TeleportRequestType.HERE)) {
            sender.get().sendMessage(Component.text("Teleportation request to player ").append(Component.text(receiver.get().getUsername())).append(Component.text("'s to your location sent")));
            receiver.get().sendMessage(Component.text("Player ").append(Component.text(receiver.get().getUsername())).append(Component.text(" wants you to teleport to their location.")));
        }

        // TODO: Buttons
    }

    @Override
    public void acceptRequest(VelocityTeleportRequest request) {
        super.acceptRequest(request);
        request.task().cancel();

        var sender = request.senderAsPlayer();
        var receiver = request.receiverAsPlayer();

        if(sender.isEmpty()) {
            this.plugin.logger.info("Player " + request.sender() + " created teleportation request but they are not online?");
            return;
        }

        if(receiver.isEmpty()) {
            this.plugin.logger.info("Player " + request.receiver() + " accepted teleportation request but they are not online?");
            return;
        }

        if(request.requestType().equals(TeleportRequestType.TO)) {
            teleportPlayerToPlayer(sender.get(), receiver.get());
            return;
        }

        if(request.requestType().equals(TeleportRequestType.HERE)) {
            teleportPlayerToPlayer(receiver.get(), sender.get());
        }

    }

    @Override
    protected void cancelRequest(VelocityTeleportRequest request) {
        super.cancelRequest(request);
        request.task().cancel();
    }

    private void teleportPlayerToPlayer(Player sender, Player receiver) {
        this.plugin.teleportationService().teleportPlayerToPlayer(sender, receiver, (done) -> {
            if(done) {
                sender.sendMessage(Component.text("Teleported to player ").append(Component.text(receiver.getUsername())).append(Component.text("'s location")));
                receiver.sendMessage(Component.text("Player ").append(Component.text(sender.getUsername())).append(Component.text(" teleported to your location.")));
            }
        });
    }
}
