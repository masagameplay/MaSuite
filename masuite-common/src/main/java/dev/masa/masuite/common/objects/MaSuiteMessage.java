package dev.masa.masuite.common.objects;

/**
 * Plugin message channels to use between instances
 */
public enum MaSuiteMessage {
    // Main channel
    MAIN("masuite:main"),
    // Setup servers
    SETUP("masuite:setup"),

    // User
    USER_INFO("masuite:user:info"),

    // Warps
    WARPS_SET("masuite:warps:set"),
    WARPS_DELETE("masuite:warps:delete"),
    WARPS_TELEPORT("masuite:warps:teleport"),
    WARPS_LIST("masuite:warps:list"),

    // Homes
    HOMES_SET("masuite:homes:set"),
    HOMES_SET_OTHERS("masuite:homes:set:others"),
    HOMES_DELETE("masuite:homes:delete"),
    HOMES_DELETE_OTHERS("masuite:homes:delete:others"),
    HOMES_TELEPORT("masuite:homes:teleport"),
    HOMES_TELEPORT_OTHERS("masuite:homes:teleport:others"),
    HOMES_LIST("masuite:homes:list"),
    HOMES_LIST_OTHERS("masuite:homes:list:others"),
    // Teleportation
    TELEPORT_TO_LOCATION("masuite:user:teleport:location"),
    TELEPORT_TO_PLAYER("masuite:user:teleport:player"),
    TELEPORT_PLAYER_TO_PLAYER("masuite:user:teleport:player:other"),
    TELEPORT_PLAYER_TO_LOCATION("masuite:user:teleport:location:other");

    public final String channel;

    MaSuiteMessage(String channel) {
        this.channel = channel;
    }


}
