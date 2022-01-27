package dev.masa.masuite.api.services;

import dev.masa.masuite.api.models.user.IUser;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * UserService handles data related to homes
 *
 * @param <T> platform type of {@link IUser}
 */
public interface IUserService<T extends IUser> {

    /**
     * Get {@link T} by unique id
     *
     * @param uniqueId unique id of the user
     * @return optional {@link T}
     */
    CompletableFuture<Optional<T>> user(UUID uniqueId);

    /**
     * Get {@link T} by username
     *
     * @param username unique id of the user
     * @return optional {@link T}
     */
    CompletableFuture<Optional<T>> user(String username);

    /**
     * Create or update new user
     *
     * @param user user to create or update
     */
    void createOrUpdateUser(T user);


}
