package dev.masa.masuite.common.services;

import dev.masa.masuite.api.services.ITeleportRequestService;
import dev.masa.masuite.common.objects.AbstractTeleportRequest;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Accessors(fluent = true)
public abstract class AbstractTeleportRequestService<V extends AbstractTeleportRequest> implements ITeleportRequestService<V> {

    @Getter
    private final ConcurrentHashMap<UUID, V> requests = new ConcurrentHashMap<>();
    @Getter
    private final ConcurrentHashMap<UUID, Boolean> teleportationLocks = new ConcurrentHashMap<>();

    public Optional<V> request(UUID uuid) {
        return Optional.ofNullable(this.requests().get(uuid));
    }

    @Override
    public void expireRequest(V request) {
        this.cancelRequest(request);
    }

    @Override
    public void denyRequest(V request) {
        this.cancelRequest(request);
    }

    @Override
    public void toggleTeleportationLock(UUID uuid, boolean enabled) {
        this.teleportationLocks().put(uuid, enabled);
    }

    @Override
    public void removeTeleportationLock(UUID uuid) {
        this.teleportationLocks().remove(uuid);
    }

    @Override
    public void acceptRequest(V request) {
        this.requests().remove(request.receiver());
    }

    /**
     * Cancels request
     * @param request - request to cancel
     * @implNote Timer should be cancelled/stopped here
     */
    protected void cancelRequest(V request) {
        this.requests().remove(request.receiver());
    }


}
