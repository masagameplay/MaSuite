package dev.masa.masuite.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.masa.masuite.paper.MaSuitePaper;
import org.bukkit.entity.Player;

@CommandAlias("userinfo|masuiteuser")
public class UserInfoCommand extends BaseCommand {

    private final MaSuitePaper plugin;

    public UserInfoCommand(MaSuitePaper plugin) {
        this.plugin = plugin;
    }

    @Default()
    @CommandPermission("masuite.user.info")
    @Description("Shows info about user")
    @CommandCompletion("@players")
    public void userInfoCommand(Player player, @Single String user) {
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("masuite:userinfo");
            out.writeUTF(user);
            player.sendPluginMessage(this.plugin, "BungeeCord", out.toByteArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
