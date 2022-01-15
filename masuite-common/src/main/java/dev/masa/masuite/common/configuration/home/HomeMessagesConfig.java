package dev.masa.masuite.common.configuration.home;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
@Accessors(fluent = true)
public class HomeMessagesConfig {

    @Getter
    @Setting("home-limit-reached")
    private String homeLimitReached = "<dark_red>><red>> <dark_gray>- <gray>You have reached your home limit";

    @Getter
    @Setting("home-not-found")
    private String homeNotFound = "<dark_red>><red>> <dark_gray>- <gray>Home with that name not found";

    @Getter
    @Setting("home-set")
    private String homeSet = "<dark_green>><green>> <dark_gray>+ <gray>Created a home with name <white><home-name>";

    @Getter
    @Setting("home-updated")
    private String homeUpdated = "<dark_green>><green>> <dark_gray>+ <gray>Updated home with name <white><home-name>";

    @Getter
    @Setting("home-deleted")
    private String homeDeleted = "<dark_aqua>><aqua>> <dark_gray>- <gray>Deleted home with name <white><home-name>";

    @Getter
    @Setting("home-teleported")
    private String homeTeleported = "<dark_aqua>><aqua>> <dark_gray>+ <gray>Teleported to <white><home-name>";

    @Getter
    @Setting("home-list-title")
    private String homeListTitle = "<dark_aqua>><aqua>> <dark_gray>+ <gray>Your homes: ";

    @Getter
    @Setting("home-list-title-others")
    private String homeListTitleOthers = "<dark_aqua>><aqua>> <dark_gray>+ <white><username><gray>'s homes: ";

    @Getter
    @Setting("home-list-name")
    private String homeListName = "<hover:show_text:'<blue>Click to teleport'><click:run_command:'/home <home-name>'><white><home-name>";

    @Getter
    @Setting("home-list-splitter")
    private String homeListSplitter = "<gray>, ";

    @Getter
    @Setting("home-list-server-name")
    private String homeListServerName = "<blue><location-server><gray>:";

}
