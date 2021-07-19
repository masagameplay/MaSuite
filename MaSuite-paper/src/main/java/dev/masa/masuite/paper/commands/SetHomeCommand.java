package dev.masa.masuite.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.paper.utils.BukkitAdapter;
import dev.masa.masuite.paper.utils.BukkitPluginMessage;
import org.bukkit.entity.Player;

@CommandAlias("sethome|createhome|addhome")
public class SetHomeCommand extends BaseCommand {

    @Default()
    @CommandPermission("masuite.home.set")
    @Description("Create a new home or update an existing one")
    public void setHome(Player player, @Single @Default("home") String home) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.HOMES_SET, home, BukkitAdapter.adapt(player.getLocation()).serialize());
        bpm.send();
    }

    @Default()
    @CommandPermission("masuite.home.set.others")
    @Description("Create a new home or update an existing one for player")
    public void setHome(Player player, String user, @Single String home) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.HOMES_SET_OTHERS, user, home, BukkitAdapter.adapt(player.getLocation()).serialize());
        bpm.send();
    }

}
