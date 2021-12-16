package dev.masa.masuite.paper.commands.warp;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.paper.utils.BukkitPluginMessage;
import org.bukkit.entity.Player;

@CommandAlias("warps|listwarps")
public class ListWarpCommand extends BaseCommand {

    @Default()
    @CommandPermission("masuite.warp.list")
    @Description("List all warps")
    public void listWarps(Player player) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.WARPS_LIST, player.hasPermission("masuite.warp.list.global"), player.hasPermission("masuite.warp.list.server"), player.hasPermission("masuite.warp.list.hidden"));
        bpm.send();
    }
}