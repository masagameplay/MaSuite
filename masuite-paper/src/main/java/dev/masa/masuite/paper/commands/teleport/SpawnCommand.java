package dev.masa.masuite.paper.commands.teleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.paper.utils.BukkitAdapter;
import dev.masa.masuite.paper.utils.BukkitPluginMessage;
import org.bukkit.entity.Player;

public class SpawnCommand extends BaseCommand {

    @CommandAlias("spawn")
    @CommandPermission("masuite.spawn.teleport")
    @Description("Teleport to the spawn point")
    public void teleportToSpawn(Player player) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.SPAWN_TELEPORT, true);
        bpm.send();
    }

    @CommandAlias("setspawn")
    @CommandPermission("masuite.spawn.set")
    @Description("Create a new spawn point")
    @CommandCompletion("default|first")
    public void createSpawn(Player player, @Single String defaultSpawn) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.SPAWN_CREATE, BukkitAdapter.adapt(player.getLocation()).serialize(), defaultSpawn.equals("default"));
        bpm.send();
    }

    @CommandAlias("delspawn")
    @CommandPermission("masuite.spawn.delete")
    @Description("Delete a spawn point")
    @CommandCompletion("default|first")
    public void deleteSpawn(Player player, @Single String defaultSpawn) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.SPAWN_DELETE, defaultSpawn.equals("default"));
        bpm.send();
    }

}
