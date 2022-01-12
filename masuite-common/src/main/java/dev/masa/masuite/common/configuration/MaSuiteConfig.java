package dev.masa.masuite.common.configuration;

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
public class MaSuiteConfig {

    @Getter
    @Comment("Database settings")
    @Setting("database")
    private DatabaseConfig database = new DatabaseConfig();

    @Getter
    @Comment("Warps settings")
    @Setting("warps")
    private WarpSettingsConfig warps = new WarpSettingsConfig();

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

    private static final ObjectMapper<MaSuiteConfig> MAPPER;

    static {
        try {
            MAPPER = ObjectMapper.factory().get(MaSuiteConfig.class);
        } catch (final SerializationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static MaSuiteConfig loadFrom(final ConfigurationNode node) throws SerializationException {
        return MAPPER.load(node);
    }

    @SneakyThrows
    public void saveTo(ConfigurationNode node) {
        MAPPER.save(this, node);
    }
}
