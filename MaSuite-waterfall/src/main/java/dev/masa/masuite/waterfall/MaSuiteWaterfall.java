package dev.masa.masuite.waterfall;

import dev.masa.masuite.common.AbstractMaSuitePlugin;
import dev.masa.masuite.common.configuration.MaSuiteConfig;
import dev.masa.masuite.common.services.DatabaseService;
import dev.masa.masuite.common.services.UserService;
import dev.masa.masuite.waterfall.listeners.UserLeaveListener;
import dev.masa.masuite.waterfall.listeners.UserLoginListener;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;

@Accessors(fluent = true)
public final class MaSuiteWaterfall extends AbstractMaSuitePlugin<MaSuiteWaterfallLoader> {

    @Getter
    private MaSuiteWaterfallLoader loader = null;

    @Getter
    private UserService userService;

    @Getter
    private DatabaseService databaseService;

    @Getter
    private MaSuiteConfig config;

    @Override
    public void loader(MaSuiteWaterfallLoader loader) throws IllegalStateException {
        if (this.loader != null) {
            throw new IllegalStateException("Plugin is already initialized");
        }
        this.loader = loader;
    }

    @Override
    public void onEnable() {
        // Generate configs
        this.generateConfigs();

        // Initialize services
        try {
            this.databaseService = new DatabaseService(this.config().database().databaseAddress(), this.config().database().databasePort(), this.config().database().databaseName(), this.config().database().databaseUsername(), this.config().database().databasePassword());
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.loader.getLogger().log(Level.SEVERE, "Could not connect to database.");
        }

        this.userService = new UserService(databaseService);

        // Add listeners
        this.loader.getProxy().getPluginManager().registerListener(this.loader, new UserLoginListener(this));
        this.loader.getProxy().getPluginManager().registerListener(this.loader, new UserLeaveListener(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void generateConfigs() {
        YamlConfigurationLoader configLoader = YamlConfigurationLoader.builder()
                .file(new File("plugins/MaSuite/config.yml"))
                .defaultOptions(opts -> opts.shouldCopyDefaults(true))
                .nodeStyle(NodeStyle.BLOCK)
                .build();

        CommentedConfigurationNode configNode;
        try {
            configNode = configLoader.load();
            this.config = MaSuiteConfig.loadFrom(configNode);
            this.config.saveTo(configNode);
            configLoader.save(configNode);
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
    }
}
