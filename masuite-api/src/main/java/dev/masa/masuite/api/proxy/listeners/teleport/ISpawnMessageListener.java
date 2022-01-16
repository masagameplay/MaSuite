package dev.masa.masuite.api.proxy.listeners.teleport;

import java.io.IOException;

public interface ISpawnMessageListener<E> {

    public void createSpawn(E event) throws IOException;
    public void deleteSpawn(E event) throws IOException;
    public void teleportToSpawn(E event) throws IOException;

}
