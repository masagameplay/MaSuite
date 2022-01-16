package dev.masa.masuite.common.configuration.teleport;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@Accessors(fluent = true)
@ConfigSerializable
public class TeleportSettingsConfig {

    @Getter
    @Setting("spawn-on-join")
    private boolean spawnOnJoin = false;

    @Getter
    @Setting("keep-teleport-request-alive")
    private Integer keepTeleportRequestAlive = 20;

    @Getter
    @Setting("spawn-on-first-join")
    private Boolean spawnOnFirstJoin = true;

    @Getter
    @Setting("spawn-type")
    private String spawnType = "server";


}
