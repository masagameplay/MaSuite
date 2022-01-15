package dev.masa.masuite.common.configuration.warp;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
@Accessors(fluent = true)
public class WarpSettingsConfig {

    @Getter
    @Setting("enable-per-warp-permission")
    private boolean enablePerWarpPermission = false;

}
