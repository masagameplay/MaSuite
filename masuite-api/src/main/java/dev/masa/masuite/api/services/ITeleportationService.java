package dev.masa.masuite.api.services;

import dev.masa.masuite.api.objects.ILocation;

import java.util.function.Consumer;

public interface ITeleportationService<P, L extends ILocation> {

    /**
     * Teleport player to {@link ILocation}. If the locations {@link ILocation#server()} is null, the server will be the current server of the player.
     * @param player player to teleport
     * @param location target location
     * @param done callback when teleportation is done from sender side
     */
    void teleportPlayerToLocation(P player, L location, Consumer<Boolean> done);

    /**
     * Teleport player to {@link ILocation}
     * @param player player to teleport
     * @param target target player
     * @param done callback when teleportation is done from sender's side (for example proxy)
     */
    void teleportPlayerToPlayer(P player, P target, Consumer<Boolean> done);

}
