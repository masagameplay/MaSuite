package dev.masa.masuite.common.models;

import com.j256.ormlite.field.DatabaseField;
import dev.masa.masuite.common.objects.Location;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Data()
@NoArgsConstructor()
public class DatabaseLocation {

    @DatabaseField
    private Double x;

    @DatabaseField

    private Double y;
    @DatabaseField

    private Double z;
    @DatabaseField
    private Float yaw = 0.0F;

    @DatabaseField
    private Float pitch = 0.0F;

    @DatabaseField
    private String server;

    @DatabaseField
    private String world;

    public Location location() {
        return new Location(this.x, this.y, this.z, this.yaw, this.pitch, this.world, this.server);
    }

    public void location(Location location) {
        this.x = location.x();
        this.y = location.y();
        this.z = location.z();
        this.yaw = location.yaw();
        this.pitch = location.pitch();
        this.world = location.world();
        this.server = location.server();
    }
}
