package dev.masa.masuite.api.models.home;

import dev.masa.masuite.api.models.object.ILocation;

import java.util.UUID;

/**
 * Network wide home point
 */
public interface IHome {
    /**
     * Unique id of the home
     *
     * @return unique id of the home
     */
    UUID uniqueId();

    /**
     * Name of the home point
     *
     * @return name of the home
     */
    String name();

    /**
     * Location of the home point
     *
     * @return location of the home
     */
    ILocation location();

    /**
     * Unique id of the home point's owner
     *
     * @return owner's unique id
     */
    UUID owner();
}
