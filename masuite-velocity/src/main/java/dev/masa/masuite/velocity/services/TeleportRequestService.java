package dev.masa.masuite.velocity.services;

import com.velocitypowered.api.proxy.Player;
import dev.masa.masuite.api.objects.TeleportRequestType;
import dev.masa.masuite.common.services.AbstractTeleportRequestService;
import dev.masa.masuite.common.services.MessageService;
import dev.masa.masuite.velocity.MaSuiteVelocity;
import dev.masa.masuite.velocity.objects.VelocityTeleportRequest;
import net.kyori.adventure.text.minimessage.Template;

import java.util.List;
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
        var sender = request.senderAsPlayer();
        var receiver = request.receiverAsPlayer();

        if (sender.isPresent() && receiver.isPresent()) {
            MessageService.sendMessage(sender.get(), this.plugin.messages().teleports().sender().teleportRequestExpired(), this.buildRequestTemplate(sender.get(), receiver.get()));
            MessageService.sendMessage(receiver.get(), this.plugin.messages().teleports().receiver().teleportRequestExpired(), this.buildRequestTemplate(sender.get(), receiver.get()));
        }

    }

    @Override
    public void denyRequest(VelocityTeleportRequest request) {
        super.denyRequest(request);

        var sender = request.senderAsPlayer();
        var receiver = request.receiverAsPlayer();

        if (sender.isPresent() && receiver.isPresent()) {
            MessageService.sendMessage(sender.get(), this.plugin.messages().teleports().sender().teleportRequestDenied(), this.buildRequestTemplate(sender.get(), receiver.get()));
            MessageService.sendMessage(receiver.get(), this.plugin.messages().teleports().receiver().teleportRequestDenied(), this.buildRequestTemplate(sender.get(), receiver.get()));
        }
    }

    @Override
    public void createRequest(UUID senderId, UUID receiverId, TeleportRequestType requestType) {
        var request = new VelocityTeleportRequest(senderId, receiverId, requestType);

        var sender = request.senderAsPlayer();
        var receiver = request.receiverAsPlayer();

        if (sender.isEmpty()) {
            this.plugin.logger.info("Player " + senderId + " created teleportation request but they are not online?");
            return;
        }

        if (receiver.isEmpty()) {
            MessageService.sendMessage(sender.get(), this.plugin.messages().playerNotOnline());
            return;
        }

        var task = this.plugin.proxy().getScheduler()
                .buildTask(this.plugin, () -> this.expireRequest(request))
                .delay(30, TimeUnit.SECONDS)
                .schedule();

        request.task(task);

        this.requests().put(receiverId, request);

        if (this.teleportationLocks().containsKey(receiverId)) {
            var teleportLock = this.teleportationLocks().get(receiverId);
            if (teleportLock) {
                this.acceptRequest(request);
                return;
            }
            this.denyRequest(request);
            return;
        }

        if (requestType.equals(TeleportRequestType.TO)) {
            MessageService.sendMessage(sender.get(), this.plugin.messages().teleports().sender().teleportRequestSentTo(), this.buildRequestTemplate(sender.get(), receiver.get()));
            MessageService.sendMessage(receiver.get(), this.plugin.messages().teleports().receiver().teleportRequestReceivedTo(), this.buildRequestTemplate(sender.get(), receiver.get()));
        }

        if (requestType.equals(TeleportRequestType.HERE)) {
            MessageService.sendMessage(sender.get(), this.plugin.messages().teleports().sender().teleportRequestSentHere(), this.buildRequestTemplate(sender.get(), receiver.get()));
            MessageService.sendMessage(receiver.get(), this.plugin.messages().teleports().receiver().teleportRequestReceivedHere(), this.buildRequestTemplate(sender.get(), receiver.get()));
        }

        // TODO: Buttons
    }

    @Override
    public void acceptRequest(VelocityTeleportRequest request) {
        super.acceptRequest(request);
        request.task().cancel();

        var sender = request.senderAsPlayer();
        var receiver = request.receiverAsPlayer();

        if (sender.isEmpty()) {
            this.plugin.logger.info("Player " + request.sender() + " created teleportation request but they are not online?");
            return;
        }

        if (receiver.isEmpty()) {
            this.plugin.logger.info("Player " + request.receiver() + " accepted teleportation request but they are not online?");
            return;
        }

        if (request.requestType().equals(TeleportRequestType.TO)) {
            teleportPlayerToPlayer(sender.get(), receiver.get());
            return;
        }

        if (request.requestType().equals(TeleportRequestType.HERE)) {
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
            if (done) {
                MessageService.sendMessage(sender, this.plugin.messages().teleports().sender().teleportRequestAccepted(), this.buildRequestTemplate(sender, receiver));
                MessageService.sendMessage(receiver, this.plugin.messages().teleports().receiver().teleportRequestAccepted(), this.buildRequestTemplate(sender, receiver));;
            }
        });
    }

    private List<Template> buildRequestTemplate(Player sender, Player receiver) {
        return MessageService.Templates.teleportRequestTemplate(sender.getUsername(), receiver.getUsername());
    }
}
