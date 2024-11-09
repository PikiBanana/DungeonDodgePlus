package io.github.pikibanana.dungeonapi;

import io.github.pikibanana.data.config.DungeonDodgePlusConfig;
import io.github.pikibanana.util.TaskScheduler;
import io.github.pikibanana.util.UpdateChecker;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class DungeonDodgeConnection {

    private static boolean isConnected = false;
    private boolean isToggled = false;
    private int hiddenTogglePet = 0;

    public static boolean isConnected() {
        return isConnected;
    }

    public void onJoin(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
        if (handler.getServerInfo() != null && handler.getServerInfo().address.contains("dungeondodge")) {
            isConnected = true;

            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null && DungeonDodgePlusConfig.get().features.autoTogglePet.enabled && !isToggled) {
                player.networkHandler.sendCommand("togglepet");
                isToggled = true;

                UpdateChecker updateChecker = new UpdateChecker();
                boolean isUpdateAvailable = updateChecker.isNewVersionAvailable(UpdateChecker.latestVersionNumber);

                if (isUpdateAvailable) {
                    TaskScheduler.scheduleDelayedTask(100, this::sendUpdateMessageToChat);
                }
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
                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, UpdateChecker.downloadUrl))))
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
