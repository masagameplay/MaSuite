package dev.masa.masuite.api.proxy.listeners.warp;

import java.io.IOException;

public interface ISetWarpMessageListener<E> {

    public void createWarp(E event) throws IOException;
    
}
