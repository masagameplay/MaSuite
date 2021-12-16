package dev.masa.masuite.paper.commands.warp;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.paper.utils.BukkitPluginMessage;
import org.bukkit.entity.Player;

@CommandAlias("delwarp|deletewarp|removewarp")
public class DelWarpCommand extends BaseCommand {

    @Default()
    @CommandPermission("masuite.warp.delete")
    @Description("Delete warp")
    @CommandCompletion("@warps")
    public void deleteHome(Player player, @Single String warp) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.WARPS_DELETE, warp);
        bpm.send();
    }

}
