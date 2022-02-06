package dev.masa.masuite.api.proxy.listeners.home;

import java.io.IOException;

public interface IListHomeMessageListener<E> {

    public void listHomes(E event) throws IOException;
    public void listUserHomes(E event) throws IOException;
    public void serverRequestHomes(E event) throws IOException;
}
