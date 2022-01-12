package dev.masa.masuite.paper.listeners;

import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.paper.MaSuitePaper;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public record MaSuiteSetupListener(MaSuitePaper plugin) implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player pl, @NotNull byte[] message) {
        if (!channel.equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        try {
            String subchannel = in.readUTF();
            if (subchannel.equals(MaSuiteMessage.ADD_USER.channel)) {
                var username = in.readUTF();
                this.plugin.onlinePlayers().add(username);
                this.plugin.getLogger().info("Added player " + username);
                return;
            }

            if (subchannel.equals(MaSuiteMessage.REMOVE_USER.channel)) {
                var username = in.readUTF();
                this.plugin.onlinePlayers().remove(username);
                this.plugin.getLogger().info("Removed player " + username);
                return;
            }

            if (!subchannel.equals(MaSuiteMessage.SETUP.channel)) {
                return;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}