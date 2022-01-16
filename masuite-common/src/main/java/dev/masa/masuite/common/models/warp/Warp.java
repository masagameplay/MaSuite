package dev.masa.masuite.common.models.warp;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import dev.masa.masuite.api.models.warp.IWarp;
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
@Table(name = "masuite_warps")
public class Warp extends DatabaseLocation implements IWarp {

    @DatabaseField(dataType = DataType.UUID, generatedId = true, columnName = "uuid")
    private UUID uniqueId;
    @DatabaseField
    private String name;

    @DatabaseField(columnName = "public")
    private boolean isPublic;

    @DatabaseField
    private boolean global;

    public Warp(String name, Location location, boolean isPublic, boolean isGlobal) {
        this.name = name;
        this.isPublic = isPublic;
        this.global = isGlobal;
        this.location(location);
    }

    @Override
    public boolean isPublic() {
        return this.isPublic;
    }

    @Override
    public boolean isGlobal() {
        return this.global;
    }

}
