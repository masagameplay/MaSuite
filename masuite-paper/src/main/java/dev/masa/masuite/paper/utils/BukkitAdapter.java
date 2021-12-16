package dev.masa.masuite.paper.utils;

import dev.masa.masuite.common.objects.Location;
import org.bukkit.Bukkit;

public class BukkitAdapter {

    public static Location adapt(org.bukkit.Location bukkitLocation) {
        return new Location(bukkitLocation.getX(), bukkitLocation.getY(), bukkitLocation.getZ(), bukkitLocation.getYaw(), bukkitLocation.getPitch(), bukkitLocation.getWorld().getName());
    }

    public static org.bukkit.Location adapt(Location location) {
        return new org.bukkit.Location(Bukkit.getWorld(location.world()), location.x(), location.y(), location.z(), location.yaw(), location.pitch());
    }

}
