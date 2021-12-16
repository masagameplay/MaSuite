package dev.masa.masuite.common.configuration.warp;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.serialize.SerializationException;

@ConfigSerializable
@Accessors(fluent = true)
public class WarpMessagesConfig {

    @Getter
    @Setting("warp-not-found")
    private Component warpNotFound = Component.text("That warp point does not exists!", NamedTextColor.RED);

    @Getter
    @Setting("warp-created")
    private Component warpCreated = Component.text("Created a warp with name ", NamedTextColor.GRAY).append(Component.text("%warp%", NamedTextColor.BLUE));

    @Getter
    @Setting("warp-updated")
    private Component warpUpdated = Component.text("Updated a warp with name ", NamedTextColor.GRAY).append(Component.text("%warp%", NamedTextColor.BLUE));

    @Getter
    @Setting("warp-deleted")
    private Component warpDeleted = Component.text("Deleted a warp with name ", NamedTextColor.GRAY).append(Component.text("%warp%", NamedTextColor.BLUE));

    @Getter
    @Setting("teleported")
    private Component warpTeleported = Component.text("Teleport to ", NamedTextColor.GRAY).append(Component.text("%warp%", NamedTextColor.BLUE));

    @Getter
    @Setting("warp-global-list-title")
    private Component globalListTitle = Component.text("Global warps: ", NamedTextColor.GRAY);

    @Getter
    @Setting("warp-server-list-title")
    private Component serverListTitle = Component.text("Server warps: ", NamedTextColor.GRAY);

    @Getter
    @Setting("warp-hidden-list-title")
    private Component hiddenListTitle = Component.text("Hidden warps: ", NamedTextColor.GRAY);

    @Getter
    @Setting("warp-list-name")
    private Component warpListName = Component.text("%warp%", NamedTextColor.BLUE).hoverEvent(HoverEvent.showText(Component.text("Click to teleport", NamedTextColor.BLUE)));

    @Getter
    @Setting("warp-list-splitter")
    private Component warpListSplitter = Component.text(", ", NamedTextColor.GRAY);

    private static final ObjectMapper<WarpMessagesConfig> MAPPER;

    static {
        try {
            MAPPER = ObjectMapper.factory().get(WarpMessagesConfig.class);
        } catch (final SerializationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static WarpMessagesConfig loadFrom(final ConfigurationNode node) throws SerializationException {
        return MAPPER.load(node);
    }

    @SneakyThrows
    public void saveTo(ConfigurationNode node) {
        MAPPER.save(this, node);
    }
}
