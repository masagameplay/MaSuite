package dev.masa.masuite.waterfall.utils;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Data
@NoArgsConstructor
public class BungeePluginMessage {

    /**
     * Send plugin message to server
     */
    public static class Server {

        private final ServerInfo server;
        private final Object[] params;

        /**
         * A constructor for BungeePluginChannel
         *
         * @param server server to send messages
         * @param params params to send
         */
        public Server(ServerInfo server, Object... params) {
            this.server = server;
            this.params = params;
        }

        /**
         * Send given data to player's server
         */
        public void send() {
            try (ByteArrayOutputStream b = new ByteArrayOutputStream();
                 DataOutputStream out = new DataOutputStream(b)) {
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
                ProxyServer.getInstance().getScheduler().runAsync(ProxyServer.getInstance().getPluginManager().getPlugin("MaSuite"), () -> server.sendData("BungeeCord", b.toByteArray()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Player {

        private final ProxiedPlayer player;
        private final Object[] params;

        /**
         * A constructor for BungeePluginChannel
         *
         * @param player player to send messages
         * @param params params to send
         */
        public Player(ProxiedPlayer player, Object... params) {
            this.player = player;
            this.params = params;
        }

        /**
         * Send given data to player's server
         */
        public void send() {
            try (ByteArrayOutputStream b = new ByteArrayOutputStream();
                 DataOutputStream out = new DataOutputStream(b)) {
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
                ProxyServer.getInstance().getScheduler().runAsync(ProxyServer.getInstance().getPluginManager().getPlugin("MaSuite"), () -> player.sendData("BungeeCord", b.toByteArray()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}