package dev.masa.masuite.common.models;

import dev.masa.masuite.api.models.home.IHome;
import dev.masa.masuite.api.models.object.ILocation;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Accessors(fluent = true)
@Data()
public class Home implements IHome {

    private UUID uniqueId;
    private String name;
    private ILocation location;
    private UUID owner;

}
