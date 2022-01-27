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
    @Conditions("cooldown:type=homes,bypass=masuite.home.cooldown.bypass")
    public void teleportHome(Player player, @Single @Default("home") String home) {
        this.plugin.warmupManager().applyWarmup(player, "masuite.home.warmup.bypass", "homes", success -> {
            if(!success) return;
            BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.HOMES_TELEPORT, home);
            bpm.send();
        });
    }

    @Default()
    @CommandPermission("masuite.home.teleport.others")
    @Description("Teleport to other player's home")
    @CommandCompletion("@masuite_players")
    public void teleportHome(Player player, String user, @Single String home) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.HOMES_TELEPORT_OTHERS, user, home);
        bpm.send();
    }

}
