package dev.masa.masuite.api.services;

import dev.masa.masuite.api.models.teleport.ISpawn;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * SpanService handles data related to spawns
 *
 * @param <T> platform type of {@link ISpawn}
 */
public interface ISpawnService<T extends ISpawn> {

    /**
     * Returns spawn point with given filter
     *
     * @param serverName   - name of the server
     * @param defaultSpawn - is spawn default or first time
     * @return optional {@link T}
     */
    CompletableFuture<Optional<T>> spawn(String serverName, boolean defaultSpawn);

    /**
     * Get first spawn from database with given type. Useful for retrieving global spawn
     *
     * @param defaultSpawn - is spawn default or first time
     * @return optional {@link T}
     */
    CompletableFuture<Optional<T>> spawn(boolean defaultSpawn);

    /**
     * Create or update {@link T}
     *
     * @param spawn spawn to create or update
     * @param done  callback after home has been created or updated (first = ok?, second = isCreated?)
     */
    void createOrUpdateSpawn(T spawn, BiConsumer<Boolean, Boolean> done);

    /**
     * Delete {@link T}
     *
     * @param spawn spawn to delete
     *              returns if the deletion was successful or not
     */
    CompletableFuture<Boolean> deleteSpawn(T spawn);
}
