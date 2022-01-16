package dev.masa.masuite.common.configuration.teleport;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
@Accessors(fluent = true)
public class TeleportMessageConfig {

    @Getter
    @Setting("sender")
    private SenderMessageConfig sender = new SenderMessageConfig();

    @Getter
    @Setting("receiver")
    private ReceiverMessageConfig receiver = new ReceiverMessageConfig();

    @Getter
    @Setting("teleportation-request-lock")
    private TeleportationLockMessageConfig teleportationRequestLock = new TeleportationLockMessageConfig();

    @Getter
    @Setting("teleported-to-player")
    private String teleportedToPlayer = "<dark_green>><green>> <dark_gray>+ <gray>Teleported to <white><username></white>.";

    @Getter
    @Setting("teleported-player-to-player")
    private String teleportedPlayerToPlayer = "<dark_green>><green>> <dark_gray>+ <gray>Teleported <white><first-player></white> to <white><second-player></white>.";

    @Getter
    @Setting("teleported-to-location")
    private String teleportedToLocation = "<dark_green>><green>> <dark_gray>+ <gray>Teleported to <white><location-world></white>, <white><location-x></white>, <white><location-y></white>, <white><location-z></white>.";

    @Getter
    @Setting("teleported-player-to-location")
    private String teleportedPlayerToLocation = "<dark_green>><green>> <dark_gray>+ <gray>Teleported <white><username></white> to <white><location-world></white>, <white><location-x></white>, <white><location-y></white>, <white><location-z></white>.";

    @ConfigSerializable
    public static class SenderMessageConfig {

        @Getter
        @Setting("teleport-request-sent-to")
        private String teleportRequestSentTo = "<dark_green>><green>> <dark_gray>+ <gray>Teleportation request sent to <white><receiver><gray>.";

        @Getter
        @Setting("teleport-request-sent-here")
        private String teleportRequestSentHere = "<dark_green>><green>> <dark_gray>+ <gray>Teleportation request sent to <white><receiver><gray>.";

        @Getter
        @Setting("teleport-request-accepted")
        private String teleportRequestAccepted = "<dark_green>><green>> <dark_gray>+ <white><receiver> <gray>has accepted your teleportation request.";

        @Getter
        @Setting("teleport-request-denied")
        private String teleportRequestDenied = "<dark_red>><red>> <dark_gray>- <white><receiver> <gray>has declined your teleportation request.";

        @Getter
        @Setting("teleport-request-expired")
        private String teleportRequestExpired = "<dark_red>><red>> <dark_gray>- <gray>Your teleportation request to <white><receiver> <gray>has expired.";

        @Getter
        @Setting("teleport-request-pending-sender")
        private String teleportRequestPendingSender = "<dark_red>><red>> <dark_gray>- <gray>You have already a pending teleportation request.";

        @Getter
        @Setting("teleport-request-pending-receiver")
        private String teleportRequestPendingReceiver = "<dark_red>><red>> <dark_gray>- <white><receiver> <gray>has already a pending teleportation request.";

        @Getter
        @Setting("teleported")
        private String teleported = "<dark_green>><green>> <dark_gray>+ <white><receiver> <gray>has been teleported to your location.";

        @Getter
        @Setting("you-cannot-teleport-to-yourself")
        private String cannotTeleportToYourself = "<dark_red>><red>> <dark_gray>- <gray>You can't teleport to yourself.";

    }

    @ConfigSerializable
    public static class ReceiverMessageConfig {

        @Getter
        @Setting("teleport-request-received-to")
        private String teleportRequestReceivedTo = "<dark_green>><green>> <dark_gray>+ <white><sender> <gray>wants teleport to you.";

        @Getter
        @Setting("teleport-request-received-here")
        private String teleportRequestReceivedHere = "<dark_green>><green>> <dark_gray>+ <white><sender> <gray>wants you to teleport to them.";

        @Getter
        @Setting("teleport-request-accepted")
        private String teleportRequestAccepted = "<dark_green>><green>> <dark_gray>+ <gray>You have accepted <white><sender><gray>'s teleportation request.";

        @Getter
        @Setting("teleport-request-denied")
        private String teleportRequestDenied = "<dark_red>><red>> <dark_gray>- <gray>You have denied <white><sender><gray>'s teleportation request.";

        @Getter
        @Setting("teleport-request-expired")
        private String teleportRequestExpired = "<dark_red>><red>> <dark_gray>- <gray>Teleportation request from <white><receiver> <gray>has expired.";

        @Getter
        @Setting("no-pending-requests")
        private String noPendingRequests = "<dark_red>><red>> <dark_gray>- <gray>You don't have any pending teleportation requests.";

        @Getter
        @Setting("teleported")
        private String teleported = "<dark_green>><green>> <dark_gray>+ <gray>You have been teleported to <white><sender><gray>'s location.";

    }

    @ConfigSerializable
    public static class TeleportationLockMessageConfig {
        @Getter
        @Setting("accept")
        private String accept = "<dark_green>><green>> <dark_gray>+ <gray>You are now <white>accepting <gray>teleportation requests.";

        @Getter
        @Setting("deny")
        private String deny = "<dark_red>><red>> <dark_gray>- <gray>You are now <white>denying <gray>teleportation requests.";

        @Getter
        @Setting("off")
        private String off = "<dark_red>><red>> <dark_gray>- <gray>You have <white>disabled <gray>teleportation lock.";
    }

}
