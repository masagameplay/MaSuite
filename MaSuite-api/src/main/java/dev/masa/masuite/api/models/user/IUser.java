package dev.masa.masuite.api.models.user;

import java.util.Date;
import java.util.UUID;

/**
 * MaSuiteUser contains data of the user and queries optional data from other databases (homes)
 */
public interface IUser {

    /**
     * Unique id of the user
     *
     * @return unique id of the user
     */
    UUID uniqueId();

    /**
     * User's Minecraft username. This might not be up-to-date, if the user has changed their username and they've not logged in.
     *
     * @return last known username
     */
    String username();

    /**
     * User's first login to the network
     *
     * @return first login timestamp
     */
    Date firstLogin();

    /**
     * User's last login to the network
     *
     * @return last login timestamp
     */
    Date lastLogin();
}
