package dev.masa.masuite.paper;

import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.PaperCommandManager;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.common.services.CooldownService;
import dev.masa.masuite.paper.commands.home.DeleteHomeCommand;
import dev.masa.masuite.paper.commands.home.ListHomeCommand;
import dev.masa.masuite.paper.commands.home.SetHomeCommand;
import dev.masa.masuite.paper.commands.home.TeleportHomeCommand;
import dev.masa.masuite.paper.commands.teleport.TeleportAllCommand;
import dev.masa.masuite.paper.commands.teleport.TeleportCommand;
import dev.masa.masuite.paper.commands.teleport.TeleportHereCommand;
import dev.masa.masuite.paper.commands.teleport.TeleportRequestCommand;
import dev.masa.masuite.paper.commands.warp.DeleteWarpCommand;
import dev.masa.masuite.paper.commands.warp.ListWarpCommand;
import dev.masa.masuite.paper.commands.warp.SetWarpCommand;
import dev.masa.masuite.paper.commands.warp.TeleportWarpCommand;
import dev.masa.masuite.paper.listeners.MaSuiteSetupListener;
import dev.masa.masuite.paper.listeners.PlayerJoinListener;
import dev.masa.masuite.paper.listeners.UserTeleportationMessageListener;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Accessors(fluent = true)
public final class MaSuitePaper extends JavaPlugin {

    private PaperCommandManager manager;

    @Getter
    private CooldownService cooldownService;

    @Getter
    private final ConcurrentHashMap<UUID, Location> locationTeleportationQueue = new ConcurrentHashMap<>();
    @Getter
    private final ConcurrentHashMap<UUID, UUID> playerTeleportationQueue = new ConcurrentHashMap<>();

    @Getter
    private final List<String> onlinePlayers = new ArrayList<>();

    @Override
    public void onEnable() {
        this.cooldownService = new CooldownService();
        this.manager = new PaperCommandManager(this);

        this.manager.registerCommand(new SetHomeCommand());
        this.manager.registerCommand(new TeleportHomeCommand());
        this.manager.registerCommand(new DeleteHomeCommand());
        this.manager.registerCommand(new ListHomeCommand());

        this.manager.registerCommand(new SetWarpCommand());
        this.manager.registerCommand(new TeleportWarpCommand());
        this.manager.registerCommand(new DeleteWarpCommand());
        this.manager.registerCommand(new ListWarpCommand());

        this.manager.registerCommand(new TeleportCommand());
        this.manager.registerCommand(new TeleportHereCommand());
        this.manager.registerCommand(new TeleportAllCommand());
        this.manager.registerCommand(new TeleportRequestCommand());

        this.manager.getCommandConditions().addCondition("cooldown", c -> {
            UUID uuid = c.getIssuer().getUniqueId();

            String cooldownType = c.getConfigValue("type", "");
            String byPassPermission = c.getConfigValue("bypass", "masuite.cooldown.bypass");

            if (this.cooldownService().cooldownLength(cooldownType) > 0 && this.cooldownService().hasCooldown(cooldownType, uuid)) {
                if (!c.getIssuer().hasPermission(byPassPermission)) {
                    throw new ConditionFailedException("You can do that after " + this.cooldownService().cooldownLength(cooldownType) + " seconds.");
                }
            }
        });

        manager.getCommandCompletions().registerAsyncCompletion("masuite_players", c -> this.onlinePlayers());

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, MaSuiteMessage.MAIN.channel);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, MaSuiteMessage.MAIN.channel, new UserTeleportationMessageListener(this));
        this.getServer().getMessenger().registerIncomingPluginChannel(this, MaSuiteMessage.MAIN.channel, new MaSuiteSetupListener(this));

        this.cooldownService().addCooldownLength("homes", 3);
        this.cooldownService().addCooldownLength("warps", 3);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
