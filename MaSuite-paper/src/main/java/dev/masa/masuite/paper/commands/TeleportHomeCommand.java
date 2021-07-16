package dev.masa.masuite.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.masa.masuite.paper.MaSuitePaper;
import org.bukkit.entity.Player;

@CommandAlias("home|teleporthome")
public class TeleportHomeCommand extends BaseCommand {

    private final MaSuitePaper plugin;

    public TeleportHomeCommand(MaSuitePaper plugin) {
        this.plugin = plugin;
    }

    @Default()
    @CommandPermission("masuite.home.teleport")
    @Description("Teleport to home")
    public void setHome(Player player, @Single @Default("home") String home) {
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("masuite:homes:teleport");
            out.writeUTF(home);
            player.sendPluginMessage(this.plugin, "BungeeCord", out.toByteArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
