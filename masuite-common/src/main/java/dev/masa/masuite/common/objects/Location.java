package dev.masa.masuite.common.objects;

import com.google.gson.Gson;
import dev.masa.masuite.api.objects.ILocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Location implements ILocation {

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private String world;
    private String server;

    public Location(double x, double y, double z, float yaw, float pitch, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
    }

    public String serialize() {
        return new Gson().toJson(this);
    }

    public Location deserialize(String json) {
        return new Gson().fromJson(json, Location.class);
    }

}
