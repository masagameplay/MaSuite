package dev.masa.masuite.common.models.home;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import dev.masa.masuite.api.models.home.IHome;
import dev.masa.masuite.common.models.DatabaseLocation;
import dev.masa.masuite.common.objects.Location;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@NoArgsConstructor()
@Data()
@Entity()
@Table(name = "masuite_homes")
public class Home extends DatabaseLocation implements IHome {

    @DatabaseField(dataType = DataType.UUID, generatedId = true, columnName = "uuid")
    private UUID uniqueId;
    @DatabaseField
    private String name;

    @DatabaseField(dataType = DataType.UUID)
    private UUID owner;

    public Home(String name, UUID owner, Location location) {
        this.name = name;
        this.owner = owner;
        this.location(location);
    }

}
