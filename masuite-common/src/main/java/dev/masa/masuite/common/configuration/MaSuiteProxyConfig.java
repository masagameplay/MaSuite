package dev.masa.masuite.common.configuration;

import dev.masa.masuite.common.configuration.teleport.TeleportSettingsConfig;
import dev.masa.masuite.common.configuration.warp.WarpSettingsConfig;
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
public class MaSuiteProxyConfig {

    @Getter
    @Comment("Database settings")
    @Setting("database")
    private DatabaseConfig database = new DatabaseConfig();

    @Getter
    @Comment("Enabled modules")
    @Setting("modules")
    private ModulesConfig modules = new ModulesConfig();

    @Getter
    @Comment("Warps settings")
    @Setting("warps")
    private WarpSettingsConfig warps = new WarpSettingsConfig();

    @Getter
    @Comment("Teleports settings")
    @Setting("teleports")
    private TeleportSettingsConfig teleports = new TeleportSettingsConfig();

    @ConfigSerializable
    static public class DatabaseConfig {
        @Getter
        @Setting("name")
        protected String databaseName = "minecraft?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8";

        @Getter
        @Setting("address")
        protected String databaseAddress = "localhost";

        @Getter
        @Setting("port")
        protected Integer databasePort = 3306;

        @Getter
        @Setting("username")
        protected String databaseUsername = "minecraft";

        @Getter
        @Setting("password")
        protected String databasePassword = "minecraft";
    }

    private static final ObjectMapper<MaSuiteProxyConfig> MAPPER;

    static {
        try {
            MAPPER = ObjectMapper.factory().get(MaSuiteProxyConfig.class);
        } catch (final SerializationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static MaSuiteProxyConfig loadFrom(final ConfigurationNode node) throws SerializationException {
        return MAPPER.load(node);
    }

    @SneakyThrows
    public void saveTo(ConfigurationNode node) {
        MAPPER.save(this, node);
    }
}
