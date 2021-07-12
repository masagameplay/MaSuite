package dev.masa.masuite.api.object;

/**
 * Network wide location object
 */
public interface ILocation {

    /**
     * The x-coordinate of this location
     * @return x-coordinate
     */
    double x();
    /**
     * The y-coordinate of this location
     * @return y-coordinate
     */
    double y();
    /**
     * The z-coordinate of this location
     * @return z-coordinate
     */
    double z();
    /**
     * The yaw of this location
     * @return yaw
     */
    float yaw();

    /**
     * The pitch of this location
     * @return pitch
     */
    float pitch();

    /**
     * The name of this location's world
     * @return location's world's name
     */
    String world();

    /**
     * The name of this location's server
     * @return location's server's name
     */
    String server();

}
