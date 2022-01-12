package dev.masa.masuite.paper.commands.teleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.paper.utils.BukkitPluginMessage;
import org.bukkit.entity.Player;

@CommandAlias("tp")
public class TeleportCommand extends BaseCommand {

    @Default
    @CommandPermission("masuite.teleport.player")
    @Description("Teleport to player")
    public void teleportToPlayer(Player player, @Single String target) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.TELEPORT_TO_PLAYER, target);
        bpm.send();
    }

    @Default
    @CommandPermission("masuite.teleport.player.others")
    @Description("Teleport player to player")
    public void teleportPlayerToPlayer(Player player, @Single String target, @Single String secondPlayer) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.TELEPORT_PLAYER_TO_PLAYER, target, secondPlayer);
        bpm.send();
    }

}
