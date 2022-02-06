package dev.masa.masuite.paper.listeners;

import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.paper.MaSuitePaper;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public record HomeMessageListener(MaSuitePaper plugin) implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player pl, @NotNull byte[] message) {
        if (!channel.equals(MaSuiteMessage.MAIN.channel)) {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        try {
            String subchannel = in.readUTF();
            if (subchannel.equals(MaSuiteMessage.HOME_LIST_ADD.channel)) {
                UUID uuid = UUID.fromString(in.readUTF());
                String homeName = in.readUTF();
                this.plugin.homes().get(uuid).add(homeName);
            }

            if (subchannel.equals(MaSuiteMessage.HOME_LIST_REMOVE.channel)) {
                UUID uuid = UUID.fromString(in.readUTF());
                String homeName = in.readUTF();
                this.plugin.homes().get(uuid).remove(homeName);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
