package dev.masa.masuite.paper.listeners;

import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.paper.MaSuitePaper;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class MaSuiteSetupListener implements PluginMessageListener {

    public final MaSuitePaper plugin;

    public MaSuiteSetupListener(MaSuitePaper plugin) {
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

            if(!channel.equals(MaSuiteMessage.SETUP.channel)) {
                return;
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}