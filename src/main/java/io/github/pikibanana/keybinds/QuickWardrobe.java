package io.github.pikibanana.keybinds;

import io.github.pikibanana.dungeonapi.DungeonDodgeConnection;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class QuickWardrobe {

    public static void register() {
        KeyBinding[] wardrobeKeyBindings = new KeyBinding[9];

        for (int i = 0; i < 9; i++) {
            final int slotIndex = i + 1;

            wardrobeKeyBindings[i] = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.dungeondodgeplus.wardrobe." + slotIndex,
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_UNKNOWN,
                    "category.dungeondodgeplus.wardrobe"
            ));
        }

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (int i = 0; i < 9; i++) {
                if (wardrobeKeyBindings[i].wasPressed()) {
                    ClientPlayerEntity player = MinecraftClient.getInstance().player;
                    if (player != null && DungeonDodgeConnection.isConnected()) {
                        player.networkHandler.sendCommand("wardrobe " + (i + 1));
                    }
                }
            }
        });
    }


}
