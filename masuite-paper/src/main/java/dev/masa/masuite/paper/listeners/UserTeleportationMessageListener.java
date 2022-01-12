package dev.masa.masuite.paper.listeners;

import dev.masa.masuite.common.objects.Location;
import dev.masa.masuite.common.objects.MaSuiteMessage;
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
        if (!channel.equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        try {
            String subchannel = in.readUTF();

            if (subchannel.equals(MaSuiteMessage.TELEPORT_TO_LOCATION.channel)) {
                UUID uuid = UUID.fromString(in.readUTF());
                org.bukkit.Location loc = BukkitAdapter.adapt(new Location().deserialize(in.readUTF()));

                Player player = this.plugin.getServer().getPlayer(uuid);

                if (player == null) {
                    this.plugin.locationTeleportationQueue().put(uuid, loc);
                } else {
                    player.teleport(loc);
                }
            }

            if (subchannel.equals(MaSuiteMessage.TELEPORT_TO_PLAYER.channel)) {
                UUID playerUUID = UUID.fromString(in.readUTF());
                UUID targetUUID = UUID.fromString(in.readUTF());

                Player player = this.plugin.getServer().getPlayer(playerUUID);
                Player target = this.plugin.getServer().getPlayer(targetUUID);

                if (player == null) {
                    this.plugin.playerTeleportationQueue().put(playerUUID, targetUUID);
                } else {
                    if (target == null) {
                        this.plugin.getLogger().info("[Teleports] Player with unique id " + targetUUID + " not found. Cancelled player " + playerUUID + " teleportation to their location.");
                        return;
                    }
                    player.teleport(target);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
