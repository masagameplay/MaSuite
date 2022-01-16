package dev.masa.masuite.common.models.home;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import dev.masa.masuite.api.models.home.IHome;
import dev.masa.masuite.common.objects.Location;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Accessors(fluent = true)
@NoArgsConstructor()
@Data()
@Entity()
@Table(name = "masuite_homes")
public class Home implements IHome {

    @DatabaseField(dataType = DataType.UUID, generatedId = true, columnName = "uuid")
    private UUID uniqueId;
    @DatabaseField
    private String name;

    @DatabaseField(dataType = DataType.UUID)
    private UUID owner;

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

    public Home(String name, UUID owner, Location location) {
        this.name = name;
        this.owner = owner;
        this.location(location);
    }

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
