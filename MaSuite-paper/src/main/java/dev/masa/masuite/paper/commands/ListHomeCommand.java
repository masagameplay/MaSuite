package dev.masa.masuite.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
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
    public void listOtherHomes(Player player, String user) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.HOMES_LIST_OTHERS, user);
        bpm.send();
    }

}