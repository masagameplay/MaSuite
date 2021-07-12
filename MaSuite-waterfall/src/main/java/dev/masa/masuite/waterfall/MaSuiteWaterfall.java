package dev.masa.masuite.waterfall;

import dev.masa.masuite.common.AbstractMaSuitePlugin;
import dev.masa.masuite.common.services.DatabaseService;
import dev.masa.masuite.common.services.UserService;
import dev.masa.masuite.waterfall.listeners.UserLoginListener;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public final class MaSuiteWaterfall extends AbstractMaSuitePlugin<MaSuiteWaterfallLoader> {

    private MaSuiteWaterfallLoader loader = null;

    @Getter
    private UserService userService;

    @Getter
    private DatabaseService databaseService;

    @Override
    public MaSuiteWaterfallLoader loader() {
        return null;
    }

    @Override
    public void loader(MaSuiteWaterfallLoader loader) throws IllegalStateException {
        if (this.loader != null) {
            throw new IllegalStateException("Plugin is already initialized");
        }
        this.loader = loader;
    }

    @Override
    public void onEnable() {
        this.loader.getLogger().info("Loading MaSuite!!!!!!!!!!");
        this.databaseService = new DatabaseService("127.0.0.1", 3306, "minecraft", "minecraft", "minecraft");
        this.userService = new UserService(databaseService);

        this.loader.getProxy().getPluginManager().registerListener(this.loader, new UserLoginListener(this));
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
