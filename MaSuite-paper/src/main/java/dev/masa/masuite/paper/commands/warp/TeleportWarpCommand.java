package dev.masa.masuite.paper.commands.warp;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.paper.utils.BukkitPluginMessage;
import org.bukkit.entity.Player;

@CommandAlias("warp|teleportwarp")
public class TeleportWarpCommand extends BaseCommand {

    @Default()
    @CommandPermission("masuite.warp.teleport")
    @Description("Teleport to warp")
    @Conditions("cooldown:type=warps,bypass=masuite.warp.cooldown.bypass")
    @CommandCompletion("@warps")
    public void teleportWarp(Player player, @Single String warp) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.WARPS_TELEPORT, warp);
        bpm.send();
    }

}
