package dev.masa.masuite.api.services;

import dev.masa.masuite.api.models.home.IHome;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * HomeService handles data related to homes
 *
 * @param <T> platform type of {@link IHome}
 */
public interface IHomeService<T extends IHome> {

    /**
     * Get {@link T} by owner's unique id and home name
     *
     * @param ownerId owner of the home
     * @param name    name of the home
     * @return optional {@link T}
     */
    Optional<T> home(UUID ownerId, String name);

    /**
     * Create or update {@link T}.
     *
     * @param home home to create or update
     * @param done callback after home has been created or updated (first = ok?, second = isCreated?)
     */
    void createOrUpdateHome(T home, BiConsumer<Boolean, Boolean> done);

    /**
     * Delete {@link T}
     * @param home home to delete
     * @param done callback after has been deleted successfully or not
     */
    void deleteHome(T home, Consumer<Boolean> done);

    /**
     * Get a list of {@link T}s from owner
     * @param ownerId id of the homes
     * @return a list of user's {@link T}s
     */
    List<T> homes(UUID ownerId);
}
