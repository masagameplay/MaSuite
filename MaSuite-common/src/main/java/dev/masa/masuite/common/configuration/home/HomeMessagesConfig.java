package dev.masa.masuite.common.configuration.home;

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
public class HomeMessagesConfig {

    @Getter
    @Setting("home-limit-reached")
    private Component homeLimitReached = Component.text("You have reached home limit.", NamedTextColor.RED);

    @Getter
    @Setting("home-not-found")
    private Component homeNotFound = Component.text("Home with that name not found", NamedTextColor.RED);

    @Getter
    @Setting("home-set")
    private Component homeSet = Component.text("Created home with name ", NamedTextColor.GRAY).append(Component.text("%home%", NamedTextColor.BLUE));

    @Getter
    @Setting("home-updated")
    private Component homeUpdated = Component.text("Updated home with name ", NamedTextColor.GRAY).append(Component.text("%home%", NamedTextColor.BLUE));

    @Getter
    @Setting("home-deleted")
    private Component homeDeleted = Component.text("Deleted home with name ", NamedTextColor.GRAY).append(Component.text("%home%", NamedTextColor.BLUE));

    @Getter
    @Setting("home-teleported")
    private Component homeTeleported = Component.text("Teleport to ", NamedTextColor.GRAY).append(Component.text("%home%", NamedTextColor.BLUE));

    @Getter
    @Setting("home-list-title")
    private Component homeListTitle = Component.text("Your homes: ", NamedTextColor.BLUE);

    @Getter
    @Setting("home-list-title-others")
    private Component homeListTitleOthers = Component.text("%player%'s homes: ", NamedTextColor.BLUE);

    @Getter
    @Setting("home-list-name")
    private Component homeListName = Component.text("%home%", NamedTextColor.GRAY).hoverEvent(HoverEvent.showText(Component.text("Click to teleport", NamedTextColor.BLUE)));

    @Getter
    @Setting("home-list-splitter")
    private Component homeListSplitter = Component.text(", ", NamedTextColor.GRAY);

    @Getter
    @Setting("home-list-server-name")
    private Component homeListServerName = Component.text("%server%", NamedTextColor.BLUE).append(Component.text(": ", NamedTextColor.GRAY));


    private static final ObjectMapper<HomeMessagesConfig> MAPPER;

    static {
        try {
            MAPPER = ObjectMapper.factory().get(HomeMessagesConfig.class);
        } catch (final SerializationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static HomeMessagesConfig loadFrom(final ConfigurationNode node) throws SerializationException {
        return MAPPER.load(node);
    }

    @SneakyThrows
    public void saveTo(ConfigurationNode node) {
        MAPPER.save(this, node);
    }
}
