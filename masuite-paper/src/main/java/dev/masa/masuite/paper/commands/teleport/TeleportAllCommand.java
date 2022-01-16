package dev.masa.masuite.paper.commands.teleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.paper.utils.BukkitPluginMessage;
import org.bukkit.entity.Player;

@CommandAlias("tpall|teleportall")
public class TeleportAllCommand extends BaseCommand {

    @Default
    @CommandPermission("masuite.teleport.all")
    @Description("Teleport all players to given player or yourself")
    @CommandCompletion("@masuite_players")
    public void teleportAllToPlayer(Player player, @Optional @Single String target) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.TELEPORT_ALL_TO_PLAYER, target == null ? player.getName() : target);
        bpm.send();
    }
}
