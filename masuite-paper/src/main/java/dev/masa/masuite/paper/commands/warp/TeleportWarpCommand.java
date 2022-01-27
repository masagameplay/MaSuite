package dev.masa.masuite.paper.commands.warp;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.paper.MaSuitePaper;
import dev.masa.masuite.paper.utils.BukkitPluginMessage;
import org.bukkit.entity.Player;

@CommandAlias("warp|teleportwarp")
public class TeleportWarpCommand extends BaseCommand {

    private final MaSuitePaper plugin;

    public TeleportWarpCommand(MaSuitePaper plugin) {
        this.plugin = plugin;
    }

    @Default()
    @CommandPermission("masuite.warp.teleport")
    @Description("Teleport to warp")
    @Conditions("cooldown:type=warps,bypass=masuite.warp.cooldown.bypass")
    @CommandCompletion("@warps")
    public void teleportWarp(Player player, @Single String warp) {
        boolean permissionToWarpName = player.hasPermission("masuite.warp.to." + warp) || player.hasPermission("masuite.warp.to.*");
        boolean permissionToGlobal = player.hasPermission("masuite.warp.global");
        boolean permissionToServer = player.hasPermission("masuite.warp.server");
        boolean permissionToHidden = player.hasPermission("masuite.warp.hidden");
        this.plugin.warmupManager().applyWarmup(player, "masuite.warp.warmup.bypass", "warps", success -> {
            if(!success) return;
            BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.WARPS_TELEPORT, warp, permissionToWarpName, permissionToGlobal, permissionToServer, permissionToHidden);
            bpm.send();
        });
    }

}
