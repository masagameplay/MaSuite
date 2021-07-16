package dev.masa.masuite.paper.listeners;

import dev.masa.masuite.common.objects.Location;
import dev.masa.masuite.paper.MaSuitePaper;
import dev.masa.masuite.paper.utils.BukkitAdapter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class UserTeleportationMessageListener implements PluginMessageListener {

    public final MaSuitePaper plugin;

    public UserTeleportationMessageListener(MaSuitePaper plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player pl, @NotNull byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        try {
            String subchannel = in.readUTF();

            if (subchannel.equals("masuite:user:teleport:location")) {
                UUID uuid = UUID.fromString(in.readUTF());
                org.bukkit.Location loc = BukkitAdapter.adapt(new Location().deserialize(in.readUTF()));

                Player player = this.plugin.getServer().getPlayer(uuid);

                if (player == null) {
                    this.plugin.locationTeleportationQueue().put(uuid, loc);
                } else {
                    player.teleport(loc);
                }
            }

            // TODO: Player to Player Teleport

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
