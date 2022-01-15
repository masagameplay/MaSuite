package dev.masa.masuite.common.services;

import dev.masa.masuite.api.objects.ITeleportRequest;
import dev.masa.masuite.api.services.IMessageService;
import dev.masa.masuite.common.models.Home;
import dev.masa.masuite.common.models.User;
import dev.masa.masuite.common.models.Warp;
import dev.masa.masuite.common.objects.Location;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

import java.util.ArrayList;
import java.util.List;

public class MessageService implements IMessageService {

    public static void sendMessage(Audience audience, String message, List<Template> templates) {
        var msg = MiniMessage.get().parse(message, templates);
        audience.sendMessage(msg);
    }

    public static void sendMessage(Audience audience, String message) {
        var msg = MiniMessage.get().parse(message);
        audience.sendMessage(msg);
    }

    public static class Templates {

        public static List<Template> homeTemplate(Home home) {
            var templates = new ArrayList<>(locationTemplate(home.location()));
            templates.add(Template.of("home-name", home.name()));
            return templates;
        }

        public static List<Template> warpTemplate(Warp warp) {
            var templates = new ArrayList<>(locationTemplate(warp.location()));
            templates.add(Template.of("warp-name", warp.name()));
            templates.add(Template.of("warp-is-public", String.valueOf(warp.isPublic())));
            templates.add(Template.of("warp-is-global", String.valueOf(warp.isGlobal())));
            return templates;
        }

        public static List<Template> userTemplate(User user) {
            return List.of(
                    Template.of("username", user.username()),
                    Template.of("unique-id", user.uniqueId().toString())
            );
        }

        public static List<Template> teleportRequestTemplate(String senderName, String receiverName) {
            return List.of(
                    Template.of("sender", senderName),
                    Template.of("receiver", receiverName)
            );
        }

        public static List<Template> locationTemplate(Location location) {
            return List.of(
                    Template.of("location-server", location.server()),
                    Template.of("location-world", location.world()),
                    Template.of("location-x", String.valueOf(location.x())),
                    Template.of("location-y", String.valueOf(location.y())),
                    Template.of("location-z", String.valueOf(location.z())),
                    Template.of("location-yaw", String.valueOf(location.yaw())),
                    Template.of("location-pitch", String.valueOf(location.pitch()))
            );
        }

    }
}
