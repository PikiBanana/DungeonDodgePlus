package io.github.pikibanana.dungeonapi;

import io.github.pikibanana.config.DungeonDodgePlusConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;

public class DungeonDodgeConnection {

    private static boolean isConnected = false;
    private boolean isToggled = false;

    public static boolean isConnected() {
        return isConnected;
    }

    public void handleMessage(Text text, boolean b) {
        String message = text.getString();
        if (message.contains("Enjoy your stay at DungeonDodge!"))
            isConnected = true;

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null && isConnected && DungeonDodgePlusConfig.get().features.autoTogglePet.enabled && !isToggled) {
            player.networkHandler.sendCommand("togglepet");
            isToggled = true;
        }
    }

}
