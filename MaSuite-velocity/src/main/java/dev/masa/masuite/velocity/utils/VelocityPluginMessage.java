package dev.masa.masuite.velocity.utils;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.masa.masuite.common.objects.MaSuiteMessage;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static dev.masa.masuite.velocity.MaSuiteVelocity.MASUITE_MAIN_CHANNEL;

public class VelocityPluginMessage {

    private final RegisteredServer server;
    private final MaSuiteMessage channel;
    private final Object[] params;

    public VelocityPluginMessage(RegisteredServer server, MaSuiteMessage channel, Object... params) {
        this.server = server;
        this.channel = channel;
        this.params = params;
    }

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
            this.server.sendPluginMessage(MASUITE_MAIN_CHANNEL, b.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
