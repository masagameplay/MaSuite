package dev.masa.masuite.common.configuration.home;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.HashMap;

@ConfigSerializable
@Accessors(fluent = true)
public class HomeConfig {

    @Getter
    private HashMap<String, ServerSettings> servers = new HashMap<>();

    @ConfigSerializable
    static public class ServerSettings {
        @Getter
        @Setting("warmup")
        protected Integer warmup = 0;

        @Getter
        @Setting("cooldown")
        protected Integer cooldown = 0;
    }

    private static final ObjectMapper<HomeConfig> MAPPER;

    static {
        try {
            MAPPER = ObjectMapper.factory().get(HomeConfig.class);
        } catch (final SerializationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static HomeConfig loadFrom(final ConfigurationNode node) throws SerializationException {
        return MAPPER.load(node);
    }

    @SneakyThrows
    public void saveTo(ConfigurationNode node) {
        MAPPER.save(this, node);
    }
}
