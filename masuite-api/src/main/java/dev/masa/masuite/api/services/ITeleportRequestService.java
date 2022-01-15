package dev.masa.masuite.api.services;

import dev.masa.masuite.api.objects.ITeleportRequest;
import dev.masa.masuite.api.objects.TeleportRequestType;

import java.util.Optional;
import java.util.UUID;

public interface ITeleportRequestService<T extends ITeleportRequest> {

    /**
     * Get available teleportation request of the user
     * @param userId - id of the user
     * @return optional {@link ITeleportRequest}
     */
    public Optional<T> request(UUID userId);

    /**
     * Create a new {@link T}
     * @param sender - sender's unique id
     * @param receiver - receiver's unique id
     * @param requestType - {@link TeleportRequestType}
     */
    public void createRequest(UUID sender, UUID receiver, TeleportRequestType requestType);

    /**
     * Expire {@link T}
     * @param request - request to expire
     */
    public void expireRequest(T request);

    /**
     * Accept teleport request. The players in the request will be teleported by {@link T#requestType()}
     * @param request - request to accept
     */
    public void acceptRequest(T request);

    /**
     * Deny the request
     * @param request - request to accept
     */
    public void denyRequest(T request);

    /**
     * Allows player to enable or disable
     * @param uuid - unique id of the user
     * @param enabled - is the lock enabled or not
     */
    public void toggleTeleportationLock(UUID uuid, boolean enabled);

    /**
     * Removes teleportation lock from the user
     * @param uuid - unique id of the user
     */
    public void removeTeleportationLock(UUID uuid);
}
