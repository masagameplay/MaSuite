package dev.masa.masuite.paper.utils;

import dev.masa.masuite.common.objects.MaSuiteMessage;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Data
public class BukkitPluginMessage {
    private Player player;
    private Object[] params;
    private MaSuiteMessage channel;

    /**
     * A constructor for BukkitPluginMessage
     *
     * @param player  player to use send messages
     * @param channel in which channel we send the messages
     * @param params  params to send
     */
    public BukkitPluginMessage(Player player, MaSuiteMessage channel, Object... params) {
        this.player = player;
        this.channel = channel;
        this.params = params;
    }

    /**
     * Send given data to Proxy
     */
    public void send() {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream();
             DataOutputStream out = new DataOutputStream(b)) {
            out.writeUTF(this.channel.channel);
            for (Object param : params) {
                if (param instanceof String) {
                    out.writeUTF((String) param);
                } else if (param instanceof Integer) {
                    out.writeInt((int) param);
                } else if (param instanceof Double) {
                    out.writeDouble((double) param);
                } else if (param instanceof Float) {
                    out.writeFloat((float) param);
                } else if (param instanceof Boolean) {
                    out.writeBoolean((boolean) param);
                } else if (param instanceof Short) {
                    out.writeShort((short) param);
                } else if (param instanceof Long) {
                    out.writeLong((long) param);
                } else if (param instanceof Byte) {
                    out.writeByte((byte) param);
                } else if (param instanceof Character) {
                    out.writeChar((char) param);
                }
            }
            Plugin plugin = Bukkit.getPluginManager().getPlugin("MaSuite");
            Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin, () -> this.player.sendPluginMessage(plugin, MaSuiteMessage.MAIN.channel, b.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}