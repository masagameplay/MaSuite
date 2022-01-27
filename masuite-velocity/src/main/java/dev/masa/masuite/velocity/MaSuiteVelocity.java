package dev.masa.masuite.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import dev.masa.masuite.common.configuration.MaSuiteProxyConfig;
import dev.masa.masuite.common.configuration.MaSuiteProxyMessagesConfig;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.common.services.*;
import dev.masa.masuite.velocity.listeners.home.DeleteHomeMessageListener;
import dev.masa.masuite.velocity.listeners.home.ListHomeMessageListener;
import dev.masa.masuite.velocity.listeners.home.SetHomeMessageListener;
import dev.masa.masuite.velocity.listeners.home.TeleportHomeMessageListener;
import dev.masa.masuite.velocity.listeners.teleport.SpawnMessageListener;
import dev.masa.masuite.velocity.listeners.teleport.TeleportMessageListener;
import dev.masa.masuite.velocity.listeners.teleport.TeleportRequestMessageListener;
import dev.masa.masuite.velocity.listeners.user.UserLeaveListener;
import dev.masa.masuite.velocity.listeners.user.UserLoginListener;
import dev.masa.masuite.velocity.listeners.warp.DeleteWarpMessageListener;
import dev.masa.masuite.velocity.listeners.warp.ListWarpMessageListener;
import dev.masa.masuite.velocity.listeners.warp.SetWarpMessageListener;
import dev.masa.masuite.velocity.listeners.warp.TeleportWarpMessageListener;
import dev.masa.masuite.velocity.services.TeleportRequestService;
import dev.masa.masuite.velocity.services.TeleportationService;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.sql.SQLException;

@Plugin(
        id = "masuite",
        name = "MaSuite",
        version = "@version@",
        description = "Proxy wide homes, teleportations and warps",
        url = "https://masa.dev",
        authors = {"Masa"}
)
@Accessors(fluent = true)
public class MaSuiteVelocity  {

    @Inject
    public Logger logger;

    @Inject
    @Getter
    private ProxyServer proxy;

    @Getter
    private UserService userService;

    @Getter
    private HomeService homeService;

    @Getter
    private WarpService warpService;

    @Getter
    private TeleportationService teleportationService;

    @Getter
    private TeleportRequestService teleportRequestService;

    @Getter
    private SpawnService spawnService;

    @Getter
    private DatabaseService databaseService;

    @Getter
    private MaSuiteProxyConfig config;

    @Getter
    private MaSuiteProxyMessagesConfig messages;

    public static MaSuiteVelocity MASUITE_INSTANCE;

    public static MinecraftChannelIdentifier MASUITE_MAIN_CHANNEL = MinecraftChannelIdentifier.from(MaSuiteMessage.MAIN.channel);

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        MASUITE_INSTANCE = this;
        this.generateConfigs();

        try {
            this.databaseService = new DatabaseService(this.config().database().databaseAddress(), this.config().database().databasePort(), this.config().database().databaseName(), this.config().database().databaseUsername(), this.config().database().databasePassword());
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.logger.error("Could not connect to database.");
        }

        this.userService = new UserService(databaseService);
        this.homeService = new HomeService(databaseService);
        this.warpService = new WarpService(databaseService);
        this.spawnService = new SpawnService(databaseService);

        this.teleportationService = new TeleportationService(this);
        this.teleportRequestService = new TeleportRequestService(this);

        this.proxy.getEventManager().register(this, new UserLoginListener(this));
        this.proxy.getEventManager().register(this, new UserLeaveListener(this));

        this.proxy.getEventManager().register(this, new TeleportHomeMessageListener(this));
        this.proxy.getEventManager().register(this, new SetHomeMessageListener(this));
        this.proxy.getEventManager().register(this, new DeleteHomeMessageListener(this));
        this.proxy.getEventManager().register(this, new ListHomeMessageListener(this));

        this.proxy.getEventManager().register(this, new SetWarpMessageListener(this));
        this.proxy.getEventManager().register(this, new TeleportWarpMessageListener(this));
        this.proxy.getEventManager().register(this, new DeleteWarpMessageListener(this));
        this.proxy.getEventManager().register(this, new ListWarpMessageListener(this));

        this.proxy.getEventManager().register(this, new TeleportMessageListener(this));
        this.proxy.getEventManager().register(this, new TeleportRequestMessageListener(this));

        this.proxy.getEventManager().register(this, new SpawnMessageListener(this));

        this.proxy().getChannelRegistrar().register(MASUITE_MAIN_CHANNEL);

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
            this.config = MaSuiteProxyConfig.loadFrom(configNode);
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
            this.messages = MaSuiteProxyMessagesConfig.loadFrom(messagesNode);
            this.messages.saveTo(messagesNode);
            messagesLoader.save(messagesNode);
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
    }


}
