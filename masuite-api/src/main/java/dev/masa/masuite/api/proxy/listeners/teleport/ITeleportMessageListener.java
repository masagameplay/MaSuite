package dev.masa.masuite.api.proxy.listeners.teleport;

import java.io.IOException;

public interface ITeleportMessageListener<E> {

    void teleportToPlayer(E event) throws IOException;
    void teleportPlayerToPlayer(E event) throws IOException;
    void teleportToLocation(E event) throws IOException;
    void teleportPlayerToLocation(E event) throws IOException;

}
