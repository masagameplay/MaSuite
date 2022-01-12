package dev.masa.masuite.api.proxy.listeners.home;

import java.io.IOException;

public interface IDeleteHomeMessageListener<E> {

    public void deleteHome(E event) throws IOException;
    public void deleteHomeOthers(E event) throws IOException;

}
