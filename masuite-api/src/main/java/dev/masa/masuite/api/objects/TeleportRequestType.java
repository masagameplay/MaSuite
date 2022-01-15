package dev.masa.masuite.api.objects;

public enum TeleportRequestType {
    /**
     * Teleport receiver to sender's location
     */
    HERE("here"),
    /**
     * Teleport sender to receiver's location
     */
    TO("to");

    public final String type;
    TeleportRequestType(String type) {
        this.type = type;
    }
}
