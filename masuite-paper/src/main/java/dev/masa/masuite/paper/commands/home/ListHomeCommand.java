package dev.masa.masuite.paper.commands.home;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.paper.utils.BukkitPluginMessage;
import org.bukkit.entity.Player;

@CommandAlias("homes|listhomes")
public class ListHomeCommand extends BaseCommand {

    @Default()
    @CommandPermission("masuite.home.list")
    @Description("List all of your homes")
    public void listHomes(Player player) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.HOMES_LIST);
        bpm.send();
    }

    @Default()
    @CommandPermission("masuite.home.list.others")
    @Description("List all of other player's homes")
    @CommandCompletion("@masuite_players")
    public void listOtherHomes(Player player, @Single String user) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.HOMES_LIST_OTHERS, user);
        bpm.send();
    }

}