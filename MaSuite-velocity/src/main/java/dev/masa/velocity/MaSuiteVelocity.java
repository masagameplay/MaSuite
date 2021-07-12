package dev.masa.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;

@Plugin(
        id = "masuite",
        name = "MaSuite",
        version = "@version@",
        description = "Proxy wide homes, teleportations and warps",
        url = "https://masa.dev",
        authors = {"Masa"}
)
public class MaSuiteVelocity {

    @Inject
    private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    }
}
