package dev.masa.masuite.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.masa.masuite.paper.MaSuitePaper;
import dev.masa.masuite.paper.utils.BukkitAdapter;
import org.bukkit.entity.Player;

@CommandAlias("sethome|createhome|addhome")
public class SetHomeCommand extends BaseCommand {

    private final MaSuitePaper plugin;

    public SetHomeCommand(MaSuitePaper plugin) {
        this.plugin = plugin;
    }

    @Default()
    @CommandPermission("masuite.home.set")
    @Description("Create a new home or update an existing one")
    public void setHome(Player player, @Single @Default("home") String home) {
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("masuite:homes:set");
            out.writeUTF(home);
            out.writeUTF(BukkitAdapter.adapt(player.getLocation()).serialize());
            player.sendPluginMessage(this.plugin, "BungeeCord", out.toByteArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
