package dev.masa.masuite.paper.commands.teleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.paper.utils.BukkitPluginMessage;
import org.bukkit.entity.Player;

@CommandAlias("tphere")
public class TeleportHereCommand extends BaseCommand {

    @Default
    @CommandPermission("masuite.teleport.player.here")
    @Description("Teleport to player to your position")
    public void teleportPlayerToHere(Player player, @Single String target) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.TELEPORT_PLAYER_TO_PLAYER, target, player.getName());
        bpm.send();
    }
}
