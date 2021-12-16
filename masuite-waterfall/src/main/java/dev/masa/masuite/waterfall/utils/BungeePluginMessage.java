package dev.masa.masuite.waterfall.utils;

import dev.masa.masuite.common.objects.MaSuiteMessage;
import lombok.Data;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Data
public class BungeePluginMessage {

    /**
     * Send plugin message to server
     */
    private final ServerInfo server;
    private final MaSuiteMessage channel;
    private final Object[] params;

    /**
     * A constructor for BungeePluginChannel
     *
     * @param server server to send messages
     * @param params params to send
     */
    public BungeePluginMessage(ServerInfo server, MaSuiteMessage channel, Object... params) {
        this.server = server;
        this.channel = channel;
        this.params = params;
    }

    /**
     * Send given data to player's server
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
            ProxyServer.getInstance().getScheduler().runAsync(ProxyServer.getInstance().getPluginManager().getPlugin("MaSuite"), () -> server.sendData(MaSuiteMessage.MAIN.channel, b.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}