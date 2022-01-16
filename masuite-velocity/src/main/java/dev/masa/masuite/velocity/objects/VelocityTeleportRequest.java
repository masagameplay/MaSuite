package dev.masa.masuite.velocity.objects;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.scheduler.ScheduledTask;
import dev.masa.masuite.api.objects.TeleportRequestType;
import dev.masa.masuite.common.objects.AbstractTeleportRequest;

import java.util.Optional;
import java.util.UUID;

import static dev.masa.masuite.velocity.MaSuiteVelocity.MASUITE_INSTANCE;

public class VelocityTeleportRequest extends AbstractTeleportRequest {

    private ScheduledTask task;

    public VelocityTeleportRequest(UUID sender, UUID receiver, TeleportRequestType requestType) {
        super(sender, receiver, requestType);
    }

    public ScheduledTask task() {
        return this.task;
    }

    public void task(ScheduledTask task) {
        this.task = task;
    }

    public Optional<Player> senderAsPlayer() {
       return MASUITE_INSTANCE.proxy().getPlayer(this.sender());
    }

    public Optional<Player> receiverAsPlayer() {
        return MASUITE_INSTANCE.proxy().getPlayer(this.receiver());
    }
}
