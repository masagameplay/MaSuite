package dev.masa.masuite.api.proxy.listeners.warp;

import java.io.IOException;

public interface IListWarpMessageListener<E> {

    public void listWarps(E event) throws IOException;
    
}
