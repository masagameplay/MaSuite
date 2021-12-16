package dev.masa.masuite.waterfall;

import net.md_5.bungee.api.plugin.Plugin;

public class MaSuiteWaterfallLoader extends Plugin {

    private final MaSuiteWaterfall plugin;

    public MaSuiteWaterfallLoader() {
        this.plugin = new MaSuiteWaterfall();
        plugin.loader(this);
    }

    @Override
    public void onEnable() {
        plugin.onEnable();
    }

    @Override
    public void onDisable() {
        plugin.onDisable();
    }
}
