package dev.masa.masuite.paper.commands.warp;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.paper.utils.BukkitAdapter;
import dev.masa.masuite.paper.utils.BukkitPluginMessage;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

@CommandAlias("setwarp|createwarp|addwarp")
public class SetWarpCommand extends BaseCommand {

    @Default()
    @CommandPermission("masuite.warp.set")
    @Description("Create a new warp or update an existing one")
    @CommandCompletion("@warps hidden|public server|global")
    public void setWarp(Player player, @Single String warp, @Nullable @Optional @Single String publicity, @Nullable @Optional @Single String type) {

        boolean isPublic = publicity != null && publicity.equalsIgnoreCase("public");
        boolean global = type != null && type.equalsIgnoreCase("global");

        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.WARPS_SET, warp, BukkitAdapter.adapt(player.getLocation()).serialize(), isPublic, global);
        bpm.send();
    }

}
