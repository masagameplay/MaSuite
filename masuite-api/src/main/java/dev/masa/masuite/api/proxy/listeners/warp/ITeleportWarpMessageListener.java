package dev.masa.masuite.api.proxy.listeners.warp;

import java.io.IOException;

public interface ITeleportWarpMessageListener<E> {

    public void teleportWarp(E event) throws IOException;

}
