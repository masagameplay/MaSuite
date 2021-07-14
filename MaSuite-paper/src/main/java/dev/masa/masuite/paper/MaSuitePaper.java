package dev.masa.masuite.paper;

import co.aikar.commands.PaperCommandManager;
import dev.masa.masuite.paper.commands.UserInfoCommand;
import dev.masa.masuite.paper.listeners.MaSuiteMessageListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class MaSuitePaper extends JavaPlugin {

    private PaperCommandManager manager;

    @Override
    public void onEnable() {
        this.manager = new PaperCommandManager(this);

        this.manager.registerCommand(new UserInfoCommand(this));

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new MaSuiteMessageListener());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
