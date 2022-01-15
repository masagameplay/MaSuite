package dev.masa.masuite.common.configuration.warp;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
@Accessors(fluent = true)
public class WarpMessagesConfig {

    @Getter
    @Setting("warp-not-found")
    private String warpNotFound = "<dark_red>><red>> <dark_gray>- <gray>That warp point does not exists";

    @Getter
    @Setting("warp-created")
    private String warpCreated = "<dark_green>><green>> <dark_gray>+ Created a warp with name <white><warp-name>";

    @Getter
    @Setting("warp-updated")
    private String warpUpdated = "<dark_green>><green>> <dark_gray>+ Updated a warp with name <white><warp-name>";

    @Getter
    @Setting("warp-deleted")
    private String warpDeleted = "<dark_aqua>><aqua>> <dark_gray>- <gray>Deleted a warp with name <white><warp-name>";

    @Getter
    @Setting("teleported")
    private String warpTeleported = "<dark_aqua>><aqua>> <dark_gray>+ <gray>Teleported to <white><warp-name>";

    @Getter
    @Setting("warp-global-list-title")
    private String globalListTitle = "<dark_aqua>><aqua>> <dark_gray>+ <gray>Global warps: ";

    @Getter
    @Setting("warp-server-list-title")
    private String serverListTitle = "<dark_aqua>><aqua>> <dark_gray>+ <gray>Server warps: ";

    @Getter
    @Setting("warp-hidden-list-title")
    private String hiddenListTitle = "<dark_aqua>><aqua>> <dark_gray>+ <gray>Hidden warps: ";

    @Getter
    @Setting("warp-list-name")
    private String warpListName = "<hover:show_text:'<blue>Click to teleport'><click:run_command:'/warp <warp-name>'><white><warp-name>";

    @Getter
    @Setting("warp-list-splitter")
    private String warpListSplitter = "<gray>, ";

}
