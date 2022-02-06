package dev.masa.masuite.paper.commands.teleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.paper.MaSuitePaper;
import dev.masa.masuite.paper.utils.BukkitAdapter;
import dev.masa.masuite.paper.utils.BukkitPluginMessage;
import org.bukkit.entity.Player;

public class SpawnCommand extends BaseCommand {

    private final MaSuitePaper plugin;

    public SpawnCommand(MaSuitePaper plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("spawn")
    @CommandPermission("masuite.teleport.spawn.teleport")
    @Description("Teleport to the spawn point")
    @Conditions("cooldown:type=teleports,bypass=masuite.teleport.cooldown.bypass")
    @CommandCompletion("@masuite_players")
    public void teleportToSpawn(Player player, @Optional @CommandPermission("masuite.teleport.spawn.teleport.others") String user) {
        if(user != null) {
            var bpm = new BukkitPluginMessage(player, MaSuiteMessage.SPAWN_TELEPORT_OTHERS, user, true);
            bpm.send();
            return;
        }

        this.plugin.warmupManager().applyWarmup(player, "masuite.teleport.warmup.bypass", "teleports", success -> {
            if(!success) return;
            var bpm = new BukkitPluginMessage(player, MaSuiteMessage.SPAWN_TELEPORT, true);
            bpm.send();
        });
    }

    @CommandAlias("setspawn")
    @CommandPermission("masuite.teleport.spawn.set")
    @Description("Create a new spawn point")
    @CommandCompletion("default|first")
    public void createSpawn(Player player, @Single String defaultSpawn) {
        var bpm = new BukkitPluginMessage(player, MaSuiteMessage.SPAWN_CREATE, BukkitAdapter.adapt(player.getLocation()).serialize(), defaultSpawn.equals("default"));
        bpm.send();
    }

    @CommandAlias("delspawn")
    @CommandPermission("masuite.teleport.spawn.delete")
    @Description("Delete a spawn point")
    @CommandCompletion("default|first")
    public void deleteSpawn(Player player, @Single String defaultSpawn) {
        var bpm = new BukkitPluginMessage(player, MaSuiteMessage.SPAWN_DELETE, defaultSpawn.equals("default"));
        bpm.send();
    }

}
