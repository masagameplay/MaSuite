package dev.masa.masuite.paper.commands.home;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.paper.MaSuitePaper;
import dev.masa.masuite.paper.utils.BukkitPluginMessage;
import org.bukkit.entity.Player;

@CommandAlias("home|teleporthome")
public class TeleportHomeCommand extends BaseCommand {

    private final MaSuitePaper plugin;

    public TeleportHomeCommand(MaSuitePaper plugin) {
        this.plugin = plugin;
    }

    @Default()
    @CommandPermission("masuite.home.teleport")
    @Description("Teleport to home")
    @CommandCompletion("@homes @masuite_players")
    @Conditions("cooldown:type=homes,bypass=masuite.home.cooldown.bypass")
    public void teleportHome(Player player, @Default("home") String home, @Optional @CommandPermission("masuite.home.teleport.others") String user) {
        if(user != null) {
            var bpm = new BukkitPluginMessage(player, MaSuiteMessage.HOMES_TELEPORT_OTHERS, user, home);
            bpm.send();
            return;
        }

        this.plugin.warmupManager().applyWarmup(player, "masuite.home.warmup.bypass", "homes", success -> {
            if(!success) return;
            var bpm = new BukkitPluginMessage(player, MaSuiteMessage.HOMES_TELEPORT, home);
            bpm.send();
        });
    }

}
