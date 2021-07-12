package dev.masa.masuite.api.services;

import dev.masa.masuite.api.models.user.IUser;

import java.util.Optional;
import java.util.UUID;

public interface IUserService<T extends IUser> {

    /**
     * Get {@link T}
     *
     * @param uniqueId unique id of the user
     * @return optional @{T}
     */
    Optional<T> user(UUID uniqueId);

    /**
     * Create or update new user
     *
     * @param user user to create or update
     */
    void user(T user);


}
