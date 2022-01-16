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
    ADD_USER("masuite:user:add"),
    REMOVE_USER("masuite:user:remove"),

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
    TELEPORT_TO_LOCATION("masuite:teleport:location"),
    TELEPORT_TO_PLAYER("masuite:teleport:player"),
    TELEPORT_ALL_TO_PLAYER("masuite:teleport:all"),
    TELEPORT_PLAYER_TO_PLAYER("masuite:teleport:player:other"),
    TELEPORT_PLAYER_TO_LOCATION("masuite:teleport:location:other"),

    TELEPORT_REQUEST_CREATE("masuite:teleport:request:create"),
    TELEPORT_REQUEST_ACCEPT("masuite:teleport:request:accept"),
    TELEPORT_REQUEST_DENY("masuite:teleport:request:deny"),
    TELEPORT_REQUEST_LOCK("masuite:teleport:request:lock");

    public final String channel;

    MaSuiteMessage(String channel) {
        this.channel = channel;
    }


}
