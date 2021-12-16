package dev.masa.masuite.paper.commands.user;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.masa.masuite.common.objects.MaSuiteMessage;
import dev.masa.masuite.paper.utils.BukkitPluginMessage;
import org.bukkit.entity.Player;

@CommandAlias("userinfo|masuiteuser")
public class UserInfoCommand extends BaseCommand {

    @Default()
    @CommandPermission("masuite.user.info")
    @Description("Shows info about user")
    @CommandCompletion("@players")
    public void userInfoCommand(Player player, @Single String user) {
        BukkitPluginMessage bpm = new BukkitPluginMessage(player, MaSuiteMessage.USER_INFO, user);
        bpm.send();
    }

}
