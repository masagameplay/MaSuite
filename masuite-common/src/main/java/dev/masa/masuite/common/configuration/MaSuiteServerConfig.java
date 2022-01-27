package dev.masa.masuite.common.configuration;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.serialize.SerializationException;

@ConfigSerializable
@Accessors(fluent = true)
public class MaSuiteServerConfig {

    @Getter
    @Comment("Enabled modules")
    @Setting("modules")
    private ModulesConfig modules = new ModulesConfig();

    @Getter
    @Comment("Homes")
    @Setting("homes")
    private HomesConfig homes = new HomesConfig();

    @Getter
    @Comment("Teleports")
    @Setting("teleports")
    private TeleportsConfig teleports = new TeleportsConfig();

    @Getter
    @Comment("Warps")
    @Setting("warps")
    private WarpsConfig warps = new WarpsConfig();

    @ConfigSerializable
    static public class TimingConfig {
        @Getter
        @Setting("cooldown")
        protected int cooldown = 3;

        @Getter
        @Setting("warmup")
        protected int warmup = 3;
    }

    @ConfigSerializable
    static public class HomesConfig extends TimingConfig {
    }

    @ConfigSerializable
    static public class WarpsConfig extends TimingConfig {
    }

    @ConfigSerializable
    static public class TeleportsConfig extends TimingConfig {
        @Getter
        @Setting("respawn-type")
        protected String respawnType = "none";

        @Getter
        @Setting("spawn-on-first-join")
        protected boolean spawnOnFirstJoin = true;
    }

    private static final ObjectMapper<MaSuiteServerConfig> MAPPER;

    static {
        try {
            MAPPER = ObjectMapper.factory().get(MaSuiteServerConfig.class);
        } catch (final SerializationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static MaSuiteServerConfig loadFrom(final ConfigurationNode node) throws SerializationException {
        return MAPPER.load(node);
    }

    @SneakyThrows
    public void saveTo(ConfigurationNode node) {
        MAPPER.save(this, node);
    }
}
