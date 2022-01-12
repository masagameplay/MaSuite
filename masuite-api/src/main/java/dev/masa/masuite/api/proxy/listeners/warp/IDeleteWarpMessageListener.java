package dev.masa.masuite.api.proxy.listeners.warp;

import java.io.IOException;

public interface IDeleteWarpMessageListener<E> {

    public void deleteWarp(E event) throws IOException;

}
