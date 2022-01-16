package dev.masa.masuite.common.models.teleport;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import dev.masa.masuite.api.models.teleport.ISpawn;
import dev.masa.masuite.common.models.DatabaseLocation;
import dev.masa.masuite.common.objects.Location;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.UUID;

@Accessors(fluent = true)
@NoArgsConstructor()
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
public class Spawn extends DatabaseLocation implements ISpawn {

    @DatabaseField(dataType = DataType.UUID, generatedId = true, columnName = "uuid")
    private UUID uuid;

    @DatabaseField
    private Boolean defaultSpawn;

    @Override
    public UUID uniqueId() {
        return this.uuid;
    }

    @Override
    public boolean isDefaultSpawn() {
        return this.defaultSpawn;
    }

    public Spawn(Location location, boolean isDefaultSpawn) {
        this.location(location);
        this.defaultSpawn = isDefaultSpawn;
    }

}
