package dev.masa.masuite.common.configuration;

import dev.masa.masuite.common.configuration.home.HomeMessagesConfig;
import dev.masa.masuite.common.configuration.warp.WarpMessagesConfig;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.serialize.SerializationException;

@ConfigSerializable
@Accessors(fluent = true)
public class MessagesConfig {

    @Getter
    @Setting("player-not-online")
    private String playerNotOnline = "&cPlayer is not online.";

    @Getter
    @Setting("player-not-found")
    private String playerNotFound = "&cCould not find player.";

    @Getter
    @Setting("homes")
    private HomeMessagesConfig homes = new HomeMessagesConfig();

    @Getter
    @Setting("warps")
    private WarpMessagesConfig warps = new WarpMessagesConfig();

    private static final ObjectMapper<MessagesConfig> MAPPER;

    static {
        try {
            MAPPER = ObjectMapper.factory().get(MessagesConfig.class);
        } catch (final SerializationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static MessagesConfig loadFrom(final ConfigurationNode node) throws SerializationException {
        return MAPPER.load(node);
    }

    @SneakyThrows
    public void saveTo(ConfigurationNode node) {
        MAPPER.save(this, node);
    }
}
