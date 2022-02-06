package dev.masa.masuite.paper;

import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.PaperCommandManager;
import dev.masa.masuite.common.configuration.MaSuiteServerConfig;
import dev.masa.masuite.common.configuration.MaSuiteServerMessagesConfig;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.common.services.CooldownService;
import dev.masa.masuite.paper.commands.home.DeleteHomeCommand;
import dev.masa.masuite.paper.commands.home.ListHomeCommand;
import dev.masa.masuite.paper.commands.home.SetHomeCommand;
import dev.masa.masuite.paper.commands.home.TeleportHomeCommand;
import dev.masa.masuite.paper.commands.teleport.*;
import dev.masa.masuite.paper.commands.warp.DeleteWarpCommand;
import dev.masa.masuite.paper.commands.warp.ListWarpCommand;
import dev.masa.masuite.paper.commands.warp.SetWarpCommand;
import dev.masa.masuite.paper.commands.warp.TeleportWarpCommand;
import dev.masa.masuite.paper.listeners.HomeMessageListener;
import dev.masa.masuite.paper.listeners.MaSuiteSetupListener;
import dev.masa.masuite.paper.listeners.PlayerJoinListener;
import dev.masa.masuite.paper.listeners.UserTeleportationMessageListener;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Accessors(fluent = true)
public final class MaSuitePaper extends JavaPlugin {

    private BukkitAudiences adventure;

    public BukkitAudiences adventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    private PaperCommandManager manager;

    @Getter
    private CooldownService cooldownService;

    @Getter
    private WarmupManager warmupManager;

    @Getter
    private final ConcurrentHashMap<UUID, Location> locationTeleportationQueue = new ConcurrentHashMap<>();
    @Getter
    private final ConcurrentHashMap<UUID, UUID> playerTeleportationQueue = new ConcurrentHashMap<>();

    @Getter
    private final List<String> onlinePlayers = new ArrayList<>();

    @Getter
    private MaSuiteServerConfig config;

    @Getter
    private MaSuiteServerMessagesConfig messages;

    @Getter
    private ConcurrentHashMap<UUID, List<String>> homes = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        this.adventure = BukkitAudiences.create(this);
        this.generateConfigs();
        this.cooldownService = new CooldownService();
        this.warmupManager = new WarmupManager(this);
        this.getServer().getPluginManager().registerEvents(this.warmupManager, this);

        this.manager = new PaperCommandManager(this);

        try {
            this.manager.getLocales().loadYamlLanguageFile("acf_lang.yml", Locale.ENGLISH);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        this.manager.getCommandConditions().addCondition("cooldown", c -> {
            UUID uuid = c.getIssuer().getUniqueId();

            String cooldownType = c.getConfigValue("type", "");
            String byPassPermission = c.getConfigValue("bypass", "masuite.cooldown.bypass");

            if (this.cooldownService().cooldownLength(cooldownType) > 0 && this.cooldownService().hasCooldown(cooldownType, uuid)) {
                if (!c.getIssuer().hasPermission(byPassPermission)) {
                    throw new ConditionFailedException(this.messages().inCooldown().replaceAll("<time>", String.valueOf(this.cooldownService().cooldownLength(cooldownType))));
                }
            }
        });

        manager.getCommandCompletions().registerAsyncCompletion("masuite_players", c -> this.onlinePlayers());

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, MaSuiteMessage.MAIN.channel);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, MaSuiteMessage.MAIN.channel, new UserTeleportationMessageListener(this));
        this.getServer().getMessenger().registerIncomingPluginChannel(this, MaSuiteMessage.MAIN.channel, new MaSuiteSetupListener(this));


        this.cooldownService().addCooldownLength("homes", this.config.homes().cooldown());
        this.cooldownService().addCooldownLength("teleports", this.config.teleports().cooldown());
        this.cooldownService().addCooldownLength("warps", this.config.warps().cooldown());

        this.warmupManager().addWarmupTime("homes", this.config.homes().warmup());
        this.warmupManager().addWarmupTime("teleports", this.config.teleports().warmup());
        this.warmupManager().addWarmupTime("warps", this.config.warps().warmup());

        if (this.config.modules().homes()) {
            this.manager.registerCommand(new SetHomeCommand());
            this.manager.registerCommand(new TeleportHomeCommand(this));
            this.manager.registerCommand(new DeleteHomeCommand());
            this.manager.registerCommand(new ListHomeCommand());
            this.getServer().getMessenger().registerIncomingPluginChannel(this, MaSuiteMessage.MAIN.channel, new HomeMessageListener(this));

            manager.getCommandCompletions().registerCompletion("homes", c -> {
                if (homes.containsKey(c.getPlayer().getUniqueId())) {
                    this.getLogger().info("Tab completing home names");
                    return homes.get(c.getPlayer().getUniqueId());
                }
                return new ArrayList<>();
            });

        }

        if (this.config.modules().warps()) {
            this.manager.registerCommand(new SetWarpCommand());
            this.manager.registerCommand(new TeleportWarpCommand(this));
            this.manager.registerCommand(new DeleteWarpCommand());
            this.manager.registerCommand(new ListWarpCommand());
        }

        if (this.config.modules().teleports()) {
            this.manager.registerCommand(new TeleportCommand());
            this.manager.registerCommand(new TeleportHereCommand());
            this.manager.registerCommand(new TeleportAllCommand());
            this.manager.registerCommand(new TeleportRequestCommand());

            this.manager.registerCommand(new SpawnCommand(this));
        }
    }

    @Override
    public void onDisable() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }


    private void generateConfigs() {
        // config.yml
        YamlConfigurationLoader configLoader = YamlConfigurationLoader.builder()
                .file(new File("plugins/MaSuite/config.yml"))
                .defaultOptions(opts ->
                        opts.shouldCopyDefaults(true)
                )
                .nodeStyle(NodeStyle.BLOCK)
                .build();

        CommentedConfigurationNode configNode;
        try {
            configNode = configLoader.load();
            this.config = MaSuiteServerConfig.loadFrom(configNode);
            this.config.saveTo(configNode);
            configLoader.save(configNode);
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }

        // messages.yml
        YamlConfigurationLoader messagesLoader = YamlConfigurationLoader.builder()
                .file(new File("plugins/MaSuite/messages.yml"))
                .defaultOptions(opts -> opts.shouldCopyDefaults(true))
                .nodeStyle(NodeStyle.BLOCK)
                .build();

        CommentedConfigurationNode messagesNode;
        try {
            messagesNode = messagesLoader.load();
            this.messages = MaSuiteServerMessagesConfig.loadFrom(messagesNode);
            this.messages.saveTo(messagesNode);
            messagesLoader.save(messagesNode);
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }

        // Copy lang file over
        this.saveResource("acf_lang.yml", false);
    }
}
