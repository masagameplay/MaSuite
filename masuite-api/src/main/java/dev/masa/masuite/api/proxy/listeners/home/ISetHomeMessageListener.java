package dev.masa.masuite.api.proxy.listeners.home;

import java.io.IOException;

public interface ISetHomeMessageListener<E> {

    public void createHome(E event) throws IOException;
    public void createHomeOthers(E event) throws IOException;

}
