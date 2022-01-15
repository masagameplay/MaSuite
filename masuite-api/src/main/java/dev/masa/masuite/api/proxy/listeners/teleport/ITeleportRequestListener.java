package dev.masa.masuite.api.proxy.listeners.teleport;

import java.io.IOException;

public interface ITeleportRequestListener<E> {

    void createRequest(E event) throws IOException;
    void acceptRequest(E event) throws IOException;
    void denyRequest(E event) throws IOException;
    void teleportLock(E event) throws IOException;

}
