package dev.masa.masuite.api.objects;

import java.util.UUID;

public interface ITeleportRequest {

    /**
     * Unique id of the sender
     * @return unique id of the sender
     */
    public UUID sender();

    /**
     * Unique id of the receiver
     * @return unique id of the receiver
     */
    public UUID receiver();

    /**
     * Type of the request
     * @return type of the request
     */
    public TeleportRequestType requestType();

}
