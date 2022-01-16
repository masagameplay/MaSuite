package dev.masa.masuite.api.models.warp;

import dev.masa.masuite.api.objects.ILocation;

import java.util.UUID;

/**
 * Network wide warp point
 */
public interface IWarp {
    /**
     * Unique id of the warp
     *
     * @return unique id of the warp
     */
    UUID uniqueId();

    /**
     * Name of the warp point
     *
     * @return name of the warp
     */
    String name();

    /**
     * Location of the warp point
     *
     * @return location of the warp
     */
    ILocation location();

    /**
     * Warp can be public (true) or hidden (false). If warp is public, it will be shown in default warp listing, otherwise it will be shown in hidden warp list
     *
     * @return Is warp publicly available or not.
     */
    boolean isPublic();

    /**
     * Warp can be global (true) or server (false) specific. If warp is global, player can teleport into this warp from any server
     * if they have permission to do so. If warp is server specific, only players in the same server as the warp
     * can be teleported into this warp.
     *
     * @return Is warp globally available or not.
     */
    boolean isGlobal();

}
