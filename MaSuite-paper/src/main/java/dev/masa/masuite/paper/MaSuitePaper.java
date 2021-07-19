package dev.masa.masuite.paper;

import co.aikar.commands.PaperCommandManager;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.paper.commands.*;
import dev.masa.masuite.paper.listeners.PlayerJoinListener;
import dev.masa.masuite.paper.listeners.UserTeleportationMessageListener;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Accessors(fluent = true)
public final class MaSuitePaper extends JavaPlugin {

    private PaperCommandManager manager;

    @Getter
    private final ConcurrentHashMap<UUID, Location> locationTeleportationQueue = new ConcurrentHashMap<>();
    @Getter
    private final ConcurrentHashMap<UUID, Location> playerTeleportationQueue = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        this.manager = new PaperCommandManager(this);

        this.manager.registerCommand(new UserInfoCommand(this));
        this.manager.registerCommand(new SetHomeCommand(this));
        this.manager.registerCommand(new TeleportHomeCommand(this));
        this.manager.registerCommand(new DelHomeCommand(this));
        this.manager.registerCommand(new ListHomeCommand(this));

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, MaSuiteMessage.MAIN.channel);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, MaSuiteMessage.MAIN.channel, new UserTeleportationMessageListener(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
