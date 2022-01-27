package dev.masa.masuite.common.configuration;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
@Accessors(fluent = true)
public class ModulesConfig {

    @Getter
    @Setting("homes")
    protected boolean homes = true;

    @Getter
    @Setting("teleports")
    protected boolean teleports = true;

    @Getter
    @Setting("warps")
    protected boolean warps = true;

}
