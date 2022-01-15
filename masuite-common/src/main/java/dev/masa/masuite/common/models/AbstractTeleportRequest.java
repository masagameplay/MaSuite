package dev.masa.masuite.common.models;

import dev.masa.masuite.api.objects.ITeleportRequest;
import dev.masa.masuite.api.objects.TeleportRequestType;

import java.util.UUID;

public abstract class AbstractTeleportRequest implements ITeleportRequest {
    private final UUID sender;
    private final UUID receiver;
    private final TeleportRequestType requestType;

    public AbstractTeleportRequest(UUID sender, UUID receiver, TeleportRequestType requestType) {
        this.sender = sender;
        this.receiver = receiver;
        this.requestType = requestType;
    }

    public UUID sender() {
        return sender;
    }

    public UUID receiver() {
        return receiver;
    }

    public TeleportRequestType requestType() {
        return requestType;
    }

}
