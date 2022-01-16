package dev.masa.masuite.common.models.teleport;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import dev.masa.masuite.api.models.teleport.ISpawn;
import dev.masa.masuite.api.objects.ILocation;
import dev.masa.masuite.common.objects.Location;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.UUID;

@Accessors(fluent = true)
@NoArgsConstructor()
@Data()
@Entity()
@Table(name = "masuite_spawns")
@NamedQuery(
        name = "findSpawnByType",
        query = "SELECT s FROM Spawn s WHERE s.defaultSpawn = :defaultSpawn"
)

@NamedQuery(
        name = "findSpawnByTypeAndServer",
        query = "SELECT s FROM Spawn s WHERE s.defaultSpawn = :defaultSpawn AND s.server = :server"
)
public class Spawn implements ISpawn {

    @DatabaseField(dataType = DataType.UUID, generatedId = true, columnName = "uuid")
    private UUID uuid;

    @DatabaseField
    private Boolean defaultSpawn;

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

    @Override
    public UUID uniqueId() {
        return null;
    }

    @Override
    public ILocation location() {
        return new Location(this.x, this.y, this.z, this.yaw, this.pitch, this.world, this.server);
    }

    @Override
    public boolean isDefaultSpawn() {
        return this.defaultSpawn;
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
