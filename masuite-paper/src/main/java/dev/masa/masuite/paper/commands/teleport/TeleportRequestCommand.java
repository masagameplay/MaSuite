package dev.masa.masuite.paper.commands.teleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.masa.masuite.api.objects.TeleportRequestType;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.paper.utils.BukkitPluginMessage;
import org.bukkit.entity.Player;


public class TeleportRequestCommand extends BaseCommand {

    @CommandAlias("tpa")
    @CommandPermission("masuite.teleport.request.to")
    @Description("Create a teleport request to player's location")
    @CommandCompletion("@masuite_players")
    public void teleportRequestTo(Player player, @Single String target) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.TELEPORT_REQUEST_CREATE, target, TeleportRequestType.TO.type);
        bpm.send();
    }

    @CommandAlias("tpahere")
    @CommandPermission("masuite.teleport.request.here")
    @Description("Create a teleport request to your location")
    @CommandCompletion("@masuite_players")
    public void teleportRequestHere(Player player, @Single String target) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.TELEPORT_REQUEST_CREATE, target, TeleportRequestType.HERE.type);
        bpm.send();
    }

    @CommandAlias("tpaccept|tpyes")
    @CommandPermission("masuite.teleport.request.accept")
    @Description("Accepts a teleportation request")
    public void teleportRequestAccept(Player player) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.TELEPORT_REQUEST_ACCEPT);
        bpm.send();
    }

    @CommandAlias("tpdeny|tpno")
    @CommandPermission("masuite.teleport.request.deny")
    @Description("Denies a teleportation request")
    public void teleportRequestDeny(Player player) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.TELEPORT_REQUEST_DENY);
        bpm.send();
    }

    @CommandAlias("tpalock")
    @CommandPermission("masuite.teleport.request.lock")
    @Description("Lock your teleportation requests to specific state")
    @CommandCompletion("accept|deny|off")
    public void teleportRequestLock(Player player, @Single String type) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.TELEPORT_REQUEST_LOCK, type);
        bpm.send();
    }

}
