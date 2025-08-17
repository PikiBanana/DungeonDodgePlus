package io.github.pikibanana.dungeonapi;

import io.github.pikibanana.Main;
import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import io.github.pikibanana.dungeonapi.packet.serverbound.DungeonDodgePlusStatusC2SPayload;
import io.github.pikibanana.util.TaskScheduler;
import io.github.pikibanana.util.UpdateChecker;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

public class DungeonDodgeConnection {

    private static boolean isConnected = false;
    private static String connectedServerAddress;
    private boolean isToggled = false;
    private int hiddenTogglePet = 0;

    /**
     * @return True if the client is connected to DungeonDodge, false otherwise
     */
    public static boolean isConnected() {
        return isConnected;
    }

    /**
     * Checks if the client is logged in to a specific server.<br>
     * In reality, this checks if the server address of the
     * remote server the client has connected to contains
     * the specified string, ignoring case.
     * @param serverAddressContains The portion of the client's
     * connected server address to check for, e.g. "dungeondodge"
     * @return True if the client is connected to the server with
     * the inputted address portion, false otherwise
     */
    public static boolean isOnServer(@NotNull String serverAddressContains) {
        return connectedServerAddress != null && connectedServerAddress.toLowerCase().contains(serverAddressContains.toLowerCase());
    }

    public void onJoin(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
        if (handler.getServerInfo() != null) {

            connectedServerAddress = handler.getServerInfo().address;

            //DungeonDodge specific logic
            if (isOnServer("dungeondodge")) {
                isConnected = true;

                //send DungeonDodge+ connection packet
                DungeonDodgePlusStatusC2SPayload payload = new DungeonDodgePlusStatusC2SPayload(Main.MOD_VERSION);
                ClientPlayNetworking.send(payload);
                Main.LOGGER.info("Sent DD+ status payload packet to DungeonDodge server!");

                ClientPlayerEntity player = MinecraftClient.getInstance().player;
                if (player != null && DungeonDodgePlusConfig.get().features.autoTogglePet.enabled && !isToggled) {
                    player.networkHandler.sendCommand("togglepet");
                    isToggled = true;
                }
            }

            //auto-update
            UpdateChecker updateChecker = new UpdateChecker();
            boolean isUpdateAvailable = updateChecker.isNewVersionAvailable(UpdateChecker.latestVersionNumber);

            if (isUpdateAvailable) {
                TaskScheduler.scheduleDelayedTask(100, this::sendUpdateMessageToChat);
            }
        }
    }

    public void onDisconnect(ClientPlayNetworkHandler handler, MinecraftClient minecraftClient) {
        if (isConnected) {
            isConnected = false;
            isToggled = false;
        }
    }

    private void sendUpdateMessageToChat() {
        ChatHud chatHud = MinecraftClient.getInstance().inGameHud.getChatHud();

        chatHud.addMessage(Text.literal("🚀 DungeonDodge+ Update Available!")
                .setStyle(Style.EMPTY.withColor(Formatting.GREEN).withBold(true)));

        chatHud.addMessage(Text.literal(" "));
        chatHud.addMessage(Text.literal("A fresh new update is here, featuring exciting new features and improvements!")
                .setStyle(Style.EMPTY.withColor(Formatting.YELLOW)));
        chatHud.addMessage(Text.literal("Don't miss out on the latest enhancements—download it now! ")
                .append(Text.literal("Click here").setStyle(Style.EMPTY.withColor(Formatting.AQUA)
                        .withUnderline(true)
                        .withClickEvent(new ClickEvent.RunCommand("custom:openUpdateScreen"))))
                .append(Text.literal(" or find the update button in the menu at the bottom right of the pause/title screen."))
                .setStyle(Style.EMPTY.withColor(Formatting.WHITE)));

        chatHud.addMessage(Text.literal(" "));
        chatHud.addMessage(Text.literal("⚠ Update now to experience the newest features and improvements!")
                .setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true)));
    }

    public boolean allowMessage(Text text, boolean b) {
        String message = text.getString();
        boolean blockCooldowns = DungeonDodgePlusConfig.get().features.hideCooldownMessages.enabled;
        if (message.contains("Your pet is now hidden!") && isToggled && hiddenTogglePet == 0) {
            hiddenTogglePet += 1;
            return false;
        } else if (message.contains("This ability is still on cooldown") && blockCooldowns) {
            return false;
        } else if (message.contains("You may use this ability again in") && blockCooldowns) {
            return false;
        }
        return true;
    }
}
