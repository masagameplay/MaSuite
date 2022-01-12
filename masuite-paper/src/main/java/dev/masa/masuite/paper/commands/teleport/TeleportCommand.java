package dev.masa.masuite.paper.commands.teleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.masa.masuite.common.objects.Location;
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

    @Default
    @CommandPermission("masuite.teleport.location")
    @Description("Teleport to location")
    @CommandCompletion("@worlds")
    public void teleportToLocation(Player player, @Single String world, double x, double y, double z) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.TELEPORT_TO_LOCATION, new Location(x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch(), world).serialize());
        bpm.send();
    }

    @Default
    @CommandPermission("masuite.teleport.location")
    @Description("Teleport to location")
    public void teleportToLocation(Player player, double x, double y, double z) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.TELEPORT_TO_LOCATION, new Location(x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch(), player.getLocation().getWorld().getName()).serialize());
        bpm.send();
    }

    @Default
    @CommandPermission("masuite.teleport.location.others")
    @Description("Teleport to location")
    @CommandCompletion("@masuite_players @worlds")
    public void teleportPlayerToLocation(Player player, @Single String target, @Single String world, double x, double y, double z) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.TELEPORT_PLAYER_TO_LOCATION, target, new Location(x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch(), world).serialize());
        bpm.send();
    }

}
