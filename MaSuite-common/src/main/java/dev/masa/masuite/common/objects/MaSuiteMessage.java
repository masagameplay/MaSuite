package dev.masa.masuite.common.objects;

/**
 * Plugin message channels to use between instances
 */
public enum MaSuiteMessage {
    // Main channel
    MAIN ("BungeeCord"),
    // User
    USER_INFO ("masuite:user:info"),
    // Homes
    HOMES_SET ("masuite:homes:set"),
    HOMES_DELETE ("masuite:homes:delete"),
    HOMES_TELEPORT ("masuite:homes:teleport"),
    HOMES_LIST ("masuite:homes:list"),
    // Teleportation
    TELEPORT_TO_LOCATION ("masuite:user:teleport:location"),
    TELEPORT_TO_PLAYER ("masuite:user:teleport:player");

    public final String channel;

    MaSuiteMessage(String channel) {
        this.channel = channel;
    }


}
