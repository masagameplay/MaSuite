package dev.masa.masuite.api.proxy.listeners.home;

import java.io.IOException;

public interface ITeleportHomeMessageListener<E> {

    public void teleportHome(E event) throws IOException;
    public void teleportHomeOthers(E event) throws IOException;

}
