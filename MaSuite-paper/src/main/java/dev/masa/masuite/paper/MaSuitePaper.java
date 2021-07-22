package dev.masa.masuite.paper;

import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.PaperCommandManager;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.common.services.CooldownService;
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
    private CooldownService cooldownService;

    @Getter
    private final ConcurrentHashMap<UUID, Location> locationTeleportationQueue = new ConcurrentHashMap<>();
    @Getter
    private final ConcurrentHashMap<UUID, Location> playerTeleportationQueue = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        this.cooldownService = new CooldownService();
        this.manager = new PaperCommandManager(this);

        this.manager.registerCommand(new UserInfoCommand());
        this.manager.registerCommand(new SetHomeCommand());
        this.manager.registerCommand(new TeleportHomeCommand());
        this.manager.registerCommand(new DelHomeCommand());
        this.manager.registerCommand(new ListHomeCommand());

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

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, MaSuiteMessage.MAIN.channel);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, MaSuiteMessage.MAIN.channel, new UserTeleportationMessageListener(this));

        this.cooldownService().addCooldownLength("homes", 3);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
