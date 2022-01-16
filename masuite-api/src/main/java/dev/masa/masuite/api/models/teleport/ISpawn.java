package dev.masa.masuite.api.models.teleport;

import dev.masa.masuite.api.objects.ILocation;

import java.util.UUID;

/**
 * When player joins, dies or execute spawn command they can be teleported to the spawn.
 * Spawn can be global or server specific and there can be different spawns for first time joiners and regular users.
 */
public interface ISpawn {

    /**
     * Unique id of the spawn
     *
     * @return unique id of the spawn
     */
    UUID uniqueId();

    /**
     * Location of the spawn
     *
     * @return location of the spawn
     */
    ILocation location();

    /**
     * Spawn can be default spawn (true) or first time spawn (false).
     *
     * @return is spawn default or first time spawn
     */
    boolean isDefaultSpawn();

}
