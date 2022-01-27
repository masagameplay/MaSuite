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
public class MaSuiteServerMessagesConfig {

    @Getter
    @Comment("This is in the old format because ACF requires that.")
    @Setting("in-cooldown")
    private String inCooldown = "§4>§c> §8- §7You can use that command after §r<time> §7seconds.";

    @Getter
    @Setting("teleportation-cancelled")
    private String teleportationCancelled = "<dark_red>><red>> <dark_gray>- <gray>Teleportation cancelled.";

    @Getter
    @Setting("teleportation-started")
    private String teleportationStarted = "<dark_green>><green>> <dark_gray>+ <gray>You will be teleported in <white><time></white> seconds. Don't move!";

    private static final ObjectMapper<MaSuiteServerMessagesConfig> MAPPER;

    static {
        try {
            MAPPER = ObjectMapper.factory().get(MaSuiteServerMessagesConfig.class);
        } catch (final SerializationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static MaSuiteServerMessagesConfig loadFrom(final ConfigurationNode node) throws SerializationException {
        return MAPPER.load(node);
    }

    @SneakyThrows
    public void saveTo(ConfigurationNode node) {
        MAPPER.save(this, node);
    }
}
