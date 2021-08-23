package dev.masa.masuite.api.services;

import dev.masa.masuite.api.models.warp.IWarp;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * WarpService handles data related to warps
 *
 * @param <T> platform type of {@link IWarp}
 */
public interface IWarpService<T extends IWarp> {

    /**
     * Get {@link T} by warp name
     *
     * @param name name of the home
     * @return optional {@link T}
     */
    Optional<T> warp(String name);

    /**
     * Create or update {@link T}.
     *
     * @param warp warp to create or update
     * @param done callback after warp has been created or updated (first = ok?, second = isCreated?)
     */
    void createOrUpdateWarp(T warp, BiConsumer<Boolean, Boolean> done);

    /**
     * Delete {@link T}
     *
     * @param warp warp to delete
     * @param done callback after has been deleted successfully or not
     */
    void deleteWarp(T warp, Consumer<Boolean> done);

    /**
     * Get a list of warps
     *
     * @return a list of {@link T}s
     */
    List<T> warps();
}
