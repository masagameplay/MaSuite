package dev.masa.masuite.api.services;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.Template;

public interface IMessageService {

    static void sendMessage(Audience audience, String message, Template... templates) {}

    static void sendMessage(Audience audience, String message) {}
}
